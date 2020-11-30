package com.authin.iam.api.request;

import com.authin.iam.api.common.*;
import com.authin.iam.api.model.*;
import okhttp3.*;
import retrofit2.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.jackson.*;

import java.util.*;
import java.util.concurrent.*;

public class RefreshTokenRequest implements Executable<TokenResponse> {

    public String baseUrl;
    public String grantType;
    public String refreshToken;
    public List<String> scopes;
    public String clientId;
    public String clientSecret;

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
        retrofit2.Callback<TokenResponse> callback = new Callback<TokenResponse>() {
            @Override
            public void onResponse(retrofit2.Call<TokenResponse> call, Response<TokenResponse> response) {
                future.complete(response.body());
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable throwable) {
                future.completeExceptionally(throwable);
            }
        };

        String scope = String.join(" ", this.scopes);
        authinApi.refreshToken(this.grantType, this.refreshToken, scope, this.clientId, this.clientSecret).enqueue(callback);
        return future;
    }

    public static class Builder {
        private String baseUrl;
        private String grantType;
        private String refreshToken;
        private final List<String> scopes = new ArrayList<>();
        private String clientId;
        private String clientSecret;

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder grantType(String grantType) {
            this.grantType = grantType;
            return this;
        }

        public Builder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public Builder addScope(String scope) {
            this.scopes.add(scope);
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

        public RefreshTokenRequest build() {

            if (StringUtils.isNullOrEmpty(this.baseUrl))
                throw new IllegalArgumentException("BaseUrl is a required field");

            if (StringUtils.isNullOrEmpty(this.grantType))
                throw new IllegalArgumentException("GrantType is a required field");

            if (StringUtils.isNullOrEmpty(this.refreshToken))
                throw new IllegalArgumentException("RefreshToken is a required field");

            if (StringUtils.isNullOrEmpty(this.clientId))
                throw new IllegalArgumentException("ClientId is a required field");

            if (StringUtils.isNullOrEmpty(this.clientSecret))
                throw new IllegalArgumentException("ClientSecret is a required field");

            RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
            refreshTokenRequest.baseUrl = this.baseUrl;
            refreshTokenRequest.grantType = this.grantType;
            refreshTokenRequest.refreshToken = this.refreshToken;
            refreshTokenRequest.scopes = this.scopes;
            refreshTokenRequest.clientId = this.clientId;
            refreshTokenRequest.clientSecret = this.clientSecret;
            return refreshTokenRequest;
        }

    }
}
