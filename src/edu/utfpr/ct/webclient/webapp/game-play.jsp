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

	th
	{
		font-weight: bold
	}

	img
	{
		height: auto;
		width: auto;
		max-width: 25%;
		max-height: 25%;
	}

	iframe
	{
		height: auto;
		width: auto;
		max-width: 100%;
		max-height: 100%;
	}
	</style>

	<body>
		<h1>BEER GAME!</h1>

		<h3>Week 1/50</h3>

		<table>
			<tr id="function">
				<th>
				<th>Retailer
				<th>Wholesaler
				<th>Distributor
				<th>Producer

			<tr id="image">
				<td>
				<td><img src="resources\retailer.png" alt="Retailer">
				<td><img src="resources\wholesaler.png" alt="Wholesaler">
				<td><img src="resources\distributor.png" alt="Distributor">
				<td><img src="resources\Industry.png" alt="Producer">

			<tr id="name">
				<th id="description">Name
				<td id="retailer">Name 1
				<td id="wholesaler">Name 2
				<td id="distributor">Name 3
				<td id="producer">Name 4
				
			<tr id="profit">
				<th id="description">Profit
				<td id="retailer">0.00
				<td id="wholesaler">0.00
				<td id="distributor">0.00
				<td id="producer">0.00

			<tr id="stock">
				<th id="description">Stock
				<td id="retailer">16
				<td id="wholesaler">16
				<td id="distributor">16
				<td id="producer">16

			<tr id="incoming-order">
				<th id="description">Incoming Order

				<td id="retailer">
					<table>
						<tr> <td>↓ <td>4
					</table>

				<td id="wholesaler">
					<table>
						<tr> <td>↑ <td>4
						<tr> <td>↑ <td>4
					</table>

				<td id="distributor">
					<table>
						<tr> <td>↑ <td>4
						<tr> <td>↑ <td>4
					</table>

				<td id="producer">
					<table>
						<tr> <td>↑ <td>4
						<tr> <td>↑ <td>4
					</table>

			<tr id="demand">
				<th id="description">Demand

				<td id="retailer">
					<form>
						<input type="number" value=0 min=0 max=100>
						<br><br>
						<input type="submit" value="Enter">
					</form>

				<td id="wholesaler">
					<form>
						<input type="number" value=0 min=0 max=100>
						<br><br>
						<input type="submit" value="Enter">
					</form>

				<td id="distributor">
					<form>
						<input type="number" value=0 min=0 max=100>
						<br><br>
						<input type="submit" value="Enter">
					</form>

				<td id="producer">
					<form>
						<input type="number" value=0 min=0 max=100>
						<br><br>
						<input type="submit" value="Enter">
					</form>
		</table>

		<h1>History</h1>

		<table>
			<tr>
				<th>Function
				<th>Week
				<th>Stock
				<th>Profit
				<th colspan="2">Incoming Order
				<th>Demand
			<tr>
				<td>Wholesaler
				<td>1
				<td>16
				<td>0.00
				<td>4
				<td>10
				<td>8
		</table>

		<br><br><br><br>

		<iframe src="frame.html"></iframe>

		<br><br><br><br>

		<table>
			<tr>
				<td>Caminh&atilde;o</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>Caminh&atilde;o</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>N&oacute;</td>
				<td>Caminh&atilde;o</td>
				<td>Caminh&atilde;o</td>
				<td>N&oacute;</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>Caminh&atilde;o</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>Caminh&atilde;o</td>
			</tr>
			<tr>
				<td>N&oacute;</td>
				<td>Caminh&atilde;o</td>
				<td>Caminh&atilde;o</td>
				<td>N&oacute;</td>
			</tr>
			<tr>
				<td>Cliente</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
		</table>

		<br><br><br><br>

		<table>
			<tr>
				<td>
					<table>
						<tr>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>
					</table>
				</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>
					<table>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
					</table>
				</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			<td>
				<table>
					<tr>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		<td>
			<table>
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
			</table>
		</td>
			<td>&nbsp;</td>
		</tr>
		</table>

		<form>
			Client: <br>
			<input type="text" name="client" disabled> <br>
			<br><br>
			Retailer: <br>
			<input type="number" min=1 max=100  name="retailer"> <input type="text" name="stock retailer" disabled> <input type="text" name="profit retailer" disabled>
			<br><br>
			<input type="text" name="travellingtime 01" disabled> <br>
			<input type="text" name="travellingtime 02" disabled> <br>
			<br><br>
			Wholesaler: <br>
			<input type="number" min=1 max=100 name="wholesaler"> <input type="text" name="stock wholesaler" disabled> <input type="text" name="profit retailer" disabled>
			<br><br>
			<input type="text" name="travellingtime 03" disabled disabled> <br>
			<input type="text" name="travellingtime 04" disabled disabled> <br>
			<br><br>
			Distributor<br>
			<input type="number" min=1 max=100 name="distributor"> <input type="text" name="stock distributor" disabled> <input type="text" name="profit retailer" disabled>
			<br><br>
			<input type="text" name="travellingtime 05" disabled> <br>
			<input type="text" name="travellingtime 06" disabled> <br>
			<br><br>
			Producer<br>
			<input type="number" min=1 max=100 name="producer"> <input type="text" name="stock producer" disabled> <input type="text" name="profit retailer" disabled>
			<br><br>
			<input type="text" name="travellingtime 07" disabled> <br>
			<input type="text" name="travellingtime 08" disabled> <br>
			<br><br>
			<input type="submit" value="Enter">
		</form>
	</body>
</html>
