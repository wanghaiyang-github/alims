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

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>北京市公安局法医鉴定案件委托系统</title>
    <%@ include file="../linkCss.jsp" %>
    <style>
        .breadcrumb {
            padding: 8px 30px;
            margin-bottom: 0px;
            list-style: none;
            background-color: #fff;
            border-radius: 0px;
            border-bottom: 2px solid #f8f8f8;
            position: absolute;
            top: 0px;
            width: 100%;
            margin-left: -30px;
        }

        .breadcrumb > li > a {
            color: #b2b2b2
        }

        .breadcrumb > li.active {
            color: #007ef9
        }

        body > .row {
            width: 100%;
            height: 100%;
            padding: 55px 60px 87px 60px;
            padding-top: 55px;
        }

        body > .row > .col-md-12 {
            width: 100%;
            height: 100%;
            padding: 0px;
            border: 1px solid #f2f2f2;
            box-shadow: 0px 0px 10px 5px #f3f3f3;
        }

        body > .row > .col-md-12 > .row {
            position: relative;
            height: 100%;
        }

        body > .row > .col-md-12 > .row > .col-md-12:nth-child(1) {
            height: 65px;
            background: #f8f8f8;
            line-height: 65px;
            font-size: 22px;
            font-weight: 600;
            text-align: center;
            color: #296fff;
            position: absolute;
            top: 0px;
        }

        body > .row > .col-md-12 > .row > .col-md-12:nth-child(2) {
            height: 100%;
            padding-top: 65px;
            background: #fff;
        }

        body > .row > .col-md-12 > .row > .col-md-12:nth-child(2) > ul {
            position: absolute;
            top: 50%;
            transform: translateY(-50%);
            width: 100%;
        }

        body > .row > .col-md-12 > .row > .col-md-12:nth-child(2) > ul > li {
            width: 122px;
            height: 44px;
            color: #fff;
            line-height: 44px;
            border-radius: 50px;
            margin: 0 auto;
            text-align: center;
            cursor: pointer;
        }

        body > .row > .col-md-12 > .row > .col-md-12:nth-child(2) > ul > li > a {
            color: #fff;
        }

        body > .row > .col-md-12 > .row > .col-md-12:nth-child(2) > ul > li + li {
            margin-top: 32px;
        }

        body > .row > .col-md-12 > .row > .col-md-12:nth-child(2) > ul > li:nth-child(1) {
            background: #007ef9
        }

        body > .row > .col-md-12 > .row > .col-md-12:nth-child(2) > ul > li:nth-child(2) {
            /*background: #ffb802*/
            background: #929291
        }

        body > .row > .col-md-12 > .row > .col-md-12:nth-child(2) > ul > li:nth-child(3) {
            /*background: #ff5951*/
            background: #929291
        }

        body > .row > .col-md-12 > .row > .col-md-12:nth-child(2) > ul > li:nth-child(4) {
            /*background: #00b39b*/
            background: #929291
        }

        .form-control {
            background: #fafafa;
            border-color: #ececec
        }

        .smallBox .modal-content .modal-body {
            padding: 20px 93px;
            padding-bottom: 15px;
        }

        .smallBox .modal-content .modal-body {
            padding: 20px 60px;
            padding-bottom: 15px;
        }

        .smallBox .modal-content .modal-footer {
            padding: 15px 60px;
        }

        .error {
            font-weight: 400;
            font-size: 12px;
            text-align: center;
            color: #a94442;
            margin-left: 10px;
        }

        .form-group .radio-inline:nth-child(2) {
            margin-left: 30px !important;
        }

        .form-group .radio-inline:nth-child(3) {
            margin-left: 76px !important;
        }

        .smallBox .modal-content .modal-body {
            padding: 25px 20px;
            padding-bottom: 15px;
        }

        .smallBox .modal-content .modal-body #caseXkNo {
            border-radius: 50px;
            text-align: center;
        }

        #caseXkNoForm .btn-checkbox > li {
            width: 100%;
            margin-bottom: 10px;
            margin-left: 0px !important;
            text-align: center;
        }

        #caseXkNoForm .btn-checkbox > li input {
            cursor: pointer;
        }

        #caseXkNoForm .modal-body > .form-group {
            margin-bottom: 15px;
        }

        .smallBox .modal-content .modal-footer {
            padding: 15px 104px;
            padding-top: 0px;
            padding-right: 55px;
        }

        .smallBox .modal-content .modal-footer + div,
        #saveBox .modal-content .modal-footer + div {
            position: absolute;
            bottom: -74px;
            background: #ffeceb;
            width: 100%;
            padding: 25px 0;
            text-align: center;
            font-weight: 600;
            border: 1px solid #ff746a;
        }

        .smallBox .modal-content .modal-footer + div span,
        #saveBox .modal-content .modal-footer + div span {
            color: #ff5d51;
        }
    </style>
