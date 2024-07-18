package trade.wayruha.hyperliquid.dto.wsresponse;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import trade.wayruha.hyperliquid.dto.OrderSide;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {
  private String coin;
  private OrderSide side;
  @JsonAlias("limitPx")
  private BigDecimal limitPrice;
  @JsonAlias("sz")
  private BigDecimal size;
  @JsonAlias("origSz")
  private BigDecimal originalSize;
  @JsonAlias("oid")
  private long orderId;
  private long timestamp;
  @JsonAlias("cloid")
  private String clientOrderId;
}
