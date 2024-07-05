package trade.wayruha.hyperliquid.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import trade.wayruha.hyperliquid.HyperLiquidConfig;
import trade.wayruha.hyperliquid.config.ApiClient;

import static trade.wayruha.hyperliquid.config.Constant.DEFAULT_RECEIVING_WINDOW;

public abstract class ServiceBase {
  protected int receivingWindow = DEFAULT_RECEIVING_WINDOW;
  protected final ApiClient client;

  public ServiceBase(ApiClient client) {
    this.client = client;
  }

  public ServiceBase(HyperLiquidConfig config) {
    this(new ApiClient(config));
  }

  protected <T> T createService(Class<T> apiClass) {
    return client.createService(apiClass);
  }

  protected ObjectMapper getObjectMapper(){
    return client.getConfig().getObjectMapper();
  }

  protected long now() {
    return System.currentTimeMillis();
  }
}
