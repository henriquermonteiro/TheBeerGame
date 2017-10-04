<%@page import="java.io.*" %>
<%@page import="java.net.*" %>
<%@page import="org.json.simple.*" %>
<%@page import="org.json.simple.parser.*" %>
<%@page import="edu.utfpr.ct.localization.*" %>

<%
    if(session.getAttribute("USER-ID") == null || ((String)session.getAttribute("USER-ID")).isEmpty() || session.getAttribute("LOGGED_GAME") == null || ((String)session.getAttribute("LOGGED_GAME")).isEmpty() ){
        response.sendRedirect("/check_in.jsp");
        return;
    }
    
    URL checkin_json = new URL(request.getScheme(), request.getServerName(), request.getServerPort(), "/update?game-name=" + URLEncoder.encode((String)session.getAttribute("LOGGED_GAME"), "UTF-8").replace("+", "%20") + "&player-name=" + URLEncoder.encode((String)session.getAttribute("USER-ID"), "UTF-8").replace("+", "%20") + "&table-function=" + URLEncoder.encode("0", "UTF-8").replace("+", "%20") + "&table-week=" + URLEncoder.encode("0", "UTF-8").replace("+", "%20"));
    HttpURLConnection urlcon = (HttpURLConnection)checkin_json.openConnection();
    urlcon.setRequestMethod("GET");
    
    JSONObject json = null;
    try{
        json = (JSONObject)new JSONParser().parse(new BufferedReader(new InputStreamReader(urlcon.getInputStream())));
        if(json.get("state") == null){
            session.removeAttribute("LOGGED_GAME");
            response.sendRedirect("/choose_room.jsp");
            return;
        }
    }catch(ParseException ex){
        session.removeAttribute("LOGGED_GAME");
        response.sendRedirect("/choose_room.jsp");
        return;
    }
    
    
    if(request.getParameter("lang") != null){
        session.setAttribute("PREF-LANG", request.getParameter("lang"));
    }
    
    String lang = "default";
    if(session.getAttribute("PREF-LANG") != null){ 
        lang = (String) session.getAttribute("PREF-LANG");
    }
    
    Localize localize = ClientLocalizationManager.getInstance().getClientFor(lang);
 
