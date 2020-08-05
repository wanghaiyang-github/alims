<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
%>
<%--
Created by IntelliJ IDEA.
User: Dell
Date: 2018/12/19
Time: 18:32
To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>北京市公安局法医鉴定案件委托系统</title>
    <%@ include file="../linkCss.jsp" %>
    <style>
        body{
            background: #fff;
        }
        body .box {
            padding: 26px 0;
            height: 100%;
            padding-top: 60px;
        }

        body .box > .col-md-12 {
            display: block;
            width: 645px;
            height: 95%;
            background: #f7f7f7;
            margin: 0 auto;
            float: none;
            text-align: center;
            padding-top: 30px;
        }
    </style>
</head>
<body>
<ol class="breadcrumb">
    <li><a href="#">首页</a></li>
    <li><a href="#">案件委托登记</a></li>
    <li class="active">DNA鉴定登记</li>
</ol>

<div class="row box">
    <div class="col-md-12">
        <div class="row">
            <div class="col-md-12" style="color:red; font-weight:bold;">
                <i class="fa fa-times-circle" aria-hidden="true"></i>
                <p>${errMsg}</p>
            </div>
            <div class="col-md-12">
                <form>
                    <div class="form-group">
                        <label><strong>请重新输入现勘编号</strong></label>
                        <input type="text" id="caseXkNo" name="caseXkNo" class="form-control">
                    </div>
                </form>
            </div>
            <div class="col-md-12">
                <a href="javaScript:;" target="ifm" class="btn btn-blue btn-lang jixuBTn" id="submitBtn">提 交</a>
            </div>
        </div>
    </div>
</div>
<%@ include file="../linkJs.jsp" %>
<script>
    $(function () {
        $("#caseXkNo").focus();

        //输入现堪号点击确认
        $("#submitBtn").on("click", function () {
            var caseXkNo = $("#caseXkNo").val();
            if (caseXkNo == "") {
                $("label").removeClass("hidden")
            }else{
                location.href = "<%=path%>/delegate/dnaReg.html?xkNo=" + caseXkNo;
            }

        });
        $("#caseXkNo").keydown(function (e) {
            if (e.keyCode == 13) {
                var caseXkNo = $("#caseXkNo").val();
                if (caseXkNo == "") {
                    $("label").removeClass("hidden")
                }else{
                    location.href = "<%=path%>/delegate/dnaReg.html?xkNo=" + caseXkNo;
                }
            }
        })
    })
</script>
</body>

</html>