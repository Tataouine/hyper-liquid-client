package trade.wayruha.hyperliquid.dto.wsrequest;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class WSSubscriptionMessage extends WSMessage {
  private final Subscription subscription;

  public WSSubscriptionMessage(Subscription subscription) {
    super("subscribe");
    this.subscription = subscription;
  }
}
