package edu.utfpr.ct.tests;

import edu.utfpr.ct.datamodel.AbstractNode;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.datamodel.TravellingTime;
import edu.utfpr.ct.logmanager.Logger;
import java.util.Random;

public class LoggerTest
{
	private Game reference;
	private Game retrieved;

	public void test()
	{
		int qtd;
		Logger logger = new Logger();
		Comparator comparator = new Comparator();

		reference = new GameBuilderTest().test();
		logger.logGameStart(reference);
		fillPLayerMoves(logger);

		retrieved = logger.retrieveGameData(logger.getUnfinishedGamesID()[0].gameID);
		comparator.compareAll(reference, retrieved);

		logger.purgeGame(retrieved.gameID);
		qtd = logger.getUnfinishedGamesID().length;
		System.out.println("Qtd de jogos depois do purge: " + qtd);
	}

	private void fillPLayerMoves(Logger logger)
	{
		Node node;
		Random random = new Random();

		for(AbstractNode abstractNode : reference.supplyChain)
		{
			if(abstractNode instanceof TravellingTime)
				continue;

			node = (Node) abstractNode;
			node.playerMove.clear();

			for(int i = 0; i < reference.realDuration; i++)
			{
				node.playerMove.add(Math.abs(random.nextInt()));
				logger.logPlayerMove(reference.gameID, node);
			}
		}
	}
}
