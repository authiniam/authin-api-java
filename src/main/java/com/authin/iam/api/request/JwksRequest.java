package com.authin.iam.api.request;

import com.authin.iam.api.common.*;
import com.authin.iam.api.model.*;
import com.authin.iam.api.model.jwk.*;
import okhttp3.*;
import retrofit2.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.jackson.*;

import java.util.concurrent.*;

public class JwksRequest implements Executable<Jwks> {

    private String baseUrl;

    @Override
    public CompletableFuture<Jwks> execute() {

        CompletableFuture<Jwks> future = new CompletableFuture<>();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(httpClient.build())
                .build();

        AuthinApi authinApi = retrofit.create(AuthinApi.class);
        Callback<Jwks> callback = new Callback<Jwks>() {
            @Override
            public void onResponse(Call<Jwks> call, Response<Jwks> response) {
                future.complete(response.body());
            }

            @Override
            public void onFailure(Call<Jwks> call, Throwable throwable) {
                future.completeExceptionally(throwable);
            }
        };

        authinApi.getKeys().enqueue(callback);
        return future;
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public static class Builder {

        private String baseUrl;

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public JwksRequest build() {

            if (StringUtils.isNullOrEmpty(this.baseUrl))
                throw new IllegalArgumentException("BaseUrl is a required field");

            JwksRequest jwksRequest = new JwksRequest();
            jwksRequest.baseUrl = this.baseUrl;
            return jwksRequest;
        }
    }
}
