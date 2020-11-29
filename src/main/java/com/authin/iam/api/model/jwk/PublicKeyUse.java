package com.authin.iam.api.model.jwk;

public enum PublicKeyUse {

    SIGNATURE("sig"),
    ENCRYPTION("enc"),
    ;

    private String type;

    PublicKeyUse(String type) {

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