%>
<jsp:include page="resources/head_begin.jsp"/>
        <link rel="stylesheet" type="text/css" href="game.css"/>
        <script src='/resources/chartjs/Chart.js'></script>
        <style> canvas { -moz-user-select: none; -webkit-user-select: none; -ms-user-select: none; } </style>
        <script>
            
            var state = "w";
            var _func = 0;
            var _week = -1;
            var update_interval;
            var report_data;

            function allow_move() {
                var input = document.getElementById("order__input");
                var label = document.getElementById("order__label");
                var button = document.getElementById("order__button");

                if (input.hasAttribute("disabled")) {
                    input.removeAttribute("disabled");
                } else {
                    return;
                }

                if (button.hasAttribute("disabled")) {
                    button.removeAttribute("disabled");
                }

                label.innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.GAME_CONTROL_LABEL_ALLOW)) %>";

                input.focus();
                input.parentNode.classList.add('is-dirty');
                input.select();
            }

            function block_move() {
                var input = document.getElementById("order__input");
                var label = document.getElementById("order__label");
                var button = document.getElementById("order__button");

                if (!input.hasAttribute("disabled")) {
                    input.setAttribute("disabled", "");
                } else {
                    return;
                }

                if (!button.hasAttribute("disabled")) {
                    button.setAttribute("disabled", "");
                }

                label.innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.GAME_CONTROL_LABEL_BLOCK)) %>";
                input.value = "";
            }

            function send_request() {
                var move = document.getElementById("order__input").value;
                if (isNaN(move))
                    return;

                var jsonHttp = new XMLHttpRequest();
                jsonHttp.open("POST", "/do-move.jsp", false);
                jsonHttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
                jsonHttp.send("move=" + move);
            }

            function add_history_info(func, week, stock, profit, orders, move) {
                var history = document.getElementById("history");
                
                _func = func;
                _week = week;

                var k = 0;
                var row = history.insertRow(1);
                var cell = row.insertCell(k++);
                if(func === 0){
                    cell.innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_FUNC_CON)) %>";
                } else if (func === 1){
                    cell.innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_FUNC_RET)) %>";
                } else if (func === 2){
                    cell.innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_FUNC_WHO)) %>";
                } else if (func === 3){
                    cell.innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_FUNC_DIS)) %>";
                } else if (func === 4){
                    cell.innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_FUNC_PRO)) %>";
                }
                    
                
                var cell = row.insertCell(k++);
                cell.innerHTML = week + 1;
                
                var cell = row.insertCell(k++);
                if(func === 0){
                    cell.innerHTML = "---";
                }else{
                    cell.innerHTML = stock;
                }
                
                var cell = row.insertCell(k++);
                if(func === 0){
                    cell.innerHTML = "---";
                }else{
                    cell.innerHTML = profit;
                }
                
                for( var ord in orders){
                    cell = row.insertCell(k++);
                    if(func === 0){
                        cell.innerHTML = "---";
                    }else{
                        cell.innerHTML = orders[ord];
                    }
                }
                
                var cell = row.insertCell(k++);
                cell.innerHTML = move;
            }

            function start_game() {
                var wait = document.getElementById("waiting");
                if (wait !== undefined && wait !== null) {
                    wait.outerHTML = '';
                }
                delete wait;
                var game = document.getElementById("game-panel");
                if (game !== null) {
                    game.classList.remove("hidden");
                }

                state = "p";
            }

            function goto_report() {
                start_game();

                var game = document.getElementById("game-panel");
                if (game !== undefined && game !== null) {
                    game.outerHTML = '';
                }
                delete game;

                var report = document.getElementById("report-panel");
                if (report !== undefined) {
                    report.classList.remove("hidden");
                }

                //clearInterval(update_interval);
                state = "r";
            }

            function draw_chart() {
                var options = {
                    title: 'Estoque',
                    titleTextStyle: {fontSize: 20},
                    legend: {position: 'top', maxLines: 3},
                    hAxis: {title: 'Semana', titleTextStyle: {color: '#333'}},
                    vAxis: {title: 'Unidades', minValue: 0}
                };

                var graph_area = document.getElementById('graph-content');
                graph_area.innerHTML = '';

                var chart = new google.visualization.AreaChart(graph_area);
                chart.draw(google.visualization.arrayToDataTable(report_data), options);
            }

            function draw() {
                var canvas = document.getElementById("game_canvas");
                var ctx = canvas.getContext("2d");

                var w_width = 480; // Keep this values updated
                var w_heigth = 480;// Keep this values updated
                var size = 80; // Change size here
                var half_size = size / 2;

                ctx.clearRect(0, 0, canvas.width, canvas.height);

                ctx.save();
                ctx.lineWidth = 2;
                ctx.shadowColor = "black";
                ctx.strokeStyle = "rgba(0,0,0,1)";
                ctx.shadowBlur = 10;
                ctx.shadowOffsetX = -3;
                ctx.shadowOffsetY = 3;

                ctx.beginPath();
                ctx.arc((25 + half_size), (25 + half_size), half_size - 1, 0, 2 * Math.PI);
                ctx.stroke();
                ctx.closePath();
                var img = document.getElementById("ind");
                ctx.drawImage(img, 25, 25, size, size);

                ctx.beginPath();
                ctx.arc(((w_width - 25 - size) + half_size), (25 + half_size), half_size - 1, 0, 2 * Math.PI);
                ctx.stroke();
                ctx.closePath();
                var img = document.getElementById("dis");
                ctx.drawImage(img, (w_width - 25 - size), 25, size, size);

                ctx.beginPath();
                ctx.arc(((w_width - 25 - size) + half_size), ((w_heigth - 25 - size) + half_size), half_size - 1, 0, 2 * Math.PI);
                ctx.stroke();
                ctx.closePath();
                var img = document.getElementById("who");
                ctx.drawImage(img, (w_width - 25 - size), (w_heigth - 25 - size), size, size);

                ctx.beginPath();
                ctx.arc((25 + half_size), ((w_heigth - 25 - size) + half_size), half_size - 1, 0, 2 * Math.PI);
                ctx.stroke();
                ctx.closePath();
                var img = document.getElementById("ret");
                ctx.drawImage(img, 25, (w_heigth - 25 - size), size, size);

                ctx.restore();
            }

            function processWait(json_state) {
                if (state !== "w") {
                    location.reload();
                    return;
                }

                for (var player in json_state.players) {
                    var name = json_state.players[player].name;

                    if (name === "" || name === null) {
                        document.getElementById(json_state.players[player].function + "_name").innerHTML = "?";
                        document.getElementById(json_state.players[player].function + "_img").className = "alpha6";
                    } else {
                        document.getElementById(json_state.players[player].function + "_name").innerHTML = json_state.players[player].name;
                        document.getElementById(json_state.players[player].function + "_img").className = "";
                    }
                }
            }
            
            var is_set = false;
            var my_func;

            function processPlay(json_state) {
                if (state === "w") {
                    start_game();
                } else if (state !== "p") {
                    location.reload();
                    return;
                }

                if (json_state.your_turn) {
                    allow_move();
                } else {
                    block_move();
                }

                document.getElementById("curweek").innerHTML = json_state.current_week + 1;
                document.getElementById("totweek").innerHTML = json_state.total_week;

                for (var player in json_state.players) {
                    var func = json_state.players[player].function;
                    
                    if(is_set === false){
                        if(json_state.players[player].name === "<%=(String)session.getAttribute("USER-ID") %>"){
                            document.getElementById("game_"+func+"_img").style.opacity = "1.0";
                            document.getElementById("game_"+func+"_img").style.filter = "alpha(opacity=100)";
                            is_set = true;
                        }
                    }
                    
                    var cost = json_state.players[player].cost;
                    if(cost !== "---")
                        cost = Number(json_state.players[player].cost).toFixed(2);

                    document.getElementById(func + "_name2").innerHTML = json_state.players[player].name;
                    document.getElementById(func + "_cost").innerHTML = cost;
                    document.getElementById(func + "_stock").innerHTML = json_state.players[player].stock;
                    document.getElementById(func + "_last-req").innerHTML = json_state.players[player].last_request;

                    for (var inc in json_state.players[player].incoming) {
                        var dist = json_state.players[player].incoming[inc].distance;
                        document.getElementById(func + "_inc_" + dist).innerHTML = json_state.players[player].incoming[inc].value;
                    }
                }
                
                for(var line in json_state.history) {
                    add_history_info(
                        json_state.history[line].function,
                        json_state.history[line].week,
                        json_state.history[line].current_stock,
                        Number(json_state.history[line].profit).toFixed(2),
                        json_state.history[line].order,
                        json_state.history[line].move
                    );
                }
            }

            function processReport(json_state) {
                if (state !== "r") {

                    document.getElementById("rep_name").innerHTML = json_state.name;
                    if(json_state.informed_chain === true){
                        document.getElementById("rep_type").innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.REPORT_TYPE_INFO)) %>";
                    }else{
                        document.getElementById("rep_type").innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.REPORT_TYPE_NINF)) %>";
                    }

                    document.getElementById("rep_stockcost").innerHTML = json_state.stock_cost;
                    document.getElementById("rep_missingcost").innerHTML = Number(json_state.missing_cost).toFixed(2);
                    document.getElementById("rep_saleprofit").innerHTML = json_state.selling_profit;

                    document.getElementById("rep_delay").innerHTML = json_state.delay;
                    document.getElementById("rep_infduration").innerHTML = json_state.total_week;
                    document.getElementById("rep_realduration").innerHTML = json_state.real_duration;

                    for (var player in json_state.players) {
                        var func = json_state.players[player].function;

                        document.getElementById("rep_" + func + "_name").innerHTML = json_state.players[player].name;
                        document.getElementById("rep_" + func + "_cost").innerHTML = json_state.players[player].cost;
                        document.getElementById("rep_" + func + "_stock").innerHTML = json_state.players[player].stock;
                    }
                    
                    document.getElementById("chart_frame").contentWindow.location.reload();

                    goto_report();
                }
            }

            function updatePage() {
                var jsonHttp = new XMLHttpRequest();
                jsonHttp.open("POST", "/update.jsp", false);
                jsonHttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

                jsonHttp.onreadystatechange = function () {
                    if(state === "r") return;
                    
                    var json = JSON.parse(jsonHttp.responseText);

                    if (json !== null) {
                        switch (json.state) {
                            case "waiting":
                                processWait(json);
                                break;
                            case "playing":
                                processPlay(json);
                                break;
                            case "reporting":
                                processReport(json);
                                break;
                            default :
                                if(json.state === undefined){
                                    location.reload();
                                }
                                break;
                        }
                    }
                };

                jsonHttp.send("function=" + _func + "&week=" + _week);
            }

            function initialize() {
                bubbles();
                update_interval = setInterval(updatePage, 1000);
            }

        </script>
        <jsp:include page="resources/head_finish.jsp"/>
        <body onload="initialize();">
        <jsp:include page="resources/body_begin.jsp">
            <jsp:param name="hide_body" value="true"/>
            <jsp:param name="source" value="game.jsp"/>
            <jsp:param name="show_return" value="false"/>
        </jsp:include>

    <div class="center-content">
        <div id="waiting">
            <div class="wait-card mdl-card mdl-shadow--2dp">
                <div class="mdl-card__title mdl-card--expand">
                    <h2 class="mdl-card__title-text"><%=(localize.getTextFor(ClientLocalizationKeys.WAIT_TITLE)) %></h2>
                </div>
                <div class="mdl-card__media">
                    <div class="table">
                        <div class="row">
                            <div id="ret_cell" class="cell">
                                <img id="RETAILER_img" src="resources/retailer.png" style="width: 120px; height: 120px;">
                                <p id="RETAILER_name"></p>
                            </div>
                            <div id="who_cell" class="cell">
                                <img id="WHOLESALER_img" src="resources/wholesaler.png" style="width: 120px; height: 120px;">
                                <p id="WHOLESALER_name"></p>
                            </div>
                            <div id="dis_cell" class="cell">
                                <img id="DISTRIBUTOR_img" src="resources/distributor.png" style="width: 120px; height: 120px;">
                                <p id="DISTRIBUTOR_name"></p>
                            </div>
                            <div id="pro_cell" class="cell">
                                <img id="PRODUCER_img" src="resources/Industry.png" style="width: 120px; height: 120px;">
                                <p id="PRODUCER_name"></p>
                            </div>
                        </div>
                    </div>
                    <div class="mdl-tooltip" for="ret_cell"><%=(localize.getTextFor(ClientLocalizationKeys.WAIT_RET_TOOLTIP)) %></div>
                    <div class="mdl-tooltip" for="who_cell"><%=(localize.getTextFor(ClientLocalizationKeys.WAIT_WHO_TOOLTIP)) %></div>
                    <div class="mdl-tooltip" for="dis_cell"><%=(localize.getTextFor(ClientLocalizationKeys.WAIT_DIS_TOOLTIP)) %></div>
                    <div class="mdl-tooltip" for="pro_cell"><%=(localize.getTextFor(ClientLocalizationKeys.WAIT_PRO_TOOLTIP)) %></div>
                    <div class="waiting-spinner">
                        <div class="spinner-sizer">
                            <div class="mdl-spinner mdl-js-spinner is-active"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div id="game-panel" class="hidden">
            <div class="table-card mdl-card mdl-shadow--6dp mdl-card--horizontal">
                <div class="mdl-card__supporting-text  mdl-card--border">
                    <span><%=(localize.getTextFor(ClientLocalizationKeys.GAME_INFO_WEEKS)) %></span><span id="curweek">k</span>/<span id="totweek">L</span>
                </div>
                <div class="mdl-card__media mdl-card--border">
                    <table>
                        <tr>
                            <th><%=(localize.getTextFor(ClientLocalizationKeys.GAME_INFO_RET)) %>
                            <th><%=(localize.getTextFor(ClientLocalizationKeys.GAME_INFO_WHO)) %>
                            <th><%=(localize.getTextFor(ClientLocalizationKeys.GAME_INFO_DIS)) %>
                            <th><%=(localize.getTextFor(ClientLocalizationKeys.GAME_INFO_PRO)) %>
                        </tr>
                        <tr id="image">
                            <td><img id="game_RETAILER_img"    style="opacity: 0.5; filter: alpha(opacity=50);"  src="resources\retailer.png"    alt="<%=(localize.getTextFor(ClientLocalizationKeys.GAME_INFO_RET)) %>">
                            <td><img id="game_WHOLESALER_img"  style="opacity: 0.5; filter: alpha(opacity=50);"  src="resources\wholesaler.png"  alt="<%=(localize.getTextFor(ClientLocalizationKeys.GAME_INFO_WHO)) %>">
                            <td><img id="game_DISTRIBUTOR_img" style="opacity: 0.5; filter: alpha(opacity=50);"  src="resources\distributor.png" alt="<%=(localize.getTextFor(ClientLocalizationKeys.GAME_INFO_DIS)) %>">
                            <td><img id="game_PRODUCER_img"    style="opacity: 0.5; filter: alpha(opacity=50);"  src="resources\Industry.png"    alt="<%=(localize.getTextFor(ClientLocalizationKeys.GAME_INFO_PRO)) %>">

                        <tr id="name">
                            <td id="retailer_name">   <b><%=(localize.getTextFor(ClientLocalizationKeys.GAME_NAME_LABEL)) %></b><span id="RETAILER_name2">Name 1</span>
                            <td id="wholesaler_name"> <b><%=(localize.getTextFor(ClientLocalizationKeys.GAME_NAME_LABEL)) %></b><span id="WHOLESALER_name2">Name 2</span>
                            <td id="distributor_name"><b><%=(localize.getTextFor(ClientLocalizationKeys.GAME_NAME_LABEL)) %></b><span id="DISTRIBUTOR_name2">Name 3</span>
                            <td id="producer_name">   <b><%=(localize.getTextFor(ClientLocalizationKeys.GAME_NAME_LABEL)) %></b><span id="PRODUCER_name2">Name 4</span>

                        <tr id="profit">
                            <td id="retailer_profit"><b><%=(localize.getTextFor(ClientLocalizationKeys.GAME_COST_LABEL)) %></b><span id="RETAILER_cost">0.00</span>
                            <td id="wholesaler_profit"><b><%=(localize.getTextFor(ClientLocalizationKeys.GAME_COST_LABEL)) %></b><span id="WHOLESALER_cost">0.00</span>
                            <td id="distributor_profit"><b><%=(localize.getTextFor(ClientLocalizationKeys.GAME_COST_LABEL)) %></b><span id="DISTRIBUTOR_cost">0.00</span>
                            <td id="producer_profit"><b><%=(localize.getTextFor(ClientLocalizationKeys.GAME_COST_LABEL)) %></b><span id="PRODUCER_cost">0.00</span>

                        <tr id="stock">
                            <td id="retailer_stock"><b><%=(localize.getTextFor(ClientLocalizationKeys.GAME_STOCK_LABEL)) %></b><span id="RETAILER_stock">16</span>
                            <td id="wholesaler_stock"><b><%=(localize.getTextFor(ClientLocalizationKeys.GAME_STOCK_LABEL)) %></b><span id="WHOLESALER_stock">16</span>
                            <td id="distributor_stock"><b><%=(localize.getTextFor(ClientLocalizationKeys.GAME_STOCK_LABEL)) %></b><span id="DISTRIBUTOR_stock">16</span>
                            <td id="producer_stock"><b><%=(localize.getTextFor(ClientLocalizationKeys.GAME_STOCK_LABEL)) %></b><span id="PRODUCER_stock">16</span>
                                
                        <tr id="las-request">
                            <td id="retailer_last-req"><b><%=(localize.getTextFor(ClientLocalizationKeys.GAME_LAST_REQUEST_LABEL)) %></b><span id="RETAILER_last-req">-</span>
                            <td id="wolesaler_last-req"><b><%=(localize.getTextFor(ClientLocalizationKeys.GAME_LAST_REQUEST_LABEL)) %></b><span id="WHOLESALER_last-req">-</span>
                            <td id="distributor_last-req"><b><%=(localize.getTextFor(ClientLocalizationKeys.GAME_LAST_REQUEST_LABEL)) %></b><span id="DISTRIBUTOR_last-req">-</span>
                            <td id="producer_last-req"><b><%=(localize.getTextFor(ClientLocalizationKeys.GAME_LAST_REQUEST_LABEL)) %></b><span id="PRODUCER_last-req">-</span>
                        </tr>
                        
                        <tr id="incoming-order">
                            <td id="retailer">
                                <table>
                                    <% for(int r = 0; r < ((Long)json.get("delay")); r++){ %>
                                    <tr> <td> <td id="RETAILER_inc_<%=r+1%>"> ---
                                    <%}%>
                                </table>

                            <td id="wholesaler">
                                <table>
                                    <% for(int r = 0; r < ((Long)json.get("delay")); r++){ %>
                                    <tr> <td> <td id="WHOLESALER_inc_<%=r+1%>"> ---
                                    <%}%>
                                </table>

                            <td id="distributor">
                                <table>
                                    <% for(int r = 0; r < ((Long)json.get("delay")); r++){ %>
                                    <tr> <td> <td id="DISTRIBUTOR_inc_<%=r+1%>"> ---
                                    <%}%>
                                </table>

                            <td id="producer">
                                <table>
                                    <% for(int r = 0; r < ((Long)json.get("delay")); r++){ %>
                                    <tr> <td> <td id="PRODUCER_inc_<%=r+1%>"> ---
                                    <%}%>
                                </table>
                    </table>
                </div>
                <div class="mdl-card__supporting-text  mdl-card--border">
                    <span><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_NAME)) %></span>
                </div>
                <div class="mdl-card__media mdl-card--border">
                    <table id="history">
                        <tr>
                            <th><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_FUNC)) %>
                            <th><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_WEEK)) %>
                            <th><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_STOCK)) %>
                            <th><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_PROFIT)) %>
                        <% if(((Long)json.get("delay")) > 0){ %>
                            <th colspan="<%=json.get("delay")%>"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_INCORDER)) %>
                        <% } %>
                            <th><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_REQUEST)) %>
                    </table>
                </div>
            </div>
            <div class="control-panel page-content">
                <div class="half-oval-shaped drop-shadow">
                    <div id="order_input" class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <input class="mdl-textfield__input" type="text" pattern="[0-9]{1,9}" id="order__input">
                        <label class="mdl-textfield__label" for="order_input" id="order__label"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_CONTROL_LABEL_ALLOW)) %></label>
                        <span class="mdl-textfield__error"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_CONTROL_ERROR)) %></span>
                    </div>
                    <div class="mdl-tooltip" for="order_input"><%=(localize.getTextFor(ClientLocalizationKeys.PLAY_ORDER_INPUT_TOOLTIP)) %></div>
                    <span style="display: inline-block;">
                        <button id="order__button" class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect" onclick="send_request();">
                            <%=(localize.getTextFor(ClientLocalizationKeys.GAME_CONTROL_BUTTON)) %>
                        </button>
                    </span>
                </div>
                <div class="mdl-tooltip mdl-tooltip--right" for="order__button"><%=(localize.getTextFor(ClientLocalizationKeys.PLAY_ORDER_BUTTON_TOOLTIP)) %></div>
            </div>
        </div>

        <div id="report-panel" class="hidden">
            <div class="mdl-card mdl-shadow--6dp mdl-card--horizontal">
                <div class="mdl-card__supporting-text mdl-card--border">
                    <p style="display: table; width: 100%;">
                        <label style="display: table-row; font-size: 22px;"><b><%=(localize.getTextFor(ClientLocalizationKeys.REPORT_INFO_NAME)) %></b><span id="rep_name">Sala B-204</span></label>
                        <label style="display: table-row;"><b><%=(localize.getTextFor(ClientLocalizationKeys.REPORT_INFO_TYPE)) %></b><span id="rep_type">Não Informada</span></label>
                    </p>
                </div>
                <div class="mdl-card__supporting-text mdl-card--border">
                    <p style="display: table; width: 100%;">
                        <label style="display: table-cell;"><b><%=(localize.getTextFor(ClientLocalizationKeys.REPORT_INFO_STOCKCOST)) %></b><span id="rep_stockcost">0,5</span></label>
                        <label style="display: table-cell;"><b><%=(localize.getTextFor(ClientLocalizationKeys.REPORT_INFO_MISSCOST)) %></b><span id="rep_missingcost">1,0</span></label>
                        <label style="display: table-cell;"><b><%=(localize.getTextFor(ClientLocalizationKeys.REPORT_INFO_SALEPROFIT)) %></b><span id="rep_saleprofit">1,0</span></label>
                    </p>
                </div>
                <div class="mdl-card__supporting-text mdl-card--border">
                    <p style="display: table; width: 100%;">
                        <label style="display: table-cell;"><b><%=(localize.getTextFor(ClientLocalizationKeys.REPORT_INFO_DELDELAY)) %></b><span id="rep_delay">2</span></label>
                        <label style="display: table-cell;"><b><%=(localize.getTextFor(ClientLocalizationKeys.REPORT_INFO_REALDURATION)) %></b><span id="rep_realduration">50</span></label>
                        <label style="display: table-cell;"><b><%=(localize.getTextFor(ClientLocalizationKeys.REPORT_INFO_INFDURATION)) %></b><span id="rep_infduration">70</span></label>
                    </p>
                </div>
                <div class="mdl-card__supporting-text mdl-card--border">
                    <table>
                        <tr>
                            <th><%=(localize.getTextFor(ClientLocalizationKeys.REPORT_INFO_RETAILER)) %>
                            <th><%=(localize.getTextFor(ClientLocalizationKeys.REPORT_INFO_WHOLESALER)) %>
                            <th><%=(localize.getTextFor(ClientLocalizationKeys.REPORT_INFO_DISTRIBUTOR)) %>
                            <th><%=(localize.getTextFor(ClientLocalizationKeys.REPORT_INFO_PRODUCER)) %>
                        </tr>
                        <tr id="image">
                            <td><img src="resources\retailer.png" alt="Retailer">
                            <td><img src="resources\wholesaler.png" alt="Wholesaler">
                            <td><img src="resources\distributor.png" alt="Distributor">
                            <td><img src="resources\Industry.png" alt="Producer">

                        <tr id="name">
                            <td id="retailer_name"><b><%=(localize.getTextFor(ClientLocalizationKeys.REPORT_NAME_LABEL)) %></b><span id="rep_RETAILER_name">Name 1</span>
                            <td id="wholesaler_name"><b><%=(localize.getTextFor(ClientLocalizationKeys.REPORT_NAME_LABEL)) %></b><span id="rep_WHOLESALER_name">Name 2</span>
                            <td id="distributor_name"><b><%=(localize.getTextFor(ClientLocalizationKeys.REPORT_NAME_LABEL)) %></b><span id="rep_DISTRIBUTOR_name">Name 3</span>
                            <td id="producer_name"><b><%=(localize.getTextFor(ClientLocalizationKeys.REPORT_NAME_LABEL)) %></b><span id="rep_PRODUCER_name">Name 4</span>

                        <tr id="profit">
                            <td id="retailer_profit"><b><%=(localize.getTextFor(ClientLocalizationKeys.REPORT_COST_LABEL)) %></b><span id="rep_RETAILER_cost">0.00</span>
                            <td id="wholesaler_profit"><b><%=(localize.getTextFor(ClientLocalizationKeys.REPORT_COST_LABEL)) %></b><span id="rep_WHOLESALER_cost">0.00</span>
                            <td id="distributor_profit"><b><%=(localize.getTextFor(ClientLocalizationKeys.REPORT_COST_LABEL)) %></b><span id="rep_DISTRIBUTOR_cost">0.00</span>
                            <td id="producer_profit"><b><%=(localize.getTextFor(ClientLocalizationKeys.REPORT_COST_LABEL)) %></b><span id="rep_PRODUCER_cost">0.00</span>

                        <tr id="stock">
                            <td id="retailer_stock"><b><%=(localize.getTextFor(ClientLocalizationKeys.REPORT_STOCK_LABEL)) %></b><span id="rep_RETAILER_stock">16</span>
                            <td id="wholesaler_stock"><b><%=(localize.getTextFor(ClientLocalizationKeys.REPORT_STOCK_LABEL)) %></b><span id="rep_WHOLESALER_stock">16</span>
                            <td id="distributor_stock"><b><%=(localize.getTextFor(ClientLocalizationKeys.REPORT_STOCK_LABEL)) %></b><span id="rep_DISTRIBUTOR_stock">16</span>
                            <td id="producer_stock"><b><%=(localize.getTextFor(ClientLocalizationKeys.REPORT_STOCK_LABEL)) %></b><span id="rep_PRODUCER_stock">16</span>
                    </table>
                    </table>
                </div>
            </div>
            <div class="graph-card mdl-card mdl-shadow--6dp mdl-card--horizontal">
                <div class="mdl-card__supporting-text" id="rep_ord_chart">
                    <iframe id="chart_frame" src="/info?order=true&stock=true&game-name=<%=URLEncoder.encode((String)session.getAttribute("LOGGED_GAME"), "UTF-8").replace("+", "%20") %>&lang=<%=lang %>" style="border: 0pt none; height: 610px; width: 580px;"></iframe>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="resources/body_finish.jsp" />