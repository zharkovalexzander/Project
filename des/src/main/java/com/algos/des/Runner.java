package com.algos.des;

import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.DecoderException;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

public class Runner {

    public static void main(String[] args) throws DecoderException, UnsupportedEncodingException {
        long value = 0x44d63951be2a3bf0L;
        long key = 0xE329232EA6D0D73L;
        String v = "Your lips are smoother than vaseline\r\n";
        DESProcessor desProcessor = new DESProcessor(key);
        /*String encoded = desProcessor.encode(v);
        System.out.println(encoded);
        String decoded = desProcessor.decode(encoded);
        System.out.println(decoded);*/
        long encoded = desProcessor.encode(value);
        System.out.println(Long.toHexString(encoded));
        long decoded = desProcessor.decode(encoded);
        System.out.println(Long.toHexString(decoded));
    }
}
