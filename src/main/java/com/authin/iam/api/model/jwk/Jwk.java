package com.authin.iam.api.model.jwk;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Getter
@Setter(AccessLevel.PACKAGE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Jwk {

    //region Private Fields
    @JsonProperty(value = "kty")
    private KeyType keyType;

    @JsonProperty(value = "use")
    private PublicKeyUse publicKeyUse;

    @JsonProperty(value = "key_ops")
    private KeyOperations keyOperations;

    @JsonProperty(value = "alg")
    private Jwa algorithm;

    @JsonProperty(value = "kid")
    private String keyId;

    @JsonProperty(value = "x5u")
    private String x509Url;

    @JsonProperty(value = "x5c")
    private String x509CertificateChain;

    @JsonProperty(value = "x5t")
    private String x509CertificateSha1Thumbprint;

    @JsonProperty(value = "x5t#s256")
    private String x509CertificateSha256Thumbprint;
    //endregion
}
