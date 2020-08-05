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
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>北京市公安局法医鉴定案件委托系统</title>
    <link rel="shortcut icon" href="<%=path%>/img/favicon.ico" type="image/x-icon" />
    <%@ include file="linkCss.jsp" %>
    <style>
        html,
        body {
            width: 100%;
            height: 100%;
            padding: 0px;
        }

        main {
            height: 99%;
            padding-top: 50px;
        }

        iframe {
            background: #ffffff;
        }

        header .badge {
            line-height: 1.7;
            color: #fff;
            background-color: #ff5951;
            width: 18px;
            height: 18px;
            border-radius: 50%;
            padding: 0px;
            position: absolute;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            top: 7px;
            right: 4px;
        }

        .tipsA {
            position: relative;
        }

        .tips {
            padding-top: 20px;
            position: absolute;
            background: #fff;
            width: 276px;
            box-shadow: 0px 6px 10px 3px #f3f3f3;
            right: -50%;
            margin-right: -73px;
            display: none;
        }

        .tips ul {
            overflow: hidden;
        }

        .tips .caret {
            position: absolute;
            border-top: 8px dashed #fff;
            border-right: 8px solid transparent;
            border-left: 8px solid transparent;
            top: -8px;
            transform: rotate(180deg);
            left: 50%;
        }

        .tips li {
            padding: 15px 35px;
            position: relative;
            cursor: pointer;
        }

        .tips li::before {
            content: "";
            position: absolute;
            background: #3698fa;
            width: 12px;
            height: 12px;
            border-radius: 50%;
            left: 15px;
            top: 20px;
            z-index: 1;
        }

        .tips li:nth-child(4n-1)::before {
            background: #ff7a75;
        }

        .tips li:nth-child(4n-2)::before {
            background: #fec548;
        }

        .tips li:nth-child(4n-3)::before {
            background: #3698fa;
        }

        .tips li:nth-child(4n-4)::before {
            background: #00b39b;
        }

        .tips li:nth-child(n+2):after {
            content: "";
            height: 100%;
            border-left: 2px dashed #f1f1f1;
            position: absolute;
            left: 20px;
            top: -49px;
        }

        .tips li p:nth-child(1) {
            color: #000;
            font-size: 16px;
        }

        .tips li p:nth-child(2) {
            color: #838383;
            margin: 0px
        }

        .tips li p:nth-child(2) span:nth-child(2) {
            float: right;
            color: #ff5951;
        }

        .tips li:hover {
            background: #f6f6f6
        }

        .noNews {
            text-align: center;
            border-bottom: 1px dashed #dcdcdc;
            padding-bottom: 20px;
            color: #dcdcdc;
        }

        .btn-noBg {
            background: #fff;
            color: #666666
        }

        .btn-noBg.active,
        .btn-noBg:active {
            box-shadow: none
        }

        .btn-noBg.active.focus,
        .btn-noBg.active:focus,
        .btn-noBg.focus,
        .btn-noBg:active.focus,
        .btn-noBg:active:focus,
        .btn-noBg:focus {
            outline: none;
        }
    </style>
</head>

