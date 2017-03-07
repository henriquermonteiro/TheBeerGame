<%-- 
    Document   : top-bar
    Created on : 07/03/2017, 19:34:22
    Author     : henrique
--%>

<div>
    <% 
        if(session.getAttribute("USER-ID") != null){
    %>
    <a><%= session.getAttribute("USER-ID") %></a>
    <%
        }
    %>
    <a>Menu Item</a>
</div>