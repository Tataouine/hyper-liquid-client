package trade.wayruha.hyperliquid.dto.request;

import lombok.Value;

@Value
public class PublicDataRequest {
  InfoRequestType type;
  String user;

  private PublicDataRequest(InfoRequestType type, String user) {
    this.type = type;
    this.user = user;
  }

  public static PublicDataRequest metadata(InfoRequestType type) {
    return new PublicDataRequest(type, null);
  }

  public static PublicDataRequest accountSummary(String walletAddress) {
    return new PublicDataRequest(InfoRequestType.ACCOUNT_STATE, walletAddress);
  }
}
