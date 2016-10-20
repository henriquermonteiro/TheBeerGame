package edu.utfpr.ct.datamodel;

public enum Function
{
	RETAILER(1),
	WHOLESALER(2),
	DISTRIBUTOR(3),
	PRODUCER(4);

	private final int position;
	
	Function(int numVal)
	{
		this.position = numVal;
	}

	public int getPosition()
	{
		return position;
	}
}
