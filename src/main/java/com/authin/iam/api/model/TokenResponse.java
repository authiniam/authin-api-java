package com.authin.iam.api.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Getter
@Setter(AccessLevel.PACKAGE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TokenResponse {

    //region Private Fields
    @JsonProperty(value = "access_token")
    private String accessToken;

    @JsonProperty(value = "token_type")
    private String tokenType;

    @JsonProperty(value = "expires_in")
    private Long expiresIn;

    //just for authorization code flow
    @JsonProperty(value = "refresh_token")
    private String refreshToken;

    //just for implicit flow
    private String state;

    //just for OIDC
    @JsonProperty(value = "id_token")
    private String idToken;

    //should be removed in later releases
    private String username;
    //endregion
}
