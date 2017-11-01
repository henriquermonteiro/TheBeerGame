<%@page import="java.io.*" %>
<%@page import="java.net.*" %>
<%@page import="java.util.Locale" %>
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
    
    String lang = Locale.getDefault().getLanguage()+"_"+Locale.getDefault().getCountry();
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
                var curr_row = document.getElementById("curr-row");
//                var last_row = document.getElementsByClassName("last_row");
                
//                var column = document.getElementsByClassName("this_player");
//                if(column.length > 0){
//                    document.getElementsByClassName("this_player")[0].className = "this_player your_turn";
//                }
                var flag = false;
                
                if (input.hasAttribute("disabled")) {
                    input.removeAttribute("disabled");
                    flag = true;
                }

                if (button.hasAttribute("disabled")) {
                    button.removeAttribute("disabled");
                }
                
                button.classList.remove('hidden');

//                if(last_row.length > 0){
//                    last_row[0].className = "";
//                }

                curr_row.className = "last_row";
                
                if(flag){

                    label.innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.GAME_CONTROL_LABEL_ALLOW)) %>";

                    input.focus();
                    input.parentNode.classList.add('is-dirty');
                    input.parentNode.classList.add('is-focused');
                    input.select();
                
                    var twrap = document.getElementById("table-wrapper");
                    twrap.scrollTop = twrap.scrollTop + curr_row.offsetHeight + button.offsetHeight;

                    change_alert(true);
                }
            }

            function block_move() {
                var input = document.getElementById("order__input");
                var label = document.getElementById("order__label");
                var button = document.getElementById("order__button");
                var curr_row = document.getElementById("curr-row");
                
//                if(document.getElementsByClassName("this_player").length > 0){
//                    document.getElementsByClassName("this_player")[0].className = "this_player";
//                }

                if (!input.hasAttribute("disabled")) {
                    input.setAttribute("disabled", "");
                }

                if (!button.hasAttribute("disabled")) {
                    button.setAttribute("disabled", "");
                }
                
                button.classList.add('hidden');
                
                curr_row.className = "last_row hidden";

                label.innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.GAME_CONTROL_LABEL_BLOCK)) %>";
                input.value = "";
                
                change_alert(false);
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

            function add_history_info(func, week, init_stock, rec_order, prev_pend_ord, expec_del, act_del, ord_unf, fin_stock, conf_del, cost_unf, cost_stock, profit, week_bal, orders, move, your_turn, player_funct) {
                var history = document.getElementById("history");
                
                var list = document.getElementsByClassName("last_row");
                var i;
                
                for (i = 0; i < list.length; i++){
                    if(list[i].id !== "curr-row")
                        list[i].classList.remove("last_row");
                }
                
                _func = func;
                _week = week;

                var k = 0;
                var row = document.createElement("TR");
                document.getElementById("history-body").insertBefore(row, document.getElementById("curr-row"));
//                if(!your_turn)
//                    row.className = "last_row";
                
                <% if(((Boolean)json.get("informed_chain"))) { %>
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
                <%}%>    
                
                var cell = row.insertCell(k++);
                cell.innerHTML = week + 1;
                
                var cell = row.insertCell(k++);
                if(func === 0){
                    cell.innerHTML = "---";
                }else{
                    cell.innerHTML = init_stock;
                }
                
                var cell = row.insertCell(k++);
                if(func === 0){
                    cell.innerHTML = "---";
                }else{
                    cell.innerHTML = rec_order;
                }
                
                <% if(!((Boolean)json.get("informed_chain"))) { %>
                if(player_funct !== "0"){
                <%}%>
                var cell = row.insertCell(k++);
                if(func === 0){
                    cell.innerHTML = "---";
                }else{
                    cell.innerHTML = prev_pend_ord;
                }
                
                var cell = row.insertCell(k++);
                if(func === 0){
                    cell.innerHTML = "---";
                }else{
                    cell.innerHTML = expec_del;
                }
                <% if(!((Boolean)json.get("informed_chain"))) { %>
                }
                <%}%>
                
                var cell = row.insertCell(k++);
                if(func === 0){
                    cell.innerHTML = "---";
                }else{
                    cell.innerHTML = act_del;
                }
                
                var cell = row.insertCell(k++);
                if(func === 0){
                    cell.innerHTML = "---";
                }else{
                    cell.innerHTML = ord_unf;
                }
                
                var cell = row.insertCell(k++);
                if(func === 0){
                    cell.innerHTML = "---";
                }else{
                    cell.innerHTML = fin_stock;
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
                if(func === 0){
                    cell.innerHTML = "---";
                }else{
                    cell.innerHTML = cost_unf;
                }
                
                var cell = row.insertCell(k++);
                if(func === 0){
                    cell.innerHTML = "---";
                }else{
                    cell.innerHTML = cost_stock;
                }
                
                <% if(((Double)json.get("selling_profit")) != 0.0) { %>
                var cell = row.insertCell(k++);
                if(func === 0){
                    cell.innerHTML = "---";
                }else{
                    cell.innerHTML = profit;
                }
                <%}%>
                
                var cell = row.insertCell(k++);
                cell.innerHTML = move;
                
                <% if(!((Boolean)json.get("informed_chain"))) { %>
                if(player_funct !== "3"){
                <%}%>
                var cell = row.insertCell(k++);
                if(func === 0){
                    cell.innerHTML = "---";
                }else{
                    cell.innerHTML = conf_del;
                }
                <% if(!((Boolean)json.get("informed_chain"))) { %>
                }
                <%}%>
                
//                var cell = row.insertCell(k++);
//                if(func === 0){
//                    cell.innerHTML = "---";
//                }else{
//                    cell.innerHTML = week_bal;
//                }

                var twrap = document.getElementById("table-wrapper");
                twrap.scrollTop = twrap.scrollTop + row.offsetHeight;
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

                state = "r";
            }
            
            function ax_sync_play_table(header_id, column_id){
                var header = document.getElementById(header_id);
                var column = document.getElementById(column_id);
                
                column.style = "width: " + header.offsetWidth + "px;";
                
                return header.offsetWidth;
            }
            
            function sync_play_table(){
                if(state === "p"){
                    var hist = document.getElementById("history");
                    var hist_h = document.getElementById("history_header");
                    
                    hist.style = "heigth: calc(100% - " + hist_h.offsetHeight + "px);";
                    
                    var hist_media = document.getElementById("history-media");
                    var top_media = document.getElementById("top-media");
                    
                    hist_media.style = "heigth: calc(100% - " + top_media.offsetHeight + "px);";
                    
                    var sum = 0;
                    sum += ax_sync_play_table("history_header", "history");
                    ax_sync_play_table("week_header", "week_column");
                    ax_sync_play_table("init_stock_header", "istock_column");
                    ax_sync_play_table("rec_order_header", "recorder_column");
                    if(is_set !== "0"){ 
                        ax_sync_play_table("prev_unf_header", "prevunf_column");
                        ax_sync_play_table("expe_del_header", "expdel_column");
                    }
                    ax_sync_play_table("act_del_header", "actdel_column");
                    ax_sync_play_table("unf_order_header", "unford_column");
                    ax_sync_play_table("final_stock_header", "fstock_column");
                    <% for(int u = 1; u <= ((Long)json.get("delay")); u++){ %>ax_sync_play_table("inc_order_header_week<%=u%>", "inc<%=u%>_column");<%  } %>
                    ax_sync_play_table("cost_del_header", "costdel_column");
                    ax_sync_play_table("cost_stock_header", "coststo_column");
                    <% if(((Double)json.get("selling_profit")) != 0.0) { %>ax_sync_play_table("prof_sale_header", "profsal_column");<% } %>
                    sum -= ax_sync_play_table("ordered_header", "ordered_column");
                    if(is_set !== "3") sum -= ax_sync_play_table("conf_deli_header", "confdel_column");
                    
                    var button = document.getElementById("order__button");
                    button.style = "margin-left: " + (sum + 4) + "px; width:" + (document.getElementById("ordered_header").offsetWidth - 3) + "px;";
                }
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
            
            var is_set = -1;
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

                for (var player in json_state.players) {
                    var func = json_state.players[player].function;
                    
                    if(is_set === -1){
                        if(json_state.players[player].name === "<%=(String)session.getAttribute("USER-ID") %>"){
                            document.getElementById("game_"+func+"_img").style.opacity = "1.0";
                            document.getElementById("game_"+func+"_img").style.filter = "alpha(opacity=100)";
                            is_set = player;
                            
                            if(is_set === "0"){
                                var pending = document.getElementById("prev_unf_header");
                                var expected = document.getElementById("expe_del_header");
                                var row = document.getElementById("header_row");
                                
                                document.getElementById("RETAILER_column").className = "this_player";
                                
                                document.getElementsByClassName("ret_tint")[0].className = "ret_tint paint";
                                
                                if(pending !== undefined || expected !== undefined){
                                    row.removeChild(pending);
                                    row.removeChild(expected);
                                }
                                
                                pending = document.getElementById("curr-prevunf");
                                expected = document.getElementById("curr-expdel");
                                row = document.getElementById("curr-row");
                                
                                if(pending !== undefined || expected !== undefined){
                                    row.removeChild(pending);
                                    row.removeChild(expected);
                                }
                                
                                var col = document.getElementById("prevunf_column");
                                
                                if(col !== undefined){
                                    col.parentNode.removeChild(col);
                                }
                                
                                col = document.getElementById("expdel_column");
                                
                                if(col !== undefined){
                                    col.parentNode.removeChild(col);
                                }
                                
                                document.getElementById("unf_order_header").innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_UNFULFORDER_RETAILER)) %>";
                                document.getElementById("unf_order_header_tooltip").innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_UNFULFORDER_RETAILER_TT)) %>";
                            }else if(is_set === "1"){
                                document.getElementById("WHOLESALER_column").className = "this_player";
                                
                                document.getElementById("rec_order_header").innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_RECORDER_WHOLESALER)) %>";
                                document.getElementById("rec_order_header_tooltip").innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_RECORDER_WHOLESALER_TT)) %>";
                                document.getElementById("conf_deli_header").innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_CONFIRDELI_WHOLESALER)) %>";
                                document.getElementById("conf_deli_header_tooltip").innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_CONFIRDELI_WHOLESALER_TT)) %>";
                                document.getElementById("ordered_header").innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_REQUEST_WHOLESALER)) %>";
                                document.getElementById("ordered_header_tooltip").innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_REQUEST_WHOLESALER_TT)) %>";
                                
                                document.getElementsByClassName("who_tint")[0].className = "who_tint paint";
                            }else if(is_set === "2"){
                                document.getElementById("DISTRIBUTOR_column").className = "this_player";
                                
                                document.getElementById("rec_order_header").innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_RECORDER_DISTRIBUTOR)) %>";
                                document.getElementById("rec_order_header_tooltip").innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_RECORDER_DISTRIBUTOR_TT)) %>";
                                document.getElementById("conf_deli_header").innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_CONFIRDELI_DISTRIBUTOR)) %>";
                                document.getElementById("conf_deli_header_tooltip").innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_CONFIRDELI_DISTRIBUTOR_TT)) %>";
                                document.getElementById("ordered_header").innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_REQUEST_DISTRIBUTOR)) %>";
                                document.getElementById("ordered_header_tooltip").innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_REQUEST_DISTRIBUTOR_TT)) %>";
                                
                                document.getElementsByClassName("dis_tint")[0].className = "dis_tint paint";
                            }else if(is_set === "3"){
                                <% if(((Long)json.get("delay")) > 0) { %> 
                                    document.getElementById("inc_order_header").innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_INCORDER_PRODUCER)) %>"; 
                                    document.getElementById("inc_order_header_tooltip").innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_INCORDER_PRODUCER_TT)) %>"; 
                                <% } %>
                                    
                                document.getElementById("PRODUCER_column").className = "this_player";
                                
                                document.getElementById("rec_order_header").innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_RECORDER_PRODUCER)) %>";
                                document.getElementById("rec_order_header_tooltip").innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_RECORDER_PRODUCER_TT)) %>";
                                document.getElementById("conf_deli_header").innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_CONFIRDELI_PRODUCER)) %>";
                                document.getElementById("conf_deli_header_tooltip").innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_CONFIRDELI_PRODUCER_TT)) %>";
                                document.getElementById("ordered_header").innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_REQUEST_PRODUCER)) %>";
                                document.getElementById("ordered_header_tooltip").innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_REQUEST_PRODUCER_TT)) %>";
                                
                                var row = document.getElementById("header_row");
                                var confir = document.getElementById("conf_deli_header");
                                
                                if(confir !== undefined){
                                    row.removeChild(confir);
                                }
                                
                                row = document.getElementById("curr-row");
                                confir = document.getElementById("curr-confdel");
                                
                                if(confir !== undefined){
                                    row.removeChild(confir);
                                }
                                
                                var col = document.getElementById("confdel_column");
                                
                                if(col !== undefined){
                                    col.parentNode.removeChild(col);
                                }
                                
                                document.getElementsByClassName("pro_tint")[0].className = "pro_tint paint";
                            }
                        }
                        
                        sync_play_table();
                    }
                    
                    document.getElementById(func + "_name2").innerHTML = json_state.players[player].name;
                        
                    if(json_state.players[player].name === "<%=(String)session.getAttribute("USER-ID") %>"){
                        var cost = json_state.players[player].cost;
                        if(cost !== "---")
                            cost = Number(json_state.players[player].cost).toFixed(2);

                        
                        document.getElementById("curr-week").innerHTML = json_state.current_week + 1;
                        document.getElementById("curr-istock").innerHTML = json_state.players[player].initial_stock;
                        if(is_set !== "0") document.getElementById("curr-prevunf").innerHTML = json_state.players[player].previously_pending_orders;
                        
                        if(json_state.your_turn){
                            document.getElementById("curr-recorder").innerHTML = json_state.players[player].received_order;
                            document.getElementById("curr-actdel").innerHTML = json_state.players[player].actual_delivery;
                            document.getElementById("curr-unford").innerHTML = json_state.players[player].order_unfulfilled;
                            document.getElementById("curr-fstock").innerHTML = json_state.players[player].final_stock;
                            document.getElementById("curr-costdel").innerHTML = Number(json_state.players[player].cost_unfulfillment).toFixed(2);
                            document.getElementById("curr-coststo").innerHTML = Number(json_state.players[player].cost_stock).toFixed(2);
                            <% if(((Double)json.get("selling_profit")) != 0.0) { %>document.getElementById("curr-profsal").innerHTML = Number(json_state.players[player].cost_stock).toFixed(2);<%}%>

                            if(is_set !== "0"){
                                document.getElementById("curr-expdel").innerHTML = json_state.players[player].expected_delivery;
                            }

                            for (var inc in json_state.players[player].incoming) {
                                var dist = json_state.players[player].incoming[inc].distance;
                                document.getElementById("curr-inc" + dist).innerHTML = json_state.players[player].incoming[inc].value;
                            }
                        }else{
                            document.getElementById("curr-recorder").innerHTML = "";
                            document.getElementById("curr-actdel").innerHTML = "";
                            document.getElementById("curr-unford").innerHTML = "";
                            document.getElementById("curr-fstock").innerHTML = "";
                            document.getElementById("curr-costdel").innerHTML = "";
                            document.getElementById("curr-coststo").innerHTML = "";
                            <% if(((Double)json.get("selling_profit")) != 0.0) { %>document.getElementById("curr-profsal").innerHTML = "";<%}%>

                            if(is_set !== "0"){
                                document.getElementById("curr-expdel").innerHTML = "";
                            }

                            for (var inc in json_state.players[player].incoming) {
                                var dist = json_state.players[player].incoming[inc].distance;
                                document.getElementById("curr-inc" + dist).innerHTML = "";
                            }
                        }
                    }
                }
                
                for(var line in json_state.history) {
                    add_history_info(
                        json_state.history[line].function,
                        json_state.history[line].week,
                        json_state.history[line].initial_stock,
                        json_state.history[line].received_order,
                        json_state.history[line].previously_pending_orders,
                        json_state.history[line].expected_delivery,
                        json_state.history[line].actual_delivery,
                        json_state.history[line].order_unfulfilled,
                        json_state.history[line].final_stock,
                        json_state.history[line].confirmed_delivery,
                        Number(json_state.history[line].cost_unfulfillment).toFixed(2),
                        Number(json_state.history[line].cost_stock).toFixed(2),
                        Number(json_state.history[line].profit).toFixed(2),
                        Number(json_state.history[line].week_balance).toFixed(2),
                        json_state.history[line].order,
                        json_state.history[line].move,
                        json_state.your_turn,
                        is_set
                    );
                }
            }

            function processReport(json_state) {
                if (state !== "r") {
                    
                    show_return();

                    document.getElementById("rep_name").innerHTML = json_state.name;
                    if(json_state.informed_chain === true){
                        document.getElementById("rep_type").innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.REPORT_TYPE_INFO)) %>";
                    }else{
                        document.getElementById("rep_type").innerHTML = "<%=(localize.getTextFor(ClientLocalizationKeys.REPORT_TYPE_NINF)) %>";
                    }

                    document.getElementById("rep_stockcost").innerHTML = json_state.stock_cost;
                    document.getElementById("rep_missingcost").innerHTML = Number(json_state.missing_cost).toFixed(2);
