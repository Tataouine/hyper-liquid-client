package trade.wayruha.hyperliquid.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({"a", "o"})
public class CancelOrderParams {

  @JsonProperty("a")
  private int assetId;
  @JsonProperty("o")
  private long orderId;

  public CancelOrderParams(int assetId, long orderId) {
    this.assetId = assetId;
    this.orderId = orderId;
  }
}
