package edu.utfpr.ct.gamecontroller;

import edu.utfpr.ct.logmanager.db.Create;
import edu.utfpr.ct.logmanager.db.Drop;

public class TheBeerGame
{
	public static void main(String[] args)
	{
		new Create().createTables();
		new Drop().dropTables();
	}
}
