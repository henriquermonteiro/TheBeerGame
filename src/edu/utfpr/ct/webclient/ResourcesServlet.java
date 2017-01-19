/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.webclient;

import edu.utfpr.ct.datamodel.Game;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author henrique
 */
@WebServlet(
        name = "BeerGamePlayerService-Resorces",
        urlPatterns = {"/resorces"}
)
public class ResourcesServlet extends HttpServlet{

    private void getResorces(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        JSONObject json = new JSONObject();
        
        resp.setContentType("application/json");
        
        Object obj = req.getServletContext().getAttribute("action-service");
        ActionService service = null;
        
        if(obj != null && obj instanceof ActionService){
            service = (ActionService) obj;
        }
        
        if(service != null){
            Game[] list = service.listAvailableRooms();
            
            JSONArray array = new JSONArray();
            
            for(Game g : list){
                JSONObject element = new JSONObject();
                
                element.put("id", g.gameID);
                element.put("name", g.name);
                element.put("use-pw", (g.password != null && !g.password.isEmpty() ? Boolean.TRUE : Boolean.FALSE));
                element.put("timestamp", g.timestamp);
                
                array.add(element);
            }
            
            json.put("game-list", array);
            
            resp.getOutputStream().write(json.toJSONString().getBytes());
            resp.getOutputStream().flush();
        }
        
        resp.getOutputStream().close();
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getResorces(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getResorces(req, resp);
    }
    
}
