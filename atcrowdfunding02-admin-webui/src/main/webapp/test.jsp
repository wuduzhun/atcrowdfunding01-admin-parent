<%--
  Created by IntelliJ IDEA.
  User: OY
  Date: 2020/4/30
  Time: 16:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="/WEB-INF/include-head.jsp"%>
<script type="text/javascript">

    $(function () {
        $("#asyncBtn").click(function () {
            console.log("Ajax函数之前");
            $.ajax({
                "url":"test/ajax/async.html",
                "type":"post",
                "dataType":"text",
                "async":false,
                "success":function (reponse) {
                    // success接收到服务器端响应后执行
                    console.log("Ajax函数内部success函数 = "+reponse);
                }
            });
            // 在$.ajax()执行完成后执行，不等待success()函数
            console.log("Ajax函数之后");
        });
    });
</script>
<body>

<%@include file="/WEB-INF/include-nav.jsp"%>
<div class="container-fluid">
    <div class="row">
        <%@include file="/WEB-INF/include-sidebar.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

        <button id="asyncBtn">发送Ajax请求</button>

        </div>
    </div>
</div>

</body>
</html>

