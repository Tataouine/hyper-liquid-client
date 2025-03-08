package trade.wayruha.hyperliquid.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.neovisionaries.ws.client.*;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import trade.wayruha.hyperliquid.HyperLiquidConfig;
import trade.wayruha.hyperliquid.config.ApiClient;
import trade.wayruha.hyperliquid.config.Constant;
import trade.wayruha.hyperliquid.dto.wsrequest.Subscription;
import trade.wayruha.hyperliquid.dto.wsrequest.WSMessage;
import trade.wayruha.hyperliquid.dto.wsrequest.WSSubscriptionMessage;
import trade.wayruha.hyperliquid.util.IdGenerator;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static trade.wayruha.hyperliquid.config.Constant.MAX_WS_BATCH_SUBSCRIPTIONS;

@Slf4j
public class AlternativeWSClient<T> {
    protected static long WEB_SOCKET_RECONNECTION_DELAY_MS = 10_000;
    protected static final WSMessage pingRequest = new WSMessage("ping");

    protected final HyperLiquidConfig config;
    protected final ApiClient apiClient;
    protected final ObjectMapper objectMapper;
    protected final WebSocketCallback<T> callback;
    @Getter
    protected final int id;
    protected final String logPrefix;
    protected final WebSocketFactory wsFactory;
    protected final AtomicInteger reconnectionCounter;
    protected Set<Subscription> subscriptions;
    protected WebSocket webSocket;
    @Getter
    protected long lastReceivedTime;

    @SneakyThrows
    public AlternativeWSClient(ApiClient apiClient, ObjectMapper mapper, WebSocketCallback<T> callback) {
        this.apiClient = apiClient;
        this.config = apiClient.getConfig();
        this.callback = callback;
        this.objectMapper = mapper;
        this.subscriptions = Set.of();
        this.reconnectionCounter = new AtomicInteger(0);
        this.id = IdGenerator.getNextId();
        this.logPrefix = "[ws-" + this.id + "]";
        this.wsFactory = getWebSocketFactory();
    }

    @SneakyThrows
    public void connect(Set<Subscription> subscriptions) {
        try {
            if (subscriptions != null) {
                this.subscriptions = new HashSet<>(subscriptions);
            }
            if (this.webSocket != null) {
                log.warn("{} Already connected. Closing the current connection.", logPrefix);
                close();
            }
            log.info("{} Connecting WS. Subscriptions: {}", logPrefix, subscriptions);
            this.webSocket = wsFactory
                    .createSocket(config.getWebSocketHost())
                    .setMissingCloseFrameAllowed(true)
                    .setPingInterval(config.getWebSocketPingIntervalSec() * 1000L)
                    .addExtension(WebSocketExtension.PERMESSAGE_DEFLATE)
                    .addListener(new AdapterImplementation());
            this.webSocket.connect();
            subscribe(subscriptions);
        } catch (OpeningHandshakeException ex) {
            logOpeningException(ex);
            throw ex;
        }
    }

    public void subscribe(Set<Subscription> subscriptions) {
        if (subscriptions.size() > MAX_WS_BATCH_SUBSCRIPTIONS)
            throw new IllegalArgumentException("Can't subscribe to more then " + MAX_WS_BATCH_SUBSCRIPTIONS + " channels in single WS connection");
        subscriptions.forEach(this::subscribe);
    }

    @SneakyThrows
    public void subscribe(Subscription subscription) {
        final WSSubscriptionMessage msg = new WSSubscriptionMessage(subscription);
        if (this.sendRequest(msg)) {
            this.subscriptions.add(subscription);
        }
    }

    public boolean sendRequest(WSMessage request) {
        try {
            final String requestStr = objectMapper.writeValueAsString(request);
            if (!request.equals(pingRequest)) {
                log.debug("{} sending: {}", logPrefix, requestStr);
            }
            webSocket.sendText(requestStr);
        } catch (Exception e) {
            log.error("{} Failed to send message: {}. Closing the connection...", logPrefix, request);
            handleFailure(e);
            return false;
        }
        return true;
    }

    public void close() {
        log.info("{} Closing WS.", logPrefix);
        if (webSocket != null) {
            webSocket.disconnect();
            webSocket = null;
        }
    }

