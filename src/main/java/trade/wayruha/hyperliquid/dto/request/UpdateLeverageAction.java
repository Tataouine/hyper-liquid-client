package trade.wayruha.hyperliquid.dto.request;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"type", "asset", "isCross", "leverage"})
@JsonIncludeProperties({"type", "asset", "isCross", "leverage"})
public class UpdateLeverageAction extends BaseAction {
    private static final String TYPE = "updateLeverage";
    private int asset;
    @JsonProperty(value = "isCross")
    private boolean isCross;
    private int leverage;

    public UpdateLeverageAction(int asset, int leverage, boolean isCross) {
        super(TYPE);
        this.asset = asset;
        this.leverage = leverage;
        this.isCross = isCross;
    }

    public UpdateLeverageAction() {
        super(TYPE);
    }
}
