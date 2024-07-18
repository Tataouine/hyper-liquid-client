package trade.wayruha.hyperliquid.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class PublicDataRequest {
  InfoRequestType type;
  String user;
  @JsonProperty("oid")
  Long orderId;

  private PublicDataRequest(InfoRequestType type, String user, Long orderId) {
    this.type = type;
    this.user = user;
    this.orderId = orderId;
  }

  public static PublicDataRequest metadata(InfoRequestType type) {
    return new PublicDataRequest(type, null, null);
  }

  public static PublicDataRequest accountSummary(String walletAddress) {
    return new PublicDataRequest(InfoRequestType.ACCOUNT_STATE, walletAddress, null);
  }

  public static PublicDataRequest orderStatus(String walletAddress, long orderId) {
    return new PublicDataRequest(InfoRequestType.ORDER_STATUS, walletAddress, orderId);
  }
}
