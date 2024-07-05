package trade.wayruha.hyperliquid.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import trade.wayruha.hyperliquid.HyperLiquidConfig;
import trade.wayruha.hyperliquid.config.ApiClient;
import trade.wayruha.hyperliquid.config.Constant;
import trade.wayruha.hyperliquid.dto.wsrequest.Subscription;
import trade.wayruha.hyperliquid.dto.wsrequest.WSMessage;
import trade.wayruha.hyperliquid.dto.wsrequest.WSSubscriptionMessage;
import trade.wayruha.hyperliquid.util.IdGenerator;

import java.io.EOFException;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static trade.wayruha.hyperliquid.config.Constant.MAX_WS_BATCH_SUBSCRIPTIONS;

@Slf4j
public class WebSocketSubscriptionClient<T> extends WebSocketListener {
  protected static long WEB_SOCKET_RECONNECTION_DELAY_MS = 10_000;
  protected static final WSMessage pingRequest = new WSMessage("ping");

  protected final HyperLiquidConfig config;
  protected final ApiClient apiClient;
  protected final ObjectMapper objectMapper;
  protected final WebSocketCallback<T> callback;
  @Getter
  protected final int id;
  protected final String logPrefix;
  protected final Set<Subscription> subscriptions;

  protected WebSocket webSocket;
  protected Request connectionRequest;
  protected final AtomicInteger reconnectionCounter;
  @Setter
  protected ScheduledExecutorService scheduler;
  protected ScheduledFuture<?> scheduledPingTask;
  @Getter
  protected WSState state;
  @Getter
  protected long lastReceivedTime;

  public WebSocketSubscriptionClient(ApiClient apiClient, ObjectMapper mapper, WebSocketCallback<T> callback) {
    this(apiClient, mapper, callback, Executors.newSingleThreadScheduledExecutor());
  }

  public WebSocketSubscriptionClient(ApiClient apiClient, ObjectMapper mapper, WebSocketCallback<T> callback,
                                     ScheduledExecutorService scheduler) {
    this.apiClient = apiClient;
    this.config = apiClient.getConfig();
    this.connectionRequest = buildRequestFromHost(this.config.getWebSocketHost());
    this.callback = callback;
    this.objectMapper = mapper;
    this.subscriptions = new HashSet<>();
    this.reconnectionCounter = new AtomicInteger(0);
    this.scheduler = scheduler;
    this.id = IdGenerator.getNextId();
    this.logPrefix = "[ws-" + this.id + "]";
  }

  protected void connect(Set<Subscription> subscriptions) {
    if (this.state != WSState.CONNECTED && this.state != WSState.CONNECTING) {
      log.debug("{} Connecting to channels {} ...", logPrefix, subscriptions);
      this.state = WSState.CONNECTING;
      this.webSocket = apiClient.createWebSocket(this.connectionRequest, this);
      if (this.webSocket == null) {
        log.error("Error connect {}", this.connectionRequest);
      }
    } else {
      log.warn("{} Already connected to channels {}", logPrefix, this.subscriptions);
    }
    this.scheduledPingTask = scheduler.scheduleAtFixedRate(new PingTask(), this.config.getWebSocketPingIntervalSec(), this.config.getWebSocketPingIntervalSec(), TimeUnit.SECONDS);
    reconnectionCounter.set(0); //reset reconnection indexer due to successful connection
    this.subscribe(subscriptions);
  }

  @SneakyThrows
  public void subscribe(Subscription subscription) {
    final WSSubscriptionMessage msg = new WSSubscriptionMessage(subscription);
    if (this.sendRequest(msg)) {
      this.subscriptions.add(subscription);
    }
  }

  public void subscribe(Set<Subscription> subscriptions) {
    if (subscriptions.size() > MAX_WS_BATCH_SUBSCRIPTIONS)
      throw new IllegalArgumentException("Can't subscribe to more then " + MAX_WS_BATCH_SUBSCRIPTIONS + " channels in single WS connection");
    subscriptions.forEach(this::subscribe);
  }

  @SneakyThrows
  public boolean sendRequest(WSMessage request) {
    boolean result = false;
    final String requestStr = objectMapper.writeValueAsString(request);
    log.debug("{} sending {}", logPrefix, requestStr);
    if (nonNull(webSocket)) {
      result = webSocket.send(requestStr);
    }
    if (!result) {
      log.error("{} Failed to send message: {}. Closing the connection...", logPrefix, request);
      closeOnError(null);
    }
    return result;
  }

  public void close() {
    log.debug("{} Closing WS.", logPrefix);
    state = WSState.IDLE;
    if (webSocket != null) {
      webSocket.cancel();
      webSocket = null;
    }
    this.subscriptions.clear();
    if (nonNull(scheduledPingTask))
      scheduledPingTask.cancel(false);
  }

