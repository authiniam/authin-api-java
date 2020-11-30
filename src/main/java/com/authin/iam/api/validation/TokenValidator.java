package com.authin.iam.api.validation;

import com.authin.iam.api.model.jwk.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import java.io.*;
import java.math.*;
import java.security.*;
import java.security.spec.*;
import java.util.*;

public class TokenValidator {

    public static ObjectNode validate(String token, Jwks jwks, String issuer, String audience) throws TokenValidationException {

        Jws jws = deserializeJwsCompact(token);

        JoseHeader joseHeader = deserializeJoseHeader(jws.getHeader());
        validateJoseHeaders(joseHeader);

        RsaJwk jwk = (RsaJwk) jwks.getKeys().get(0);
        PublicKey publicKey = convertToPublicKey(jwk);
        validateSignature(publicKey, jws);

        ObjectNode claims = deserializePayload(jws);

        String tokenType = claims.get("token_type").asText().toLowerCase();
        switch (tokenType) {
            case "access_token":
            case "id_token":
                validateAccessToken(claims, issuer, audience);
                break;
            case "logout_token":
                validateLogoutToken(claims, issuer, audience);
                break;
        }

        return claims;
    }

    private static PublicKey convertToPublicKey(RsaJwk jwk) throws TokenValidationException {
        byte[] modulusBytes = Base64.getUrlDecoder().decode(jwk.getModulus());
        byte[] exponentBytes = Base64.getUrlDecoder().decode(jwk.getExponent());
        BigInteger modulus = new BigInteger(1, modulusBytes);
        BigInteger exponent = new BigInteger(1, exponentBytes);
        RSAPublicKeySpec publicSpec = new RSAPublicKeySpec(modulus, exponent);

        try {
            KeyFactory factory = KeyFactory.getInstance("RSA");
            return factory.generatePublic(publicSpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new TokenValidationException(e);
        }
    }

    private static void validateSignature(PublicKey pubKey, Jws jws) throws TokenValidationException {
        String signed = urlDecode(jws.getHeader()) + "." + urlDecode(jws.getPayload());
        if (!verifySignature(pubKey, signed.getBytes(), jws.getSignature()))
            throw new TokenValidationException("Provided signature does not match payload.");
    }

    private static void validateJoseHeaders(JoseHeader joseHeader) throws TokenValidationException {
        if (joseHeader.getTyp() != TokenType.JWT)
            throw new TokenValidationException("Token type is not supported.");

        if (joseHeader.getAlg() != Jwa.RS256)
            throw new TokenValidationException("Token algorithm is not supported.");
    }

    private static ObjectNode deserializePayload(Jws jws) throws TokenValidationException {
        try {
            return new ObjectMapper().readValue(jws.getPayload(), ObjectNode.class);
        } catch (IOException e) {
            throw new TokenValidationException(e);
        }
    }

    private static Jws deserializeJwsCompact(String serialized) {
        String[] jwsSections = serialized.split("\\.");
        return new Jws(
                Base64.getUrlDecoder().decode(jwsSections[0]),
                Base64.getUrlDecoder().decode(jwsSections[1]),
                Base64.getUrlDecoder().decode(jwsSections[2]));
    }

    private static JoseHeader deserializeJoseHeader(byte[] headerBytes) throws TokenValidationException {
        try {
            return new ObjectMapper().readValue(headerBytes, JoseHeader.class);
        } catch (IOException e) {
            throw new TokenValidationException(e);
        }
    }

    private static boolean verifySignature(PublicKey pubKey, byte[] plainTextBytes, byte[] signature) throws TokenValidationException {
        try {
            Signature publicSignature = Signature.getInstance("SHA256withRSA");
            publicSignature.initVerify(pubKey);
            publicSignature.update(plainTextBytes);
            return publicSignature.verify(signature);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new TokenValidationException(e);
        }
    }

    private static String urlDecode(byte[] rawBytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(rawBytes);
    }

    private static void validateAccessToken(ObjectNode claims, String issuer, String audience) throws TokenValidationException {
        if (!claims.has("iss") || !claims.get("iss").asText().equals(issuer))
            throw new TokenValidationException("Invalid issuer");

        if (!claims.has("aud") || !claims.get("aud").asText().equals(audience))
            throw new TokenValidationException("Invalid audience");

        long totalSeconds = System.currentTimeMillis() / 1000;
        if (!claims.has("exp") || claims.get("exp").asLong() <= totalSeconds)
            throw new TokenValidationException("Expired token.");
    }

    private static void validateLogoutToken(ObjectNode claims, String issuer, String audience) throws TokenValidationException {

        if (!claims.has("iss") || !claims.get("iss").asText().equals(issuer))
            throw new TokenValidationException("Invalid issuer");

        if (!claims.has("aud") || !claims.get("aud").asText().equals(audience))
            throw new TokenValidationException("Invalid audience");

        if (!claims.has("sub") || !claims.has("sid"))
            throw new TokenValidationException("No sub or sid claim found");

        if (claims.has("nonce"))
            throw new TokenValidationException("Logout token must not contains nonce claim");

        if (!claims.has("events") || !claims.get("events").has("http://schemas.openid.net/event/backchannel-logout"))
            throw new TokenValidationException("Invalid events");

        long totalSeconds = System.currentTimeMillis() / 1000;
        if (!claims.has("exp") || claims.get("exp").asLong() <= totalSeconds)
            throw new TokenValidationException("Expired token");

    }
}
