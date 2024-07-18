package trade.wayruha.hyperliquid.dto.response;

import lombok.Value;

@Value
public class CancellationStatus {
  boolean success;
  String error;

  private CancellationStatus(boolean success, String error) {
    this.success = success;
    this.error = error;
  }

  public static CancellationStatus success() {
    return new CancellationStatus(true, null);
  }

  public static CancellationStatus failed(String error) {
    return new CancellationStatus(false, error);
  }
}