</head>
<div class="modal fade popBox smallBox" id="siteSurveyBox" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <form id="caseXkNoForm" onsubmit="return false;">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">输入现勘编号</h4>
                </div>
                <div class="modal-body">
                    <div class="form-group clearfix">
                        <c:if test="${orgInfos != null}">
                            <label class="col-md-2">送检至</label>
                        </c:if>
                        <c:if test="${orgInfos == null}">
                            <label class="col-md-1"></label>
                        </c:if>
                        <div class="col-md-10">
                            <div class="form-group">
                                <c:if test="${orgInfos != null}">
                                    <ul class="btn-checkbox clearfix btn-checkbox-yellow">
                                        <c:forEach items="${orgInfos}" var="org" varStatus="s">
                                           <%-- <li class="<c:if test="${s.index eq 0}">active</c:if>">${org.orgQualification}
                                                <input type="radio" title="${org.orgQualification}" value="${org.orgId}"
                                                       name="orgQualification">
                                            </li>--%>
                                            <li class="<c:if test="${org.defaultCenterFlag eq 1}">active</c:if>">${org.orgQualification}
                                                <input type="radio" title="${org.orgQualification}" value="${org.orgId}"
                                                       name="orgQualification">
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </c:if>
                                <%--<label class="hidden error">请输入现勘编号</label>--%>
                                <input type="text" id="caseXkNo" name="caseXkNo" class="form-control" value="">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer clearfix" <c:if test="${orgInfos == null}">style="padding: 15px 65px;"</c:if>>
                    <button type="button" id="saveBtn" class="btn btn-lang pull-left btn-blue">确认</button>
                    <button type="button" class="btn btn-lang pull-right btn-blue-border" data-dismiss="modal"
                            id="cancelBtn">取消
                    </button>
                </div>
                <div style="display: none">您确认送检至<span>()</span>吗？</div>
            </div>
        </form>
    </div>
</div>
<%--<div class="modal fade popBox smallBox" id="siteSurveyBox" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">--%>
<%--<div class="modal-dialog" role="document">--%>
<%--<div class="modal-content">--%>
<%--<div class="modal-header">--%>
<%--<h4 class="modal-title">输入现勘编号</h4>--%>
<%--</div>--%>
<%--<div class="modal-body">--%>
<%--<c:if test="${orgInfos != null}">--%>
<%--<div class="form-group">--%>
<%--<label>送检至</label>--%>
<%--<c:forEach items="${orgInfos}" var="org" varStatus="s">--%>
<%--<label class="radio-inline radio-sex">--%>
<%--<input type="radio" class="rdo" title="${org.orgQualification}" value="${org.orgId}" name="orgQualification" <c:if test="${s.index eq 0}">checked </c:if>>${org.orgQualification}--%>
<%--</label>--%>
<%--</c:forEach>--%>
<%--</div>--%>
<%--</c:if>--%>
<%--<div class="form-group">--%>
<%--<label class="hidden error">请输入现勘编号</label>--%>
<%--<input type="text" id="caseXkNo" name="caseXkNo" class="form-control">--%>
<%--</div>--%>
<%--</div>--%>
<%--<div class="modal-footer clearfix">--%>
<%--&lt;%&ndash;<a href="<%=path%>/delegate/AaReg" class="btn btn-lang pull-left btn-blue" target="ifm">确认</a>&ndash;%&gt;--%>
<%--<button type="button" id="saveBtn" class="btn btn-lang pull-left btn-blue">确认--%>
<%--</button>--%>
<%--<button type="button" class="btn btn-lang pull-right btn-blue-border" data-dismiss="modal" id="cancelBtn">取消</button>--%>
<%--</div>--%>
<%--</div>--%>
<%--</div>--%>
<%--</div>--%>
<body>
<ol class="breadcrumb">
    <li><a href="javascript:void(0);" id="pageHome">首页</a></li>
    <li class="active">案件委托登记</li>
</ol>
<div class="row">
    <div class="col-md-12">
        <div class="row">
            <div class="col-md-12">选择鉴定类别</div>
            <div class="col-md-12">
                <ul>
                    <li data-toggle="modal" data-target="#siteSurveyBox">DNA鉴定</li>
                    <li>
                        <a href="#" target="ifm" disabled="disabled" aria-disabled="true">毒化鉴定</a>
                    </li>
                    <li>
                        <a href="#" target="ifm" disabled="disabled" aria-disabled="true">病理鉴定</a>
                    </li>
                    <li>
                        <a href="#" target="ifm" disabled="disabled" aria-disabled="true">临床鉴定</a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>
