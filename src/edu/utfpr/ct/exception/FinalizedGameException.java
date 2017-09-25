package edu.utfpr.ct.exception;

public class FinalizedGameException extends Exception
{
	private static final String messageText = "The game requested is finalized. Use the report methods.";

	public FinalizedGameException()
	{
		super(messageText);
	}

	public FinalizedGameException(String string)
	{
		super(messageText);
	}

	public FinalizedGameException(String string, Throwable thrwbl)
	{
		super(messageText, thrwbl);
	}

	public FinalizedGameException(Throwable thrwbl)
	{
		super(messageText, thrwbl);
	}

	public FinalizedGameException(String string, Throwable thrwbl, boolean bln, boolean bln1)
	{
		super(messageText, thrwbl, bln, bln1);
	}

	public static void throwException() throws FinalizedGameException
	{
		throw new FinalizedGameException();
	}

	public static void throwException(Throwable thrwbl) throws FinalizedGameException
	{
		throw new FinalizedGameException(messageText, thrwbl);
	}

	public static void throwException(Throwable thrwbl, boolean bln, boolean bln1) throws FinalizedGameException
	{
		throw new FinalizedGameException(messageText, thrwbl, bln, bln1);
	}
}
