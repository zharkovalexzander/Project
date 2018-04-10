package com.crypto.diffie;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class Main {
    private static final int[] ROOT_FACTORS = new int[] {
            1, 2, 3, 4, 5, 6, 7, 9, 10, 11, 13, 14, 17, 18, 19, 22, 23, 25, 26, 27, 29, 31, 34, 37, 38, 41, 43, 46, 47,
            49, 50, 53, 54, 58, 59, 61, 62, 67, 71, 73, 74, 79, 81, 82, 83, 86, 89, 94, 97, 98, 101, 103, 106, 107, 109,
            113, 118, 121, 122, 125, 127, 131, 134, 137, 139
    };

    private static final BigInteger MODULUS = createModulus();
    private static final BigInteger BASE = generateBase();

    public static void main(String[] args) {
        for (int i = 0; i < 1000; ++i) {
            DiffieProcessor user1 = new DiffieProcessor(MODULUS, BASE);
            DiffieProcessor user2 = new DiffieProcessor(MODULUS, BASE);

            user1.generateSecret();
            user2.generateSecret();

            System.out.println("User 1 secret is : " + user1.peekSecret());
            System.out.println("User 2 secret is : " + user2.peekSecret());

            BigInteger secret1 = user1.calculateSecret(user2.createPublicTransport());
            BigInteger secret2 = user2.calculateSecret(user1.createPublicTransport());

            System.out.println("Shared secret is : " + secret1 + " == " + secret2 + " => " + secret1.equals(secret2));
        }
    }

    private static BigInteger createModulus() {
        BigInteger bi = BigInteger.probablePrime(32, new SecureRandom());
        while (!returnPrime(bi.subtract(BigInteger.ONE).divide(BigInteger.valueOf(2)))) {
            bi = BigInteger.probablePrime(32, new SecureRandom());
        }
        return bi;
    }

    private static boolean returnPrime(BigInteger number) {
        if (!number.isProbablePrime(5))
            return false;

        BigInteger two = new BigInteger("2");
        if (!two.equals(number) && BigInteger.ZERO.equals(number.mod(two)))
            return false;

        for (BigInteger i = new BigInteger("3"); i.multiply(i).compareTo(number) < 1; i = i.add(two)) { //start from 3, 5, etc. the odd number, and look for a divisor if any
            if (BigInteger.ZERO.equals(number.mod(i))) //check if 'i' is divisor of 'number'
                return false;
        }
        return true;
    }

    private static BigInteger generateBase() {
        return BigInteger.valueOf(ROOT_FACTORS[new Random().nextInt(ROOT_FACTORS.length)]);
    }
}
