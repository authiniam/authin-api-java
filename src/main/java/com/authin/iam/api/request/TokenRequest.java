package com.authin.iam.api.request;

import com.authin.iam.api.common.*;
import com.authin.iam.api.model.*;
import okhttp3.*;
import retrofit2.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.jackson.*;

import java.util.concurrent.*;

public class TokenRequest implements Executable<TokenResponse> {

    private String baseUrl;
    private String code;
    private String redirectUri;
    private String clientId;
    private String clientSecret;
    private String grantType;

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public CompletableFuture<TokenResponse> execute() {

        CompletableFuture<TokenResponse> future = new CompletableFuture<>();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(httpClient.build())
                .build();

        AuthinApi authinApi = retrofit.create(AuthinApi.class);
        Callback<TokenResponse> callback = new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                future.complete(response.body());
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable throwable) {
                future.completeExceptionally(throwable);
            }
        };
        authinApi.getToken(this.code, this.redirectUri, this.clientId, this.clientSecret, this.grantType).enqueue(callback);
        return future;
    }

    public static class Builder {

        private String baseUrl;
        private String code;
        private String redirectUri;
        private String clientId;
        private String clientSecret;
        private String grantType;

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder redirectUri(String redirectUri) {
            this.redirectUri = redirectUri;
            return this;
        }

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder clientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public Builder grantType(String grantType) {
            this.grantType = grantType;
            return this;
        }

        public TokenRequest build() {

            if (StringUtils.isNullOrEmpty(this.baseUrl))
                throw new IllegalArgumentException("BaseUrl is a required field");

            if (StringUtils.isNullOrEmpty(this.code))
                throw new IllegalArgumentException("Code is a required field");

            if (StringUtils.isNullOrEmpty(this.redirectUri))
                throw new IllegalArgumentException("RedirectUri is a required field");

            if (StringUtils.isNullOrEmpty(this.clientId))
                throw new IllegalArgumentException("ClientId is a required field");

            if (StringUtils.isNullOrEmpty(this.clientSecret))
                throw new IllegalArgumentException("ClientSecret is a required field");

            if (StringUtils.isNullOrEmpty(this.grantType))
                throw new IllegalArgumentException("GrantType is a required field");

            TokenRequest tokenRequest = new TokenRequest();
            tokenRequest.baseUrl = baseUrl;
            tokenRequest.code = code;
            tokenRequest.redirectUri = redirectUri;
            tokenRequest.clientId = clientId;
            tokenRequest.clientSecret = clientSecret;
            tokenRequest.grantType = grantType;

            return tokenRequest;
        }
    }
}
