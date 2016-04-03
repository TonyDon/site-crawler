<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"  trimDirectiveWhitespaces="true" session="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" name="viewport" id="viewport" />
<title>exception</title>
<style>
body{font-size:12px; font-family: Verdana, Arial;color:#000; background:#EAEAEA;padding:0;margin:0;overflow-x:hidden;}
#tip{color:red;font-size:13px;display:block;width:100%;padding:5px;background:#DFE7F6;border-bottom:1px solid #C6D4EF}
#tip a{color:#213B66;}
#exception{border:1px solid #F3E16F;width:90%;margin:9px auto;display:none;background:#FEFEF4;padding:9px;line-height:24px;word-wrap:break-word;word-break;break-all;}
</style>
<script>
function show(){
	document.getElementById('exception').style.display='block';
}
</script>
</head>
<body>
<b id="tip"> 系统异常!  <a href="javascript:show();"> 查看信息  </a> </b>
<div style="align:center;width:100%;">
	<div id="exception">
		<c:out value="${exception}"></c:out>
	</div>
</div>
</body>
</html>