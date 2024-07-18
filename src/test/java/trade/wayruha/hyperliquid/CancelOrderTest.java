package trade.wayruha.hyperliquid;

import com.fasterxml.jackson.core.JsonProcessingException;
import trade.wayruha.hyperliquid.dto.request.CancelOrderAction;
import trade.wayruha.hyperliquid.dto.response.CancellationStatus;
import trade.wayruha.hyperliquid.service.ExchangeService;

import java.util.List;

public class CancelOrderTest {

  public static void main(String[] args) {
    //testOrderCancellation
    final String walletAddress = "";
    final String orderId = "";
    final HyperLiquidConfig config = new HyperLiquidConfig();
    final ExchangeService service = new ExchangeService(config);
    final CancelOrderAction cancelOrderAction = new CancelOrderAction();
    cancelOrderAction.addCancel(1, Long.parseLong(orderId));
    List<CancellationStatus> resp = service.cancelOrder(cancelOrderAction, walletAddress);
    System.out.println(resp);
  }
}
