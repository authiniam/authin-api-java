package com.authin.iam.api.validation;

enum TokenType {

    JWT("JWT");

    private String type;

    TokenType(String type) {

        this.type = type;
    }

    public String type() {

        return this.type;
    }
}
