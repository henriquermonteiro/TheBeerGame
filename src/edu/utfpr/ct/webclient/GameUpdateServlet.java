package edu.utfpr.ct.webclient;

import edu.utfpr.ct.datamodel.Game;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

            Integer gameID = null;
            String playerName = null;

            boolean flag = true;

            String s = req.getParameter("game-id");
            if (s == null || s.isEmpty()) {
                flag = false;
            } else {
                try {
                    gameID = Integer.parseInt(s);
                } catch (NumberFormatException ex) {
                    flag = false;
                }
            }

            playerName = req.getParameter("player-name");
            if (playerName == null || playerName.isEmpty()) {
                flag = false;
            }

            if (flag) {
                Game g = service.updateData(gameID, playerName);

                if (g != null) {
                    json.put("id", g.gameID);
                    json.put("name", g.name);
                    json.put("missing-cost", g.missingUnitCost);
                    json.put("stock-cost", g.stockUnitCost);
                    json.put("selling-profit", g.sellingUnitProfit);
                    json.put("", g);
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

}
