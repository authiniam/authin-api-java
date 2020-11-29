package com.authin.iam.api.model.jwk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static com.authin.iam.api.model.jwk.PublicKeyUse.SIGNATURE;

@Getter
@Setter(AccessLevel.PACKAGE)
public class Jwks {

    //region Private Fields
    @JsonProperty
    private List<Jwk> keys;
    //endregion

    //region Constructors
    public Jwks(final String keyId, final String keyModulus, final String keyExponent) {

        keys = new ArrayList<Jwk>() {{
            add(new RsaJwk() {{
                setKeyType(KeyType.RSA);
                setKeyId(keyId);
                setPublicKeyUse(SIGNATURE);
                setAlgorithm(Jwa.RS256);
                setModulus(keyModulus);
                setExponent(keyExponent);
            }});
        }};
    }
    //endregion
}
