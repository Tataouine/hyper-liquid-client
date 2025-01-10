package trade.wayruha.hyperliquid.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import trade.wayruha.hyperliquid.HyperLiquidConfig;
import trade.wayruha.hyperliquid.exception.HyperliquidException;

import java.io.IOException;

import static java.util.Objects.nonNull;
import static trade.wayruha.hyperliquid.config.Constant.API_CLIENT_ERROR_MESSAGE_PARSE_EXCEPTION;

@Slf4j
public class ApiClient {
  @Getter
  private final HyperLiquidConfig config;
  private final OkHttpClient httpClient;
  private final Retrofit retrofit;

  public ApiClient(HyperLiquidConfig config) {
    this(config, new HttpClientBuilder(config));
  }

  public ApiClient(HyperLiquidConfig config, HttpClientBuilder httpClientBuilder) {
    this.config = config;
    this.httpClient = httpClientBuilder.buildClient();
    this.retrofit = RetrofitBuilder.buildRetrofit(config, this.httpClient);
  }

  public WebSocket createWebSocket(Request request, WebSocketListener listener) {
    return httpClient.newWebSocket(request, listener);
  }

  public <T> T createService(final Class<T> service) {
    return this.retrofit.create(service);
  }

  public <T> T executeSync(Call<T> call) {
    String rawRequestData = call.request().toString();
    try {
      final Response<T> response = call.execute();
      final T body = response.body();
      if (response.isSuccessful()) {
        return body;
      }
      final ResponseBody errBody = response.errorBody();
      String errorMessage = nonNull(errBody) ? errBody.string() : API_CLIENT_ERROR_MESSAGE_PARSE_EXCEPTION;
      log.error("Request failed. Request data: {}. Response error message: {}", rawRequestData, errorMessage);
      throw new HyperliquidException(response.code() + ": " + errorMessage);
    } catch (IOException e) {
      log.error("Request failed. Request data: {},  response: {} ", rawRequestData, call.request(), e);
      throw new HyperliquidException(e.getMessage(), e);
    }
  }
}