<body>
<header>
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                        data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#">
                    <p>北京市公安局法医鉴定案件委托系统</p>
                    <p>Case Consignation For Forensic Identification of Beijing Public Security Bureau</p>
                </a>
            </div>
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav navbar-right">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle header-nav-img" data-toggle="dropdown" role="button"
                           aria-haspopup="true" aria-expanded="false">
                            <img src="<%=path%>/img/touxiang1.png" alt="">${user.amPersonalInfo.fullName}
                            <c:if test="${user.amPersonalInfo.policeNo} != null">(${user.amPersonalInfo.policeNo})</c:if>
                        </a>
                        <ul class="dropdown-menu">
                            <span class="caret"></span>
                            <li><a href="<%=path%>/manage/personalInformation" target="ifm">个人信息</a></li>
                            <li><a href="<%=path%>/loginOut">退出</a></li>
                        </ul>
                    </li>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <shiro:user></shiro:user>
                    <c:forEach items="${permissionList}" var="list">
                        <shiro:hasPermission name="${list.permissionName}">
                            <c:if test="${list.permissionList != null}">
                                <li class="dropdown">
                                    <a href="#" class="dropdown-toggle" role="button" aria-haspopup="true"
                                       aria-expanded="false">${list.permissionName}</a>
                                    <ul class="dropdown-menu">
                                        <span class="caret"></span>
                                        <c:forEach items="${list.permissionList}" var="nodes">
                                            <c:if test="${user != null}">
                                                <c:if test="${user.loginName == 'jgj'}">
                                                    <c:if test="${nodes.permissionName == '非现场案件委托'}">
                                                        <li>
                                                            <a href="<%=path%>${nodes.permissionLink}"
                                                               target="ifm">${nodes.permissionName}</a>
                                                        </li>
                                                    </c:if>
                                                </c:if>
                                                <c:if test="${user.loginName != 'jgj'}">
                                                    <li>
                                                        <a href="<%=path%>${nodes.permissionLink}"
                                                           target="ifm">${nodes.permissionName}</a>
                                                    </li>
                                                </c:if>
                                            </c:if>
                                        </c:forEach>
                                    </ul>
                                </li>
                            </c:if>
                            <c:if test="${list.permissionList == null}">
                                <li <c:if test="${list.activeFlag == '1'}">class="active"</c:if>><a
                                        href="<%=path%>${list.permissionLink}" target="ifm">${list.permissionName}</a>
                                </li>
                            </c:if>
                        </shiro:hasPermission>
                    </c:forEach>
                    <li class="tipsA">
                        <a href="javascript:;"><i class="fa fa-commenting" aria-hidden="true"></i><span
                                class="badge">0</span></a>
                        <div class="tips">
                            <p class="noNews hidden">暂无消息通知</p>
                            <ul>
                                <li>
                                    <a href="<%=path%>/stateQuery/search?text=FT2017WZ0031" target="ifm">
                                        <p>您还没有消息</p>
                                        <%--
                                                                                <p>
                                                                                    <span>16分钟前</span>
                                                                                    <span>已受理</span>
                                                                                </p>
                                        --%>
                                    </a>
                                </li>
                                <%--
                                                                <li>
                                                                    <a href="<%=path%>/stateQuery/search?text=FYB1700156-2017WZ0156" target="ifm">
                                                                        <p>FYB1700156-2017WZ0156</p>
                                                                        <p>
                                                                            <span>16分钟前</span>
                                                                            <span>已受理</span>
                                                                        </p>
                                                                    </a>
                                                                </li>
                                                                <li>
                                                                    <a href="<%=path%>/stateQuery/search?text=DX2017WZ0029" target="ifm">
                                                                        <p>DX2017WZ0029</p>
                                                                        <p>
                                                                            <span>16分钟前</span>
                                                                            <span>已受理</span>
                                                                        </p>
                                                                    </a>
                                                                </li>
                                                                <li>
                                                                    <a href="<%=path%>/stateQuery/search?text=DX2017WZ0027" target="ifm">
                                                                        <p>DX2017WZ0027</p>
                                                                        <p>
                                                                            <span>16分钟前</span>
                                                                            <span>已受理</span>
                                                                        </p>
                                                                    </a>
                                                                </li>
                                --%>
                            </ul>
                            <span class="caret"></span>
                            <div>
                                <button type="button" class="btn btn-noBg btn-lg btn-block clean">清除通知</button>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
</header>
<main>
    <iframe src="<%=path%>/main/home" name="ifm" frameborder="0" width="100%" height="100%"></iframe>
</main>
<%@ include file="linkJs.jsp" %>
<script>
    $(".navbar-nav").eq(1).find("li").click(function () {
        $(this).addClass("active").siblings().removeClass("active")
    })
    $(".navbar-nav").eq(0).find("li").click(function () {
        $(".navbar-nav").eq(1).find("li").removeClass("active")
    })
    $(".navbar-nav").find(".dropdown").mouseenter(function () {
        $(this).addClass("open").find('.dropdown-toggle').attr("aria-expanded", "true")
    })
    $(".navbar-nav").find(".dropdown").mouseleave(function () {
        $(this).removeClass("open").find('.dropdown-toggle').attr("aria-expanded", "false")
    })

    $(".tipsA").find("a").click(function () {
        if ($(".tips").css("display") == "none") {
            $(".tips").slideDown()
            if ($(".tips").find("li").length > 0) {
                $(".tips").find(".noNews").addClass("hidden")
            } else {
                $(".tips").find(".noNews").removeClass("hidden")
            }
        } else {
            $(".tips").slideUp()
        }

    })
    $(".tipsA").siblings().children().click(function () {
        $(".tips").slideUp()
    })
    $(".clean").click(function () {
        if ($(".tips").find("li").length > 0) {
            $(".tips").find("li").each(function (i, e) {
                if (i == $(".tips").find("ul").children().length - 1) {
                    $(this).delay(100 * i).animate({
                        left: '-10px',
                    }, 1000).animate({
                        left: '460px'
                    }, 500, function () {
                        $(".tips").slideUp()
                        $(".tips").find("li").remove();
                        $(".tipsA").find(".badge").remove();
                    });
                } else {
                    $(this).delay(100 * i).animate({
                        left: '-10px',
                    }, 1000).animate({
                        left: '460px'
                    }, 500);
                }
            })
        } else {
            $(".tips").slideUp()
        }
    })
    $(".tips").find("li").click(function () {
        $(this).delay(100).animate({
            left: '-10px',
        }, 1000).animate({
            left: '460px'
        }, 500, function () {
            $(".tips").slideUp()
            $(this).remove();
            $(".tipsA").find(".badge").html(Number($(".tipsA").find(".badge").html()) - 1);
        });
    })
</script>
</body>

</html>
