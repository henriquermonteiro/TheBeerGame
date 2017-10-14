package edu.utfpr.ct.report;

import edu.utfpr.ct.datamodel.AbstractNode;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.datamodel.TravellingTime;
import edu.utfpr.ct.gamecontroller.Table;
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
	public boolean generateReport(Table table)
	{
		BufferedWriter bw;

		try
		{
			bw = new BufferedWriter(new FileWriter(createFile(getFileName(table.getGame()))));
			writeGameConfig(bw, table.getGame());
			writeClientData(bw, table.getGame());
			writeNodeData(bw, table);
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

	private void writeNodeData(BufferedWriter bw, Table table) throws IOException
	{
		Node node;

		for(AbstractNode abstractNode : table.getGame().supplyChain)
		{
			if(abstractNode instanceof TravellingTime)
				continue;
			node = (Node) abstractNode;

			bw.write(node.function.getName());
			bw.newLine();

			bw.write("Week, "
					 + "Initial stock, "
					 + "Order received, "
					 + "Previous pending order, "
					 + "Expected delivery, "
					 + "Actual delivery, "
					 + "Order unfullfiled, "
					 + "Final stock, "
					 + "Player move, "
					 + "Confirmed order delivery,"
					 + "Incoming order, "
					 + "Unfulfillment cost, "
					 + "Stock cost, "
					 + "Profit, "
					 + "Week balance");
			bw.newLine();

			for(Table.Line line : table.getByFunction(node.function))
			{
				bw.write(line.week + ", ");
				bw.write(line.initialStock + ", ");
				bw.write(line.orderReceived + ", ");
				bw.write(line.orderPreviousPending + ", ");
				bw.write(line.expectedDelivery + ", ");
				bw.write(line.actualyDelivery + ", ");
				bw.write(line.orderUnfullfiled + ", ");
				bw.write(line.finalStock + ", ");
				bw.write(line.playerMove + ", ");
				bw.write(line.confirmedOrderDelivery + ", ");
				bw.write(line.incomingOrder + ", ");
				bw.write(line.costUnfulfillment.toString() + ", ");
				bw.write(line.costStock.toString() + ", ");
				bw.write(line.profit.toString() + ", ");
				bw.write(line.weekBalance.toString() + ", ");
				bw.newLine();
			}
		}
	}
}
