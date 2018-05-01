<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/4/30 0030
  Time: 下午 12:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录页面</title>
</head>
<body>
    <form action="/Page/login.do" method="post">
        用户名：<input type="text" name="username" id="username"/> <br>
        密码: <input type="password" name="password" id="password"/></br>
        <input type="checkbox" name="remember">保存</br>
        <input type="submit" value="登录">
  </form>

</body>
</html>
