package trade.wayruha.hyperliquid.dto.wsresponse;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserEventUpdate {
    @JsonAlias("fills")
    private List<UserFillsUpdate> userFillsList;
    @JsonAlias("funding")
    private UserFunding userFunding;
    @JsonAlias("liquidation")
    private UserLiquidation userLiquidation;
    @JsonAlias("nonUserCancel")
    private NonUserCancel nonUserCancel;


    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserFunding {
        private int time;
        private String coin;
        private String usdc;
        @JsonProperty("szi")
        private String signedSize;
        private String fundingRate;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserLiquidation {
        @JsonAlias("lid")
        private long liquidationId;
        private String liquidator;
        @JsonAlias("liquidated_user")
        private String liquidatedUser;
        @JsonAlias("liquidated_ntl_pos")
        private String liquidatedNtlPos;
        @JsonAlias("liquidated_account_value")
        private String liquidatedAccountValue;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NonUserCancel {
        private String coin;
        @JsonAlias("oid")
        private long orderId;
    }
}