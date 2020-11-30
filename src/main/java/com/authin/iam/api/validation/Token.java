package com.authin.iam.api.validation;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class Token {

    //region Private Fields
    @JsonProperty(value = "token_type")
    private String tokenType;
    //endregion

    //region Constructors
    public Token(String tokenType) {
        this.tokenType = tokenType;
    }
    //endregion
}
