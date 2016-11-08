package edu.utfpr.ct.datamodel;

import edu.utfpr.ct.interfaces.IFunction;

public enum Function implements IFunction
{
	RETAILER(1),
	WHOLESALER(2),
	DISTRIBUTOR(3),
	PRODUCER(4);

	private final int position;

	Function(int position)
	{
		this.position = position;
	}

	@Override
	public String getName()
	{
		return this.name();
	}

	@Override
	public int getPosition()
	{
		return position;
	}

	@Override
	public IFunction[] getValues()
	{
		return Function.values();
	}

	@Override
	public boolean isLast()
	{
		//return (this.position == Function.values()[Function.values().length - 1].getPosition());
		return (this.position == Function.values().length);
	}

	@Override
	public IFunction first()
	{
		return Function.values()[0];
	}

	@Override
	public IFunction next()
	{
		if((this.position - 1) >= Function.values().length)
			return null;
		else
			return Function.values()[this.position];
	}
}