  public boolean reConnect() {
    boolean success = false;
    final Set<Subscription> cancelledSubscriptions = new HashSet<>(this.subscriptions);
    while (!success &&
        (config.isWebSocketReconnectAlways() || reconnectionCounter.incrementAndGet() < config.getWebSocketMaxReconnectAttempts())) {
      try {
        log.debug("{} Try to reconnect. Attempt #{}", logPrefix, reconnectionCounter.get());
        this.close();
        this.connect(cancelledSubscriptions);
        success = true;
      } catch (Exception e) {
        log.error("{} [Connection error] Error while reconnecting: {}", logPrefix, e.getMessage(), e);
        try {
          Thread.sleep(WEB_SOCKET_RECONNECTION_DELAY_MS);
        } catch (InterruptedException ex) {
          log.error("{} [Connection error] Interrupted while Thread.sleep(). {}", logPrefix, ex.getMessage());
        }
      }
    }
    log.debug("{} Successfully reconnected to WebSocket channels: {}.", logPrefix, cancelledSubscriptions);
    cancelledSubscriptions.clear();
    return success;
  }

  @SneakyThrows
  public void ping() {
    this.sendRequest(pingRequest);
    log.trace("{} Ping.", logPrefix);
  }

  @Override
  public void onOpen(WebSocket webSocket, Response response) {
    super.onOpen(webSocket, response);
    log.debug("{} onOpen WS event: Connected to channels {}", logPrefix, this.subscriptions);
    state = WSState.CONNECTED;
    lastReceivedTime = System.currentTimeMillis();
    this.webSocket = webSocket;
    callback.onOpen(response);
  }

  @Override
  public void onMessage(WebSocket webSocket, String text) {
    super.onMessage(webSocket, text);
    lastReceivedTime = System.currentTimeMillis();
    log.debug("{} onMessage WS event: {}", logPrefix, text);
    try {
      final ObjectNode response = objectMapper.readValue(text, ObjectNode.class);
      final Optional<String> channel = Optional.ofNullable(response.get("channel"))
          .map(JsonNode::textValue);
      if (channel.isPresent()) {
        if(channel.get().equalsIgnoreCase("pong")) return;
        if(channel.get().equalsIgnoreCase("subscriptionResponse")) return;
      }

      final JsonNode dataNode = response.get("data");
      final T data = objectMapper.convertValue(dataNode, callback.getType());
      callback.onResponse(data);
    } catch (Exception e) {
      log.error("{} WS message parsing failed. Response: {}", log, text, e);
      closeOnError(e);
    }
  }

  @Override
  public void onMessage(WebSocket webSocket, ByteString bytes) {
    log.debug("onMessage: {} ", bytes.toString());
    super.onMessage(webSocket, bytes);
  }

  @Override
  public void onClosed(WebSocket webSocket, int code, String reason) {
    log.debug("{} onClosed WS event: {}, code: {}", logPrefix, reason, code);
    super.onClosed(webSocket, code, reason);
    if (state == WSState.CONNECTED || state == WSState.CONNECTING) {
      state = WSState.IDLE;
    }
    log.debug("{} Closed", logPrefix);
    callback.onClosed(code, reason);
  }

  @SneakyThrows
  @Override
  public void onFailure(WebSocket webSocket, Throwable t, Response response) {
    if (state == WSState.IDLE || isUselessError(t)) {
      return; //this is a handled websocket closure. No failure event should be created.
    }
    log.error("{} WS failed. Response: {}. Trying to reconnect...", logPrefix, extractResponseBody(response), t);

    if (!reConnect()) {
      log.warn("{} [Connection error] Could not reconnect in {} attempts.", logPrefix, config.getWebSocketMaxReconnectAttempts());
      super.onFailure(webSocket, t, response);
      closeOnError(t);
      callback.onFailure(t, response);
    }
  }

  private void closeOnError(Throwable ex) {
    log.warn("{} [Connection error] Connection will be closed due to error: {}", logPrefix,
        ex != null ? ex.getMessage() : Constant.WEBSOCKET_INTERRUPTED_EXCEPTION);
    this.close();
    state = WSState.CLOSED_ON_ERROR;
  }

  private boolean isUselessError(Throwable throwable) {
    if (!(throwable instanceof SocketException)) return false;
    final SocketException ex = (SocketException) throwable;
    final StackTraceElement lastMethodCall = ex.getStackTrace()[0];
    final StackTraceElement beforeLastMethodCall = ex.getStackTrace()[1];
    final String producingFile = "SocketOutputStream.java";
    return lastMethodCall.getMethodName().equalsIgnoreCase("socketWrite0") && producingFile.equalsIgnoreCase(lastMethodCall.getFileName())
        && beforeLastMethodCall.getMethodName().equalsIgnoreCase("socketWrite") && producingFile.equalsIgnoreCase(beforeLastMethodCall.getFileName());
  }

  static Request buildRequestFromHost(String host) {
    return new Request.Builder().url(host).build();
  }

  @SneakyThrows
  static String extractResponseBody(Response response) {
    if (isNull(response)) return null;
    if (isNull(response.body())) return null;
    return response.body().string();
  }

  class PingTask implements Runnable {

    @SneakyThrows
    @Override
    public void run() {
      try {
        ping();
      } catch (Exception ex) {
        log.error("{} Ping error. Try to reconnect and send again in {} sec...", logPrefix, WEB_SOCKET_RECONNECTION_DELAY_MS / 1000);
        Thread.sleep(WEB_SOCKET_RECONNECTION_DELAY_MS);
        reConnect();
      }
    }
  }
}
