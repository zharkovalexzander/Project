package com.algos.des;

import org.apache.commons.codec.DecoderException;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

public class DESProcessor {
    private static final Permutation[] FUNCTIONS = new Permutation[] {
            Permutation.S1, Permutation.S2, Permutation.S3, Permutation.S4,
            Permutation.S5, Permutation.S6, Permutation.S7, Permutation.S8
    };

    private long initialKey;
    private Key generatedKeys;

    public DESProcessor(long key) {
        this.initialKey = key;
        generateKeys();
    }

    private void generateKeys() {
        generatedKeys = new Key(Bits.valueOf(this.initialKey));
    }

    public long encode(long value) {
        Bits bits = Bits.valueOf(value);
        Bits IP = Bits.permute(bits, Permutation.INITIAL_PERMUTATION);
        Bits lS = IP.getLeftHalf();
        Bits rS = IP.getRightHalf();
        Bits functionResult;
        for (int i = 0; i < 16; i++) {
            functionResult = new Bits();
            Bits e = Bits.permute(rS, Permutation.E_BIT_SELECTION);
            Bits func = e.xor(this.generatedKeys.getKey(i));
            for (int j = 0; j < 48; j += 6) {
                Bits result = func.partition(j, j + 6);
                Bits modified = result.applyFunction(FUNCTIONS[j / 6]);
                functionResult = functionResult.merge(modified);
            }
            functionResult = Bits.permute(functionResult, Permutation.P);
            Bits tempR = lS.xor(functionResult);
            lS = rS;
            rS = tempR;
        }
        Bits merged = rS.merge(lS);
        return Bits.permute(merged, Permutation.FINAL_PERMUTATION).toLong();
    }

    public long decode(long value) {
        Bits bits = Bits.valueOf(value);
        Bits IP = Bits.permute(bits, Permutation.INITIAL_PERMUTATION);
        Bits lS = IP.getLeftHalf();
        Bits rS = IP.getRightHalf();
        Bits functionResult;
        for (int i = 15; i >= 0; --i) {
            functionResult = new Bits();
            Bits e = Bits.permute(rS, Permutation.E_BIT_SELECTION);
            Bits func = e.xor(this.generatedKeys.getKey(i));
            for (int j = 0; j < 48; j += 6) {
                Bits result = func.partition(j, j + 6);
                Bits modified = result.applyFunction(FUNCTIONS[j / 6]);
                functionResult = functionResult.merge(modified);
            }
            functionResult = Bits.permute(functionResult, Permutation.P);
            Bits tempR = lS.xor(functionResult);
            lS = rS;
            rS = tempR;
        }
        Bits merged = rS.merge(lS);
        return Bits.permute(merged, Permutation.FINAL_PERMUTATION).toLong();
    }

    public String encode(String value) throws DecoderException, UnsupportedEncodingException {
        List<Bits> subs = new LinkedList<>();
        try {
            Bits bits = Bits.valueOf(value);
            if (bits.size() > 64) {
                for(int i = 0; i < bits.size(); i += 64) {
                    subs.add(bits.partition(i, i + 64));
                }
            } else {
                subs.add(bits);
            }
        } catch (DecoderException e) {
            e.printStackTrace();
        }
       return encodeStrings(subs);
    }

    public String decode(String value) throws UnsupportedEncodingException, DecoderException {
        List<Bits> subs = new LinkedList<>();
        try {
            Bits bits = Bits.valueOfHex(value);
            if (bits.size() > 64) {
                for(int i = 0; i < bits.size(); i += 64) {
                    subs.add(bits.partition(i, i + 64));
                }
            } else {
                subs.add(bits);
            }
        } catch (DecoderException e) {
            e.printStackTrace();
        }
        return decodeStrings(subs);
    }

    private String encodeStrings(List<Bits> strings) throws DecoderException, UnsupportedEncodingException {
        String resultString = "";
        for(Bits bits : strings) {
            Bits IP = Bits.permute(bits, Permutation.INITIAL_PERMUTATION);
            Bits lS = IP.getLeftHalf();
            Bits rS = IP.getRightHalf();
            Bits functionResult;
            for (int i = 0; i < 16; i++) {
                functionResult = new Bits();
                Bits e = Bits.permute(rS, Permutation.E_BIT_SELECTION);
                Bits func = e.xor(this.generatedKeys.getKey(i));
                for (int j = 0; j < 48; j += 6) {
                    Bits result = func.partition(j, j + 6);
                    Bits modified = result.applyFunction(FUNCTIONS[j / 6]);
                    functionResult = functionResult.merge(modified);
                }
                functionResult = Bits.permute(functionResult, Permutation.P);
                Bits tempR = lS.xor(functionResult);
                lS = rS;
                rS = tempR;
            }
            Bits merged = rS.merge(lS);
            Bits resulted = Bits.permute(merged, Permutation.FINAL_PERMUTATION);
            resultString += resulted.toFormattedHexString();
        }
        return resultString;
    }

    private String decodeStrings(List<Bits> strings) throws UnsupportedEncodingException, DecoderException {
        String resultString = "";
        for(Bits bits : strings) {
            Bits IP = Bits.permute(bits, Permutation.INITIAL_PERMUTATION);
            Bits lS = IP.getLeftHalf();
            Bits rS = IP.getRightHalf();
            Bits functionResult;
            for (int i = 15; i >= 0; --i) {
                functionResult = new Bits();
                Bits e = Bits.permute(rS, Permutation.E_BIT_SELECTION);
                Bits func = e.xor(this.generatedKeys.getKey(i));
                for (int j = 0; j < 48; j += 6) {
                    Bits result = func.partition(j, j + 6);
                    Bits modified = result.applyFunction(FUNCTIONS[j / 6]);
                    functionResult = functionResult.merge(modified);
                }
                functionResult = Bits.permute(functionResult, Permutation.P);
                Bits tempR = lS.xor(functionResult);
                lS = rS;
                rS = tempR;
            }
            Bits merged = rS.merge(lS);
            Bits resulted = Bits.permute(merged, Permutation.FINAL_PERMUTATION);
            resultString += resulted.toFormattedString();
        }
        return resultString;
    }


}
