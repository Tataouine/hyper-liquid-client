package trade.wayruha.hyperliquid.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.SneakyThrows;
import trade.wayruha.hyperliquid.HyperLiquidConfig;
import trade.wayruha.hyperliquid.dto.request.InfoRequestType;
import trade.wayruha.hyperliquid.dto.request.PublicDataRequest;
import trade.wayruha.hyperliquid.dto.response.PerpAssetMarketData;
import trade.wayruha.hyperliquid.dto.response.PerpAssetsMetadata;
import trade.wayruha.hyperliquid.service.endpoint.PublicEndpoints;

import java.util.List;

public class MetadataService extends ServiceBase {
  private final PublicEndpoints api;
  private final CollectionType assetsMarketDataType;

  public MetadataService(HyperLiquidConfig config) {
    super(config);
    this.api = createService(PublicEndpoints.class);
    this.assetsMarketDataType = getObjectMapper().getTypeFactory().constructCollectionType(List.class, PerpAssetMarketData.class);
  }

  @SneakyThrows
  public PerpAssetsMetadata getPerpetualsMetadata() {
    final PublicDataRequest meta = PublicDataRequest.metadata(InfoRequestType.META);
    final JsonNode node = client.executeSync(api.getPublicInfo(meta));
    return getObjectMapper().treeToValue(node, PerpAssetsMetadata.class);
  }

  @SneakyThrows
  public List<PerpAssetMarketData> getMarketData() {
    final PublicDataRequest meta = PublicDataRequest.metadata(InfoRequestType.META_ASSET_CONTEXT);
    final JsonNode node = client.executeSync(api.getPublicInfo(meta));
    if (!node.isArray()) throw new IllegalArgumentException("Expected array of objects");

    final JsonNode assetMarketData = node.get(1);
    return getObjectMapper().treeToValue(assetMarketData, assetsMarketDataType);
  }
}
