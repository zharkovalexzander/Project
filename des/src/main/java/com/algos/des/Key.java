package com.algos.des;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Key {
    private Bits key;
    private List<Bits> generatedKeys;

    public Key(Bits key) {
        this.key = key;
        this.generatedKeys = new LinkedList<>();
        generateKeys();
    }

    protected void generateKeys() {
        int shiftPosition;
        Bits permutedKey = initialPermute(this.key);
        Bits c = permutedKey.getLeftHalf();
        Bits d = permutedKey.getRightHalf();
        for(int i = 1; i <= 16; ++i) {
            if(i == 1 || i == 2 || i == 9 || i == 16) {
                shiftPosition = 1;
            } else {
                shiftPosition = 2;
            }
            Bits cI = c.leftShift(shiftPosition);
            Bits dI = d.leftShift(shiftPosition);
            this.generatedKeys.add(Bits.permute(cI.merge(dI), Permutation.PC_2));
            c = cI;
            d = dI;
        }
    }

    protected Bits initialPermute(Bits bits) {
        return Bits.permute(this.key, Permutation.PC_1);
    }

    public Bits getKey(int index) {
        return this.generatedKeys.get(index);
    }

}
