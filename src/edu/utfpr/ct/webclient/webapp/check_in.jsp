<%@page import="java.io.*" %>
<%@page import="java.net.*" %>
<%@page import="org.json.simple.*" %>
<%@page import="org.json.simple.parser.*" %>
<%
    //System.out.println("Test - " + request.getMethod());
    if(session.getAttribute("USER-ID") != null){
        response.sendRedirect("/choose_room.jsp");
    }
    
    String hidden = (request.getParameter("warning") != null ? "" : " hidden");
    
    if(request.getMethod().equals("POST")){
        String nick = request.getParameter("nickname");
        
        String recv;
        String recvbuff = "";
        URL checkin_json = new URL(request.getScheme(), request.getServerName(), request.getServerPort(), "/checkin?player-name="+nick);
        URLConnection urlcon = checkin_json.openConnection();
        BufferedReader buffread = new BufferedReader(new InputStreamReader(urlcon.getInputStream()));

        while ((recv = buffread.readLine()) != null)
            recvbuff += recv;
        buffread.close();
        
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(recvbuff);
        
        if(json.get("accepted").equals(false)){
            response.sendRedirect("/check_in.jsp?warning");
        }
        
        if(json.get("accepted").equals(true)){
            session.setAttribute("USER-ID", nick);
            response.sendRedirect("/choose_room.jsp");
        }
        
        //if(nick != null && nick != ""){
            //System.out.println("o/ " + nick);
            //session.setAttribute("USER-ID", nick);
            //System.out.println("\\o " + session.getAttribute("USER-ID"));
            //response.sendRedirect("/select-room.jsp");
        //}
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
        <link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.amber-blue.min.css" />
        <script defer src="https://code.getmdl.io/1.3.0/material.min.js"></script>
        <meta charset="utf-8"/>
        <link rel="stylesheet" type="text/css" href="bub.css"/>
        <link rel="stylesheet" type="text/css" href="login.css"/>
        <script src="bub.js"></script>
        <script type="text/javascript">
            function validateForm(){
                var input = document.getElementById("sample4");
                if(input.value !== null && input.value !== ""){
                    var patt = /^[A-Z,a-z,0-9]{6,36}$/;
                    var test = patt.test(input.value);
                    if (!test) {
                        return false;
                    }
                }
            }
            
            function submitForm(){
                var res = validateForm();
                if(res !== false){
                    document.login.submit();
                }else{
                    location.reload(true);
                }
            }
        </script>
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
                <div id="bub_back" class="bubbles"></div>

                <div class="center-content">
                    <div class="login-card mdl-card mdl-shadow--2dp">
                        <div class="mdl-card__title mdl-card--expand">
                            <h2 class="mdl-card__title-text">JOGO DA CERVEJA!</h2>
                        </div>
                        <div class="mdl-card__supporting-text">
                            <form name="login" action="/check_in.jsp" onsubmit="validateForm()" method="POST">
                                <span id="inv_name" class="warning<%=hidden %>">O nome já está sendo usado.</span>
                                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                    <input name="nickname" class="mdl-textfield__input" type="text" pattern="-?[A-Z,a-z,0-9]{6,36}?" id="sample4">
                                    <label class="mdl-textfield__label" for="sample4">Apelido...</label>
                                    <span class="mdl-textfield__error">Não é um nome válido!</span>
                                </div>
                            </form>
                        </div>
                        <!--div class="mdl-card__supporting-text">
                            <a href="/choose_room.jsp?guest">Entrar como convidado</a>
                        </div-->
                        <div class="mdl-card__actions">
                            <button onclick="submitForm()" class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect">
                                Entrar
                            </button>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </body>

</html>
