package trade.wayruha.hyperliquid.dto.wsresponse;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderBookUpdate {
  private String coin;
  private List<List<WsLevel>> levels;
  private long time;

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class WsLevel {

    @JsonAlias("px")
    private BigDecimal price;

    @JsonAlias("sz")
    private BigDecimal size;

    @JsonAlias("n")
    private int numberOfOrders;
  }
}
