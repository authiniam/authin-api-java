package com.authin.iam.api.request;

import com.authin.iam.api.common.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class AuthorizationCodeRequest implements Executable<URI> {

    public String baseUrl;
    public String clientId;
    public String responseType;
    public String redirectUri;
    public List<String> scopes;
    public String state;
    public ClaimsModel claims;

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public CompletableFuture<URI> execute() {

        try {
            StringBuilder builder = new StringBuilder(this.baseUrl);
            builder.append("/openidauthorize");

                String scope = String.join(" ", this.scopes);
            String claims = new ObjectMapper().writeValueAsString(this.claims);

            builder.append("?client_id=").append(URLEncoder.encode(this.clientId, "UTF-8"));
            builder.append("&response_type=").append(URLEncoder.encode(this.responseType, "UTF-8"));
            builder.append("&redirect_uri=").append(URLEncoder.encode(this.redirectUri, "UTF-8"));
            builder.append("&scope=").append(URLEncoder.encode(scope, "UTF-8"));
            builder.append("&state=").append(URLEncoder.encode(this.state, "UTF-8"));
            builder.append("&claims=").append(URLEncoder.encode(this.state, "UTF-8"));
            URI uri = new URI(builder.toString());
            return CompletableFuture.completedFuture(uri);
        } catch (URISyntaxException | JsonProcessingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static class ClaimsModel {
        @JsonProperty(value = "userinfo")
        private final Map<String, String> userInfo = new HashMap<>();

        @JsonProperty(value = "id_token")
        private final Map<String, String> idToken = new HashMap<>();
    }

    public static class Builder {

        private String baseUrl;
        private String clientId;
        private String responseType;
        private String redirectUri;
        private final List<String> scopes = new ArrayList<>();
        private String state;
        private final List<String> userInfoClaims = new ArrayList<>();
        private final List<String> idTokenClaims = new ArrayList<>();

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder responseType(String responseType) {
            this.responseType = responseType;
            return this;
        }

        public Builder redirectUri(String redirectUri) {
            this.redirectUri = redirectUri;
            return this;
        }

        public Builder addScope(String scope) {
            this.scopes.add(scope);
            return this;
        }

        public Builder state(String state) {
            this.state = state;
            return this;
        }

        public Builder addUserInfoClaim(String claim) {
            this.userInfoClaims.add(claim);
            return this;
        }

        public Builder addIdTokenClaim(String claim) {
            this.idTokenClaims.add(claim);
            return this;
        }

        public AuthorizationCodeRequest build() {

            if (StringUtils.isNullOrEmpty(this.baseUrl))
                throw new IllegalArgumentException("BaseUrl is a required field");

            if (StringUtils.isNullOrEmpty(this.clientId))
                throw new IllegalArgumentException("ClientId is a required field");

            if (StringUtils.isNullOrEmpty(this.responseType))
                throw new IllegalArgumentException("ResponseType is a required field");

            if (StringUtils.isNullOrEmpty(this.redirectUri))
                throw new IllegalArgumentException("RedirectUri is a required field");

            if (!this.scopes.contains("openid"))
                throw new IllegalArgumentException("\"openid\" scope is a required scope");

            ClaimsModel claimsModel = new ClaimsModel();
            this.userInfoClaims.forEach(claim -> claimsModel.userInfo.put(claim, null));
            this.idTokenClaims.forEach(claim -> claimsModel.idToken.put(claim, null));

            AuthorizationCodeRequest request = new AuthorizationCodeRequest();
            request.baseUrl = this.baseUrl;
            request.clientId = this.clientId;
            request.responseType = this.responseType;
            request.redirectUri = this.redirectUri;
            request.scopes = this.scopes;
            request.state = this.state;
            request.claims = claimsModel;

            return request;
        }

    }
}
