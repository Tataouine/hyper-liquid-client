package trade.wayruha.hyperliquid;

import trade.wayruha.hyperliquid.dto.OrderSide;
import trade.wayruha.hyperliquid.dto.TimeInForceType;
import trade.wayruha.hyperliquid.dto.request.PlaceOrderAction;
import trade.wayruha.hyperliquid.dto.response.OrderResponseDetails;
import trade.wayruha.hyperliquid.service.ExchangeService;

import java.math.BigDecimal;
import java.util.List;

public class PlaceOrderTest {

  public static void main(String[] args) {
    //testOrderPlacement
    final String walletPrivateKey = "";
    final HyperLiquidConfig config = new HyperLiquidConfig();
    final ExchangeService service = new ExchangeService(config);
    final PlaceOrderAction orderAction = new PlaceOrderAction();
    orderAction.addLimitOrder(1, OrderSide.BID, new BigDecimal(3000), new BigDecimal("0.005"), TimeInForceType.GTC);
    List<OrderResponseDetails> resp = service.placeOrder(orderAction, walletPrivateKey);
    System.out.println(resp);
  }
}
