package edu.utfpr.ct.gamecontroller;

import edu.utfpr.ct.logmanager.db.Create;

public class TheBeerGame
{
	public static void main(String[] args)
	{
		new Create().createTables();
	}
}
