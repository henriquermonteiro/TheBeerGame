package edu.utfpr.ct.report;

import edu.utfpr.ct.datamodel.AbstractNode;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.datamodel.TravellingTime;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

class CSVReport extends AbstractReport
{
	public CSVReport()
	{
		super(".csv");
	}

	@Override
	public boolean generateReport(Game game)
	{
		BufferedWriter bw;

		try
		{
			bw = new BufferedWriter(new FileWriter(createFile(getFileName(game))));
			writeGameConfig(bw, game);
			writePlayerMoves(bw, game);
			bw.close();
			return true;
		}
		catch(IOException e)
		{
			System.out.println("CSVReport::generateReport(Game game): " + e.getMessage());
			return false;
		}
	}

	@Override
	public Game[] loadReport()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	private void writeGameConfig(BufferedWriter bw, Game game) throws IOException
	{
		String header = "gameID, "
						+ "timestamp, "
						+ "name, "
						+ "password, "
						+ "missingUnitCost, "
						+ "stockUnitCost, "
						+ "sellingUnitProfit, "
						+ "realDuration, "
						+ "informedDuration, "
						+ "deliveryDelay, "
						+ "unitiesOnTravel, "
						+ "initialStock, "
						+ "informedChainSupply";

		String data = game.gameID + ", "
					  + game.timestamp + ", "
					  + game.name + ", "
					  + game.password + ", "
					  + game.missingUnitCost + ", "
					  + game.stockUnitCost + ", "
					  + game.sellingUnitProfit + ", "
					  + game.realDuration + ", "
					  + game.informedDuration + ", "
					  + game.deliveryDelay + ", "
					  + game.unitiesOnTravel + ", "
					  + game.initialStock + ", "
					  + game.informedChainSupply;

		bw.write("Configuration");
		bw.newLine();
		bw.write(header);
		bw.newLine();
		bw.write(data);
		bw.newLine();
		bw.newLine();
	}

	private void writePlayerMoves(BufferedWriter bw, Game game) throws IOException
	{
		Node node;

		bw.write("Player moves");
		bw.newLine();

		bw.write("Function, PlayerName, Stock, Profit");
		for(int i = 0; i < game.realDuration; i++)
			bw.write(", Week " + (i + 1));

		for(AbstractNode abstractNode : game.supplyChain)
		{
			if(abstractNode instanceof TravellingTime)
				continue;

			node = (Node) abstractNode;
			bw.newLine();
			bw.write(node.function.getName() + ", ");
			bw.write(node.playerName + ", ");
			bw.write(node.currentStock + ", ");
			bw.write(Double.toString(node.profit));

			for(int i = 0; i < node.playerMove.size(); i++)
				bw.write(", " + node.playerMove.get(i));
		}
	}
}
