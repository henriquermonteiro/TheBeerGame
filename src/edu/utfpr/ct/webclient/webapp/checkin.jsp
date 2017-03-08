<%
    System.out.println("Test - " + request.getMethod());
    if(request.getMethod().equals("POST")){
        System.out.println("Post");
        String nick = request.getParameter("nickname");
        
        if(nick != null && nick != ""){
            System.out.println("o/ " + nick);
            session.setAttribute("USER-ID", nick);
            System.out.println("\\o " + session.getAttribute("USER-ID"));
            response.sendRedirect("/select-room.jsp");
        }
    }
%>

<!DOCTYPE html>
<html>
    <meta charset="utf-8"/>

    <body>
        <h1>JOGO DA CERVEJA!</h1>
        <form method="POST" action="/checkin.jsp">
            Choose a nickname:<br>
            <input type="text" id="nickname" name="nickname" maxlength="20" pattern="[A-Za-z0-9]{1,20}" required>
            <br><br>
            <input type="submit" value="Enter">
        </form>
        <a href="select-room.jsp">Enter as guest</a>
    </body>

</html>
