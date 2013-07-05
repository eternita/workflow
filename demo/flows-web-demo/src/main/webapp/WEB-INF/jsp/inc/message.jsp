<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<br><br>
<div class="container_12" >
<c:if test="${null != errorMessage}">
     <div class="grid_4">&nbsp;</div><div class="grid_3 message_red"><c:out value="${errorMessage}"></c:out></div>
</c:if>
<c:if test="${null != infoMessage}">
    <div class="grid_4">&nbsp;</div>  <div class="grid_3 message_green"><c:out value="${infoMessage}"></c:out></div>
</c:if>
</div>