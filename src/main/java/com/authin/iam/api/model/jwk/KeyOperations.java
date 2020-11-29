package com.authin.iam.api.model.jwk;

public enum KeyOperations {

    SIGN("sign"),
    VERIFY("verify"),
    ENCRYPT("encrypt"),
    DECRYPT("decrypt"),
    WRAPKEY("wrapkey"),
    UNWRAPKEY("unwrapkey"),
    DERIVEKEY("derivekey"),
    DERIVEBITS("derivebits"),
    ;

    private String type;

    KeyOperations(String type) {

        this.type = type;
    }

    public String type() {

        return this.type;
    }

    @Override
    public String toString() {

        return this.type();
    }
}