package com.authin.iam.api.validation;

import com.authin.iam.api.model.jwk.*;
import lombok.*;

@Getter
@NoArgsConstructor
class JoseHeader {

    //region Private Fields
    private TokenType typ;
    private Jwa alg;
    //endregion

    //region Constructors
    public JoseHeader(TokenType typ, Jwa alg) {
        this.typ = typ;
        this.alg = alg;
    }
    //endregion
}
