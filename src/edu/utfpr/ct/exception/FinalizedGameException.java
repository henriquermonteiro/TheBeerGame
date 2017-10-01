package edu.utfpr.ct.exception;

public class FinalizedGameException extends Exception
{
	private static final long serialVersionUID = 6608165778360505212L;

	private static final String MESSAGE_TEXT = "The game requested is finalized. Use the report methods.";

	public FinalizedGameException()
	{
		super(MESSAGE_TEXT);
	}

	public FinalizedGameException(String string)
	{
		super(MESSAGE_TEXT);
	}

	public FinalizedGameException(String string, Throwable thrwbl)
	{
		super(MESSAGE_TEXT, thrwbl);
	}

	public FinalizedGameException(Throwable thrwbl)
	{
		super(MESSAGE_TEXT, thrwbl);
	}

	public FinalizedGameException(String string, Throwable thrwbl, boolean bln, boolean bln1)
	{
		super(MESSAGE_TEXT, thrwbl, bln, bln1);
	}

	public static void throwException() throws FinalizedGameException
	{
		throw new FinalizedGameException();
	}

	public static void throwException(Throwable thrwbl) throws FinalizedGameException
	{
		throw new FinalizedGameException(MESSAGE_TEXT, thrwbl);
	}

	public static void throwException(Throwable thrwbl, boolean bln, boolean bln1) throws FinalizedGameException
	{
		throw new FinalizedGameException(MESSAGE_TEXT, thrwbl, bln, bln1);
	}
}
