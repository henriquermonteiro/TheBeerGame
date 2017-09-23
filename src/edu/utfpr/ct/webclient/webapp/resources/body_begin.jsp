<%@page import="edu.utfpr.ct.localization.ClientLocalizationManager" %>
<%@page import="edu.utfpr.ct.localization.ClientLocalizationKeys" %>
<%@page import="edu.utfpr.ct.localization.LocalizeClient" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String lang = "default";
    if(session.getAttribute("PREF-LANG") != null){ 
        lang = (String) session.getAttribute("PREF-LANG");
    }
    
    LocalizeClient localize = ClientLocalizationManager.getInstance().getClientFor(lang);
%>
    <% if(!"true".equals(request.getParameter("hide_body"))){ %><body onload="bubbles();"><% }%>
        <div class="mdl-layout mdl-js-layout">
            <header class="mdl-layout__header">
                <div class="mdl-layout-icon"> </div>
                <div class="mdl-layout__header-row">
                    <span class="mdl-layout__title"><img id="flag" src="resources/flags/<%=(localize.getTextForKey(ClientLocalizationKeys.FLAG_PATH))%>" ><%=(localize.getTextForKey(ClientLocalizationKeys.LANG_ID)) %></span>
                    <% if(session.getAttribute("USER-ID") != null && !((String)session.getAttribute("USER-ID")).isEmpty()){ 
                        String name = (String) session.getAttribute("USER-ID");
                    %>
                    <div class="mdl-layout-spacer"></div>
                    <nav class="mdl-navigation">
                        <a id="logout" class="mdl-navigation__link" href="logout.jsp"><i class="material-icons md-24">power_settings_new</i>&nbsp;&nbsp;&nbsp;<%=name %></a>
                        <div class="mdl-tooltip" for="logout"><%=(localize.getTextForKey(ClientLocalizationKeys.COMMON_LOGOUT_TOOLTIP)) %></div>
                    </nav>
                    <% } %>
                </div>
            </header>
            <div class="mdl-layout__drawer">
                <span class="mdl-layout__title"><img id="flag" src="resources/flags/<%=(localize.getTextForKey(ClientLocalizationKeys.FLAG_PATH))%>" ><%=(localize.getTextForKey(ClientLocalizationKeys.LANG_ID)) %></span>
                <nav class="mdl-navigation">
                    <%
                        for(String l : ClientLocalizationManager.getValidLanguagesNames()){
                    %>
                    <a class="mdl-navigation__link" href="#"><%=l %></a>
                    <% } %>
                </nav>
            </div>
            <main class="mdl-layout__content">
                <div id="bub_back" class="bubbles"></div>
