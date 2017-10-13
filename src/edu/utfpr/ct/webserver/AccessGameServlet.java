package edu.utfpr.ct.webserver;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

@WebServlet(
        name = "BeerGamePlayerService-EnterGame",
        urlPatterns = {"/accessgame"}
)
public class AccessGameServlet extends HttpServlet {

    public AccessGameServlet() {
        super();
    }

    private void answer(HttpServletResponse resp, Object OK) throws IOException {
        resp.setContentType("application/json");

        JSONObject json = new JSONObject();

        json.put("accepted", OK);

        resp.getOutputStream().write(json.toJSONString().getBytes());
        resp.getOutputStream().flush();
        resp.getOutputStream().close();
    }

    private void access(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String player = req.getParameter("player_name");
        String game = req.getParameter("game_name");
        String psw = req.getParameter("password");

        if (player == null || player.isEmpty() || game == null || game.isEmpty() || psw == null) {
            answer(resp, player);
            return;
        }

        Object obj = getServletContext().getAttribute("action-service");

        if (obj instanceof ActionService) {
            answer(resp, ((ActionService)obj).enterGameRoom(game, player, psw));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        access(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        access(req, resp);
    }

}
