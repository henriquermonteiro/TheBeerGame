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
			//writePlayerMoves(bw, game);
			writeNodeData(bw, game);
			writeFoot(bw);
			bw.close();
			return true;
		}
		catch(Exception e)
		{
			System.out.println("HTMLReport::generateReport(Game game): " + e.getMessage());
			return false;
		}
	}

	@Override
	public Game[] loadReports()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	private void writeHeader(BufferedWriter bw) throws IOException
	{
		String header = "<!DOCTYPE html> \n"
						+ "<html> \n"
						+ "<head> \n"
						+ "<meta charset=\"UTF-8\"> \n"
						+ "<script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script> \n"
						+ "<script type=\"text/javascript\"> \n"
						+ "  google.charts.load('current', {'packages':['corechart']}); \n"
						+ "  google.charts.setOnLoadCallback(drawChart); \n\n"
						+ "  function drawChart() { \n"
						+ "    var data = google.visualization.arrayToDataTable([ \n"
						+ "      ['Year', 'Sales', 'Expenses'],\n"
						+ "      ['2004',  1000,      400],\n"
						+ "      ['2005',  1170,      460],\n"
						+ "      ['2006',  660,       1120],\n"
						+ "      ['2007',  1030,      540]\n"
						+ "    ]); \n"
						+ "\n"
						+ "    var options = {\n"
						+ "      title: 'Company Performance',\n"
						+ "      curveType: 'function',\n"
						+ "      legend: { position: 'bottom' } \n"
						+ "    };\n"
						+ "\n"
						+ "    var chart = new google.visualization.LineChart(document.getElementById('curve_chart')); \n"
						+ "\n"
						+ "    chart.draw(data, options);\n"
						+ "  }\n"
						+ "</script>"
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

//						+ "  function drawChart() { \n"
//						+ "    var data = google.visualization.arrayToDataTable([ \n"
//						+ "      ['Year', 'Sales', 'Expenses'],\n"
//						+ "      ['2004',  1000,      400],\n"
//						+ "      ['2005',  1170,      460],\n"
//						+ "      ['2006',  660,       1120],\n"
//						+ "      ['2007',  1030,      540]\n"
//						+ "    ]); \n"
//						+ "\n"
//						+ "    var options = {\n"
//						+ "      title: 'Company Performance',\n"
//						+ "      curveType: 'function',\n"
//						+ "      legend: { position: 'bottom' } \n"
//						+ "    };\n"
//						+ "\n"
//						+ "    var chart = new google.visualization.LineChart(document.getElementById('curve_chart')); \n"
//						+ "\n"
//						+ "    chart.draw(data, options);\n"
//						+ "  }\n"
//		matrix = "[['Stock', 'Move', 'Profit']";	
	private String createStockMatrix(Game game, BufferedWriter bw)
	{
		Node node;
		String function;

		function = "function drawStock() { \n"
				   + "var data = google.visualization.arrayToDataTable([ \n";

		function += "[";
		for(AbstractNode abstractNode : game.supplyChain)
			if(!(abstractNode instanceof TravellingTime))
				function += "'" + ((Node) abstractNode).function.getName() + "', \n";
		function += "]";

		return function;
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
		bw.write("<b><h1>Configuration </h1></b><br>");
		bw.newLine();
		bw.write("<b>ID: </b>" + game.gameID + "<br>");
		bw.newLine();
		bw.write("<b>Timestamp: </b>" + new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date(game.timestamp)) + "<br>");
		bw.newLine();
		bw.write("<b>Name: </b>" + game.name + "<br>");
		bw.newLine();
		bw.write("<b>Password: </b>" + game.password + "<br>");
		bw.newLine();
		bw.write("<b>Cost of missing unit: </b>" + game.missingUnitCost + "<br>");
		bw.newLine();
		bw.write("<b>Cost of unit in stock: </b>" + game.stockUnitCost + "<br>");
		bw.newLine();
		bw.write("<b>Profit per unit: </b>" + game.sellingUnitProfit + "<br>");
		bw.newLine();
		bw.write("<b>Real duration: </b>" + game.realDuration + "<br>");
		bw.newLine();
		bw.write("<b>Informed duration: </b>" + game.informedDuration + "<br>");
		bw.newLine();
		bw.write("<b>Delivery delay: </b>" + game.deliveryDelay + "<br>");
		bw.newLine();
		bw.write("<b>Units on travel: </b>" + game.unitiesOnTravel + "<br>");
		bw.newLine();
		bw.write("<b>Initial stock: </b>" + game.initialStock + "<br>");
		bw.newLine();
		bw.write("<b>Informed chain supply: </b>" + game.informedChainSupply + "<br>");
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

			bw.write("<b><h1>" + node.function.getName() + "</h1></b>");
			bw.newLine();

			bw.write("<table>");
			bw.newLine();

			/*Header*/
			bw.write("<tr><th></th>");
			for(int i = 0; i < game.realDuration; i++)
				bw.write("<th>Week " + (i + 1) + "</th>");
			bw.newLine();

			bw.write("<tr><th>Stock</th>");
			for(int value : node.currentStock)
				bw.write("<th>" + value + "</th>");
			bw.newLine();

			bw.write("<tr><th>Move</th>");
			for(int value : node.playerMove)
				bw.write("<th>" + value + "</th>");
			bw.newLine();

			bw.write("<tr><th>Profit</th>");
			for(double value : node.profit)
				bw.write("<th>" + value + "</th>");
			bw.write("</tr>");
			bw.write("</table><br>");

			bw.newLine();
		}
	}
}
