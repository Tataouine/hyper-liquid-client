package trade.wayruha.hyperliquid.dto.wsresponse;

import lombok.Data;
import trade.wayruha.hyperliquid.dto.response.BaseResponse;

@Data
public class ResponseWrapper<T> {
    private long id;
    private AnotherWrapper<T> response;

    @Data
    public static class AnotherWrapper<T> {
        private String type;
        private BaseResponse<T> payload;
    }
}
