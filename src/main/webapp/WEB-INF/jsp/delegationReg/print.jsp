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

        .row .col-md-12:nth-child(1) i {
            font-size: 92px;
            color: #4aca85;
        }

        .row .col-md-12:nth-child(1) p {
            margin: 0px;
        }

        .row .col-md-12:nth-child(1) p:nth-child(2) {
            font-size: 21px;
            color: #4aca85;
            margin: 17px 0;
        }

        .row .col-md-12:nth-child(1) p:nth-child(3) {
            color: #000;
            font-size: 18px;
        }

        .row .col-md-12:nth-child(1) p:nth-child(3) a {
            color: #ff5951;
        }

        .row .col-md-12:nth-child(2) {
            margin-top: 25px;
            margin-bottom: 25px;
        }

        .row .col-md-12:nth-child(2) a {
            position: relative;
        }

        .row .col-md-12:nth-child(2) a span {
            color: #7a7a7a;
            position: absolute;
            width: 100px;
            top: 30px;
            left: 50%;
            margin-left: -50px;
        }

        .shadow {
            position: fixed;
            top: 0;
            right: 0;
            bottom: 0;
            left: 0;
            z-index: 1050;
            display: none;
            overflow: hidden;
            -webkit-overflow-scrolling: touch;
            outline: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, .6);
        }

        .shadow i {
            font-size: 80px;
            color: #fff;
            position: absolute;
            top: 30%;
            left: 50%;
            margin-left: -40px;
            transform: rotate(323deg);
            animation: myfirst 2s infinite;
        }

        @keyframes myfirst {
            0% {
                transform: rotate(0deg);
            }
            100% {
                transform: rotate(360deg);
            }
        }
    </style>
</head>

<body>
<div class="row box">
    <div class="col-md-12">
        <div class="row">
            <div class="col-md-12">
                <i class="fa fa-check-circle" aria-hidden="true"></i>
                <p>保存成功</p>
                <p>可到<a href="javaScript:;" target="ifm" class="searchBtn">通用查询</a>模块查看</p>
            </div>
            <div class="col-md-12">
                <a href="javascript:;">
                    <img src="<%=path%>/img/world.png" class="weituo" alt="" />
                    <span>委托书</span>
                </a>
                <a href="javascript:;">
                    <img src="<%=path%>/img/file.png" class="pinshu" alt="" />
                    <span>委托聘请书</span>
                </a>
            </div>
            <div class="col-md-12">
                <a href="javaScript:;" target="ifm" class="btn btn-blue btn-lang jixuBTn">继续登记</a>
            </div>
        </div>
    </div>
</div>
<%@ include file="../linkJs.jsp" %>
<script src="<%=path%>/js/entrustCurrency.js"></script>
<script>
    $(".searchBtn").click(function(){
    parent.$("#bs-example-navbar-collapse-1").children().eq(1).children().eq(2).addClass("active").siblings().removeClass("active")
     location.href="<%=path%>/caseQuery/caseQuery"
    })
    $(".jixuBTn").click(function(){
        location.href="<%=path%>/delegate/caseReg"
    })
    $(".weituo").click(function(){
        location.href="<%=path%>/DocDownload/delegateDoc?consignmentId=${consignmentId}";
    })
    $(".pinshu").click(function(){
        location.href="<%=path%>/DocDownload/employDoc?consignmentId=${consignmentId}";
    })
</script>
</body>

</html>