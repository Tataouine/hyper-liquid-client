package trade.wayruha.hyperliquid.service.endpoint;

import com.fasterxml.jackson.databind.JsonNode;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import trade.wayruha.hyperliquid.dto.request.PublicDataRequest;

public interface PublicEndpoints {
  @POST("info")
  Call<JsonNode> getPublicInfo(@Body PublicDataRequest request);

}
