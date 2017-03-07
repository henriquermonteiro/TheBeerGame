<%
//    if(request.)
%>

<!DOCTYPE html>
<html>
    <meta charset="utf-8"/>

    <body>
        <h1>JOGO DA CERVEJA!</h1>
        <form onsubmit="validateForm()">
            Choose a nickname:<br>
            <input type="text" id="nickname" maxlength="20" pattern="[A-Za-z0-9]{1,20}" required>
            <br><br>
            <input type="submit" value="Enter">
        </form>
        <a href="select-room.jsp">Enter as guest</a>
    </body>

</html>
