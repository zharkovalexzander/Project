package com.crypto.diffie;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Set;

public class DiffieProcessor {
    private BigInteger modulus;
    private BigInteger base;
    private BigInteger secret;

    public DiffieProcessor(BigInteger modulus, BigInteger base) {
        this.base = base;
        this.modulus = modulus;
        this.secret = null;
    }

    public void generateSecret() {
        this.secret = new BigInteger(32, new SecureRandom());
        if (this.secret.compareTo(BigInteger.ZERO) < 0) {
            this.secret = this.secret.negate();
        }
    }

    public void setSecret(BigInteger secret) {
        this.secret = secret;
    }

    public BigInteger peekSecret() {
        return secret;
    }

    public BigInteger createPublicTransport() {
        return base.modPow(secret, modulus);
    }

    public BigInteger calculateSecret(BigInteger publicTransport) {
        return publicTransport.modPow(secret, modulus);
    }

}
