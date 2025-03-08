package trade.wayruha.hyperliquid.config;

import okhttp3.ConnectionPool;
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
        this.loggingInterceptor = getDefaultLoggingInterceptor();
    }

    public HttpClientBuilder(final HyperLiquidConfig config, HttpLoggingInterceptor loggingInterceptor) {
        this.config = config;
        this.loggingInterceptor = loggingInterceptor;
    }

    public OkHttpClient buildClient() {
        final OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        final ConnectionPool connectionPool = new ConnectionPool(2, 5, TimeUnit.MINUTES);

        clientBuilder.connectTimeout(this.config.getHttpConnectTimeout(), TimeUnit.MILLISECONDS)
//                .addInterceptor(loggingInterceptor) // left here for debugging purpose
                .connectionPool(connectionPool)
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
