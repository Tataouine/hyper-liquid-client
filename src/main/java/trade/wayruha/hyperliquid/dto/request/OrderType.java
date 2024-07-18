package trade.wayruha.hyperliquid.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;
import trade.wayruha.hyperliquid.dto.TimeInForceType;
import trade.wayruha.hyperliquid.dto.Tpsl;

@Value
public class OrderType {
  Limit limit;
  @JsonProperty("trigger")
  TriggerDetails triggerDetails;

  public OrderType(TriggerDetails triggerDetails) {
    this.triggerDetails = triggerDetails;
    limit = null;
  }

  public OrderType(Limit limit) {
    this.limit = limit;
    triggerDetails = null;
  }

  @Value
  public static class Limit {
    TimeInForceType tif;
  }

  @Value
  public static class TriggerDetails {
    Boolean isMarket;
    @JsonProperty("triggerPx")
    String triggerPrice;
    Tpsl tpsl;
  }
}
