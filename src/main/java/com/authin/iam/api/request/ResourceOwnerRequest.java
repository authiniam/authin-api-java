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

public class ResourceOwnerRequest implements Executable<TokenResponse> {

    private String baseUrl;
    private String clientId;
    private String clientSecret;
    private String username;
    private String password;
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
        authinApi.getTokenInResourceOwnerManner(this.clientId, this.clientSecret, this.username, this.password, this.grantType).enqueue(callback);
        return future;
    }

    public static class Builder {

        private String baseUrl;
        private String clientId;
        private String clientSecret;
        private String username;
        private String password;

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
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

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public ResourceOwnerRequest build() {

            if (StringUtils.isNullOrEmpty(this.baseUrl))
                throw new IllegalArgumentException("BaseUrl is a required field");

            if (StringUtils.isNullOrEmpty(this.clientId))
                throw new IllegalArgumentException("ClientId is a required field");

            if (StringUtils.isNullOrEmpty(this.clientSecret))
                throw new IllegalArgumentException("ClientSecret is a required field");

            if (StringUtils.isNullOrEmpty(this.username))
                throw new IllegalArgumentException("Username is a required field");

            if (StringUtils.isNullOrEmpty(this.password))
                throw new IllegalArgumentException("Password is a required field");

            ResourceOwnerRequest resourceOwnerRequest = new ResourceOwnerRequest();
            resourceOwnerRequest.baseUrl = baseUrl;
            resourceOwnerRequest.clientId = clientId;
            resourceOwnerRequest.clientSecret = clientSecret;
            resourceOwnerRequest.username = username;
            resourceOwnerRequest.password = password;
            resourceOwnerRequest.grantType = "password";

            return resourceOwnerRequest;
        }
    }
}
