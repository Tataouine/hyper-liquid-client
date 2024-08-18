package trade.wayruha.hyperliquid.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import trade.wayruha.hyperliquid.dto.OrderSide;
import trade.wayruha.hyperliquid.dto.OrderStatus;
import trade.wayruha.hyperliquid.dto.TimeInForceType;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderStatusResponse {
  private OrderStatus status;
  private long statusTimestamp;
  @JsonProperty("order")
  private OrderDetails orderStatusDetails;

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class OrderDetails {
    private String coin;
    private OrderSide side;
    @JsonProperty("limitPx")
    private BigDecimal limitPrice;
    @JsonProperty("sz")
    private BigDecimal size;
    @JsonProperty("oid")
    private long orderId;
    private long timestamp;
    private String triggerCondition;
    private boolean isTrigger;
    private BigDecimal triggerPx;
    private List<OrderDetails> children; //NOTE: unchecked response body
    private boolean isPositionTpsl;
    private boolean reduceOnly;
    private String orderType;
    @JsonProperty("origSz")
    private BigDecimal originalSize;
    private TimeInForceType tif;
    @JsonProperty("cloid")
    private String clientOrderId;
  }
}