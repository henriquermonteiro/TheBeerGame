package edu.utfpr.ct.datamodel;

import edu.utfpr.ct.localization.HostLocalizationKeys;
import edu.utfpr.ct.localization.HostLocalizationManager;

public enum DemandTypes
{
	SINGLE_STEP(1);

	private final int position;

	DemandTypes(int numVal)
	{
		this.position = numVal;
	}

	public int[] getDemandForParameter(int[] inputs)
	{
		int[] ret = null;

		switch(position)
		{
		case 1:
			if(inputs.length == 4)
				if(inputs[2] > 0)
				{
					ret = new int[inputs[3]];

					for(int k = 0; k < inputs[3]; k++)
						ret[k] = (k < inputs[2] ? inputs[0] : inputs[1]);
				}
			break;
		}

		return ret;
	}

	public Object[] getParamametersType()
	{
		Object[] ret = null;

		switch(position)
		{
		case 1:
			ret = new Object[9];
			ret[0] = HostLocalizationKeys.DEMAND_PARAMETER_SINGLESTEP_INIVAL;
			ret[1] = Integer.class;
			ret[2] = 5;
			ret[3] = HostLocalizationKeys.DEMAND_PARAMETER_SINGLESTEP_FINVAL;
			ret[4] = Integer.class;
			ret[5] = 10;
			ret[6] = HostLocalizationKeys.DEMAND_PARAMETER_SINGLESTEP_STEPWEEK;
			ret[7] = Integer.class;
			ret[8] = 10;
			break;
		}

		return ret;
	}

	@Override
	public String toString()
	{
		switch(position)
		{
		case 1:
			return HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(HostLocalizationKeys.DEMAND_TYPE_SINGLESTEP);
		default:
			return HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(HostLocalizationKeys.DEMAND_TYPE_UNKNOWN);
		}
	}
}
