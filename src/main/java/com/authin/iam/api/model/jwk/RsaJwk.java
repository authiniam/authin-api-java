package com.authin.iam.api.model.jwk;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Getter
@Setter(AccessLevel.PACKAGE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RsaJwk extends Jwk {

    //region Private Fields
    @JsonProperty(value = "n")
    private String modulus;

    @JsonProperty(value = "e")
    private String exponent;
    //endregion
}
