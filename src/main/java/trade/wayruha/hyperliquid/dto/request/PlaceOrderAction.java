package trade.wayruha.hyperliquid.dto.request;

import lombok.Getter;
import trade.wayruha.hyperliquid.dto.OrderSide;
import trade.wayruha.hyperliquid.dto.TimeInForceType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PlaceOrderAction extends BaseAction {

  private final List<NewOrderParams> orders;
  private final String grouping = "na";

  public PlaceOrderAction() {
    super("order");
    this.orders = new ArrayList<>();
  }

  public void addLimitOrder(int coin, OrderSide orderSide, BigDecimal price, BigDecimal baseQty, TimeInForceType tif) {
    final OrderType orderType = new OrderType(new OrderType.Limit(tif));
    orders.add(new NewOrderParams(coin, orderSide, price, baseQty, false, orderType));
  }

  public void addOrder(NewOrderParams o) {
    orders.add(o);
  }
}