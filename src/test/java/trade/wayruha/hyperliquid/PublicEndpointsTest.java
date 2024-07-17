package trade.wayruha.hyperliquid;

import trade.wayruha.hyperliquid.dto.response.PerpAccountSummary;
import trade.wayruha.hyperliquid.service.AccountService;

public class PublicEndpointsTest {

  public static void main(String[] args) {
    //testAccountSummary
    final String walletAddress = "";
    final HyperLiquidConfig config = new HyperLiquidConfig();
    final AccountService service = new AccountService(config);
    final PerpAccountSummary resp = service.getPerpAccountSummary(walletAddress);
    System.out.println(resp);
  }
}
