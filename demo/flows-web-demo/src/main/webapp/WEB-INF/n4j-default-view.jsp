<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="org.neuro4j.core.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.neuro4j.logic.*" %>
<%@ page import="org.neuro4j.logic.swf.SWFConstants" %>


<%
	LogicContext logicContext = (LogicContext) request.getAttribute("LOGIC_CONTEXT");
%>
<table style="margin-bottom: 3px;" width="100%"  border="0" cellspacing="0" cellpadding="0" class="l r t b">
    <tr>
        <td colspan="2" align="center" valign="top" class="hp b">
          <b>Execution results</b>
        </td>
    </tr>

    <%-- Parameters --%>  
    <tr>
        <td colspan="2" align="center" valign="top" class="hp b">
          <b>Parameters</b>
        </td>
    </tr>
     <%
     	for (String param : logicContext.keySet())
          {
     %>
	    <tr>
            <td align="left" width="25%" valign="top" class="b r">
            <%=param%>
            </td>
            <td align="left" valign="top" class="b">
            <%=logicContext.get(param)%>
            </td>
	    </tr>
     <%
     	}
     %>
  
    <%-- Stack trace --%>  
    <tr>
        <td colspan="2" align="center" valign="top" class="hp b">
          <b>Execution stack trace (first was executed first)</b>
        </td>
    </tr>
    
    <%
        	Set<Entity> stack = (Set<Entity>) logicContext.get("ACTION_STACK");
            for (Entity actionEntity : stack)
            {
        %>
    <tr>
        <td align="left" colspan="2" valign="top" class="b">
        <a href="entity-details?storage=${storage}&eid=<%=actionEntity.getUuid()%>"><b><%=actionEntity.getName()%></b></a> / <%=actionEntity.getProperty(SWFConstants.SWF_BLOCK_CLASS)%>
        </td>
    </tr>
    <%
    }
    %>
    
    <%-- empty space --%>
    <tr>
        <td colspan="2" align="left" valign="top" class="">
        &nbsp;
        </td>
    </tr>
    
</table>
    
