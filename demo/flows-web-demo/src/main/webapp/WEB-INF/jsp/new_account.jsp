<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="account">
<form action="ViewFlow-Action"
	method="POST">
	
	<table >
		<tr>
			<td>First Name:</td>
			<td><input type="text" size="28" name="firstName"></td>
		</tr>
		<tr>
			<td>Last Name:</td>
			<td align="left"><input type="text" size="28" name="lastName"></td>
		</tr>
		<tr>
			<td colspan="2" align="right" style="text-align: right;"><input 
				type="submit" name="Submit" class="mb"></td>
		</tr>
	</table>
	<input type="hidden" name="actionName" value="create">
</form>
</div>