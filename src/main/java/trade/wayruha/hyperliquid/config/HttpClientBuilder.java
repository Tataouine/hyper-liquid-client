package trade.wayruha.hyperliquid.config;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;
import trade.wayruha.hyperliquid.HyperLiquidConfig;

import java.util.concurrent.TimeUnit;

public class HttpClientBuilder {
  private final HyperLiquidConfig config;
  private final HttpLoggingInterceptor loggingInterceptor;

  public HttpClientBuilder(final HyperLiquidConfig config) {
    this.config = config;
    final HttpLoggingInterceptor logInterceptor = getDefaultLoggingInterceptor();
    this.loggingInterceptor = logInterceptor;
  }

  public HttpClientBuilder(final HyperLiquidConfig config, HttpLoggingInterceptor loggingInterceptor) {
    this.config = config;
    this.loggingInterceptor = loggingInterceptor;
  }

  public OkHttpClient buildClient() {
    final OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
    clientBuilder.connectTimeout(this.config.getHttpConnectTimeout(), TimeUnit.MILLISECONDS)
        .readTimeout(this.config.getHttpReadTimeout(), TimeUnit.MILLISECONDS)
        .writeTimeout(this.config.getHttpWriteTimeout(), TimeUnit.MILLISECONDS)
        .retryOnConnectionFailure(this.config.isRetryOnConnectionFailure());
    if (this.config.isHttpLogRequestData() && loggingInterceptor != null) {
      clientBuilder.addInterceptor(loggingInterceptor);
    }
    return clientBuilder.build();
  }

  @NotNull
  private static HttpLoggingInterceptor getDefaultLoggingInterceptor() {
    final HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpClientLogger());
    logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    return logInterceptor;
  }
}
