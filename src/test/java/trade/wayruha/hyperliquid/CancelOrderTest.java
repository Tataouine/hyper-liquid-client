package trade.wayruha.hyperliquid;

import trade.wayruha.hyperliquid.dto.request.CancelOrderAction;
import trade.wayruha.hyperliquid.dto.response.CancellationStatus;
import trade.wayruha.hyperliquid.service.ExchangeService;

import java.util.List;

public class CancelOrderTest {

  public static void main(String[] args) {
    //testOrderCancellation
    final String walletPrivateKey = "";
    final String orderId = "";
    final HyperLiquidConfig config = new HyperLiquidConfig(null, walletPrivateKey);
    final ExchangeService service = new ExchangeService(config);
    final CancelOrderAction cancelOrderAction = new CancelOrderAction();
    cancelOrderAction.addCancel(1, Long.parseLong(orderId));
    List<CancellationStatus> resp = service.cancelOrder(cancelOrderAction);
    System.out.println(resp);
  }
}
