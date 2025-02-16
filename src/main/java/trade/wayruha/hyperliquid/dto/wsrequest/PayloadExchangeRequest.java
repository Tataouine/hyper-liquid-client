package trade.wayruha.hyperliquid.dto.wsrequest;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import trade.wayruha.hyperliquid.dto.EthereumSignature;
import trade.wayruha.hyperliquid.dto.request.BaseAction;
import trade.wayruha.hyperliquid.dto.request.ExchangeRequest;

@Getter
@Setter
@ToString(callSuper = true)
public class PayloadExchangeRequest<T> extends ExchangeRequest {
    private T payload;

    public PayloadExchangeRequest(BaseAction action, long nonce, EthereumSignature ethereumSignature, String vaultAddress) {
        super(action, nonce, ethereumSignature, vaultAddress);
    }

    public PayloadExchangeRequest(BaseAction orderAction, long nonce, EthereumSignature ethereumSignature) {
        super(orderAction, nonce, ethereumSignature);
    }
}
