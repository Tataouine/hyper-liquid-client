package trade.wayruha.hyperliquid;

import trade.wayruha.hyperliquid.dto.response.OrderStatusResponse;
import trade.wayruha.hyperliquid.dto.response.PerpAccountSummary;
import trade.wayruha.hyperliquid.service.AccountService;

public class PublicEndpointsTest {

  public static void main(String[] args) {
    testAccountSummary();
    testOrderStatus();
  }

  private static void testAccountSummary(){
    final String walletAddress = "";
    final HyperLiquidConfig config = new HyperLiquidConfig(walletAddress, null);
    final AccountService service = new AccountService(config);
    final PerpAccountSummary resp = service.getPerpAccountSummary();
    System.out.println(resp);
  }

  private static void testOrderStatus(){
    final String walletAddress = "";
    final long orderId = Long.parseLong("31226385612");
		final HyperLiquidConfig config = new HyperLiquidConfig(walletAddress, null);
    final AccountService service = new AccountService(config);
    final OrderStatusResponse resp = service.getOrderStatus(orderId);
    System.out.println(resp);
  }
}
