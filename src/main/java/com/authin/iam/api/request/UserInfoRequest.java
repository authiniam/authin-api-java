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

public class UserInfoRequest implements Executable<UserInfoResponse> {

    private String baseUrl;
    private String accessToken;
    private Method method;

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public CompletableFuture<UserInfoResponse> execute() {

        CompletableFuture<UserInfoResponse> future = new CompletableFuture<>();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(httpClient.build())
                .build();

        AuthinApi authinApi = retrofit.create(AuthinApi.class);
        Callback<UserInfoResponse> callback = new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(retrofit2.Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                future.complete(response.body());
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable throwable) {
                future.completeExceptionally(throwable);
            }
        };

        switch (this.method) {
            case Get:
                authinApi.getUserInfoByGet(this.accessToken).enqueue(callback);
                break;
            case Post:
                authinApi.getUserInfoByPost(this.accessToken).enqueue(callback);
                break;
        }
        return future;
    }

    public static enum Method {
        Get,
        Post
    }

    public static class Builder {
        private String baseUrl;
        private String accessToken;
        private Method method = Method.Get;

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = "Bearer " + accessToken;
            return this;
        }

        public Builder method(Method method) {
            this.method = method;
            return this;
        }

        public UserInfoRequest build() {

            if (StringUtils.isNullOrEmpty(this.baseUrl))
                throw new IllegalArgumentException("BaseUrl is a required field");

            if (StringUtils.isNullOrEmpty(this.accessToken))
                throw new IllegalArgumentException("AccessToken is a required field");

            if (this.method == null)
                throw new IllegalArgumentException("Method is a required field");

            UserInfoRequest userInfoRequest = new UserInfoRequest();
            userInfoRequest.baseUrl = this.baseUrl;
            userInfoRequest.accessToken = this.accessToken;
            userInfoRequest.method = this.method;
            return userInfoRequest;
        }

    }
}
