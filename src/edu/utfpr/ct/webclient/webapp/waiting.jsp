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

	<body onload="setInterval(loadDoc, 5000)">
		<table>
			<tr>
				<th><img src="resources\retailer.png" alt="Retailer"></th>
				<th><img src="resources\wholesaler.png" alt="Wholesaler"></th>
				<th><img src="resources\distributor.png" alt="Distributor"></th>
				<th><img src="resources\Industry.png" alt="Industry"></th>
			</tr>
			<tr id="demo">
				<td id="retailerName"></td>
				<td id="wholesalerName"></td>
				<td id="distributorName"></td>
				<td id="producerName"></td>
			</tr>
		</table>

		<br><br>
		<p>Waiting start of the game</p>

		<br><br>
		<p>Animação de bolinhas andando pra lá e pra cá!</p>
	</body>

</html>