<footer></footer>
<%@ include file="../linkJs.jsp" %>
<script>
    $(function () {
        $("#caseXkNo").focus();
       //现勘号单选
        $("#siteSurveyBox").on('shown.bs.modal', function (e) {
            if($("#siteSurveyBox").find(".btn-checkbox").find(".active").length>0){
                $("#siteSurveyBox").find(".modal-footer").next().slideDown().html("您确认送检至<span>(" + $("#siteSurveyBox").find(".btn-checkbox").find(".active").prop('firstChild').nodeValue + ")</span>吗？")
            }
        })

        $("#siteSurveyBox").find(".btn-checkbox").on("click", 'li', function () {
            if (!$(this).hasClass("active")) {
                $(this).addClass("active").siblings().removeClass("active")
                $(this).find("input").prop("checked", true)
                $(this).siblings().find("input").prop("checked", false)
            }
            $("#siteSurveyBox").find(".modal-footer").next().slideDown().html("您确认送检至<span>(" + $(this).prop('firstChild').nodeValue + ")</span>吗？")
        })
        $("#pageHome").on("click", function () {
            window.location = "<%=path%>/main/home";
            parent.$("#bs-example-navbar-collapse-1").children().eq(1).children().eq(0).addClass("active").siblings().removeClass("active")
        });

        //输入现堪号点击确认
        $("#saveBtn").on("click", function () {
            var caseXkNo = $("#caseXkNo").val();
            if (caseXkNo == "") {
                $("#siteSurveyBox").find(".modal-footer").next().slideDown().html("<span>现勘编号不能为空</span>")
            } else {
                var orgId = $(".btn-checkbox").find(".active").find("input[name='orgQualification']").val() == undefined ? "" : $(".btn-checkbox").find(".active").find("input[name='orgQualification']").val();
                var orgQualification = $(".btn-checkbox").find(".active").find("input[name='orgQualification']").attr("title") == undefined ? "" : $(".btn-checkbox").find(".active").find("input[name='orgQualification']").attr("title");
                $.ajax({
                    url: "<%=path%>/delegate/validateXkNo?caseXkNo=" + caseXkNo + "&orgId=" + orgId,
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        if (data.success == true) {
                            location.href = "<%=path%>/delegate/dnaReg.html?xkNo=" + caseXkNo + "&orgId=" + orgId;
                        } else {
                            $("#siteSurveyBox").find(".modal-footer").next().slideDown().html("<span>该现勘编号已送检"+orgQualification+"</span>")
                        }
                    },
                    error: function (e) {
                        alert(e);
                    }
                });
            }

        });
        $("input[name='orgQualification']").keydown(function (e) {
            var caseXkNo = $("#caseXkNo").val();
            if (e.keyCode == 13) {
                if (caseXkNo == "") {
                    $(".error").removeClass("hidden")
                } else {
                    var orgId = $("input[name='orgQualification']:checked").val();
                    var orgQualification = $("input[name='orgQualification']:checked").attr("title");
                    $.ajax({
                        url: "<%=path%>/delegate/validateXkNo?caseXkNo=" + caseXkNo + "&orgId=" + orgId,
                        type: "post",
                        dataType: "json",
                        success: function (data) {
                            if (data.success == true) {
                                $(".error").removeClass("hidden")
                                location.href = "<%=path%>/delegate/dnaReg.html?xkNo=" + caseXkNo + "&orgId=" + orgId;
                            } else {
                                $("#caseXkNo").siblings("label").removeClass("hidden").html("该现勘编号已送检" + orgQualification);
                            }
                        },
                        error: function (e) {
                            alert(e);
                        }
                    });
                }
            }

        })
        $("#caseXkNo").keydown(function (e) {
            if (e.keyCode == 13) {
                if ($(this).val() == "") {
                    $("#siteSurveyBox").find(".modal-footer").next().slideDown().html("<span>现勘编号不能为空</span>")
                } else {
                    /*var orgId = $("input[name='orgQualification']:checked").val();
                    var orgQualification = $("input[name='orgQualification']:checked").attr("title");*/
                    var orgId = $(".btn-checkbox").find(".active").find("input[name='orgQualification']").val() == undefined ? "" : $(".btn-checkbox").find(".active").find("input[name='orgQualification']").val();
                    var orgQualification = $(".btn-checkbox").find(".active").find("input[name='orgQualification']").attr("title") == undefined ? "" : $(".btn-checkbox").find(".active").find("input[name='orgQualification']").attr("title");
                    var that = $(this)
                    $.ajax({
                        url: "<%=path%>/delegate/validateXkNo?caseXkNo=" + $(this).val() + "&orgId=" + orgId,
                        type: "post",
                        dataType: "json",
                        success: function (data) {
                            console.log(data.success)
                            if (data.success == true) {
                                location.href = "<%=path%>/delegate/dnaReg.html?xkNo=" + that.val() + "&orgId=" + orgId;
                            } else {
                                /*$("#caseXkNo").siblings("label").removeClass("hidden").html("该现勘编号已送检" + orgQualification);*/
                                $("#siteSurveyBox").find(".modal-footer").next().slideDown().html("<span>该现勘编号已送检"+orgQualification+"</span>")
                            }
                        },
                        error: function (e) {
                            alert(e);
                        }
                    });
                }
            }
        })
        $("#cancelBtn").on("click", function () {
            $("#caseXkNo").val("");
        });
        $("input[name='orgQualification']").change(function () {
            console.log($(this).val())
        })
        $('#siteSurveyBox').on('hidden.bs.modal', function (e) {
            $(".btn-checkbox").children().eq(0).addClass("active").siblings().removeClass("active")
            $(this).find(".modal-footer").next().css("display","none").children().html("")
            $("#caseXkNo").val("");
        })

    })
</script>
</body>

</html>