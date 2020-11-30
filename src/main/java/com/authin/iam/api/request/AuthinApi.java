package com.authin.iam.api.request;

import com.authin.iam.api.model.*;
import com.authin.iam.api.model.jwk.*;
import retrofit2.*;
import retrofit2.http.*;

interface AuthinApi {

    @FormUrlEncoded
    @POST("/api/v1/oauth/token")
    Call<TokenResponse> getToken(
            @Field("code") String code,
            @Field("redirect_uri") String redirectUri,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("grant_type") String grantType
    );

    @GET("/api/v1/keys")
    Call<Jwks> getKeys();

    @GET("/api/v1/oauth/userinfo")
    Call<UserInfoResponse> getUserInfoByGet(@Header("Authorization") String accessToken);

    @POST("/api/v1/oauth/userinfo")
    Call<UserInfoResponse> getUserInfoByPost(@Header("Authorization") String accessToken);

    @FormUrlEncoded
    @POST("/api/v1/oauth/token")
    Call<TokenResponse> refreshToken(
            @Field("grant_type") String grantType,
            @Field("refresh_token") String refreshToken,
            @Field("scope") String scopes,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret
    );
}
