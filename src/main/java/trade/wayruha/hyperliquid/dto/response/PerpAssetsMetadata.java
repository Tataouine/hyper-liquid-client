package trade.wayruha.hyperliquid.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PerpAssetsMetadata {
  @JsonAlias("universe")
  private List<AssetMetadata> assets;

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class AssetMetadata {
    private String name;
    private Integer szDecimals;
    private Integer maxLeverage;
    private Boolean onlyIsolated;
  }
}
