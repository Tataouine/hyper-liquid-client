package trade.wayruha.hyperliquid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import static trade.wayruha.hyperliquid.config.Constant.HTTP_CLIENT_TIMEOUT_MS;

@Data
public class HyperLiquidConfig {
  private String host = "https://api.hyperliquid.xyz/"; // replace with the actual host
  private String webSocketHost = "wss://api.hyperliquid.xyz/ws"; // replace with the actual WebSocket host
  private String privateKey;
  private String publicKey;

  public HyperLiquidConfig(String publicKey, String privateKey) {
    this.publicKey = publicKey;
    this.privateKey = privateKey;
  }

  /**
   * Host connection timeout.
   */
  private long httpConnectTimeout = HTTP_CLIENT_TIMEOUT_MS;
  /**
   * The host reads the information timeout.
   */
  private long httpReadTimeout = HTTP_CLIENT_TIMEOUT_MS;
  /**
   * The host writes the information timeout.
   */
  private long httpWriteTimeout = HTTP_CLIENT_TIMEOUT_MS;
  /**
   * Retry on failed connection, default true.
   */
  private boolean retryOnConnectionFailure = true;
  /**
   * Should we log request's data?
   */
  private boolean httpLogRequestData = false;

  /**
   * WebSocket should try re-connecting on fail forever
   */
  private boolean webSocketReconnectAlways = false;
  /**
   * If not forever, then how many times?
   */
  private int webSocketMaxReconnectAttempts = 3;
  private int webSocketPingIntervalSec = 45;

  private ObjectMapper objectMapper = createObjectMapper();

  private static ObjectMapper createObjectMapper() {
    final ObjectMapper mapper = new ObjectMapper();
    mapper
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
    return mapper;
  }
}