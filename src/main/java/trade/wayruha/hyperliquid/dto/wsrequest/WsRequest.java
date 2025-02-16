package trade.wayruha.hyperliquid.dto.wsrequest;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class WsRequest<T> extends WSMessage {
    T request;

    public WsRequest(String method, Long id, T request) {
        super(method, id);
        this.request = request;
    }
}
