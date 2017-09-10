package edu.utfpr.ct.dataextractor;

import edu.utfpr.ct.datamodel.Game;
import java.util.HashMap;

public class DataExtractor
{
	private static DataExtractor dataExtractor;
	private final HashMap<String, Table> map;

	private DataExtractor()
	{
		this.map = new HashMap<>();
	}

	public static DataExtractor getDataExtractor()
	{
		if(dataExtractor == null)
			dataExtractor = new DataExtractor();

		return dataExtractor;
	}

	public Table addTable(Game game)
	{
		return map.put(game.name, new Table(game));
	}

	public Table getTable(String gameName)
	{
		return map.get(gameName);
	}

	public Table removeTable(String gameName)
	{
		return map.remove(gameName);
	}
}
