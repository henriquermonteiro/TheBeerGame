<%@page import="java.io.*" %>
<%@page import="java.net.*" %>
<%@page import="org.json.simple.*" %>
<%@page import="org.json.simple.parser.*" %>
<%@page import="edu.utfpr.ct.localization.*" %>

<%
    if(session.getAttribute("USER-ID") == null || ((String)session.getAttribute("USER-ID")).isEmpty()){
        response.sendRedirect("/check_in.jsp");
        return;
    }else{
        boolean returned = Boolean.parseBoolean(request.getParameter("returned"));
        
        if(returned){
            session.removeAttribute("LOGGED_GAME");
        }
        
        if(session.getAttribute("LOGGED_GAME") != null && !((String)session.getAttribute("LOGGED_GAME")).isEmpty()){
            response.sendRedirect("/game.jsp");
            return;
        }
    }
    
    if(request.getParameter("lang") != null){
        session.setAttribute("PREF-LANG", request.getParameter("lang"));
    }
    
    String lang = "default";
    if(session.getAttribute("PREF-LANG") != null){ 
        lang = (String) session.getAttribute("PREF-LANG");
    }
    
    Localize localize = ClientLocalizationManager.getInstance().getClientFor(lang);
    
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
<jsp:include page="resources/head_begin.jsp"/>
    <script src="resources/dialog-polyfill.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/dialog-polyfill.css"/>
    <link rel="stylesheet" type="text/css" href="choose.css"/>
    <jsp:include page="resources/head_finish.jsp"/>
    <jsp:include page="resources/body_begin.jsp" >
            <jsp:param name="source" value="choose_room.jsp"/>
        </jsp:include>

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
                    <a id="join-<%=((JSONObject)jsonObj).get("name")%>" class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect" data-upgraded=",MaterialButton,MaterialRipple" <% if((Boolean)((JSONObject)jsonObj).get("use_pw")){ %>data-pw="required" <% } %> data-id="<%=((JSONObject)jsonObj).get("id")%>" data-game="<%=((JSONObject)jsonObj).get("name")%>" name="gmbutton"><%=(localize.getTextFor(ClientLocalizationKeys.CHOOSE_SUBMIT)) %></a>
                    <%
                        if((Boolean)((JSONObject)jsonObj).get("use_pw")){
                    %>
                    <i class="material-icons md-36 password-protected">lock</i>
                    <%
                        }
                    %>
                </div>
            </div>
            <div class="mdl-tooltip" for="join-<%=((JSONObject)jsonObj).get("name")%>"><%=(((Boolean)((JSONObject)jsonObj).get("finished"))? (localize.getTextFor(ClientLocalizationKeys.CHOOSE_REPO_TOOLTIP)) : (localize.getTextFor(ClientLocalizationKeys.CHOOSE_GAME_TOOLTIP))) %></div>
            <%
                    }
                }
            %>

        </div>
        </main>
        </div>
        <dialog id="dialog" class="mdl-dialog">
            <h4  id="dlg_title" class="mdl-dialog__title"></h4>
            <div class="mdl-dialog__content">
                <p  id="dlg_text">
                    <%=(localize.getTextFor(ClientLocalizationKeys.CHOOSE_PW_TEXT)) %>
                </p>
                <form method="post" action="">
                    <div id="passw_input" class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                        <input class="mdl-textfield__input" type="text" pattern="[A-Z,a-z,0-9]" id="password">
                        <label class="mdl-textfield__label" for="password"><%=(localize.getTextFor(ClientLocalizationKeys.CHOOSE_PW_LABEL)) %></label>
                    </div>
                    <div class="mdl-tooltip" for="passw_input"><%=(localize.getTextFor(ClientLocalizationKeys.CHOOSE_PASSW_INP_TOOLTIP)) %></div>
                    <span id="warning_pw" class="warning hidden"><%=(localize.getTextFor(ClientLocalizationKeys.CHOOSE_PW_WARNING)) %></span>
                </form>
            </div>
            <div class="mdl-dialog__actions">
                <button id="call_button" type="button" class="mdl-button" onclick="enter_room()"><%=(localize.getTextFor(ClientLocalizationKeys.CHOOSE_PW_ENTER)) %></button>
                <button id="cancel_button" type="button" class="mdl-button close" onclick="close_dialog()"><%=(localize.getTextFor(ClientLocalizationKeys.CHOOSE_PW_CLOSE)) %></button>
            </div>
            <div class="mdl-tooltip" for="call_button"><%=(localize.getTextFor(ClientLocalizationKeys.CHOOSE_PASSW_CALL_TOOLTIP)) %></div>
            <div class="mdl-tooltip" for="cancel_button"><%=(localize.getTextFor(ClientLocalizationKeys.CHOOSE_PASSW_CANCEL_TOOLTIP)) %></div>
        </dialog>

        <script>
            var dialog = document.querySelector('dialog');
            dialogPolyfill.registerDialog(dialog);
            var showDialogButtons = document.getElementsByName('gmbutton');
            var error_reload = 0;

            for (var i = 0; i < showDialogButtons.length; i++) {
                showDialogButtons[i].addEventListener('click', function () {
                    if (this.dataset.pw === "required") {
                        callDialogBox(this.dataset.game);
                    } else {
                        enter_room(this.dataset.game);
                    }
                });
            }

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
            
            function reload(){
                location.reload();
            }
            
            setInterval(reload, 45*1000);
        </script>
    </body>
</html>