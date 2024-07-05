package trade.wayruha.hyperliquid.exception;

public class HyperliquidException extends RuntimeException {
  public HyperliquidException(String message) {
    super(message);
  }

  public HyperliquidException(String message, Throwable cause) {
    super(message, cause);
  }
}
