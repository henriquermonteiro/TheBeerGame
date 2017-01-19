<!DOCTYPE html>
<html>
	<meta charset="utf-8"/>

	<script>
	function validateForm()
	{
		alert("oi!");

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
	}
	</script>

	<body>
	<h1>JOGO DA CERVEJA!</h1>
		<form onsubmit="validateForm()">
			Choose a nickname:<br>
			<input type="text" id="nickname" maxlength="20" pattern="[A-Za-z0-9]{1,20}" required>
			<br><br>
			<input type="submit" value="Enter">
		</form>
	</body>

</html>
