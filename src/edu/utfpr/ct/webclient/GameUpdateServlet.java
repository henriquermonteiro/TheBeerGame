package edu.utfpr.ct.webclient;

import edu.utfpr.ct.gamecontroller.Table;
import edu.utfpr.ct.datamodel.AbstractNode;
import edu.utfpr.ct.datamodel.EngineData;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.gamecontroller.Engine;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@WebServlet(
        name = "BeerGamePlayerService-Update",
        urlPatterns = {"/update"}
)
public class GameUpdateServlet extends HttpServlet {

    private void doUpdate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        ActionService service = null;
        Object obj = req.getServletContext().getAttribute("action-service");

        if (obj != null && obj instanceof ActionService) {
            service = (ActionService) obj;
        }

        if (service != null) {
            JSONObject json = new JSONObject();

            String gameName;
            String playerName;
            String s_function;
            String s_week;

            boolean flag = true;

            gameName = req.getParameter("game-name");
            if (gameName == null || gameName.isEmpty()) {
                flag = false;
            }

            playerName = req.getParameter("player-name");
            if (playerName == null || playerName.isEmpty()) {
                flag = false;
            }

            s_function = req.getParameter("table-function");
            s_week = req.getParameter("table-week");

            int func;
            int week;

            try {
                func = Integer.parseInt(s_function);
                week = Integer.parseInt(s_week);
            }catch(NumberFormatException numEx){
                func = -1;
                week = -1;
            }
            
            if (flag) {
                EngineData g = service.updateData(gameName, playerName);

                if (g != null) {
                    switch (g.state) {
                        case Engine.SETUP:
                            json.put("state", "waiting");
                            break;
                        case Engine.RUNNING:
                            json.put("state", "playing");
                            json.put("your_turn", (playerName == null ? false : playerName.equals(g.playerOfTurn)));

                            JSONArray table = new JSONArray();
                            
                            for (Table.Line line : service.getTableData(gameName).getNewLines(playerName, func, week)) {
                                JSONObject lineData = new JSONObject();
                                
                                lineData.put("function", line.function);
                                lineData.put("week", line.week);
                                lineData.put("initial_stock", line.initialStock);
                                lineData.put("received_order", line.orderReceived);
                                lineData.put("previously_pending_orders", line.orderPreviousPending);
                                lineData.put("expected_delivery", line.expectedDelivery);
                                lineData.put("actual_delivery", line.actualyDelivery);
                                lineData.put("order_unfulfilled", line.orderUnfullfiled);
                                lineData.put("final_stock", line.finalStock);
                                lineData.put("move", line.playerMove);
                                lineData.put("confirmed_delivery", line.confirmedOrderDelivery);
                                lineData.put("cost_unfulfillment", line.costUnfulfillment);
                                lineData.put("cost_stock", line.costStock);
                                lineData.put("profit", line.profit);
                                lineData.put("week_balance", line.weekBalance);
                                
                                JSONArray orders = new JSONArray();
                                for(Integer order : line.incomingOrder){
                                    orders.add(order);
                                }
                                lineData.put("order", orders);
                                
                                table.add(lineData);
                            }
                            
                            json.put("history", table);
                            
                            break;
                        case Engine.FINISHED:
                            json.put("state", "reporting");
                            json.put("real_duration", g.game.realDuration);
                    }
                    json.put("id", g.game.gameID);
                    json.put("name", g.game.name);
                    json.put("informed_chain", g.game.informedChainSupply);
                    json.put("missing_cost", g.game.missingUnitCost);
                    json.put("stock_cost", g.game.stockUnitCost);
                    json.put("selling_profit", g.game.sellingUnitProfit);
                    json.put("delay", g.game.deliveryDelay);
                    json.put("current_week", g.weeks);
                    json.put("total_week", g.game.informedDuration);

                    JSONArray players = new JSONArray();

                    int players_amount = g.game.supplyChain.length / (g.game.deliveryDelay + 1);

                    for (int k = 0; k < players_amount; k++) {
                        JSONObject player = new JSONObject();

                        Node n_aux = ((Node) g.game.supplyChain[k * (g.game.deliveryDelay + 1)]);

                        player.put("name", n_aux.playerName);
                        player.put("function", n_aux.function.getName());
                        player.put("cost", (n_aux.profit != null ? (g.game.sellingUnitProfit == 0 ? (n_aux.getLastUnfullfilmentCost() + n_aux.getLastStockingCost()) : (n_aux.getLastProfit() - (n_aux.getLastUnfullfilmentCost() + n_aux.getLastStockingCost()))) : "---"));
                        player.put("stock", (n_aux.currentStock != null ? n_aux.getLastStock() : "---"));
                        player.put("debt", (n_aux.debt != null ? (n_aux.function.getPosition() == 1 ? "---" : n_aux.getLastDebt()) : "---"));
                        
                        if(g.state == Engine.RUNNING)
                            player.put("last_request", (n_aux.lastRequest != null ? n_aux.lastRequest : "---"));

                        JSONArray receiving = new JSONArray();

                        for (int l = 1; l <= g.game.deliveryDelay; l++) {
                            AbstractNode t_aux = g.game.supplyChain[(k * (g.game.deliveryDelay + 1)) + l];

                            JSONObject incoming = new JSONObject();
                            incoming.put("distance", l);
                            incoming.put("value", (t_aux != null ? t_aux.travellingStock : "?"));
                            receiving.add(incoming);
                        }

                        player.put("incoming", receiving);

                        players.add(k, player);
                    }

                    json.put("players", players);
                }
            }

            resp.getOutputStream().write(json.toJSONString().getBytes());
            resp.getOutputStream().flush();
        }

        resp.getOutputStream().close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doUpdate(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doUpdate(req, resp);
    }

}
