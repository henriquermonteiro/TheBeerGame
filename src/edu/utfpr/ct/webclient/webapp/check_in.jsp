<%@page import="java.io.*" %>
<%@page import="java.net.*" %>
<%@page import="org.json.simple.*" %>
<%@page import="org.json.simple.parser.*" %>
<%@page import="edu.utfpr.ct.localization.*" %>

<%
    if(session.getAttribute("USER-ID") != null){
        response.sendRedirect("/choose_room.jsp");
    }
    
    if(request.getParameter("lang") != null){
        session.setAttribute("PREF-LANG", request.getParameter("lang"));
    }
    
    String lang = "default";
    if(session.getAttribute("PREF-LANG") != null){ 
        lang = (String) session.getAttribute("PREF-LANG");
    }
    
    Localize localize = ClientLocalizationManager.getInstance().getClientFor(lang);
    
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
    }
%>
<jsp:include page="resources/head_begin.jsp"/>
<link rel="stylesheet" type="text/css" href="login.css"/>
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
        <jsp:include page="resources/head_finish.jsp"/>
        <jsp:include page="resources/body_begin.jsp">
            <jsp:param name="source" value="check_in.jsp"/>
        </jsp:include>
                <div class="center-content">
                    <div class="login-card mdl-card mdl-shadow--2dp">
                        <div class="mdl-card__title mdl-card--expand">
                            <h2 class="mdl-card__title-text"><%=(localize.getTextFor(ClientLocalizationKeys.CHECKIN_TITLE))%></h2>
                        </div>
                        <div class="mdl-card__supporting-text">
                            <form name="login" action="/check_in.jsp" onsubmit="validateForm()" method="POST">
                                <span id="inv_name" class="warning<%=hidden %>"><%=(localize.getTextFor(ClientLocalizationKeys.CHECKIN_WARNING))%></span>
                                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                                    <input name="nickname" class="mdl-textfield__input" type="text" pattern="[A-Z,a-z,0-9]{6,36}" id="sample4">
                                    <label class="mdl-textfield__label" for="sample4"><%=(localize.getTextFor(ClientLocalizationKeys.CHECKIN_TEXT_LABEL))%></label>
                                    <span class="mdl-textfield__error"><%=(localize.getTextFor(ClientLocalizationKeys.CHECKIN_TEXT_ERROR))%></span>
                                </div>
                            </form>
                        </div>
                        <div class="mdl-card__actions">
                            <button onclick="submitForm()" class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect">
                                <%=(localize.getTextFor(ClientLocalizationKeys.CHECKIN_SUBMIT))%>
                            </button>
                        </div>
                    </div>
                </div>
        <jsp:include page="resources/body_finish.jsp" />
