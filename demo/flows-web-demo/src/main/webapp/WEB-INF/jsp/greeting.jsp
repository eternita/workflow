<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

Greeting page
<br/>
<br/>${hello_message}
<br/>
<br/>
<form action="org.neuro4j.example.web.logic.Greeting-Start">
Your name: <input type="text" name="name">
<br/><input type="submit" name="submit" value="Submit" >
</form>