//                    document.getElementById("rep_saleprofit").innerHTML = json_state.selling_profit;

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
            
            function change_alert(bool){
                var att = document.getElementById("attention");

                if(bool){
                  att.className = "attention";
                }else{
                  att.className = "attention hidden";
                }
            }
        </script>
        <jsp:include page="resources/head_finish.jsp"/>
        <body onload="initialize();">
        <jsp:include page="resources/body_begin.jsp">
            <jsp:param name="hide_body" value="true"/>
            <jsp:param name="source" value="game.jsp"/>
            <jsp:param name="show_return" value="true"/>
            <jsp:param name="scroll_header" value="false"/>
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
            <div id="attention" class="attention hidden">
                <div class="rect"></div>
                <div class="circ"></div>
            </div>
            <div class="table-card mdl-card mdl-shadow--6dp mdl-card--horizontal">
                <div id="top-media" class="mdl-card__media mdl-card--border">
                    <table id="top_table">
                        <colgroup>
                            <col id="RETAILER_column" style="width: 25%;"/>
                            <col id="WHOLESALER_column" style="width: 25%;"/>
                            <col id="DISTRIBUTOR_column" style="width: 25%;"/>
                            <col id="PRODUCER_column" style="width: 25%;"/>
                        </colgroup>
                        <tr>
                            <th><%=(localize.getTextFor(ClientLocalizationKeys.GAME_INFO_RET)) %><span style="font-weight: normal;" id="RETAILER_name2">Name 1</span>
                            <th><%=(localize.getTextFor(ClientLocalizationKeys.GAME_INFO_WHO)) %><span style="font-weight: normal;" id="WHOLESALER_name2">Name 2</span>
                            <th><%=(localize.getTextFor(ClientLocalizationKeys.GAME_INFO_DIS)) %><span style="font-weight: normal;" id="DISTRIBUTOR_name2">Name 3</span>
                            <th><%=(localize.getTextFor(ClientLocalizationKeys.GAME_INFO_PRO)) %><span style="font-weight: normal;" id="PRODUCER_name2">Name 4</span>
                        </tr>
                        <tr id="image">
                            <td><figure class="ret_tint"><img id="game_RETAILER_img"    style="opacity: 0.5; filter: alpha(opacity=50);"  src="resources\retailer.png"    alt="<%=(localize.getTextFor(ClientLocalizationKeys.GAME_INFO_RET)) %>"></figure>
                            <td><figure class="who_tint"><img id="game_WHOLESALER_img"  style="opacity: 0.5; filter: alpha(opacity=50);"  src="resources\wholesaler.png"  alt="<%=(localize.getTextFor(ClientLocalizationKeys.GAME_INFO_WHO)) %>"></figure>
                            <td><figure class="dis_tint"><img id="game_DISTRIBUTOR_img" style="opacity: 0.5; filter: alpha(opacity=50);"  src="resources\distributor.png" alt="<%=(localize.getTextFor(ClientLocalizationKeys.GAME_INFO_DIS)) %>"></figure>
                            <td><figure class="pro_tint"><img id="game_PRODUCER_img"    style="opacity: 0.5; filter: alpha(opacity=50);"  src="resources\Industry.png"    alt="<%=(localize.getTextFor(ClientLocalizationKeys.GAME_INFO_PRO)) %>"></figure>

                    </table>
                </div>
                <div id="history-media" class="mdl-card__media mdl-card--border">
                    <table id="history_header">
                        <tr id="header_row" style="font-size: <%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_HEADER_SIZE))%>">
                            <% if(((Boolean)json.get("informed_chain"))) { %><th id="func_header" rowspan="2"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_FUNC)) %><%}%>
                            <th id="week_header" rowspan="2"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_WEEK)) %>
                            <th id="init_stock_header" rowspan="2"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_ISTOCK)) %>
                            <th id="rec_order_header" rowspan="2"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_RECORDER_RETAILER)) %>
                            <th id="prev_unf_header" rowspan="2"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_PREVPENDING)) %>
                            <th id="expe_del_header" rowspan="2"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_EXPECDELIV)) %>
                            <th id="act_del_header" rowspan="2"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_ACTUDELI)) %>
                            <th id="unf_order_header" rowspan="2"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_UNFULFORDER)) %>
                            <th id="final_stock_header" rowspan="2"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_FSTOCK)) %>
                        <% if(((Long)json.get("delay")) > 0){ %>
                            <th id="inc_order_header" colspan="<%=json.get("delay")%>"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_INCORDER)) %>
                        <% } %>
                            <th id="cost_del_header" rowspan="2"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_COSTDELI)) %>
                            <th id="cost_stock_header" rowspan="2"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_COSTSTOCK)) %>
                        <% if(((Double)json.get("selling_profit")) != 0.0) { %>
                            <th id="prof_sale_header" rowspan="2"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_PROFSALE)) %>
                        <% } %>
                            <th id="ordered_header" rowspan="2"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_REQUEST_RETAILER)) %>
                            <th id="conf_deli_header" rowspan="2"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_CONFIRDELI_RETAILER)) %>
                        
                        <% if(((Long)json.get("delay")) > 0){ %>
                        <tr style="font-size: <%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_HEADER_SIZE))%>">
                        <%
                            for(int u = 1; u <= ((Long)json.get("delay")); u++){
                        %>
                            <th id="inc_order_header_week<%=u%>"><%=u%><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_INCORDER_WEEK)) %>
                        <%  } 
                           } %>
                            
                    </table>
                    <div id="table-wrapper">
                        <table id="history">
                            <colgroup>
                                <col id="week_column"/>
                                <col id="istock_column"/>
                                <col id="recorder_column"/>
                                <col id="prevunf_column"/>
                                <col id="expdel_column"/>
                                <col id="actdel_column"/>
                                <col id="unford_column"/>
                                <col id="fstock_column"/>
                                <% for(int u = 1; u <= ((Long)json.get("delay")); u++){ %><col id="inc<%=u%>_column"/><%  } %>
                                <col id="costdel_column"/>
                                <col id="coststo_column"/>
                                <% if(((Double)json.get("selling_profit")) != 0.0) { %><col id="profsal_column"/><% } %>
                                <col id="ordered_column" class="light-orange"/>
                                <col id="confdel_column"/>
                            </colgroup>
                            <tbody id="history-body">
                                <tr id="curr-row" class="last_row hidden">
                                    <td id="curr-week"></td>
                                    <td id="curr-istock"></td>
                                    <td id="curr-recorder"></td>
                                    <td id="curr-prevunf"></td>
                                    <td id="curr-expdel"></td>
                                    <td id="curr-actdel"></td>
                                    <td id="curr-unford"></td>
                                    <td id="curr-fstock"></td>
                                    <% for(int u = 1; u <= ((Long)json.get("delay")); u++){ %><td id="curr-inc<%=u%>"><%  } %>
                                    <td id="curr-costdel"></td>
                                    <td id="curr-coststo"></td>
                                    <% if(((Double)json.get("selling_profit")) != 0.0) { %><td id="curr-profsal"></td><% } %>
                                    <td id="curr-ordered">
                                        <div id="order_input" class="mdl-textfield mdl-js-textfield">
                                            <input disabled class="mdl-textfield__input" type="text" pattern="[0-9]{1,9}" id="order__input">
                                            <label class="mdl-textfield__label" for="order_input" id="order__label"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_CONTROL_LABEL_ALLOW)) %></label>
                                            <span class="mdl-textfield__error"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_CONTROL_ERROR)) %></span>
                                        </div>
                                    </td>
                                    <td id="curr-confdel"></td>
                        </table>
                        <div class="mdl-tooltip mdl-tooltip--left" for="order_input"><%=(localize.getTextFor(ClientLocalizationKeys.PLAY_ORDER_INPUT_TOOLTIP)) %></div>
                        <button id="order__button" class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect hidden" disabled onclick="send_request();">
                            <%=(localize.getTextFor(ClientLocalizationKeys.GAME_CONTROL_BUTTON)) %>
                        </button>
                        <div class="mdl-tooltip mdl-tooltip--left" for="order__button"><%=(localize.getTextFor(ClientLocalizationKeys.PLAY_ORDER_BUTTON_TOOLTIP)) %></div>
                    </div>
                </div>
                <div class="mdl-tooltip" for="func_header"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_FUNC_TT)) %></div>
                <div class="mdl-tooltip" for="week_header"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_WEEK_TT)) %></div>
                <div class="mdl-tooltip" for="init_stock_header"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_ISTOCK_TT)) %></div>
                <div id="rec_order_header_tooltip" class="mdl-tooltip" for="rec_order_header"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_RECORDER_RETAILER_TT)) %></div>
                <div class="mdl-tooltip" for="prev_unf_header"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_PREVPENDING_TT)) %></div>
                <div class="mdl-tooltip" for="expe_del_header"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_EXPECDELIV_TT)) %></div>
                <div id="act_del_header_tooltip" class="mdl-tooltip" for="act_del_header"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_ACTUDELI_TT)) %></div>
                <div id="unf_order_header_tooltip" class="mdl-tooltip" for="unf_order_header"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_UNFULFORDER_TT)) %></div>
                <div class="mdl-tooltip" for="final_stock_header"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_FSTOCK_TT)) %></div>
                <div id="conf_deli_header_tooltip" class="mdl-tooltip" for="conf_deli_header"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_CONFIRDELI_RETAILER_TT)) %></div>
                <div class="mdl-tooltip" for="cost_del_header"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_COSTDELI_TT)) %></div>
                <div class="mdl-tooltip" for="cost_stock_header"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_COSTSTOCK_TT)) %></div>
                <div class="mdl-tooltip" for="prof_sale_header"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_PROFSALE_TT)) %></div>
                <div class="mdl-tooltip" for="bal_header"><%=(localize.getTextFor( (((Double)json.get("selling_profit")) == 0.0 ? ClientLocalizationKeys.GAME_TABLE_TCOST_TT : ClientLocalizationKeys.GAME_TABLE_BALANCE_TT) )) %></div>
                <div id="inc_order_header_tooltip" class="mdl-tooltip" for="inc_order_header"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_INCORDER_TT)) %></div>
                <div id="ordered_header_tooltip" class="mdl-tooltip" for="ordered_header"><%=(localize.getTextFor(ClientLocalizationKeys.GAME_TABLE_REQUEST_RETAILER_TT)) %></div>
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
                    <iframe id="chart_frame" src="/info?order=true&stock=true&game-name=<%=URLEncoder.encode((String)session.getAttribute("LOGGED_GAME"), "UTF-8").replace("+", "%20") %>&lang=<%=lang %>" style="border: 0pt none; height: 610px; width: 1160px;"></iframe>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="resources/body_finish.jsp" />