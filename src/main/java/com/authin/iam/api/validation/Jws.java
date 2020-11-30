package com.authin.iam.api.validation;

import lombok.*;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

@Getter
class Jws extends Token {

    //region Private Fields
    private byte[] header;
    private byte[] payload;
    private byte[] signature;
    //endregion

    //region Constructors
    public Jws(byte[] header, byte[] payload, byte[] signature) {
        this.header = header;
        this.payload = payload;
        this.signature = signature;
    }
    //endregion

    //region Public Methods
    public String serializeCompact() throws UnsupportedEncodingException {

        return new String(Base64.getUrlEncoder().withoutPadding().encode(header), StandardCharsets.UTF_8.name()) +
                "." +
                new String(Base64.getUrlEncoder().withoutPadding().encode(payload), StandardCharsets.UTF_8.name()) +
                "." +
                new String(Base64.getUrlEncoder().withoutPadding().encode(signature), StandardCharsets.UTF_8.name());
    }
    //endregion
}
