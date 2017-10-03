package edu.utfpr.ct.report;

import edu.utfpr.ct.datamodel.AbstractNode;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.datamodel.TravellingTime;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
			writeClientData(bw, game);
			writeNodeData(bw, game);
			bw.close();
			return true;
		}
		catch(Exception e)
		{
			System.out.println("CSVReport::generateReport(Game game): " + e.getMessage());
			return false;
		}
	}

	@Override
	public Game[] loadReports()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	private void writeGameConfig(BufferedWriter bw, Game game) throws IOException
	{
		String header = "ID, "
						+ "Timestamp, "
						+ "Name, "
						+ "Password, "
						+ "Cost of missing unit, "
						+ "Cost of unit in stock, "
						+ "Profit per unit, "
						+ "Real duration, "
						+ "Informed duration, "
						+ "Delivery delay, "
						+ "Units on travel, "
						+ "Initial stock, "
						+ "Informed chain supply";

		String data = game.gameID + ", "
					  + new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date(game.timestamp)) + ", "
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

		new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date(game.timestamp));

		bw.write("Configuration");
		bw.newLine();
		bw.write(header);
		bw.newLine();
		bw.write(data);
		bw.newLine();
		bw.newLine();
	}

	private void writeClientData(BufferedWriter bw, Game game) throws IOException
	{
		bw.write("Client");
		bw.newLine();

		for(int i = 0; i < game.realDuration; i++)
			bw.write(", Week " + (i + 1));
		bw.newLine();

		bw.write("Order");
		for(int value : game.demand)
			bw.write(", " + value);
		bw.newLine();
		bw.newLine();
	}

	private void writeNodeData(BufferedWriter bw, Game game) throws IOException
	{
		Node node;

		for(AbstractNode abstractNode : game.supplyChain)
		{
			if(abstractNode instanceof TravellingTime)
				continue;
			node = (Node) abstractNode;

			bw.write(node.function.getName());
			bw.newLine();

			for(int i = 0; i <= game.realDuration; i++)
				bw.write(", Week " + (i + 1));
			bw.newLine();

			bw.write("Stock");
			for(int value : node.currentStock)
				bw.write(", " + value);
			bw.newLine();

			bw.write("Move");
			for(int value : node.playerMove)
				bw.write(", " + value);
			bw.newLine();

			bw.write("Profit");
			for(double value : node.profit)
				bw.write(", " + value);
			bw.newLine();
			bw.newLine();
		}
	}
}
