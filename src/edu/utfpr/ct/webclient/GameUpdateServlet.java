package edu.utfpr.ct.webclient;

import edu.utfpr.ct.datamodel.AbstractNode;
import edu.utfpr.ct.datamodel.EngineData;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.datamodel.TravellingTime;
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

            String gameName = null;
            String playerName = null;

            boolean flag = true;

            gameName = req.getParameter("game-name");
            if (gameName == null || gameName.isEmpty()) {
                flag = false;
            }

            playerName = req.getParameter("player-name");
            if (playerName == null || playerName.isEmpty()) {
                flag = false;
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
                            break;
                        case Engine.FINISHED:
                            json.put("state", "reporting");
                            json.put("real_duration", g.game.realDuration);

                            JSONArray stocks = new JSONArray();

                            int max_size = ((((Node) g.game.supplyChain[0]).currentStock.size() - 1) / 10) + 1;
                            
                            for (int k = 0; k < ((Node) g.game.supplyChain[0]).playerMove.size(); k++) {
                                JSONObject weekData = new JSONObject();
                                
                                String week_text = String.format("%" + max_size + "d", k);

//                                weekData.put("week", k);
                                weekData.put("week", week_text);
//                                weekData.put("c", (k == 0 ? 0 : g.game.demand[k - 1]));
                                weekData.put("c", g.game.demand[k]);
                                weekData.put("r", ((Node) g.game.supplyChain[0]).playerMove.get(k));
                                weekData.put("w", ((Node) g.game.supplyChain[g.game.deliveryDelay + 1]).playerMove.get(k));
                                weekData.put("d", ((Node) g.game.supplyChain[2 * g.game.deliveryDelay + 2]).playerMove.get(k));
                                weekData.put("p", ((Node) g.game.supplyChain[3 * g.game.deliveryDelay + 3]).playerMove.get(k));

                                stocks.add(k, weekData);
                            }

                            json.put("graph_data", stocks);
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
                        player.put("cost", (n_aux.profit != null ? n_aux.getLastProfit() : "---"));
                        player.put("stock", (n_aux.currentStock != null ? n_aux.getLastStock() : "---"));

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
