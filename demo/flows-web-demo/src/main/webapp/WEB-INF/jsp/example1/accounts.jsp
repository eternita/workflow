<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="account">

	<table>
		<thead>
			<tr class="odd">
				<td class="column1"></td>
				<th scope="col" abbr="Home">First Name</th>
				<th scope="col" abbr="Premium">Last Name</th>
				<th scope="col" abbr="Premium">Remove</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${accountList}" var="account">
				<tr>
					<td class="column1"><img src="../resources/images/ch.png" alt="included"
						width="16" height="16"></td>
					<th scope="row">${account.firstName}</th>
					<th scope="row">${account.lastName}</th>
					<td><a
						href="UserAccounts-Remove?&uuid=${account.uuid}"><img
							src="../resources/images/delete.gif" alt="remove"></a></td>
				</tr>
			</c:forEach>
			</tbody>	
		<tfoot>
			<tr class="odd">
				<td class="column1">&nbsp;</td>
				<th scope="col">&nbsp;<br>
				</th>
				<th scope="col">&nbsp;<br>
				</th>
				<th scope="col"><a href="UserAccounts-New"><img
						src="../resources/images/add-icon.png" /></a><br></th>
			</tr>
		</tfoot>
	</table>
</div>

<hr>
<b>Source:</b>

<p><img alt="" src="../resources/images/example1.jpg"> 

