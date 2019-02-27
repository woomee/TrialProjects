<html>
<head><title>login page</title></head>
<body>

<form method="post" action="<%= response.encodeURL("j_security_check")%>">
<table>
    <tr>
        <td>ID</td>
        <td> <input type="text" name="j_username"></td>
    </tr>
    <tr>
        <td>Pass</td>
        <td><input type="password" name="j_password"></td>
    </tr>
</table>
<br>
<input type="submit" value="Login" name="submit">
<input type="reset" value="Reset" name="reset">
</form>

</body>
</html>