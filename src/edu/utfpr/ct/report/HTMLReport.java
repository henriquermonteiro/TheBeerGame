package edu.utfpr.ct.report;

import edu.utfpr.ct.datamodel.AbstractNode;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.datamodel.TravellingTime;
import edu.utfpr.ct.gamecontroller.Table;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class HTMLReport extends AbstractReport
{
	public HTMLReport()
	{
		super(".html");
	}

	@Override
	public boolean generateReport(Table table)
	{
		BufferedWriter bw;
		String page;

		try
		{
			page = new String(Files.readAllBytes(Paths.get("resources", "Template.html")), StandardCharsets.UTF_8);
			bw = new BufferedWriter(new FileWriter(createFile(getFileName(table.getGame()))));

			page = replaceGameConfig(page, table.getGame());
			page = replaceNodeData(page, table.getGame());

			bw.write(page);
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

	private String replaceGameConfig(String page, Game game) throws IOException
	{
		page = page.replace("{GameName}",         game.name);
		page = page.replace("{Type}",             game.informedChainSupply ? "Informed" : "Not informed");
		page = page.replace("{StockUnitCost}",    Double.toString(game.stockUnitCost));
		page = page.replace("{MissingUnitCost}",  Double.toString(game.missingUnitCost));
		page = page.replace("{Profit}",           Double.toString(game.sellingUnitProfit));
		page = page.replace("{DeliveryTime}",     Integer.toString(game.deliveryDelay));
		page = page.replace("{RealDuration}",     Integer.toString(game.realDuration));
		page = page.replace("{InformedDuration}", Integer.toString(game.informedDuration));

		return page;
	}

	private String replaceNodeData(String page, Game game)
	{
		int maxOrder = 1, maxStock = 1;
		List<Integer> sequence;
		Node node;
		String function;
                
                UnaryOperator<Integer> uO = new UnaryOperator<Integer>() {
                    @Override
                    public Integer apply(Integer t) {
                        return t + 1;
                    }
                };

		sequence = Stream.iterate(1, uO).limit(game.realDuration).collect(Collectors.toList());
		page = page.replaceAll(Pattern.quote("{RealWeeks}"), sequence.toString());
		page = page.replace("{ConsumerOrder}", Arrays.toString(game.demand));

		for(AbstractNode abstractNode : game.supplyChain)
		{
			if(abstractNode instanceof TravellingTime)
				continue;
			node = (Node) abstractNode;

			function = node.function.toString();
			function = function.toLowerCase();
			function = function.substring(0, 1).toUpperCase() + function.substring(1);
			
			List<Integer> tmpOrderList = node.playerMove.subList(0, node.playerMove.size());
			List<Integer> tmpStockList = node.currentStock.subList(0, node.playerMove.size());

			page = page.replace("{" + function + "Name}",  node.playerName);
			page = page.replace("{" + function + "Order}", tmpOrderList.toString());
			page = page.replace("{" + function + "Stock}", tmpStockList.toString());
			
			maxOrder = maxOrder > Collections.max(tmpOrderList, null) ? maxOrder : Collections.max(tmpOrderList, null);
			maxStock = maxStock > Collections.max(tmpStockList, null) ? maxStock : Collections.max(tmpStockList, null);
		}
		
		page = page.replace("{MaxOrder}", Integer.toString(maxOrder + 10));
		page = page.replace("{MaxStock}", Integer.toString(maxStock + 10));

		return page;
	}
}
