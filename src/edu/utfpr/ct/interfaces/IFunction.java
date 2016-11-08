package edu.utfpr.ct.interfaces;

import java.io.Serializable;

public interface IFunction extends Serializable
{
	public int getPosition();

	public IFunction first();

	public boolean isLast();

	public IFunction next();

	public IFunction[] getValues();

	public String getName();
}
