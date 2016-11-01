package edu.utfpr.ct.datamodel;

public enum Function
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

	public int getPosition()
	{
		return position;
	}

	public Function previous(Function function)
	{
		int position;

		position = Math.abs(function.getPosition() - 1);
		position = position % Function.values().length;

		return Function.values()[position];
	}

	public Function next(Function function)
	{
		int position;

		position = (function.getPosition() + 1);
		position = position % Function.values().length;

		return Function.values()[position];
	}
}
