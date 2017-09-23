<%@page import="java.io.*" %>
<%@page import="java.net.*" %>

<% 
    String user = (String) session.getAttribute("USER-ID");
    
    String recv;
    String recvbuff = "";
    URL checkin_json = new URL(request.getScheme(), request.getServerName(), request.getServerPort(), "/logout?player-name="+URLEncoder.encode(user, "UTF-8").replace("+", "%20"));
    URLConnection urlcon = checkin_json.openConnection();
    BufferedReader buffread = new BufferedReader(new InputStreamReader(urlcon.getInputStream()));

    while ((recv = buffread.readLine()) != null)
        recvbuff += recv;
    buffread.close();
        
    session.invalidate(); 
    response.sendRedirect("/check_in.jsp");
%>
