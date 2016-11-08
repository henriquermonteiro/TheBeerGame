package edu.utfpr.ct.interfaces;

import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;

public interface ILogger2
{
	void logGameStart(Game game);

	void logPlayerMove(int gameID, Node node);

	void purgeGame(Integer gameID);

	Game[] getGames();
}
