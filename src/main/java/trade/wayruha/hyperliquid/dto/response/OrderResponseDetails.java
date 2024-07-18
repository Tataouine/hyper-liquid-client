package trade.wayruha.hyperliquid.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderResponseDetails {
  private String error;
  private Resting resting;
  private Filled filled;

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Resting {
    @JsonAlias("oid")
    private Long orderId;
  }

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Filled {
    @JsonAlias("totalSz")
    private String totalSize;
    @JsonAlias("avgPx")
    private String averagePrice;
    @JsonAlias("oid")
    private Long orderId;
  }
}
