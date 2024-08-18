package trade.wayruha.hyperliquid.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PerpAssetMarketData {
  @JsonIgnore
  private int assetIndex;
  @JsonAlias("dayNtlVlm")
  private BigDecimal dailyNotionalVolume;
  private BigDecimal funding;
  @JsonAlias("impactPxs")
  private List<BigDecimal> impactPxs;
  @JsonAlias("markPx")
  private BigDecimal markPrice;
  @JsonAlias("midPx")
  private BigDecimal midPrice;
  private BigDecimal openInterest;
  @JsonAlias("oraclePx")
  private BigDecimal oraclePrice;
  private BigDecimal premium;
  @JsonAlias("prevDayPx")
  private BigDecimal prevDayPrice;
}
