/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.datamodel;

/**
 *
 * @author henrique
 */
public enum DemandTypes {
    SINGLE_STEP(1);

    private final int position;

    DemandTypes(int numVal) {
        this.position = numVal;
    }

    public int[] getDemandForParameter(int[] inputs) {
        int[] ret = null;

        switch (position) {
            case 1:
                if (inputs.length == 4) {
                    if (inputs[2] > 0 && inputs[2] <= inputs[3]) {
                        ret = new int[inputs[3]];

                        for (int k = 0; k < inputs[3]; k++) {
                            ret[k++] = (k < inputs[2] ? inputs[0] : inputs[1]);
                        }
                    }
                }
                break;
        }

        return ret;
    }
}