    @SneakyThrows
    public boolean reConnect() {
        boolean success = false;
        while (!success && (config.isWebSocketReconnectAlways() || reconnectionCounter.incrementAndGet() < config.getWebSocketMaxReconnectAttempts())) {
            try {
                log.debug("{} Try to reconnect. Attempt #{}", logPrefix, reconnectionCounter.get());
                close();
                connect(this.subscriptions);
                success = true;
                log.info("{} Successfully reconnected to WebSocket channels: {}.", logPrefix, this.subscriptions);
            } catch (Exception e) {
                log.error("{} [Connection error] Error while reconnecting: {}", logPrefix, e.getMessage(), e);
                Thread.sleep(WEB_SOCKET_RECONNECTION_DELAY_MS);
            }
        }
        return success;
    }

    private void handleMessage(String message) {
        lastReceivedTime = System.currentTimeMillis();
        log.trace("{} onMessage WS event: {}", logPrefix, message);
        try {
            T data = parseResponseBody(message);
            if (data != null) callback.onResponse(data);
        } catch (Exception e) {
            log.error("{} WS message parsing failed. Closing it. Response: {}", log, message, e);
            close();
        }
    }

    private void handleFailure(Throwable ex) {
        if (!reConnect()) {
            log.warn("{} [Connection error] Connection will be closed due to error: {}", logPrefix,
                    ex != null ? ex.getMessage() : Constant.WEBSOCKET_INTERRUPTED_EXCEPTION);
            close();
            callback.onFailure(ex, null);
        }
    }

    private T parseResponseBody(String message) throws IOException {
        final ObjectNode response = objectMapper.readValue(message, ObjectNode.class);
        final Optional<String> channel = Optional.ofNullable(response.get("channel"))
                .map(JsonNode::textValue);
        if (channel.isPresent()) {
            if (channel.get().equalsIgnoreCase("pong")) return null;
            if (channel.get().equalsIgnoreCase("subscriptionResponse")) return null;
        }

        final JsonNode dataNode = response.get("data");
        return objectMapper.convertValue(dataNode, callback.getType());
    }

    private static WebSocketFactory getWebSocketFactory() {
        final WebSocketFactory factory = new WebSocketFactory()
                .setConnectionTimeout(5_000);
        return factory;
    }

    private static void logOpeningException(OpeningHandshakeException e) {
        final StatusLine sl = e.getStatusLine();
        String message = String.format("=== Status Line ===|HTTP Version  = %s|Status Code   = %d|Reason Phrase = %s|",
                sl.getHttpVersion(), sl.getStatusCode(), sl.getReasonPhrase());

        // HTTP headers.
        final Map<String, List<String>> headers = e.getHeaders();
        message += "||=== HTTP Headers ===|";
        final String headersStr = headers.entrySet().stream()
                .map(entry -> entry.getValue() == null ? entry.getKey() : String.format("%s: %s", entry.getKey(), String.join(".", entry.getValue())))
                .collect(Collectors.joining("|"));
        message += headersStr;
        log.error("{} Opening exception details: {}", message, e.getMessage());
    }

    private class AdapterImplementation extends WebSocketAdapter {
        @Override
        public void onTextMessage(WebSocket websocket, String message) {
            handleMessage(message);
        }

        @Override
        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) {
            log.debug("{} onOpen WS event: Connected to channels {}", logPrefix, subscriptions);
            lastReceivedTime = System.currentTimeMillis();
            callback.onOpen(null);
        }

        @Override
        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) {
            log.warn("{} onDisconnected WS event: Server close frame: {}. Client close frame: {}. Closed by server: {}",
                    logPrefix, serverCloseFrame, clientCloseFrame, closedByServer);
            callback.onClosed(serverCloseFrame.getCloseCode(), serverCloseFrame.getCloseReason());
        }

        @Override
        public void onError(WebSocket websocket, WebSocketException cause) {
            handleFailure(cause);
        }

        @Override
        public void onFrameSent(WebSocket websocket, WebSocketFrame frame) throws Exception {
            super.onFrameSent(websocket, frame);
            if (frame.isPingFrame()) {
                //we need to send ping manually, which is different from the default ping frame
                sendRequest(pingRequest);
            }
        }
    }
}