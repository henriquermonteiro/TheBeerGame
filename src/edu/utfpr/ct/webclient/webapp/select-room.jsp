<!DOCTYPE html>
<html>
    <meta charset="utf-8"/>

    <script>
        function enter_room(){
            
        }
        
        function getUpdate() {
            populateForm();
        }

        function populateForm()
        {
            var jsonHttp = new XMLHttpRequest();
            jsonHttp.open("GET", "/resorces", false);
            jsonHttp.send(null);

            var json = JSON.parse(jsonHttp.responseText);

            document.getElementById('rooms').innerHTML = '';

            for (element of json.game_list)
            {
                var opt = document.createElement("option");
                opt.value = element.id;
                opt.innerHTML = element.name;
                opt.setAttribute("password", element.use_pw);
                document.getElementById("rooms").add(opt);
            }
        }
    </script>

    <body onload="setInterval(1000, getUpdate())">
        <h1>JOGO DA CERVEJA!</h1>
        <br><br>
        <form>
            <select id="rooms">
            </select>
            <br><br>
            <input type="submit" value="Enter" onclick="enter_room()">
        </form>
    </body>

</html>

