package trade.wayruha.hyperliquid.service.endpoint;

import com.fasterxml.jackson.databind.JsonNode;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import trade.wayruha.hyperliquid.dto.request.ExchangeRequest;

public interface ExchangeEndpoints {
    @POST("exchange")
    Call<JsonNode> postExchange(@Body ExchangeRequest request);
}
