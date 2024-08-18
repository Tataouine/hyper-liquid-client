package trade.wayruha.hyperliquid.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PerpMetadata {
  @JsonAlias("universe")
  private List<PerpAssetMetadata> assets;
}
