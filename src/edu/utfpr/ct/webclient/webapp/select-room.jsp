<!DOCTYPE html>
<html>
	<meta charset="utf-8"/>
	
	<script>
	function checkIn()
	{
		alert("oi!");

		/*
		var xhttp = new XMLHttpRequest();
		xhttp.send(document.getElementById("rooms").value);
		
		var obj = JSON.parse(xhttp.responseText);
		if(obj.accept == "true")
		{
			xhttp.open("GET", "wating.html", false);
			xhttp.send();
		}
		*/
	}
	
	function populateForm()
	{
		/*
		var xhttp = new XMLHttpRequest();
		xhttp.send(document.getElementById("nickname").value);
		
		var obj = JSON.parse(xhttp.responseText);
		if(obj.accept == "true")
		{
			xhttp.open("GET", "wating.html", false);
			xhttp.send();
		}
		*/
		
		var langArray = ["Turma B3", "Turma C4", "Turma K7", "Turma X9"];
		document.getElementById('rooms').innerHTML = '';
		
		for(element of langArray)
		{
			var opt = document.createElement("option");
			opt.value = element;
			opt.innerHTML = element;
			document.getElementById("rooms").add(opt);
		}
	}
	</script>
	
	<body onload="populateForm()">
		<h1>JOGO DA CERVEJA!</h1>
		<br><br>
		<form>
			<select id="rooms">
			</select>
			<br><br>
			<input type="submit" value="Enter" onclick="checkIn()">
		</form>
	</body>

</html>

