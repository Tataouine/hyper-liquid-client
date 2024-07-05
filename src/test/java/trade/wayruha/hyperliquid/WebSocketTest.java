package trade.wayruha.hyperliquid;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import trade.wayruha.hyperliquid.dto.wsresponse.OrderBookUpdate;
import trade.wayruha.hyperliquid.websocket.WebSocketCallback;
import trade.wayruha.hyperliquid.websocket.WebSocketClientFactory;
import trade.wayruha.hyperliquid.websocket.WebSocketSubscriptionClient;

import java.util.Set;

public class WebSocketTest {
  @SneakyThrows
  public static void main(String[] args) {
    final HyperLiquidConfig config = new HyperLiquidConfig();
    final WebSocketClientFactory factory = new WebSocketClientFactory(config);
    final Callback callback = new Callback();
    final WebSocketSubscriptionClient<OrderBookUpdate> subscription = factory.orderBookSubscription(Set.of("BTC", "W", "SOL"), callback);
    Thread.sleep(5_000);
  }

  static class Callback implements WebSocketCallback<OrderBookUpdate> {
    static final TypeReference<OrderBookUpdate> type = new TypeReference<>() {
    };

    @Override
    public void onResponse(OrderBookUpdate response) {
      System.out.println(response);

    }

    @Override
    public TypeReference<OrderBookUpdate> getType() {
      return type;
    }
  }
}
