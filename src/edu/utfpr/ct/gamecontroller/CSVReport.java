package edu.utfpr.ct.gamecontroller;

import edu.utfpr.ct.datamodel.AbstractNode;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.datamodel.TravellingTime;
import edu.utfpr.ct.interfaces.IReport;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CSVReport implements IReport
{
	@Override
	public boolean generateReport(Game game)
	{
		BufferedWriter bw;

		try
		{
			bw = createFile(game);
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

	private BufferedWriter createFile(Game game) throws IOException
	{
		DateFormat dateFormat;
		String fileName;
		BufferedWriter bw;

		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		fileName = dateFormat.format(new Date(game.timestamp));
		fileName = "Relatório " + fileName + " - " + game.name + ".txt";
		bw = new BufferedWriter(new FileWriter(new File(fileName)));

		return bw;
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
			bw.write(node.function.name() + ", ");
			bw.write(node.playerName + ", ");
			bw.write(node.currentStock + ", ");
			bw.write(Double.toString(node.profit));

			for(int i = 0; i < node.playerMove.size(); i++)
				bw.write(", " + node.playerMove.get(i));
		}
	}
}