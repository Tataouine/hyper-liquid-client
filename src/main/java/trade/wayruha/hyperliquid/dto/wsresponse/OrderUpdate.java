package trade.wayruha.hyperliquid.dto.wsresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import trade.wayruha.hyperliquid.dto.OrderStatus;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderUpdate {
  private Order order;
  private OrderStatus status;
  private long statusTimestamp;
}
