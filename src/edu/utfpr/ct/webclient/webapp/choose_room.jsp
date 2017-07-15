<%@page import="java.io.*" %>
<%@page import="java.net.*" %>
<%@page import="org.json.simple.*" %>
<%@page import="org.json.simple.parser.*" %>

<%
    //System.out.println("Test - " + request.getMethod());
    if(session.getAttribute("USER-ID") == null || ((String)session.getAttribute("USER-ID")).isEmpty()){
//        if(request.getParameter("guest") != null){
//            
//            Integer guestCount = 0;
//            
//            synchronized(this){
//                guestCount = (Integer)application.getAttribute("guest_counter");
//
//                if(guestCount == null){
//                    guestCount = 1;
//                }else{
//                    guestCount++;
//                }
//
//                application.setAttribute("guest_counter", guestCount);
//            }
//            
//            session.setAttribute("USER-ID", String.format("guest%04d", guestCount));
//            System.out.println(session.getAttribute("USER-ID"));
//        }else{
            response.sendRedirect("/check_in.jsp");
            return;
//        }
    }else{
        if(session.getAttribute("LOGGED_GAME") != null && !((String)session.getAttribute("LOGGED_GAME")).isEmpty()){
            response.sendRedirect("/game.jsp");
            return;
        }
    }
    
    if(request.getMethod().equals("POST")){
        String game = request.getParameter("game_name");
        String pw = request.getParameter("password");
        
        boolean flag_fail = false;
        if(game == null || game.isEmpty()){
            flag_fail = true;
        }
        
        if(pw == null){
            flag_fail = true;
        }
        
        if(flag_fail){
            response.sendRedirect("/choose_room.jsp?warning");
            return;
        }
        
        String recv;
        String recvbuff = "";
        URL checkin_json = new URL(request.getScheme(), request.getServerName(), request.getServerPort(), "/accessgame?game_name=" + URLEncoder.encode(game, "UTF-8").replace("+", "%20") + "&player_name=" + URLEncoder.encode(session.getAttribute("USER-ID").toString(), "UTF-8").replace("+", "%20") + "&password=" + URLEncoder.encode(pw, "UTF-8").replace("+", "%20"));
        URLConnection urlcon = checkin_json.openConnection();
        BufferedReader buffread = new BufferedReader(new InputStreamReader(urlcon.getInputStream()));

        while ((recv = buffread.readLine()) != null)
            recvbuff += recv;
        buffread.close();
        
        System.out.println(recvbuff);
        
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(recvbuff);
        
        if(((Boolean)json.get("accepted")) == false){
            response.sendRedirect("/choose_room.jsp?warning");
            return;
        }
        
        if(((Boolean)json.get("accepted")) == true){
            session.setAttribute("LOGGED_GAME", game);
            response.sendRedirect("/game.jsp");
        }
    }
    
    String recv;
    String recvbuff = "";
    URL checkin_json = new URL(request.getScheme(), request.getServerName(), request.getServerPort(), "/resorces?USER-ID="+session.getAttribute("USER-ID"));
    URLConnection urlcon = checkin_json.openConnection();
    BufferedReader buffread = new BufferedReader(new InputStreamReader(urlcon.getInputStream()));

    while ((recv = buffread.readLine()) != null)
        recvbuff += recv;
    buffread.close();
        
    JSONParser parser = new JSONParser();
    JSONObject json = (JSONObject) parser.parse(recvbuff);
    JSONArray list = (JSONArray)json.get("game_list");
