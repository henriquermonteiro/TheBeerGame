package edu.utfpr.ct.webclient;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import edu.utfpr.ct.interfaces.IControllerPlayer;

@WebServlet(
        name = "BeerGamePlayerService-Checkin",
        urlPatterns = {"/checkin"}
)
public class CheckinServlet extends HttpServlet {

    public CheckinServlet() {
        super();
    }

    private void answer(HttpServletResponse resp, Boolean OK) throws IOException {
        resp.setContentType("application/json");

        JSONObject json = new JSONObject();

        json.put("accepted", OK);

        resp.getOutputStream().write(json.toJSONString().getBytes());
        resp.getOutputStream().flush();
        resp.getOutputStream().close();
    }

    private void checkin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String player = req.getParameter("player-name");

        if (player == null || player.isEmpty()) {
            answer(resp, Boolean.FALSE);
            return;
        }

        Object obj = getServletContext().getAttribute("action-service");

        if (obj instanceof ActionService) {

            answer(resp, ((ActionService)obj).checkIn(player).equals("")); // -2 significa que o nome está indisponível;
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        checkin(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        checkin(req, resp);
    }

}
