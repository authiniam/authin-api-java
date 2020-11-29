package com.authin.iam.api.model.jwk;

public enum KeyType {

    RSA("RSA"),
    EC("EC"),
    ;

    private String type;

    KeyType(String type) {

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