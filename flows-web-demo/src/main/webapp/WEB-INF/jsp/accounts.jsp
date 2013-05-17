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
		<tfoot>
		<tbody>
			<c:forEach items="${accountList}" var="account">
				<tr>
					<td class="column1"><img src="../../images/ch.png" alt="included"
						width="16" height="16"></td>
					<th scope="row">${account.firstName}</th>
					<th scope="row">${account.lastName}</th>
					<td><a
						href="ViewFlow-Action?actionName=remove&uuid=${account.uuid}"><img
							src="../../images/delete.gif" alt="remove"></a></td>
				</tr>
			</c:forEach>
		<tfoot>
			<tr class="odd">
				<td class="column1">&nbsp;</td>
				<th scope="col">&nbsp;<br>
				</th>
				<th scope="col">&nbsp;<br>
				</th>
				<th scope="col"><a href="ViewFlow-Action?actionName=new"><img
						src="../../images/add-icon.png" /></a><br></th>
			</tr>
		</tfoot>
	</table>
</div>
