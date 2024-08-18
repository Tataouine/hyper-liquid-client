package trade.wayruha.hyperliquid.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PerpAssetMetadata {
  @JsonIgnore
  private int assetIndex;
  private String name;
  private Integer szDecimals;
  private Integer maxLeverage;
  private Boolean onlyIsolated;
}
