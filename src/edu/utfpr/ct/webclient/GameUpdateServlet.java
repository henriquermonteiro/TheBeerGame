package edu.utfpr.ct.webclient;

import edu.utfpr.ct.datamodel.EngineData;
import edu.utfpr.ct.datamodel.Game;
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
                    switch(g.state){
                        case Engine.SETUP:
                            json.put("state", "waiting");
                            break;
                        case Engine.RUNNING:
                            json.put("state", "playing");
                            json.put("your-turn", (playerName == null ? false : playerName.equals(g.playerOfTurn)));
                            break;
                        case Engine.FINISHED:
                            json.put("state", "reporting");
                    }
                    json.put("id", g.game.gameID);
                    json.put("name", g.game.name);
                    json.put("missing-cost", g.game.missingUnitCost);
                    json.put("stock-cost", g.game.stockUnitCost);
                    json.put("selling-profit", g.game.sellingUnitProfit);
                    
                    JSONArray players = new JSONArray();
                    
                    int players_amount = g.game.supplyChain.length/(g.game.deliveryDelay+1);
                    
                    for(int k = 0; k < players_amount; k++){
                        JSONObject player = new JSONObject();
                        
                        player.put("name", ((Node)g.game.supplyChain[k * (g.game.deliveryDelay + 1)]).playerName);
                        player.put("function", ((Node)g.game.supplyChain[k * (g.game.deliveryDelay + 1)]).function.getName());
                                
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
