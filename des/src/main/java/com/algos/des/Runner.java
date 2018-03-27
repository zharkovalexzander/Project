package com.algos.des;

import org.apache.commons.codec.BinaryDecoder;

import java.util.LinkedList;
import java.util.List;

public class Runner {

    public static void main(String[] args) {
        long value = 0x8787878787878787L;
        long key = 0xE329232EA6D0D73L;
        String v = "8787878787878787";
        /*DESProcessor desProcessor = new DESProcessor(key);
        String encoded = desProcessor.encode(v);
        System.out.println(encoded);*/
        /*long encoded = desProcessor.encode(value);
        System.out.println(Long.toHexString(encoded));*/
    }
}
