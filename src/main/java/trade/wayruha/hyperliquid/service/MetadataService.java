package trade.wayruha.hyperliquid.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.SneakyThrows;
import trade.wayruha.hyperliquid.HyperLiquidConfig;
import trade.wayruha.hyperliquid.dto.request.InfoRequestType;
import trade.wayruha.hyperliquid.dto.request.PublicDataRequest;
import trade.wayruha.hyperliquid.dto.response.PerpAssetMarketData;
import trade.wayruha.hyperliquid.dto.response.PerpMetadata;
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
  public PerpMetadata getPerpetualsMetadata() {
    final PublicDataRequest meta = PublicDataRequest.metadata(InfoRequestType.META);
    final JsonNode node = client.executeSync(api.getPublicInfo(meta));
    final PerpMetadata metadata = getObjectMapper().treeToValue(node, PerpMetadata.class);
    if (metadata.getAssets() == null) return metadata;
    //manually set indexes
    for (int i = 0; i < metadata.getAssets().size(); i++) {
      metadata.getAssets().get(i).setAssetIndex(i);
    }
    return metadata;
  }

  @SneakyThrows
  public List<PerpAssetMarketData> getMarketData() {
    final PublicDataRequest meta = PublicDataRequest.metadata(InfoRequestType.META_ASSET_CONTEXT);
    final JsonNode node = client.executeSync(api.getPublicInfo(meta));
    if (!node.isArray()) throw new IllegalArgumentException("Expected array of objects");

    final JsonNode assetMarketData = node.get(1);
    final List<PerpAssetMarketData> metadata = getObjectMapper().treeToValue(assetMarketData, assetsMarketDataType);
    if (metadata == null) return metadata;
    //manually set indexes
    for (int i = 0; i < metadata.size(); i++) {
      metadata.get(i).setAssetIndex(i);
    }
    return metadata;
  }
}
