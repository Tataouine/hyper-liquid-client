package trade.wayruha.hyperliquid.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.SneakyThrows;
import trade.wayruha.hyperliquid.HyperLiquidConfig;
import trade.wayruha.hyperliquid.dto.request.PerpetualsMetadataRequest;
import trade.wayruha.hyperliquid.dto.response.PerpAssetMarketData;
import trade.wayruha.hyperliquid.dto.response.PerpAssetsMetadata;
import trade.wayruha.hyperliquid.service.endpoint.MetadataEndpoints;

import java.util.List;

public class MetadataService extends ServiceBase {
  private final MetadataEndpoints api;
  private final CollectionType assetsMarketDataType;

  public MetadataService(HyperLiquidConfig config) {
    super(config);
    this.api = createService(MetadataEndpoints.class);
    this.assetsMarketDataType = getObjectMapper().getTypeFactory().constructCollectionType(List.class, PerpAssetMarketData.class);
  }

  @SneakyThrows
  public PerpAssetsMetadata getPerpetualsMetadata() {
    final PerpetualsMetadataRequest meta = new PerpetualsMetadataRequest("meta");
    final JsonNode node = client.executeSync(api.getPerpetualsMetadata(meta));
    return getObjectMapper().treeToValue(node, PerpAssetsMetadata.class);
  }

  @SneakyThrows
  public List<PerpAssetMarketData> getMarketData() {
    final PerpetualsMetadataRequest meta = new PerpetualsMetadataRequest("metaAndAssetCtxs");
    final JsonNode node = client.executeSync(api.getPerpetualsMetadata(meta));
    if (!node.isArray()) throw new IllegalArgumentException("Expected array of objects");

    final JsonNode assetMarketData = node.get(1);
    return getObjectMapper().treeToValue(assetMarketData, assetsMarketDataType);
  }
}
