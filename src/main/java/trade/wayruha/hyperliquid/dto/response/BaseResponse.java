package trade.wayruha.hyperliquid.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
public class BaseResponse<T> {
  private String status;
  private Response<T> response;

  @Data
  public static class Response<T> {
    private String type;
    private Payload<T> data;
  }

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Payload<T> {
    private List<T> statuses;
  }
}
