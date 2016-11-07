package edu.utfpr.ct.report;

import edu.utfpr.ct.datamodel.AbstractNode;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.datamodel.TravellingTime;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

class HTMLReport extends AbstractReport
{
	public HTMLReport()
	{
		super(".html");
	}

	@Override
	public boolean generateReport(Game game)
	{
		BufferedWriter bw;

		try
		{
			bw = new BufferedWriter(new FileWriter(createFile(getFileName(game))));
			writeHeader(bw);
			writeGameConfig(bw, game);
			writePlayerMoves(bw, game);
			writeFoot(bw);
			bw.close();
			return true;
		}
		catch(IOException e)
		{
			System.out.println("HTMLReport::generateReport(Game game): " + e.getMessage());
			return false;
		}
	}

	@Override
	public Game[] loadReport()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	private void writeHeader(BufferedWriter bw) throws IOException
	{
		String header = "<!DOCTYPE html> \n"
						+ "<html> \n"
						+ "<head> \n"
						+ "<style> \n"
						+ "table \n"
						+ "{ \n"
						+ "    font-family: arial, sans-serif; \n"
						+ "    border-collapse: collapse; \n"
						+ "    width: 100%; \n"
						+ "    border: 1px solid #dddddd; \n"
						+ "} \n"
						+ "\n"
						+ "td, th \n"
						+ "{ \n"
						+ "    border: 1px solid #dddddd; \n"
						+ "    text-align: left; \n"
						+ "    padding: 8px; \n"
						+ "} \n"
						+ "</style> \n"
						+ "</head> \n"
						+ "<body> \n";

		bw.write(header);
		bw.newLine();
	}

	private void writeFoot(BufferedWriter bw) throws IOException
	{
		String foot = "</body> \n"
					  + "</html>";

		bw.write(foot);
		bw.newLine();
	}

	private void writeGameConfig(BufferedWriter bw, Game game) throws IOException
	{
		bw.write("<b><h1>Configuration: </h1></b><br>");
		bw.newLine();
		bw.write("<b>gameID: </b>" + game.gameID + "<br>");
		bw.newLine();
		bw.write("<b>timestamp: </b>" + game.timestamp + "<br>");
		bw.newLine();
		bw.write("<b>name: </b>" + game.name + "<br>");
		bw.newLine();
		bw.write("<b>password: </b>" + game.password + "<br>");
		bw.newLine();
		bw.write("<b>missingUnitCost: </b>" + game.missingUnitCost + "<br>");
		bw.newLine();
		bw.write("<b>stockUnitCost: </b>" + game.stockUnitCost + "<br>");
		bw.newLine();
		bw.write("<b>sellingUnitProfit: </b>" + game.sellingUnitProfit + "<br>");
		bw.newLine();
		bw.write("<b>realDuration: </b>" + game.realDuration + "<br>");
		bw.newLine();
		bw.write("<b>informedDuration: </b>" + game.informedDuration + "<br>");
		bw.newLine();
		bw.write("<b>deliveryDelay: </b>" + game.deliveryDelay + "<br>");
		bw.newLine();
		bw.write("<b>unitiesOnTravel: </b>" + game.unitiesOnTravel + "<br>");
		bw.newLine();
		bw.write("<b>initialStock: </b>" + game.initialStock + "<br>");
		bw.newLine();
		bw.write("<b>informedChainSupply: </b>" + game.informedChainSupply + "<br>");
		bw.newLine();
		bw.newLine();
	}

	private void writePlayerMoves(BufferedWriter bw, Game game) throws IOException
	{
		Node node;

		bw.write("<b><h1>Player moves </h1></b>");
		bw.newLine();

		bw.write("<table>");
		bw.newLine();

		bw.write("<tr><th>Function</th><th>PlayerName</th><th>Stock</th><th>Profit</th>");
		for(int i = 0; i < game.realDuration; i++)
			bw.write("<th>Week " + (i + 1) + "</th>");

		bw.write("</tr>");

		for(AbstractNode abstractNode : game.supplyChain)
		{
			if(abstractNode instanceof TravellingTime)
				continue;

			node = (Node) abstractNode;
			bw.newLine();
			bw.write("<tr>");
			bw.write("<td>" + node.function.getName() + "</td>");
			bw.write("<td>" + node.playerName + "</td>");
			bw.write("<td>" + node.currentStock + "</td>");
			bw.write("<td>" + node.profit + "</td>");

			for(int i = 0; i < node.playerMove.size(); i++)
				bw.write("<td>" + node.playerMove.get(i) + "</td>");

			bw.write("</tr>");
		}

		bw.newLine();
		bw.write("</table>");
		bw.newLine();
	}
}