%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
        <link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.amber-blue.min.css" />
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <script defer src="https://code.getmdl.io/1.3.0/material.min.js"></script>
        <meta charset="utf-8"/>
        <link rel="stylesheet" type="text/css" href="bub.css"/>
        <link rel="stylesheet" type="text/css" href="choose.css"/>
        <script src="bub.js"></script>
    </head>
    <body onload="bubbles();">
        <div class="mdl-layout mdl-js-layout">
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
                <dialog id="dialog" class="mdl-dialog">
                    <h4  id="dlg_title" class="mdl-dialog__title"></h4>
                    <div class="mdl-dialog__content">
                        <p  id="dlg_text">
                            The game requires a password:
                        </p>
                        <form method="post" action="">
                            <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                <input class="mdl-textfield__input" type="text" pattern="-?[A-Z,a-z,0-9]?" id="password">
                                <label class="mdl-textfield__label" for="password">Senha...</label>
                            </div>
                            <span id="warning_pw" class="warning hidden">Senha não confere</span>
                        </form>
                    </div>
                    <div class="mdl-dialog__actions">
                        <button id="call_button" type="button" class="mdl-button" onclick="enter_room()">Entrar</button>
                        <button type="button" class="mdl-button close" onclick="close_dialog()">Fechar</button>
                    </div>
                </dialog>


                <div id="bub_back" class="bubbles"></div>

                <div class="center-content">
                    <div hidden="true">
                        <form id="form_access" name="access" action="/choose_room.jsp" method="POST">
                            <input id="input_game" name="game_name" type="text">
                            <input id="input_pw" name="password" type="password">
                        </form>
                    </div>
                    <%
                        for(Object jsonObj : list){
                            if(jsonObj instanceof JSONObject){
                                
                    %>
                    <div class="mdl-card mdl-shadow--2dp mdl-card--horizontal">
                        <div class="mdl-card__media">
                            <i class="material-icons md-light md-48"><%=(((Boolean)((JSONObject)jsonObj).get("finished"))? "find_in_page" : "gamepad")%></i>
                        </div>
                        <div class="mdl-card__title">
                            <h2 class="mdl-card__title-text"><%=((JSONObject)jsonObj).get("name")%></h2>
                        </div>
                        <div class="mdl-card__actions mdl-card--border">
                            <a class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect" data-upgraded=",MaterialButton,MaterialRipple" <% if((Boolean)((JSONObject)jsonObj).get("use_pw")){ %>data-pw="required" <% } %> data-id="<%=((JSONObject)jsonObj).get("id")%>" data-game="<%=((JSONObject)jsonObj).get("name")%>" name="gmbutton">entrar</a>
                            <%
                                if((Boolean)((JSONObject)jsonObj).get("use_pw")){
                            %>
                            <i class="material-icons md-36 password-protected">lock</i>
                            <%
                                }
                            %>
                        </div>
                    </div>
                    <%
                            }
                        }
                    %>

                </div>

                <script>
                    var dialog = document.querySelector('dialog');
                    var showDialogButtons = document.getElementsByName('gmbutton');
                    var error_reload = 0;

                    //if (! dialog.showModal) {
                    //	dialogPolyfill.registerDialog(dialog);
                    //}

                    for (var i = 0; i < showDialogButtons.length; i++) {
                        showDialogButtons[i].addEventListener('click', function () {
                            if (this.dataset.pw === "required") {
                                callDialogBox(this.dataset.game);
                            } else {
                                enter_room(this.dataset.game);
                            }
                        });
                    }

                    //showDialogButton.addEventListener('click', function() {
                    //	dialog.showModal();
                    //});
                    //dialog.querySelector('.close').addEventListener('click', function() {
                    //	dialog.close();
                    //});

                    function callDialogBox(game_name) {
                        var el = document.getElementById("dialog");
                        var title = document.getElementById("dlg_title");
                        var warning = document.getElementById("warning_pw");
                        var button = document.getElementById("call_button");

                        title.innerHTML = game_name;

                        var patt = /\bhidden/;
                        button.setAttribute("onClick", "javascript: enter_room(\"" + game_name + "\");");

                        if (!patt.test(warning.className)) {
                            warning.className = warning.className.concat(' hidden');
                        }

                        el.showModal();
                    }

                    function close_dialog() {
                        var el = document.getElementById("dialog");

                        el.close();
                    }

                    function enter_room(game) {
                        var input_game = document.getElementById("input_game");
                        var input_pw = document.getElementById("input_pw");
                        
                        input_game.value = game;
                        input_pw.value = document.getElementById("password").value;
                        
                        document.access.submit();
                    }
                </script>

            </main>
        </div>
    </body>

</html>
