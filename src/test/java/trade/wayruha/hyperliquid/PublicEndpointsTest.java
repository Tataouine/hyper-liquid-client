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
    final HyperLiquidConfig config = new HyperLiquidConfig();
    final AccountService service = new AccountService(config);
    final PerpAccountSummary resp = service.getPerpAccountSummary(walletAddress);
    System.out.println(resp);
  }

  private static void testOrderStatus(){
    final String walletAddress = "";
    final String oId = "";
    final long orderId = Long.parseLong(oId);
    final HyperLiquidConfig config = new HyperLiquidConfig();
    final AccountService service = new AccountService(config);
    final OrderStatusResponse resp = service.getOrderStatus(walletAddress, orderId);
    System.out.println(resp);
  }
}
