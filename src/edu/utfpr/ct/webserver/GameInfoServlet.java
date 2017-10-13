package edu.utfpr.ct.webserver;

import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.localization.ClientLocalizationManager;
import edu.utfpr.ct.localization.HostLocalizationKeys;
import edu.utfpr.ct.localization.HostLocalizationManager;
import edu.utfpr.ct.localization.Localize;
import edu.utfpr.ct.util.ChartJSUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@WebServlet(
        name = "BeerGameHostService-Information",
        urlPatterns = {"/info"}
)
public class GameInfoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        String gameName = req.getParameter("game-name");
        String legend = req.getParameter("no-legend");
        
        String order_s = req.getParameter("order");
        String stock_s = req.getParameter("stock");
        
        String lang = req.getParameter("lang");
        
        boolean order = Boolean.parseBoolean(order_s);
        boolean stock = Boolean.parseBoolean(stock_s);
        
        boolean no_html = Boolean.parseBoolean(req.getParameter("no-html"));

        ActionService service = null;
        Object obj = req.getServletContext().getAttribute("action-service");

        if (obj != null && obj instanceof ActionService) {
            service = (ActionService) obj;
        }

        if (service != null) {
            if (!req.getRemoteAddr().equals("127.0.0.1") && !service.getRoomActive(gameName)) {
                return;
            }

            Game g = service.getGameInfo(gameName);

            if (g == null) {
                g = service.getReportInfo(gameName);
            }

            if (g != null) {

                boolean noLegend = false;

                try {
                    noLegend = Boolean.parseBoolean(legend);
                } catch (Exception ex) {
                }

                Localize loc;
                
                if(lang == null){
                    loc = HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get());
                }else{
                    loc = ClientLocalizationManager.getInstance().getClientFor(lang);
                }

                int maxV = 0;

                JSONObject ordChartInfo = new JSONObject();
                if(order){
                    ordChartInfo.put("type", "line");

                    JSONObject chartData = new JSONObject();
                    ordChartInfo.put("data", chartData);

                    JSONArray chartLabels = new JSONArray();
                    chartData.put("labels", chartLabels);

                    for (Integer k = 1; k <= g.realDuration; k++) {
                        chartLabels.add(k.toString());
                    }

                    JSONArray chartDatasets = new JSONArray();
                    chartData.put("datasets", chartDatasets);

                    JSONObject chartOptions = new JSONObject();
                    ordChartInfo.put("options", chartOptions);

                    for (int k = ((g.supplyChain.length) / (g.deliveryDelay + 1)) - 1; k >= 0 ; k--) {
                        JSONObject dataset = new JSONObject();
                        Node n = ((Node) g.supplyChain[k * (g.deliveryDelay + 1)]);

                        dataset.put("label", loc.getTextFor(HostLocalizationKeys.CHART_OR_FUNCTION_AX + n.function.getPosition()));
                        dataset.put("fill", false);
                        dataset.put("backgroundColor", ChartJSUtils.COLORS_S[k + 1]);
                        dataset.put("borderColor", ChartJSUtils.COLORS_S[k + 1]);

                        JSONArray data = new JSONArray();
                        int[] vals = new int[n.playerMove.size()];

                        for (int l = 0; l < vals.length; l++) {
                            vals[l] = n.playerMove.get(l);
                            data.add(n.playerMove.get(l));

                            if (maxV < vals[l]) {
                                maxV = vals[l];
                            }
                        }

                        dataset.put("data", data);
                        chartDatasets.add(dataset);
                    }

                    JSONObject dataset = new JSONObject();
                    dataset.put("label", loc.getTextFor(HostLocalizationKeys.CHART_OR_FUNCTION_AX + "0"));
                    dataset.put("fill", false);
                    dataset.put("backgroundColor", ChartJSUtils.COLORS_S[0]);
                    dataset.put("borderColor", ChartJSUtils.COLORS_S[0]);
                    JSONArray data =  new JSONArray();
                    for(int k = 0; k < g.demand.length; k++){
                        data.add(g.demand[k]);

                        if (maxV < g.demand[k]) {
                            maxV = g.demand[k];
                        }

                    }
                    dataset.put("data", data);

                    chartDatasets.add(dataset);

                    chartOptions.put("responsive", "true");

                    JSONObject ax = new JSONObject();
                    ax.put("display", "true");
                    ax.put("text", loc.getTextFor(HostLocalizationKeys.CHART_OR_TITLE));

                    chartOptions.put("title", ax);

                    ax = new JSONObject();
                    ax.put("display", !noLegend);
                    ax.put("reverse", "true");

                    chartOptions.put("legend", ax);

                    ax = new JSONObject();
                    ax.put("mode", "index");

                    chartOptions.put("tooltips", ax);

                    ax = new JSONObject();
                    ax.put("mode", "nearest");

                    chartOptions.put("hover", ax);

                    JSONObject scales = new JSONObject();
                    chartOptions.put("scales", scales);

                    JSONArray axes = new JSONArray();
                    scales.put("xAxes", axes);

                    ax = new JSONObject();
                    ax.put("display", "true");

                    JSONObject scalLabel = new JSONObject();
                    scalLabel.put("display", "true");
                    scalLabel.put("labelString", loc.getTextFor(HostLocalizationKeys.CHART_OR_LABEL_X));
                    ax.put("scaleLabel", scalLabel);

                    axes.add(ax);

                    axes = new JSONArray();
                    scales.put("yAxes", axes);

                    ax = new JSONObject();
                    ax.put("display", "true");

                    scalLabel = new JSONObject();
                    scalLabel.put("display", "true");
                    scalLabel.put("labelString", loc.getTextFor(HostLocalizationKeys.CHART_OR_LABEL_Y));
                    ax.put("scaleLabel", scalLabel);

                    JSONObject ticks = new JSONObject();
                    ax.put("ticks", ticks);

                    ticks.put("beginAtZero", true);
                    ticks.put("suggestedMax", maxV + 1);

                    axes.add(ax);

                    JSONObject elements = new JSONObject();
                    chartOptions.put("elements", elements);

                    ax = new JSONObject();
                    elements.put("line", ax);

                    ax.put("tension", 0.0);
                }
                
                
                JSONObject stkChartInfo = new JSONObject();
                if(stock){
                    stkChartInfo.put("type", "line");

                    JSONObject chartData = new JSONObject();
                    stkChartInfo.put("data", chartData);

                    JSONArray chartLabels = new JSONArray();
                    chartData.put("labels", chartLabels);

                    for (Integer k = 1; k <= g.realDuration; k++) {
                        chartLabels.add(k.toString());
                    }

                    JSONArray chartDatasets = new JSONArray();
                    chartData.put("datasets", chartDatasets);

                    JSONObject chartOptions = new JSONObject();
                    stkChartInfo.put("options", chartOptions);

                    for (int k = ((g.supplyChain.length) / (g.deliveryDelay + 1)) - 1; k >= 0 ; k--) {
                        JSONObject dataset = new JSONObject();
                        Node n = ((Node) g.supplyChain[k * (g.deliveryDelay + 1)]);

                        dataset.put("label", loc.getTextFor(HostLocalizationKeys.CHART_ST_FUNCTION_AX + n.function.getPosition()));
                        dataset.put("fill", false);
                        dataset.put("backgroundColor", ChartJSUtils.COLORS_S[k + 1]);
                        dataset.put("borderColor", ChartJSUtils.COLORS_S[k + 1]);

                        JSONArray data = new JSONArray();
                        int[] vals = new int[n.currentStock.size()];

                        for (int l = 0; l < vals.length; l++) {
                            vals[l] = n.currentStock.get(l);
                            data.add(n.currentStock.get(l));

                            if (maxV < vals[l]) {
                                maxV = vals[l];
                            }
                        }

                        dataset.put("data", data);
                        chartDatasets.add(dataset);
                    }

                    chartOptions.put("responsive", "true");

                    JSONObject ax = new JSONObject();
                    ax.put("display", "true");
                    ax.put("text", loc.getTextFor(HostLocalizationKeys.CHART_ST_TITLE));

                    chartOptions.put("title", ax);

                    ax = new JSONObject();
                    ax.put("display", !noLegend);
                    ax.put("reverse", "true");

                    chartOptions.put("legend", ax);

                    ax = new JSONObject();
                    ax.put("mode", "index");

                    chartOptions.put("tooltips", ax);

                    ax = new JSONObject();
                    ax.put("mode", "nearest");

                    chartOptions.put("hover", ax);

                    JSONObject scales = new JSONObject();
                    chartOptions.put("scales", scales);

                    JSONArray axes = new JSONArray();
                    scales.put("xAxes", axes);

                    ax = new JSONObject();
                    ax.put("display", "true");

                    JSONObject scalLabel = new JSONObject();
                    scalLabel.put("display", "true");
                    scalLabel.put("labelString", loc.getTextFor(HostLocalizationKeys.CHART_ST_LABEL_X));
                    ax.put("scaleLabel", scalLabel);

                    axes.add(ax);

                    axes = new JSONArray();
                    scales.put("yAxes", axes);

                    ax = new JSONObject();
                    ax.put("display", "true");

                    scalLabel = new JSONObject();
                    scalLabel.put("display", "true");
                    scalLabel.put("labelString", loc.getTextFor(HostLocalizationKeys.CHART_ST_LABEL_Y));
                    ax.put("scaleLabel", scalLabel);

                    JSONObject ticks = new JSONObject();
                    ax.put("ticks", ticks);

                    ticks.put("beginAtZero", true);
                    ticks.put("suggestedMax", maxV + 1);

                    axes.add(ax);

                    JSONObject elements = new JSONObject();
                    chartOptions.put("elements", elements);

                    ax = new JSONObject();
                    elements.put("line", ax);

                    ax.put("tension", 0.0);
                }
                

                resp.setContentType("text/html");
                resp.setCharacterEncoding("UTF-8");

                StringBuilder html = new StringBuilder();
                
                if(!no_html){
                    html.append("<!DOCTYPE html>");
                    html.append("<html> <head>");
                    html.append("<script src='/resources/chartjs/Chart.js'></script>");
                    html.append("<style> canvas { -moz-user-select: none; -webkit-user-select: none; -ms-user-select: none; } </style></head>");
                    html.append("<body><div style=\"width: 82.5%;\">");
                }
                if(order)
                    html.append("<canvas id=\"ord_chart\"></canvas>");
                if(stock)
                    html.append("<canvas id=\"stock_chart\"></canvas>");
                if(!no_html)
                    html.append("</div>");
                html.append("<script>");
                if(no_html)
                    html.append("function call_chart(){");
                if(order){
                    html.append("var ctx = document.getElementById(\"ord_chart\");"
                            + "var myChart = new Chart(ctx, ");
                    html.append(ordChartInfo.toJSONString());
                    html.append(");");
                }
                if(stock){
                    html.append("var ctx = document.getElementById(\"stock_chart\");"
                            + "var myChart = new Chart(ctx, ");
                    html.append(stkChartInfo.toJSONString());
                    html.append(");");
                }
                if(no_html)
                    html.append("}");
                html.append("</script>");
                if(!no_html){
                    html.append("</body></html>");
                }
                
                resp.setStatus(200);
                resp.getOutputStream().write(html.toString().getBytes());
                resp.getOutputStream().flush();
            }
        } else {
            resp.setContentType("text/html");
            resp.getOutputStream().write("<!DOCTYPE html><html> <head></head><bodystyle='background-color: red;'><h1>Erro</h1></body></html>".getBytes());
            resp.getOutputStream().flush();
        }

        resp.getOutputStream().close();
    }

}
