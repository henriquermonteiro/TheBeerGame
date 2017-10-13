package edu.utfpr.ct.webserver;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

@WebServlet(
        name = "Logout-Service",
        urlPatterns = {"/logout"}
)
public class LogoutServlet extends HttpServlet{

    public LogoutServlet() {
        super();
    }
    
    private void answer(HttpServletResponse resp, Boolean OK) throws IOException {
        resp.setContentType("application/json");

        JSONObject json = new JSONObject();

        json.put("logout", OK);

        resp.getOutputStream().write(json.toJSONString().getBytes());
        resp.getOutputStream().flush();
        resp.getOutputStream().close();
    }

    private void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String player = req.getParameter("player-name");

        if (player != null && !player.isEmpty()) {
            Object obj = getServletContext().getAttribute("action-service");

            if (obj instanceof ActionService) {
                if (!((ActionService) obj).isNameAvailable(player)) {
                    answer(resp, ((ActionService) obj).logout(player));
                    return;
                }
            }
        }

        answer(resp, Boolean.FALSE);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logout(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logout(req, resp);
    }
}
