<%@ page isErrorPage="true"%>
<%@ page import="java.io.*"%>

<br/>
<h3 align="center">Error page</h3>
<br/>
<div align="center">

 <% 
     if (null == exception)
     {
         exception = (Throwable) request.getAttribute("exception");
     }
 
     String errorDump = "";
     if (null != exception)
     {
         try
         {
             ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
             exception.printStackTrace(new PrintStream(baos));
             errorDump = new String(baos.toByteArray());
         } catch (Exception ex) {
             ex.printStackTrace();
         }
     }

 %>
 
<div align="left">
 <pre width="100%" style="width: 100%;"><%= errorDump %></pre>
</div>
</div>
 

