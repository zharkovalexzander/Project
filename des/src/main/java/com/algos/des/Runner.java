package com.algos.des;

import java.util.LinkedList;
import java.util.List;

public class Runner {
    private static final Permutation[] FUNCTIONS = new Permutation[] {
            Permutation.S1, Permutation.S2, Permutation.S3, Permutation.S4,
            Permutation.S5, Permutation.S6, Permutation.S7, Permutation.S8
    };

    public static void main(String[] args) {
        long value = 0x0123456789ABCDEFL;
        Bits bits = Bits.valueOf(value);
        Bits IP = Bits.permute(bits, Permutation.INITIAL_PERMUTATION);
        Bits lS = IP.getLeftHalf();
        Bits rS = IP.getRightHalf();
        long key = 0x133457799BBCDFF1L;
        Key key1 = new Key(Bits.valueOf(key));
        Bits functionResult = new Bits();
        for (int i = 0; i < 16; i++) {
            List<Bits> splitted = new LinkedList<>();
            Bits e = Bits.permute(rS, Permutation.E_BIT_SELECTION);
            Bits func = e.xor(key1.getKey(i));
            for (int j = 0; j < 48; j += 6) {
                Bits result = func.partition(j, j + 6);
                Bits modified = result.applyFunction(FUNCTIONS[j / 6]);
                functionResult = functionResult.merge(modified);
            }
            functionResult = Bits.permute(functionResult, Permutation.P);
            Bits tempR = lS.xor(functionResult);
            lS = rS;
            rS = tempR;
            /*System.out.println(lS);
            System.out.println(rS);*/
        }
       // Bits b = rS.merge(lS);
        System.out.println(lS);
    }
}
