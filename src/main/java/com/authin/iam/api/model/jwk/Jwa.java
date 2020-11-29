package com.authin.iam.api.model.jwk;

public enum Jwa {

    HS256("HS256"),
    RS256("RS256"),
    ES256("ES256");

    private String code;

    Jwa(String code) {

        this.code = code;
    }

    public String code() {

        return this.code;
    }
}
