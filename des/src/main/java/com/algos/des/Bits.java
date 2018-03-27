package com.algos.des;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.*;

public class Bits {
    private List<Integer> bits;

    private Bits(List<Integer> bits) {
        this.bits = bits;
    }

    public Bits() {
        this.bits = new LinkedList<>();
    }

    public static Bits valueOf(long value) {
        List<Integer> list = new LinkedList<>();
        byte[] stringInBytes = longToBytes(value);
        int length = stringInBytes.length * 8;
        int trailingZeroes = length % 64;
        trailingZeroes = (trailingZeroes == length) ? (64 - length) : trailingZeroes;
        for (int i = 0; i < trailingZeroes; ++i) {
            list.add(0);
        }
        for (byte stringInByte : stringInBytes) {
            for (int j = 7; j >= 0; --j) {
                list.add((stringInByte >> j) & 1);
            }
        }
        return new Bits(list);
    }

    public static Bits valueOf(String value) throws DecoderException {
        List<Integer> list = new LinkedList<>();
        byte[] stringInBytes = value.getBytes();
        String hexed = Hex.encodeHexString(stringInBytes);
        int trailingZeroes = hexed.length() % 16;
        trailingZeroes = (trailingZeroes == hexed.length()) ? hexed.length() : trailingZeroes;
        if(trailingZeroes != 0) {
            for (int i = 0; i < (16 - trailingZeroes); ++i) {
                hexed += "0";
            }
        }
        System.out.println(hexed);
        for(int i = 0; i < hexed.length(); i += 16) {
            byte[] bytes = Hex.decodeHex(hexed.substring(i, i + 16).toCharArray());
            for (byte stringInByte : bytes) {
                for (int j = 7; j >= 0; --j) {
                    list.add((stringInByte >> j) & 1);
                }
            }
        }
        return new Bits(list);
    }

    public static Bits of(long value) {
        return new Bits(longToBits(value));
    }

    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    public static List<Integer> longToBits(long x) {
        long temp = x;
        List<Integer> list = new LinkedList<>();
        while (temp != 0) {
            long last = temp & 1;
            list.add((int)last);
            temp >>= 1;
        }
        while (list.size() != 4) {
            list.add(0);
        }
        Collections.reverse(list);
        return list;
    }

    public int size() {
        return this.bits.size();
    }

    public Integer get(int index) {
        return this.bits.get(index);
    }

    public static Bits permute(Bits bits, Permutation permutation) {
        List<Integer> bitsPermuted = new LinkedList<>();
        int[][] permutations = permutation.getOrder();
        for(int i = 0; i < permutations.length; ++i) {
            for(int j = 0; j < permutations[i].length; ++j) {
                bitsPermuted.add(bits.get(permutations[i][j] - 1));
            }
        }
        return new Bits(bitsPermuted);
    }

    public Bits getLeftHalf() {
        return partition(0, this.bits.size() / 2);
    }

    public Bits getRightHalf() {
        return partition(this.bits.size() / 2, this.bits.size());
    }

    public Bits partition(int start, int finish) {
        return new Bits(this.bits.subList(start, finish));
    }

    public Bits leftShift(int positions) {
        List<Integer> list = new LinkedList<>();
        for (int i = positions; i < this.bits.size(); ++i) {
            list.add(this.bits.get(i));
        }
        for (int i = 0; i < positions; ++i) {
            list.add(this.bits.get(i));
        }
        return new Bits(list);
    }

    public Bits merge(Bits bits) {
        List<Integer> merged = new LinkedList<>();
        merged.addAll(this.bits);
        merged.addAll(bits.bits);
        return new Bits(merged);
    }

    public Bits xor(Bits bits) {
        List<Integer> list = new LinkedList<>();
        for (int i = 0; i < this.bits.size(); ++i) {
            list.add(this.bits.get(i) == bits.get(i) ? 0 : 1);
        }
        return new Bits(list);
    }

    public Bits applyFunction(Permutation permutation) {
        int row = (this.bits.get(0) << 1) | this.bits.get(this.bits.size() - 1);
        int col = (int) partition(1, this.bits.size() - 1).toLong();
        return Bits.of(permutation.getOrder()[row][col]);
    }

    public long toLong() {
        long result = this.bits.get(0);
        for (int i = 1; i < this.bits.size(); ++i) {
            result <<= 1;
            result |= this.bits.get(i);
        }
        return result;
    }

    public String toFormattedString() {
        String a = "";
        for(Integer i : this.bits) {
            a += i.toString();
        }
        Long l = new BigInteger(a, 2).longValue();;
        return Long.toHexString(l);
    }

    @Override
    public String toString() {
        return this.bits.toString();
    }
}
