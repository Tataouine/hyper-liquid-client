package trade.wayruha.hyperliquid.dto.wsresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriptionConfirmation {
  private String method;
  private Metadata subscription;

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Metadata {
    private String type;
    private String coin;
    @JsonProperty("nSigFigs")
    private int nSigFigs;
    private String mantissa;
  }
}