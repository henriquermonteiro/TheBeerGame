package edu.utfpr.ct.webclient;

import edu.utfpr.ct.datamodel.Game;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@WebServlet(
        name = "RandomJSTest-Service",
        urlPatterns = {"/rand"}
)
public class RandomRandomServlet extends HttpServlet {

    private String p1;
    private String p2;
    private String p3;
    private String p4;

    private Integer est1;
    private Integer est2;
    private Integer est3;
    private Integer est4;

    public RandomRandomServlet() {
        String[] ss = new String[]{"Zezinho", "Pedrinho", "Luizinho", "Gerson", "Gustavo", "Marlon", "Homer", "Maria", "Abe"};
        ArrayList<String> names = new ArrayList<>(Arrays.asList(ss));

        int k = (int) Math.round(Math.random() * (names.size() - 1));

        p1 = names.get(k);
        names.remove(k);

        k = (int) Math.round(Math.random() * (names.size() - 1));

        p2 = names.get(k);
        names.remove(k);

        k = (int) Math.round(Math.random() * (names.size() - 1));

        p3 = names.get(k);
        names.remove(k);

        k = (int) Math.round(Math.random() * (names.size() - 1));

        p4 = names.get(k);
        names.remove(k);

        est1 = 10;
        est2 = 10;
        est3 = 10;
        est4 = 10;

    }

    private void getUpdate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JSONObject json = new JSONObject();

        resp.setContentType("application/json");

        JSONArray array = new JSONArray();

        JSONObject element = new JSONObject();

        element.put("name", p1);
        element.put("stock", est1);
        array.add(element);
        element = new JSONObject();
        element.put("name", p2);
        element.put("stock", est2);
        array.add(element);
        element = new JSONObject();
        element.put("name", p3);
        element.put("stock", est3);
        array.add(element);
        element = new JSONObject();
        element.put("name", p4);
        element.put("stock", est4);
        array.add(element);

        json.put("playerlist", array);

        resp.getOutputStream().write(json.toJSONString().getBytes());
        resp.getOutputStream().flush();

        resp.getOutputStream().close();
        
        Double k = Math.random();
        
        if(k >= 0.95){
            est1 += ((int)(Math.random()*6) - 3);
        }
        
        k = Math.random();
        
        if(k >= 0.95){
            est2 += ((int)(Math.random()*6) - 3);
        }
        
        k = Math.random();
        
        if(k >= 0.95){
            est3 += ((int)(Math.random()*6) - 3);
        }
        
        k = Math.random();
        
        if(k >= 0.95){
            est4 += ((int)(Math.random()*6) - 3);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getUpdate(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getUpdate(req, resp);
    }

}
