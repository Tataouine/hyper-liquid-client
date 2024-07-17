package trade.wayruha.hyperliquid.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;
import trade.wayruha.hyperliquid.HyperLiquidConfig;
import trade.wayruha.hyperliquid.dto.request.PublicDataRequest;
import trade.wayruha.hyperliquid.dto.response.PerpAccountSummary;
import trade.wayruha.hyperliquid.service.endpoint.PublicEndpoints;

public class AccountService extends ServiceBase {
  private final PublicEndpoints api;

  public AccountService(HyperLiquidConfig config) {
    super(config);
    this.api = createService(PublicEndpoints.class);
  }

  @SneakyThrows
  public PerpAccountSummary getPerpAccountSummary(String walletAddress) {
    final PublicDataRequest input = PublicDataRequest.accountSummary(walletAddress);
    final JsonNode node = client.executeSync(api.getPublicInfo(input));
    return getObjectMapper().treeToValue(node, PerpAccountSummary.class);
  }
}
