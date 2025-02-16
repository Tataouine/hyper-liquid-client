package trade.wayruha.hyperliquid.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import lombok.SneakyThrows;
import trade.wayruha.hyperliquid.HyperLiquidConfig;
import trade.wayruha.hyperliquid.dto.EthereumSignature;
import trade.wayruha.hyperliquid.dto.request.CancelOrderAction;
import trade.wayruha.hyperliquid.dto.request.ExchangeRequest;
import trade.wayruha.hyperliquid.dto.request.PlaceOrderAction;
import trade.wayruha.hyperliquid.dto.request.UpdateLeverageAction;
import trade.wayruha.hyperliquid.dto.response.BaseResponse;
import trade.wayruha.hyperliquid.dto.response.CancellationStatus;
import trade.wayruha.hyperliquid.dto.response.OrderResponseDetails;
import trade.wayruha.hyperliquid.service.endpoint.ExchangeEndpoints;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ExchangeService extends ServiceBase {
  private static final TypeReference<BaseResponse<OrderResponseDetails>> ORDER_RESPONSE_TYPE = new TypeReference<>() {};
  private static final TypeReference<BaseResponse<JsonNode>> CANCEL_ORDER_RESPONSE_TYPE = new TypeReference<>() {};
  private static final TypeReference<BaseResponse<Object>> UPDATE_LEVERAGE_RESPONSE_TYPE = new TypeReference<>() {};
  private final ExchangeEndpoints api;

  public ExchangeService(HyperLiquidConfig config) {
    super(config);
    this.api = createService(ExchangeEndpoints.class);
  }

  @SneakyThrows
  public List<OrderResponseDetails> placeOrder(PlaceOrderAction placeOrderAction) {
    final ExchangeRequest input = prepareOrderRequest(placeOrderAction);
    final JsonNode node = client.executeSync(api.postExchange(input));

    final BaseResponse<OrderResponseDetails> response = getObjectMapper().convertValue(node, ORDER_RESPONSE_TYPE);
    return response.getResponse().getData().getStatuses();
  }

  public ExchangeRequest prepareOrderRequest(PlaceOrderAction placeOrderAction) throws Exception {
    long nonce = System.currentTimeMillis();
    final LinkedHashMap<String, Object> payloadFieldsMap = getObjectMapper().convertValue(placeOrderAction, LinkedHashMap.class);
    final EthereumSignature ethereumSignature = TransactionSignatureUtil.signStandardL1Action(
        payloadFieldsMap,
        this.client.getConfig().getPrivateKey(),
        nonce,
        true);
    final ExchangeRequest input = new ExchangeRequest(placeOrderAction, nonce, ethereumSignature);
    return input;
  }

  @SneakyThrows
  public List<CancellationStatus> cancelOrder(CancelOrderAction cancelOrderAction) {
    long nonce = System.currentTimeMillis();
    final LinkedHashMap<String, Object> payloadFieldsMap = getObjectMapper().convertValue(cancelOrderAction, LinkedHashMap.class);
    final EthereumSignature ethereumSignature = TransactionSignatureUtil.signStandardL1Action(
        payloadFieldsMap,
        this.client.getConfig().getPrivateKey(),
        nonce,
        true);
    final ExchangeRequest input = new ExchangeRequest(cancelOrderAction, nonce, ethereumSignature);
    final JsonNode node = client.executeSync(api.postExchange(input));
    final BaseResponse<JsonNode> response = getObjectMapper().convertValue(node, CANCEL_ORDER_RESPONSE_TYPE);

    final List<CancellationStatus> cancellationStatusList = new ArrayList<>();

    for (final JsonNode status : response.getResponse().getData().getStatuses()) {
      if (status.getNodeType() == JsonNodeType.OBJECT) {
        final String errMsg = String.valueOf(status.get("error"));
        cancellationStatusList.add(CancellationStatus.failed(errMsg));
      } else if (status.getNodeType() == JsonNodeType.STRING) {
        cancellationStatusList.add(CancellationStatus.success());
      }
    }
    return cancellationStatusList;
  }

  @SneakyThrows
  public BaseResponse<Object> updateLeverage(UpdateLeverageAction action) {
    long nonce = System.currentTimeMillis();
    final LinkedHashMap<String, Object> payloadFieldsMap = getObjectMapper().convertValue(action, LinkedHashMap.class);
    final EthereumSignature ethereumSignature = TransactionSignatureUtil.signStandardL1Action(
            payloadFieldsMap,
            this.client.getConfig().getPrivateKey(),
            nonce,
            true);
    final ExchangeRequest input = new ExchangeRequest(action, nonce, ethereumSignature);
    final JsonNode node = client.executeSync(api.postExchange(input));
    return getObjectMapper().convertValue(node, UPDATE_LEVERAGE_RESPONSE_TYPE);
  }
}
