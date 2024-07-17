package trade.wayruha.hyperliquid.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PerpAccountSummary {
  private BigDecimal crossMaintenanceMarginUsed;
  private MarginSummary crossMarginSummary;
  private MarginSummary marginSummary;
  @JsonAlias("withdrawable")
  private BigDecimal withdrawableAmount;
  private List<AccountPosition> assetPositions;
  private long time;

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class MarginSummary {
    private BigDecimal accountValue;
    private BigDecimal totalMarginUsed;
    private BigDecimal totalNtlPos;
    private BigDecimal totalRawUsd;
  }
}
