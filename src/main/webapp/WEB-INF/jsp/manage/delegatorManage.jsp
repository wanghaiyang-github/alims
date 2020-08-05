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
</head>
<form action="<%=path%>/AmPersonalInfoController/addAndUpdateAmPersonalInfo" id="saveform">
    <div class="modal fade popBox bigBox" id="clientBox" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">委托人信息</h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>委托人姓名</label>
                                <input type="text" class="form-control" placeholder="请输入委托人姓名" name="fullName">
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group" style="margin-top: 45px;">
                                <label>性别</label>
                                <label class="radio-inline radio-sex">
                                    <input type="radio" class="sex sexman" value="01" name="gender" checked>男性
                                </label>
                                <label class="radio-inline  radio-sex">
                                    <input type="radio" class="sex sexwoman" value="02" name="gender">女性
                                </label>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>职务</label>
                                <div class="select">
                                    <select class="form-control" required="" name="position">
                                        <option value="" disabled="" selected="" hidden="">请选择权限设定</option>
                                        <c:forEach items="${listDictItem}" var="list">
                                            <option value="${list.dictCode}">${list.dictName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>警号</label>
                                <input type="text" class="form-control" placeholder="请输入警号" name="policeNo">
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>联系电话</label>
                                <input type="text" class="form-control" placeholder="请输入联系电话" name="phoneNumber">
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label>单位电话</label>
                                <input type="text" class="form-control" placeholder="请输入单位电话" name="orgPhone">
                            </div>
                        </div>
                        <%--<div class="col-md-6">--%>
                            <%--<div class="form-group">--%>
                                <%--<label>登录名</label>--%>
                                <%--<input type="text" class="form-control" placeholder="请输入登录名" name="loginName">--%>
                            <%--</div>--%>
                        <%--</div>--%>
                        <%--<div class="col-md-6">--%>
                            <%--<div class="form-group">--%>
                                <%--<label>登录密码</label>--%>
                                <%--<input type="password" class="form-control" placeholder="请输入登录密码" name="loginPassword">--%>
                            <%--</div>--%>
                        <%--</div>--%>

                        <%--<div class="col-md-6">--%>
                            <%--<div class="form-group">--%>
                                <%--<label>权限设定</label>--%>
                                <%--<div class="select">--%>
                                    <%--<select class="form-control" required="" name="roleId">--%>
                                        <%--<option value="" disabled="" selected="" hidden="">请选择权限设定</option>--%>
                                        <%--<c:forEach items="${LoaRoleList}" var="list">--%>
                                            <%--<option value="${list.roleId}">${list.roleName}</option>--%>
                                        <%--</c:forEach>--%>
                                    <%--</select>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    </div>
                </div>
                <div class="modal-footer clearfix">
                    <input type="hidden" name="personalId">
                    <button class="btn btn-lang  btn-blue addClient" type="submit">确认</button>
                    <button type="button" class="btn btn-lang btn-blue-border" data-dismiss="modal">取消</button>
                </div>
            </div>
        </div>
    </div>
</form>
<body>
<ol class="breadcrumb">
    <li><a href="javascript:void(0);" id="pageHome">首页</a></li>
    <li class="active">委托人管理</li>
</ol>

<div class="row Modular">
    <div class="col-md-12">
        <div class="panel panel-default">
            <div class="panel-heading blue">
                <div>委托人管理</div>
                <button class="btn btn-blue" data-toggle="modal" data-target="#clientBox">添加</button>
            </div>
            <div class="panel-body">
                <table class="table table-hover table-bordered bigTable">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>委托人姓名</th>
                        <th>性别</th>
                        <th>警号</th>
                        <th>职务</th>
                        <th>联系电话</th>
                        <th>单位电话</th>
                        <%--<th>登录名</th>--%>
                        <%--<th>登录密码</th>--%>
                        <%--<th>权限设定</th>--%>
                        <th>
 <%--                           <label class="custom-control checkbox-inline" style="margin-top: -23px;">
                            <input class="custom-control-input" type="checkbox" name="updateStateAll">
                            <span class="custom-control-label"></span>&ndash;%&gt;
                            </label>
--%>                            操作</th>
                    </tr>
                    </thead>
                    <tbody id="clientTbody">
                    <c:forEach items="${amPersonalInfoVoList}" var="aplist">
                        <tr>
                            <td></td>
                            <td>${aplist.entity.fullName} <input type="hidden" name="fullName" value="${aplist.entity.fullName}"></td>
                            <td>${aplist.genderName} <input type="hidden" name="gender" value="${aplist.entity.gender}"></td>
                            <td>${aplist.entity.policeNo} <input type="hidden" name="policeNo" value="${aplist.entity.policeNo}"></td>
                            <td>${aplist.positionName} <input type="hidden" name="position" value="${aplist.entity.position}"></td>
                            <td>${aplist.entity.phoneNumber} <input type="hidden" name="phoneNumber"
                                                             value="${aplist.entity.phoneNumber}"></td>
                            <td>${aplist.entity.orgPhone} <input type="hidden" name="orgPhone" value="${aplist.entity.orgPhone}"></td>
                            <%--<td>${aplist.entity.loginName} <input type="hidden" name="loginName" value="${aplist.entity.loginName}">--%>
                            <%--</td>--%>
                            <%--<td>*** <input type="hidden" name="loginPassword"--%>
                                                               <%--value="${aplist.entity.loginPassword}"></td>--%>
                            <%--<td>${aplist.entity.roleName} <input type="hidden" name="roleName" value="${aplist.entity.roleName}"></td>--%>
                            <td>
<%--
                                <label class="custom-control checkbox-inline" style="margin-top: -23px;">
                                    <input class="custom-control-input" type="checkbox" name="updateState">
                                    <span class="custom-control-label"></span>
                                </label>
--%>
                                <button class="btn-icon btn-icon-blue btn-icon-bianji-blue edit ">编辑</button>
<%--
                                <c:if test="${aplist.entity.activeFlag == '0'}">
                                    <button class="btn-icon btn-icon-yellow  btn-icon-jinyong-gray forbidden" value="${aplist.entity.personalId}">禁用</button>
                                </c:if>
--%>
                                <c:if test="${aplist.entity.activeFlag == '1'}">
                                        <button class="btn-icon btn-icon-yellow  btn-icon-jinyong-yellow startusing" value="${aplist.entity.personalId}">启用</button>
                                </c:if>
                                <button class="btn-icon btn-icon-red btn-icon-shanchu-red remove"
                                        value="${aplist.entity.personalId}">删除
                                </button>
                                <input type="hidden" name="personalId" value="${aplist.entity.personalId}">
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<%--
<div class="row btn-box hidden">
    <div class="col-md-12">
        <button type="button" class="btn btn-blue btn-lang updateStateBtn pull-right">更新状态
        </button>
        <div class="col-md-4 small-left pull-right">
            <div class="form-group">
                <div class="col-sm-9" style="margin-top: 3px;">
                    <div class="select">
                        <select class="form-control" required="" name="roleId">
                            <option value="" disabled="" selected="" hidden="">请选择权限设定</option>
                            <c:forEach items="${LoaRoleList}" var="list">
                                <option value="${list.roleId}">${list.roleName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
--%>
<%@ include file="../linkJs.jsp" %>
<script>
    $("#pageHome").on("click",function () {
        window.location="<%=path%>/main/home";
        parent.$("#bs-example-navbar-collapse-1").children().eq(1).children().eq(0).addClass("active").siblings().removeClass("active")
    });
    //委托人删除
    $("#clientTbody").on("click", ".remove", function () {
        var res = confirm("您确定要删除这条记录吗！！");
        if(res == true){
            location.href="<%=path%>/AmPersonalInfoController/deleteAmPersonalInfo?personalId=" + $(this).attr("value")
        }
    })
    //委托人禁用
    $("#clientTbody").on("click", ".forbidden", function () {
        location.href="<%=path%>/LoaUserInfoController/forbiddenLoaUserInfo?personalId=" + $(this).attr("value")
    })
    //委托人启用
    $("#clientTbody").on("click", ".startusing", function () {
        location.href="<%=path%>/LoaUserInfoController/startusingLoaUserInfo?personalId=" + $(this).attr("value")
    })

    $("#clientTbody").children().each(function (i) {
        $(this).children().eq(0).html(i + 1)
    })
    $("#clientTbody").on("click", ".edit", function () {
        $("#clientBox").modal('show')
        var fullName = $(this).parents("tr").find("input[name='fullName']").val(),
                gender = $(this).parents("tr").find("input[name='gender']").val(),
                policeNo = $(this).parents("tr").find("input[name='policeNo']").val(),
                position = $(this).parents("tr").find("input[name='position']").val(),
                phoneNumber = $(this).parents("tr").find("input[name='phoneNumber']").val(),
                orgPhone = $(this).parents("tr").find("input[name='orgPhone']").val(),
                loginName = $(this).parents("tr").find("input[name='loginName']").val(),
                loginPassword = $(this).parents("tr").find("input[name='loginPassword']").val(),
                roleName = $(this).parents("tr").find("input[name='roleName']").val(),
                personalId = $(this).parents("tr").find(".remove").attr("value");
        $("#clientBox").find("input[name='fullName']").val(fullName)
        $("#clientBox").find("input[name='gender'][value='"+gender+"']").prop("checked", true)
        $("#clientBox").find("select[name='position']").find("option").each(function(){
            if($(this).val()==position){
                $(this).prop("selected",true)
                return true
            }
        })
//        $("#clientBox").find("select[name='position']").find("option:contains('"+position+"')").prop("selected",true)
        $("#clientBox").find("input[name='policeNo']").val(policeNo)
        $("#clientBox").find("input[name='phoneNumber']").val(phoneNumber)
        $("#clientBox").find("input[name='orgPhone']").val(orgPhone)
        $("#clientBox").find("input[name='loginName']").val(loginName)
        $("#clientBox").find("input[name='loginPassword']").val(loginPassword)
        $("#clientBox").find("select[name='roleId']").find("option:contains('"+roleName+"')").prop("selected",true)
        $("#clientBox").find("input[name='personalId']").val(personalId)
    })
    $('#clientBox').on('hidden.bs.modal', function (e) {
        $("#clientBox").find("input[type='text']").val("")
        $("#clientBox").find("input[type='hidden']").val("")
        $("#clientBox").find("input[type='radio']").prop("checked", false)
        $("#clientBox").find("select").val("")
    })
    $("input[name='updateStateAll']").change(function () {
        if ($(this).is(":checked")) {
            $("tbody").find("input[name='updateState']").prop("checked", true)
            $(".btn-box").removeClass("hidden")
        } else {
            $("tbody").find("input[name='updateState']").prop("checked", false)
            $(".btn-box").addClass("hidden")
        }
    })
    $("tbody").find("input[name='updateState']").change(function(){
        if($("tbody").find("input[name='updateState']:checked").length==$("tbody").children().length){
            $("input[name='updateStateAll']").prop("checked", true)
        }else{
            $("input[name='updateStateAll']").prop("checked", false)
        }
        if($("tbody").find("input[name='updateState']:checked").length>0){
            $(".btn-box").removeClass("hidden")
        }else{
            $(".btn-box").addClass("hidden")
        }
    })
    $(".updateStateBtn").click(function(){
        if($(".btn-box").find("select").val()){
            var amPersonalInfoList = []
            $("tbody").find("input[name='updateState']:checked").each(function(){
                var amPersonalInfo = {}
                amPersonalInfo.personalId = $(this).parents("td").find("input[name='personalId']").val()
                amPersonalInfo.roleId = $(".btn-box").find("select").val()
                amPersonalInfoList.push(amPersonalInfo)
            })
            var params = {
                amPersonalInfoList: amPersonalInfoList
            };

            $.ajax({
                url: "<%=path%>/AmPersonalInfoController/updateRole",
                type: "post",
                data: params,
                data: {"params": JSON.stringify(params)},
                success: function (data) {
                    if (data.success || data.success == true || data.success == "true") {
                        location.href = '<%=path%>/manage/delegatorManage';
                    }
                },
                error: function (e) {
                    alert(e);
                }
            });        }else{
            alert("请选择选权限设定")
        }
    })
    $(".addClient").click(function () {
        var form = $("#saveform ")
        form.bootstrapValidator({
            live: 'disabled',
            message: 'This value is not valid',
            fields: {
//                phoneNumber: {
//                    validators: {
//                        regexp: {
//                            regexp: /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/,
//                            message: '手机号码格式错误'
//                        }
//                    }
//                },
                phoneNumber: {
                    validators: {
                        notEmpty: {
                            message: '手机号码不能为空'
                        },
                        stringLength: {
                            min: 11,
                            max: 11,
                            message: '手机号格式错误'
                        },
                        regexp: {
                            regexp: /^[0-9]+$/,
                            message: '手机号格式错误'
                        }
                    }
                },
                fullName: {
                    validators: {
                        notEmpty: {
                            message: "请填写委托人名称"
                        }
                    }
                },
                policeNo: {
                    validators: {
                        notEmpty: {
                            message: "请填写警号"
                        }
                    }
                },
                position: {
                    validators: {
                        notEmpty: {
                            message: "请选择职务"
                        }
                    }
                },
//                orgPhone: {
//                    validators: {
//                        regexp: {
//                            regexp: /^0\d{2,3}-?\d{7,8}$/,
//                            message: '单位号码格式错误'
//                        }
//                    }
//                }
            }
        });
        form.bootstrapValidator('validate');
        if (form.data('bootstrapValidator').isValid()) {
            form.data('bootstrapValidator').destroy();
            form.data('bootstrapValidator', null);
            $("#clientBox").modal('show')
        }
    })
</script>
</body>

</html>