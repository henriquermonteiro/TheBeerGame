package edu.utfpr.ct.datamodel;

import edu.utfpr.ct.localization.LocalizationKeys;
import edu.utfpr.ct.localization.Localize;

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
                            ret[k] = (k < inputs[2] ? inputs[0] : inputs[1]);
                        }
                    }
                }
                break;
        }

        return ret;
    }
    
    public Object[] getParamametersType(){
        Object[] ret = null;

        switch (position) {
            case 1:
                ret = new Object[9];
                ret[0] = LocalizationKeys.DEMAND_PARAMETER_SINGLESTEP_INIVAL;
                ret[1] = Integer.class;
                ret[2] = 5;
                ret[3] = LocalizationKeys.DEMAND_PARAMETER_SINGLESTEP_FINVAL;
                ret[4] = Integer.class;
                ret[5] = 10;
                ret[6] = LocalizationKeys.DEMAND_PARAMETER_SINGLESTEP_STEPWEEK;
                ret[7] = Integer.class;
                ret[8] = 10;
                break;
        }

        return ret;
    }

    @Override
    public String toString() {
        switch (position) {
            case 1:
                return Localize.getTextForKey(LocalizationKeys.DEMAND_TYPE_SINGLESTEP);
        }
        
        return "UNKNOW";
    }
    
    
}
