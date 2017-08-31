<%@page import="java.io.*" %>
<%@page import="java.net.*" %>
<%@page import="org.json.simple.*" %>
<%@page import="org.json.simple.parser.*" %>

<%
//    System.out.println("Test Game - " + request.getMethod());
    if(session.getAttribute("USER-ID") == null || ((String)session.getAttribute("USER-ID")).isEmpty() || session.getAttribute("LOGGED_GAME") == null || ((String)session.getAttribute("LOGGED_GAME")).isEmpty() ){
        response.sendRedirect("/check_in.jsp");
    }
    
    /*String recv;
    String recvbuff = "";
    URL checkin_json = new URL(request.getScheme(), request.getServerName(), request.getServerPort(), "/update.jsp");
    HttpURLConnection urlcon = (HttpURLConnection)checkin_json.openConnection();
    urlcon.setRequestMethod("POST");
    BufferedReader buffread = new BufferedReader(new InputStreamReader(urlcon.getInputStream()));

    while ((recv = buffread.readLine()) != null)
        recvbuff += recv;
    buffread.close();
        
    JSONParser parser = new JSONParser();
    JSONObject json = (JSONObject) parser.parse(recvbuff);
    JSONArray list = (JSONArray)json.get("game_state");*/
%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
        <link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.amber-blue.min.css" />
        <script defer src="https://code.getmdl.io/1.3.0/material.min.js"></script>
        <meta charset="utf-8"/>
        <link rel="stylesheet" type="text/css" href="bub.css"/>
        <link rel="stylesheet" type="text/css" href="game.css"/>
        <script src="bub.js"></script>
        <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
        <script>
            google.charts.load('current', {'packages': ['corechart']});
            google.charts.setOnLoadCallback(draw_flag);

            var ready_to_draw = false;

            var state = "w";
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

                label.innerHTML = "Pedido";

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

                label.innerHTML = "Aguarde sua vez ...";
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

            function add_history_info(func, week, stock, profit, order1, order2, demand) {
                var history = document.getElementById("history");

                var row = history.insertRow(1);
                var cell1 = row.insertCell(0);
                var cell2 = row.insertCell(1);
                var cell3 = row.insertCell(2);
                var cell4 = row.insertCell(3);
                var cell5 = row.insertCell(4);
                var cell6 = row.insertCell(5);
                var cell7 = row.insertCell(6);

                cell1.innerHTML = func;
                cell2.innerHTML = week;
                cell3.innerHTML = stock;
                cell4.innerHTML = profit;
                cell5.innerHTML = order1;
                cell6.innerHTML = order2;
                cell7.innerHTML = demand;
            }

            function start_game() {
                var wait = document.getElementById("waiting");
                if (wait !== undefined && wait !== null) {
                    wait.outerHTML = '';
                }
                delete wait;
                var game = document.getElementById("game-panel");
                if (game !== undefined) {
                    game.classList.remove("hidden");
                }

                state = "p";
            }

            function draw_flag() {
                if (ready_to_draw) {
                    draw_chart();
                }

                ready_to_draw = true;
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

                draw_flag();

                clearInterval(update_interval);
                state = "r";
            }

            function get_data() {
                return [['Semana', 'Consumidor', 'Varejista', 'Atacadista', 'Distribuidor', 'Produtor'],
                    ['Semana  1', 5, 5, 5, 5, 5],
                    ['Semana  2', 5, 7, 7, 7, 7],
                    ['Semana  3', 5, 7, 7, 7, 7],
                    ['Semana  4', 5, 7, 7, 7, 7],
                    ['Semana  5', 5, 7, 7, 7, 7],
                    ['Semana  6', 5, 7, 7, 7, 7],
                    ['Semana  7', 5, 7, 7, 7, 7],
                    ['Semana  8', 5, 7, 7, 7, 7],
                    ['Semana  9', 5, 7, 7, 7, 7],
                    ['Semana 10', 5, 7, 7, 7, 7],
                    ['Semana 11', 5, 7, 7, 7, 7],
                    ['Semana 12', 5, 7, 7, 7, 7],
                    ['Semana 13', 5, 7, 7, 7, 7],
                    ['Semana 14', 5, 7, 7, 7, 7],
                    ['Semana 15', 5, 7, 7, 7, 7],
                    ['Semana 16', 5, 7, 7, 7, 7],
                    ['Semana 17', 5, 7, 7, 7, 7],
                    ['Semana 18', 5, 7, 7, 7, 7],
                    ['Semana 19', 5, 7, 7, 7, 7],
                    ['Semana 20', 5, 7, 7, 7, 7],
                    ['Semana 21', 5, 7, 7, 7, 7],
                    ['Semana 22', 5, 7, 7, 7, 7],
                    ['Semana 23', 5, 7, 7, 7, 7],
                    ['Semana 24', 5, 7, 7, 7, 7],
                    ['Semana 25', 5, 7, 7, 7, 7],
                    ['Semana 26', 5, 7, 7, 7, 7],
                    ['Semana 27', 5, 7, 7, 7, 7],
                    ['Semana 28', 5, 7, 7, 7, 7],
                    ['Semana 29', 5, 7, 7, 7, 7],
                    ['Semana 30', 5, 7, 7, 7, 7],
                    ['Semana 31', 5, 7, 7, 7, 7],
                    ['Semana 32', 5, 7, 7, 7, 7],
                    ['Semana 33', 5, 7, 7, 7, 7],
                    ['Semana 34', 5, 7, 7, 7, 7],
                    ['Semana 35', 5, 7, 7, 7, 7],
                    ['Semana 36', 5, 7, 7, 7, 7],
                    ['Semana 37', 5, 7, 7, 7, 7],
                    ['Semana 38', 5, 7, 7, 7, 7],
                    ['Semana 39', 5, 7, 7, 7, 7],
                    ['Semana 40', 5, 7, 7, 7, 7],
                    ['Semana 41', 5, 7, 7, 7, 7],
                    ['Semana 42', 5, 7, 7, 7, 7],
                    ['Semana 43', 5, 7, 7, 7, 7],
                    ['Semana 44', 5, 7, 7, 7, 7],
                    ['Semana 45', 5, 7, 7, 7, 7],
                    ['Semana 46', 5, 7, 7, 7, 7],
                    ['Semana 47', 5, 7, 7, 7, 7],
                    ['Semana 48', 5, 7, 7, 7, 7],
                    ['Semana 49', 5, 7, 7, 7, 7],
                    ['Semana 50', 5, 7, 7, 7, 7]];
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

                document.getElementById("curweek").innerHTML = json_state.current_week;
                document.getElementById("totweek").innerHTML = json_state.total_week;

                for (var player in json_state.players) {
                    var func = json_state.players[player].function;

                    document.getElementById(func + "_name2").innerHTML = json_state.players[player].name;
                    document.getElementById(func + "_cost").innerHTML = json_state.players[player].cost;
                    document.getElementById(func + "_stock").innerHTML = json_state.players[player].stock;

                    for (var inc in json_state.players[player].incoming) {
                        var dist = json_state.players[player].incoming[inc].distance;
                        document.getElementById(func + "_inc_" + dist).innerHTML = json_state.players[player].incoming[inc].value;
                    }
                }
            }

            function processReport(json_state) {
                if (state !== "r") {
                    report_data = [['Semana', 'Consumidor', 'Varejista', 'Atacadista', 'Distribuidor', 'Produtor']];
                    
                    for(var week in json_state.graph_data){
                        var week_ax = json_state.graph_data[week];
                        
                        var array = new Array(week_ax.week, week_ax.c, week_ax.r, week_ax.w, week_ax.d, week_ax.p);
                        
                        report_data.push(array);
                    }

                    document.getElementById("rep_name").innerHTML = json_state.name;
                    document.getElementById("rep_type").innerHTML = json_state.informed_chain;

                    document.getElementById("rep_stockcost").innerHTML = json_state.stock_cost;
                    document.getElementById("rep_missingcost").innerHTML = json_state.missing_cost;
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

                    goto_report();
                }
            }

            function updatePage() {
                var jsonHttp = new XMLHttpRequest();
                jsonHttp.open("POST", "/update.jsp", false);

                jsonHttp.onreadystatechange = function () {
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
                            default : // para testes
                                break;
                        }
                    }
                };

                jsonHttp.send(null);
            }

            function initialize() {
                bubbles();
                update_interval = setInterval(updatePage, 1000);
            }

        </script>
    </head>
    <body onload="initialize();">
        <div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
            <header class="mdl-layout__header">
                <div class="mdl-layout-icon"> </div>
                <div class="mdl-layout__header-row">
                    <span class="mdl-layout__title">Simple Layout</span>
                </div>
            </header>
            <div class="mdl-layout__drawer">
                <span class="mdl-layout__title">Simple Layout</span>
                <nav class="mdl-navigation">
                    <a class="mdl-navigation__link" href="#">Nav link 1</a>
                    <a class="mdl-navigation__link" href="#">Nav link 2</a>
                    <a class="mdl-navigation__link" href="#">Nav link 3</a>
                </nav>
            </div>
            <main class="mdl-layout__content">
                <div id="bub_back" class="bubbles"></div>

                <div class="center-content">
                    <div id="waiting">
                        <div class="wait-card mdl-card mdl-shadow--2dp">
                            <div class="mdl-card__title mdl-card--expand">
                                <h2 class="mdl-card__title-text">Esperando Jogadores</h2>
                            </div>
                            <div class="mdl-card__media">
                                <div class="table">
                                    <div class="row">
                                        <div class="cell">
                                            <img id="PRODUCER_img" src="resources/Industry.png" style="width: 120px; height: 120px;">
                                            <p id="PRODUCER_name"></p>
                                        </div>
                                        <div class="cell">
                                            <img id="DISTRIBUTOR_img" src="resources/distributor.png" style="width: 120px; height: 120px;">
                                            <p id="DISTRIBUTOR_name"></p>
                                        </div>
                                        <div class="cell">
                                            <img id="WHOLESALER_img" src="resources/wholesaler.png" style="width: 120px; height: 120px;">
                                            <p id="WHOLESALER_name"></p>
                                        </div>
                                        <div class="cell">
                                            <img id="RETAILER_img" src="resources/retailer.png" style="width: 120px; height: 120px;">
                                            <p id="RETAILER_name"></p>
                                        </div>
                                    </div>
                                </div>
                                <div class="waiting-spinner">
                                    <div class="spinner-sizer">
                                        <div class="mdl-spinner mdl-js-spinner is-active"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div id="game-panel" class="hidden">
                        <div class="mdl-layout mdl-js-layout mdl-layout--fixed-header mdl-layout--fixed-tabs">
                            <header class="mdl-layout__header tab-smaller">
                                <!-- Tabs -->
                                <div class="mdl-layout__tab-bar mdl-js-ripple-effect">
                                    <!--a href="#fixed-tab-1" class="mdl-layout__tab">Jogo</a-->
                                    <a href="#fixed-tab-2" class="mdl-layout__tab is-active">Detalhes</a>
                                </div>
                            </header>
                            <main class="mdl-layout__content">
                                <section class="mdl-layout__tab-panel hidden" id="fixed-tab-1">
                                    <div class="page-content">
                                        <div class="game-card mdl-card mdl-shadow--3dp">
                                            <div class="mdl-card__media">
                                                <div class="hidden">
                                                    <img id="ind" src="resources/Industry.png">
                                                    <img id="dis" src="resources/distributor.png">
                                                    <img id="who" src="resources/wholesaler.png">
                                                    <img id="ret" src="resources/retailer.png">
                                                </div>
                                                <canvas id="game_canvas" width="480" height="480">
                                                </canvas>
                                            </div>
                                        </div>
                                    </div>
                                </section>
                                <section class="mdl-layout__tab-panel  is-active" id="fixed-tab-2">
                                    <div class="table-card mdl-card mdl-shadow--6dp mdl-card--horizontal">
                                        <div class="mdl-card__supporting-text  mdl-card--border">
                                            <span>Semana </span><span id="curweek">k</span>/<span id="totweek">L</span>
                                        </div>
                                        <div class="mdl-card__media mdl-card--border">
                                            <table>
                                                <tr>
                                                    <th>Retailer
                                                    <th>Wholesaler
                                                    <th>Distributor
                                                    <th>Producer
                                                </tr>
                                                <tr id="image">
                                                    <td><img src="resources\retailer.png" alt="Retailer">
                                                    <td><img src="resources\wholesaler.png" alt="Wholesaler">
                                                    <td><img src="resources\distributor.png" alt="Distributor">
                                                    <td><img src="resources\Industry.png" alt="Producer">

                                                <tr id="name">
                                                    <td id="retailer"><b>Nome: </b><span id="RETAILER_name2">Name 1</span>
                                                    <td id="wholesaler"><b>Nome: </b><span id="WHOLESALER_name2">Name 2</span>
                                                    <td id="distributor"><b>Nome: </b><span id="DISTRIBUTOR_name2">Name 3</span>
                                                    <td id="producer"><b>Nome: </b><span id="PRODUCER_name2">Name 4</span>

                                                <tr id="profit">
                                                    <td id="retailer"><b>Custo: </b><span id="RETAILER_cost">0.00</span>
                                                    <td id="wholesaler"><b>Custo: </b><span id="WHOLESALER_cost">0.00</span>
                                                    <td id="distributor"><b>Custo: </b><span id="DISTRIBUTOR_cost">0.00</span>
                                                    <td id="producer"><b>Custo: </b><span id="PRODUCER_cost">0.00</span>

                                                <tr id="stock">
                                                    <td id="retailer"><b>Estoque: </b><span id="RETAILER_stock">16</span>
                                                    <td id="wholesaler"><b>Estoque: </b><span id="WHOLESALER_stock">16</span>
                                                    <td id="distributor"><b>Estoque: </b><span id="DISTRIBUTOR_stock">16</span>
                                                    <td id="producer"><b>Estoque: </b><span id="PRODUCER_stock">16</span>
                                                <tr id="incoming-order">
                                                    <td id="retailer">
                                                        <table>
                                                            <tr> <td>? <td id="RETAILER_inc_1">4
                                                            <tr> <td>  <td id="RETAILER_inc_2">&nbsp</td>
                                                        </table>

                                                    <td id="wholesaler">
                                                        <table>
                                                            <tr> <td>? <td id="WHOLESALER_inc_1">4
                                                            <tr> <td>? <td id="WHOLESALER_inc_2">4
                                                        </table>

                                                    <td id="distributor">
                                                        <table>
                                                            <tr> <td>? <td id="DISTRIBUTOR_inc_1">4
                                                            <tr> <td>? <td id="DISTRIBUTOR_inc_2">4
                                                        </table>

                                                    <td id="producer">
                                                        <table>
                                                            <tr> <td>? <td id="PRODUCER_inc_1">4
                                                            <tr> <td>? <td id="PRODUCER_inc_2">4
                                                        </table>
                                            </table>
                                        </div>
                                        <div class="mdl-card__supporting-text  mdl-card--border">
                                            <span>Histórico</span>
                                        </div>
                                        <div class="mdl-card__media mdl-card--border">
                                            <table id="history">
                                                <tr>
                                                    <th>Function
                                                    <th>Week
                                                    <th>Stock
                                                    <th>Profit
                                                    <th colspan="2">Incoming Order
                                                    <th>Demand
                                                <tr>
                                                    <td>Wholesaler
                                                    <td>1
                                                    <td>16
                                                    <td>0.00
                                                    <td>4
                                                    <td>10
                                                    <td>8
                                            </table>
                                        </div>
                                    </div>
                                </section>
                            </main>
                        </div>
                        <div class="control-panel page-content">
                            <div class="half-oval-shaped drop-shadow">
                                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                    <input class="mdl-textfield__input" type="text" pattern="-?[0-9]{1,9}?" id="order__input">
                                    <label class="mdl-textfield__label" for="order_input" id="order__label">Pedido</label>
                                    <span class="mdl-textfield__error">Não é um pedido válido!</span>
                                </div>
                                <span style="display: inline-block;">
                                    <button class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect" id="order__button" onclick="send_request();">
                                        Enviar Pedido
                                    </button>
                                </span>
                            </div>
                        </div>
                    </div>

                    <div id="report-panel" class="hidden">
                        <div class="mdl-card mdl-shadow--6dp mdl-card--horizontal">
                            <div class="mdl-card__supporting-text mdl-card--border">
                                <p style="display: table; width: 100%;">
                                    <label style="display: table-row; font-size: 22px;"><b>Nome: </b><span id="rep_name">Sala B-204</span></label>
                                    <label style="display: table-row;"><b>Tipo: </b><span id="rep_type">Não Informada</span></label>
                                </p>
                            </div>
                            <div class="mdl-card__supporting-text mdl-card--border">
                                <p style="display: table; width: 100%;">
                                    <label style="display: table-cell;"><b>Custo Armazenamento: </b><span id="rep_stockcost">0,5</span></label>
                                    <label style="display: table-cell;"><b>Custo de Não Entrega: </b><span id="rep_missingcost">1,0</span></label>
                                    <label style="display: table-cell;"><b>Lucro Sobre Venda: </b><span id="rep_saleprofit">1,0</span></label>
                                </p>
                            </div>
                            <div class="mdl-card__supporting-text mdl-card--border">
                                <p style="display: table; width: 100%;">
                                    <label style="display: table-cell;"><b>Tempo de Entrega: </b><span id="rep_delay">2</span></label>
                                    <label style="display: table-cell;"><b>Total de Semanas: </b><span id="rep_realduration">50</span></label>
                                    <label style="display: table-cell;"><b>Semanas Informadas: </b><span id="rep_infduration">70</span></label>
                                </p>
                            </div>
                            <div class="mdl-card__supporting-text mdl-card--border">
                                <table>
                                    <tr>
                                        <th>Retailer
                                        <th>Wholesaler
                                        <th>Distributor
                                        <th>Producer
                                    </tr>
                                    <tr id="image">
                                        <td><img src="resources\retailer.png" alt="Retailer">
                                        <td><img src="resources\wholesaler.png" alt="Wholesaler">
                                        <td><img src="resources\distributor.png" alt="Distributor">
                                        <td><img src="resources\Industry.png" alt="Producer">

                                    <tr id="name">
                                        <td id="retailer"><b>Nome: </b><span id="rep_RETAILER_name">Name 1</span>
                                        <td id="wholesaler"><b>Nome: </b><span id="rep_WHOLESALER_name">Name 2</span>
                                        <td id="distributor"><b>Nome: </b><span id="rep_DISTRIBUTOR_name">Name 3</span>
                                        <td id="producer"><b>Nome: </b><span id="rep_PRODUCER_name">Name 4</span>

                                    <tr id="profit">
                                        <td id="retailer"><b>Custo: </b><span id="rep_RETAILER_cost">0.00</span>
                                        <td id="wholesaler"><b>Custo: </b><span id="rep_WHOLESALER_cost">0.00</span>
                                        <td id="distributor"><b>Custo: </b><span id="rep_DISTRIBUTOR_cost">0.00</span>
                                        <td id="producer"><b>Custo: </b><span id="rep_PRODUCER_cost">0.00</span>

                                    <tr id="stock">
                                        <td id="retailer"><b>Estoque: </b><span id="rep_RETAILER_stock">16</span>
                                        <td id="wholesaler"><b>Estoque: </b><span id="rep_WHOLESALER_stock">16</span>
                                        <td id="distributor"><b>Estoque: </b><span id="rep_DISTRIBUTOR_stock">16</span>
                                        <td id="producer"><b>Estoque: </b><span id="rep_PRODUCER_stock">16</span>
                                </table>
                                </table>
                            </div>
                        </div>
                        <div class="graph-card mdl-card mdl-shadow--6dp mdl-card--horizontal">
                            <div class="mdl-card__media" id="graph-content">

                            </div>
                        </div>
                    </div>

                </div>
            </main>
        </div>

        <!--script>goto_report();</script-->
    </body>

</html>