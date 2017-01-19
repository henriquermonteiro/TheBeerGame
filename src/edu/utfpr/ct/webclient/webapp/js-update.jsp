<!DOCTYPE html>
<html>
	<meta charset="utf-8"/>
	<style>
	table
	{
		font-family: arial, sans-serif;
		border-collapse: collapse;
		width: 100%;
	}

	td, th
	{
		border: 1px solid #dddddd;
		text-align: center;
		padding: 8px;
	}

	img
	{
		max-width: 100%;
		height: auto;
	}
	</style>
	
	<script>
	function loadDoc()
	{
		var text = '{ "names" : [' +
		'{ "name":"1" },'  +
		'{ "name":"2" },'  +
		'{ "name":"3" },'  +
		'{ "name":"4" }],' +
		'"start":"true"}';

		if(evaluate(text) == "true")
			alert("oi!");
		else
			alert("tchau!");

		/*
		var xhttp = new XMLHttpRequest();
		
		if(evaluate(xhttp.responseText) == "true")
		{
			xhttp.open("GET", "player.html", false);
			xhttp.send();
		}
		*/
	}
	
	function evaluate(text)
	{
		var obj = JSON.parse(text);
		
		document.getElementById("retailerName").innerHTML = obj.names[0].name;
		document.getElementById("wholesalerName").innerHTML = obj.names[1].name;
		document.getElementById("distributorName").innerHTML = obj.names[2].name;
		document.getElementById("producerName").innerHTML = obj.names[3].name;
		
		return obj.start;
	}
	</script>
        
        <script>
            function getUpdate(){
                var jsonHttp = new XMLHttpRequest();
                jsonHttp.open( "GET", "rand", false);
                jsonHttp.send( null );
                
                var json = JSON.parse(jsonHttp.responseText);

                document.getElementById("p1-name").innerHTML = json.playerlist[0].name;
                document.getElementById("p1-est").innerHTML = json.playerlist[0].stock;

                document.getElementById("p2-name").innerHTML = json.playerlist[1].name;
                document.getElementById("p2-est").innerHTML = json.playerlist[1].stock;

                document.getElementById("p3-name").innerHTML = json.playerlist[2].name;
                document.getElementById("p3-est").innerHTML = json.playerlist[2].stock;

                document.getElementById("p4-name").innerHTML = json.playerlist[3].name;
                document.getElementById("p4-est").innerHTML = json.playerlist[3].stock;
            }
        </script>

	<body onload="setInterval(getUpdate, 500)">
		<table>
			<tr>
				<th>Player</th>
				<th>Move</th>
			</tr>
			<tr id="p1">
				<td id="p1-name"></td>
				<td id="p1-est"></td>
			</tr>
			<tr id="p2">
				<td id="p2-name"></td>
				<td id="p2-est"></td>
			</tr>
			<tr id="p3">
				<td id="p3-name"></td>
				<td id="p3-est"></td>
			</tr>
			<tr id="p4">
				<td id="p4-name"></td>
				<td id="p4-est"></td>
			</tr>
		</table>
	</body>

</html>