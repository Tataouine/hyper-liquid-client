package trade.wayruha.hyperliquid.dto.wsresponse;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import trade.wayruha.hyperliquid.dto.OrderSide;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserFillsUpdate {
    private String coin;
    @JsonAlias("px")
    private BigDecimal price;
    @JsonAlias("sz")
    private BigDecimal size;
    private OrderSide side;
    private long time;
    @JsonAlias("startPosition")
    private String startPosition;
    @JsonAlias("dir")
    private String displayDir;
    @JsonAlias("closedPnl")
    private BigDecimal closedPnl;
    private String hash;
    @JsonAlias("oid")
    private long orderId;
    private boolean crossed;
    @JsonAlias("fee")
    private BigDecimal fee;
    @JsonAlias("tid")
    private long tradeId;
}