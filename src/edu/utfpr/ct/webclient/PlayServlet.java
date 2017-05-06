package edu.utfpr.ct.webclient;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

@WebServlet(
        name = "BeerGamePlayerService-Play",
        urlPatterns = {"/play"}
)
public class PlayServlet extends HttpServlet {

    private ActionService service;

    public PlayServlet(ActionService service) {
        super();
        this.service = service;
    }

    private void answer(HttpServletResponse resp, Integer moveDone) throws IOException {
        resp.setContentType("application/json");

        JSONObject json = new JSONObject();
        json.put("move-done", moveDone);

        resp.getOutputStream().write(json.toJSONString().getBytes());
        resp.getOutputStream().flush();
        resp.getOutputStream().close();
    }

    private void doMove(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String gameID = req.getParameter("game-id");
        String nodePosition = req.getParameter("node-position");
        String playerName = req.getParameter("player-name");
        String move = req.getParameter("player-move");

        if (gameID == null || gameID.isEmpty()) {
            answer(resp, -1);
            return;
        }

        if (nodePosition == null || nodePosition.isEmpty()) {
            answer(resp, -1);
            return;
        }

        if (playerName == null || playerName.isEmpty()) {
            answer(resp, -1);
            return;
        }

        if (move == null || move.isEmpty()) {
            answer(resp, -1);
            return;
        }

        try {
            Integer gID = Integer.parseInt(gameID);
            Integer nPos = Integer.parseInt(nodePosition);
            Integer mv = Integer.parseInt(move);

            answer(resp, service.postMove(gID, nPos, playerName, mv));

        } catch (NumberFormatException ex) {
            answer(resp, -1);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doMove(req, resp);
    }

}
