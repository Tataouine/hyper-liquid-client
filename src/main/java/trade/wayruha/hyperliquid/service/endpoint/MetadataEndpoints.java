package trade.wayruha.hyperliquid.service.endpoint;

import com.fasterxml.jackson.databind.JsonNode;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import trade.wayruha.hyperliquid.dto.request.PerpetualsMetadataRequest;

public interface MetadataEndpoints {
  @POST("info")
  Call<JsonNode> getPerpetualsMetadata(@Body PerpetualsMetadataRequest request);
}
