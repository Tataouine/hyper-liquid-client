package trade.wayruha.hyperliquid.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import trade.wayruha.hyperliquid.dto.MarginType;
import trade.wayruha.hyperliquid.dto.PositionType;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountPosition {
  private Position position;
  private PositionType type;

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Position {
    private String coin;
    @JsonAlias("szi")
    private BigDecimal size;
    @JsonAlias("entryPx")
    private BigDecimal entryPrice;
    private BigDecimal positionValue;
    private BigDecimal returnOnEquity;
    private BigDecimal unrealizedPnl;
    private CumFunding cumFunding;
    private Leverage leverage;
    @JsonAlias("liquidationPx")
    private BigDecimal liquidationPrice;
    private BigDecimal marginUsed;
    private int maxLeverage;
  }

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class CumFunding {
    @JsonAlias("allTime")
    private BigDecimal allTimeFunding;
    @JsonAlias("sinceChange")
    private BigDecimal sinceChange;
    @JsonAlias("sinceOpen")
    private BigDecimal sinceOpen;
  }

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Leverage {
    @JsonAlias("rawUsd")
    private BigDecimal rawUsdAmount;
    private MarginType type;
    private int value;
  }
}
