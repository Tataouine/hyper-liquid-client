package trade.wayruha.hyperliquid.dto.wsrequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Data
public abstract class Subscription {
  protected final String type;

  public Subscription(String type) {
    this.type = type;
  }

  @Value
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true)
  public static class Notification extends Subscription {
    String user;

    public Notification(String user) {
      super("notification");
      this.user = user;
    }
  }

  @Value
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true)
  public static class WebData2 extends Subscription {
    String user;

    public WebData2(String user) {
      super("webData2");
      this.user = user;
    }
  }

  @Value
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true)
  public static class Candle extends Subscription {
    String coin;
    String interval;

    public Candle(String coin, String interval) {
      super("candle");
      this.coin = coin;
      this.interval = interval;
    }
  }

  @Value
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true)
  public static class OrderBook extends Subscription {
    String coin;

    public OrderBook(String coin) {
      super("l2Book");
      this.coin = coin;
    }
  }

  @Value
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true)
  public static class Trades extends Subscription {
    String coin;

    public Trades(String coin) {
      super("trades");
      this.coin = coin;
    }
  }

  @Value
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true)
  public static class OrderUpdates extends Subscription {
    String user;

    public OrderUpdates(String user) {
      super("orderUpdates");
      this.user = user;
    }
  }

  @Value
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true)
  public static class UserEvents extends Subscription {
    String user;

    public UserEvents(String user) {
      super("user");
      this.user = user;
    }
  }

  @Value
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true)
  public static class UserFills extends Subscription {
    String user;

    public UserFills(String user) {
      super("userFills");
      this.user = user;
    }
  }

  @Value
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true)
  public static class UserFundings extends Subscription {
    String user;

    public UserFundings(String user) {
      super("userFundings");
      this.user = user;
    }
  }

  @Value
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true)
  public static class UserNonFundingLedgerUpdates extends Subscription {
    String user;

    public UserNonFundingLedgerUpdates(String user) {
      super("userNonFundingLedgerUpdates");
      this.user = user;
    }
  }
}
