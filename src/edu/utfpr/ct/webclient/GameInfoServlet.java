/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.webclient;

import be.ceau.chart.LineChart;
import be.ceau.chart.data.LineData;
import be.ceau.chart.dataset.LineDataset;
import be.ceau.chart.enums.HoverMode;
import be.ceau.chart.options.Hover;
import be.ceau.chart.options.LineOptions;
import be.ceau.chart.options.Title;
import be.ceau.chart.options.Tooltips;
import be.ceau.chart.options.elements.Line;
import be.ceau.chart.options.elements.LineElements;
import be.ceau.chart.options.scales.LinearScale;
import be.ceau.chart.options.scales.LinearScales;
import be.ceau.chart.options.scales.ScaleLabel;
import be.ceau.chart.options.ticks.LinearTicks;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.localization.HostLocalizationKeys;
import edu.utfpr.ct.localization.HostLocalizationManager;
import edu.utfpr.ct.localization.Localize;
import edu.utfpr.ct.util.ChartJSUtils;
import java.io.IOException;
import java.math.BigDecimal;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
        name = "BeerGameHostService-Information",
        urlPatterns = {"/info"}
)
public class GameInfoServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!req.getRemoteAddr().equals("127.0.0.1")){
//            System.out.println(req.getRemoteAddr());
            return;
        }
        
        String gameName = req.getParameter("game-name");
        
        ActionService service = null;
        Object obj = req.getServletContext().getAttribute("action-service");

        if (obj != null && obj instanceof ActionService) {
            service = (ActionService) obj;
        }
        
        if (service != null) {
            Game g = service.getGameInfo(gameName);
            
            if(g == null){
                g = service.getReportInfo(gameName);
            }
            
            if(g != null){
                
                Localize loc = HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get());
                
                LineChart chart = new LineChart(new LineData(), new LineOptions());
                
                LineData chartData = chart.getData();
                for(Integer k = 1; k <= g.realDuration; k++) chartData.addLabel(k.toString());
                
                int maxV = 0;
                
                LineDataset dataSet = new LineDataset();
                dataSet.setLabel(loc.getTextFor(HostLocalizationKeys.CHART_OR_FUNCTION_AX+"0")).setData(g.demand);
                dataSet.setFill(false);
                dataSet.setBorderColor(ChartJSUtils.COLORS[0]);
                dataSet.setBackgroundColor(ChartJSUtils.COLORS[0]);
                
                if(maxV < g.demand[g.demand.length - 1]) maxV = g.demand[g.demand.length - 1];
                if(maxV < g.demand[0]) maxV = g.demand[0];
                
                
                chartData.addDataset(dataSet);
                
                for(int k = 0; k < ((g.supplyChain.length)/(g.deliveryDelay + 1)); k++){
                    Node n = ((Node)g.supplyChain[k * (g.deliveryDelay + 1)]);
                    dataSet = new LineDataset();
                    dataSet.setLabel(loc.getTextFor(HostLocalizationKeys.CHART_OR_FUNCTION_AX + n.function.getPosition()));
                    dataSet.setFill(false);
                    dataSet.setBorderColor(ChartJSUtils.COLORS[k + 1]);
                    dataSet.setBackgroundColor(ChartJSUtils.COLORS[k + 1]);
                    int[] vals = new int[n.playerMove.size()];
                    
                    for(int l = 0 ; l < vals.length; l++){
                        vals[l] = n.playerMove.get(l);
                        
                        if(maxV < vals[l]) maxV = vals[l];
                    }
                    
                    dataSet.setData(vals);
                    
                    chartData.addDataset(dataSet);
                }
                
                chart.getOptions().setResponsive(true);
                chart.getOptions().setTitle(new Title().setDisplay(true).setText(loc.getTextFor(HostLocalizationKeys.CHART_OR_TITLE)));
                chart.getOptions().setTooltips(new Tooltips().setMode("index"));
                chart.getOptions().setHover(new Hover().setMode(HoverMode.SINGLE));
                chart.getOptions().setScales(new LinearScales().addyAxis(new LinearScale().setDisplay(true).setScaleLabel(new ScaleLabel().setDisplay(true).setLabelString(loc.getTextFor(HostLocalizationKeys.CHART_OR_LABEL_Y))).setTicks(new LinearTicks().setBeginAtZero(true).setSuggestedMax(new BigDecimal(maxV + 1)))));
                chart.getOptions().getScales().addxAxis(new LinearScale().setDisplay(true).setScaleLabel(new ScaleLabel().setDisplay(true).setLabelString(loc.getTextFor(HostLocalizationKeys.CHART_OR_LABEL_X))));
                chart.getOptions().setElements(new LineElements().setLine(new Line().setTension(0.0f)));
                
                String json = chart.toJson().replace("\"type\" : \"linear\",", "").replace("\"type\" : \"linear\"", "").replace("single", "nearest");
                
                resp.setContentType("text/html");
                resp.setCharacterEncoding("UTF-8");
                
                StringBuilder html = new StringBuilder();
                
                html.append("<!DOCTYPE html>");
                html.append("<html> <head>");
                html.append("<script src='/resources/chartjs/Chart.js'></script>");
                html.append("<style> canvas { -moz-user-select: none; -webkit-user-select: none; -ms-user-select: none; } </style></head>");
                html.append("<body><canvas id=\"chart\"></canvas> <script>"
                        + "var ctx = document.getElementById(\"chart\");"
                        + "var myChart = new Chart(ctx, ");
                html.append(json);
                html.append(");</script>");
                html.append("</body></html>");
                
                resp.setStatus(200);
                resp.getOutputStream().write(html.toString().getBytes());
                resp.getOutputStream().flush();
            }
        }else{
            resp.setContentType("text/html");
            resp.getOutputStream().write("<!DOCTYPE html><html> <head></head><bodystyle='background-color: red;'><h1>Erro</h1></body></html>".getBytes());
            resp.getOutputStream().flush();
        }
        
        resp.getOutputStream().close();
    }
    
}
