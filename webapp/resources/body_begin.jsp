<%@page import="edu.utfpr.ct.localization.ClientLocalizationManager" %>
<%@page import="edu.utfpr.ct.localization.ClientLocalizationKeys" %>
<%@page import="edu.utfpr.ct.localization.Localize" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String lang = "default";
    if(session.getAttribute("PREF-LANG") != null){ 
        lang = (String) session.getAttribute("PREF-LANG");
    }
    
    Localize localize = ClientLocalizationManager.getInstance().getClientFor(lang);
    
    boolean has_return = Boolean.parseBoolean(request.getParameter("show_return"));
%>
    <% if(!"true".equals(request.getParameter("hide_body"))){ %><body onload="bubbles();"><% }%>
        <div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
            <header class="mdl-layout__header">
                <div id="lang-menu" class="mdl-layout-icon"></div>
                <div class="mdl-layout__header-row">
                    <a id="curr_lang" href="#" style="text-decoration: none; color: black;" class="mdl-layout__title"><img id="flag" src="resources/flags/<%=(localize.getTextFor(ClientLocalizationKeys.FLAG_PATH))%>" >&nbsp;&nbsp;<%=(localize.getTextFor(ClientLocalizationKeys.LANG_ID)) %></a>
                    <div class="mdl-tooltip" for="curr_lang"><%=(localize.getTextFor(ClientLocalizationKeys.COMMON_CURRENT_LANGUAGE_TOOLTIP)) %></div>
                    <% if(session.getAttribute("USER-ID") != null && !((String)session.getAttribute("USER-ID")).isEmpty()){ 
                        String name = (String) session.getAttribute("USER-ID");
                    %>
                    <div class="mdl-layout-spacer"></div>
                    <nav class="mdl-navigation">
                        <%if(has_return){%>
                        <a id="return" class="mdl-navigation__link" href="choose_room.jsp?returned=true"><i class="material-icons md-24">power_settings_new</i><%=(localize.getTextFor(ClientLocalizationKeys.COMMON_RETURN_TEXT)) %></a>
                        <div class="mdl-tooltip" for="return"><%=(localize.getTextFor(ClientLocalizationKeys.COMMON_RETURN_TOOLTIP)) %></div>
                        <%}%>
                        <a id="logout" class="mdl-navigation__link" href="logout.jsp"><i class="material-icons md-24">power_settings_new</i>&nbsp;&nbsp;&nbsp;<%=name %></a>
                        <div class="mdl-tooltip" for="logout"><%=(localize.getTextFor(ClientLocalizationKeys.COMMON_LOGOUT_TOOLTIP)) %></div>
                    </nav>
                    <% } %>
                </div>
            </header>
            <div id="drawer" class="mdl-layout__drawer">
                <span class="mdl-layout__title"><img id="flag" src="resources/flags/<%=(localize.getTextFor(ClientLocalizationKeys.FLAG_PATH))%>" ><%=(localize.getTextFor(ClientLocalizationKeys.LANG_ID)) %></span>
                <nav class="mdl-navigation">
                    <%
                        for(String l : ClientLocalizationManager.getValidLanguages()){
                    %>
                    <a class="mdl-navigation__link" href="<%=(request.getParameter("source") == null ? "#" : request.getParameter("source")) %>?lang=<%=l %>"><img id="flag" src="resources/flags/<%=(ClientLocalizationManager.getInstance().getClientFor(l).getTextFor(ClientLocalizationKeys.FLAG_PATH))%>" ><%=ClientLocalizationManager.getInstance().getClientFor(l).getTextFor(ClientLocalizationKeys.LANG_ID) %></a>
                    <% } %>
                </nav>
            </div>
            <script>
                document.getElementById("curr_lang").onclick = function() {
                        var drawer = document.getElementById("drawer");
                        drawer.className = "mdl-layout__drawer is-visible";
                        drawer.setAttribute("aria-hidden", "false");
                        
                        var obfuscator = document.getElementsByClassName("mdl-layout__obfuscator");
                        obfuscator[0].className = "mdl-layout__obfuscator is-visible";
                    };
            </script>
            <main class="mdl-layout__content">
                <div id="bub_back" class="bubbles"></div>
