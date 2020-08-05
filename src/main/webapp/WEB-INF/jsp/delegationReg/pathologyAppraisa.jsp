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
<!-- 委托人添加 -->
<div class="modal fade popBox xsBox" id="addClientBox" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">委托人信息</h4>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <label>委托人姓名</label>
                    <input type="text" class="form-control" placeholder="请输入委托人姓名" name="clientName">
                </div>
                <div class="form-group">
                    <label>职务</label>
                    <input type="text" class="form-control" placeholder="请输入职务" name="clientDuties">
                </div>
                <div class="form-group">
                    <label>联系方式</label>
                    <input type="text" class="form-control" placeholder="请输入职务" name="clientPhone">
                </div>
                <div class="form-group">
                    <label>证件类型</label>
                    <div class="select">
                        <select class="form-control" required name="clientIdType">
                            <option value="" disabled selected hidden>请选择证件类型</option>
                            <option value="警官证">警官证</option>
                            <option value="身份证">身份证</option>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label>证件号</label>
                    <input type="text" class="form-control" placeholder="请输入证件号" name="clientId">
                </div>
            </div>
            <div class="modal-footer clearfix">
                <input type="hidden" name="index">
                <button class="btn btn-lang btn-blue pull-left addClient">确认</button>
                <button type="button" class="btn btn-lang pull-right btn-blue-border" data-dismiss="modal">取消</button>
            </div>
        </div>
    </div>
</div>

<body>
    <ol class="breadcrumb">
        <li><a href="#">首页</a></li>
        <li><a href="#">案件委托登记</a></li>
        <li class="active">病理鉴定</li>
    </ol>
    <div class="row Modular">
        <div class="col-md-12 starBox">
            <p class="starP">
                <i class="fa fa-star star" aria-hidden="true"></i>鉴定类别:<span>病理鉴定</span>
            </p>
        </div>
    </div>
    <div class="row Modular">
        <div class="col-md-12">
            <div class="panel panel-default">
                <div class="panel-heading blue">
                    <div>委托信息</div>
                </div>
                <div class="panel-body">
                    <div class="row inputBox">
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>委托单位 :</label>
                                <label>北市西城分局厂桥派出所</label>
                            </div>
                        </div>
                    </div>
                    <div class="row inputBox">
                        <div class="col-md-5">
                            <div class="col-md-11">
                                <div class="single-member effect-1">
                                    <select class="form-control clientSelect" required>
                                    <option value="" disabled selected hidden>请选委托人1</option>
                                    <option>张翼德</option>
                                    <option>赵云</option>
                                    <option>张飞</option>
                                    <option>刘备</option>
                                    <option value="other">其他</option>
                                </select>
                                    <div class="member-info">
                                        <div class="col-md-6">
                                            <p>职务: <span class="clientDuties">XXXXX</span></p>
                                        </div>
                                        <div class="col-md-6">
                                            <p>电话: <span class="clientPhone">13522222222</span></p>
                                        </div>
                                        <div class="col-md-6">
                                            <p>证件: <span class="clientIdType">警官证</span></p>
                                        </div>
                                        <div class="col-md-6">
                                            <p>证件号: <span class="clientId">12346789</span></p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-11">
                                <div class="single-member effect-1">
                                    <select class="form-control clientSelect" required>
                                    <option value="" disabled selected hidden>请选委托人2</option>
                                    <option>张翼德</option>
                                    <option>赵云</option>
                                    <option>张飞</option>
                                    <option>刘备</option>
                                    <option value="other">其他</option>
                                </select>
                                    <div class="member-info">
                                        <div class="col-md-6">
                                            <p>职务: <span class="clientDuties">XXXXX</span></p>
                                        </div>
                                        <div class="col-md-6">
                                            <p>电话: <span class="clientPhone">13522222222</span></p>
                                        </div>
                                        <div class="col-md-6">
                                            <p>证件: <span class="clientIdType">警官证</span></p>
                                        </div>
                                        <div class="col-md-6">
                                            <p>证件号: <span class="clientId">12346789</span></p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-7">
                            <div class="col-md-12">
                                <div class="single-member effect-1">
                                    <div class="member-image">
                                        其他信息
                                    </div>
                                    <div class="member-info">
                                        <div class="form-group">
                                            <label>通讯地址</label>
                                            <input type="text" class="form-control" placeholder="请输入通讯地址">
                                        </div>
                                        <div class="form-group">
                                            <label>邮政编号</label>
                                            <input type="text" class="form-control" placeholder="请输入邮政编号">
                                        </div>
                                        <div class="form-group">
                                            <label>传真号码</label>
                                            <input type="text" class="form-control" placeholder="请输入传真号码">
                                        </div>
                                        <div class="form-group">
                                            <label>原鉴定情况(鉴定单位及结论)</label>
                                            <textarea class="form-control" rows="3" placeholder="请输入鉴定情况"></textarea>
                                        </div>
                                        <div class="form-group">
                                            <label>重新鉴定原因</label>
                                            <textarea class="form-control" rows="3" placeholder="请输入鉴定原因"></textarea>
                                        </div>
                                        <!-- <p>通讯地址: <span>XXXXX</span></p>
                                    <p>邮政编号: <span>13522222222</span></p>
                                    <p>传真号码: <span>警官证</span></p>
                                    <p>原鉴定情况(鉴定单位及结论): <span>12346789</span></p>
                                    <p>其他说明: <span>12346789</span></p> -->
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- <div class="row inputBox">
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>通讯地址</label>
                                <i class="required">*</i>
                                <input type="text" class="form-control" placeholder="请输入通讯地址">
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>邮政编号</label>
                                <input type="text" class="form-control" placeholder="请输入邮政编号">
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>传真号码</label>
                                <input type="text" class="form-control" placeholder="请输入传真号码">
                            </div>
                        </div>
                    </div>
                    <div class="row inputBox">
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>原鉴定情况(鉴定单位及结论)</label>
                                <i class="required">*</i>
                                <textarea class="form-control" rows="3" placeholder="请输入鉴定情况"></textarea>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>重新鉴定原因</label>
                                <textarea class="form-control" rows="3" placeholder="请输入鉴定原因"></textarea>
                            </div>
                        </div>
                    </div> -->
                </div>
            </div>
        </div>
    </div>
    <div class="row Modular">
        <div class="col-md-12">
            <div class="panel panel-default">
                <div class="panel-heading yellow">
                    <div>案件信息</div>
                    <!-- <label class="custom-control checkbox-inline">
                        <input class="custom-control-input" type="checkbox">
                        <span class="custom-control-label" style="color: #ff5951">是否加急</span>
                    </label> -->
                </div>
                <div class="panel-body">
                    <div class="row inputBox">
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>案件名称</label>
                                <i class="required">*</i>
                                <input type="text" class="form-control" placeholder="请输入案件名称">
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>案发地点</label>
                                <input type="text" class="form-control" placeholder="请输入案发地点">
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>案发时间</label>
                                <input type="text" class="form-control" placeholder="请输入案发时间" readonly>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>案件类型</label>
                                <div class="select">
                                    <select class="form-control" required>
                                        <option value="" disabled selected hidden>请选择案件类型</option>
                                        <option>刑事案件</option>
                                        <option>民事案件</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>案发性质</label>
                                <div class="select">
                                    <select class="form-control" required>
                                        <option value="" disabled selected hidden>请选择案发性质</option>
                                        <option>抢劫</option>
                                        <option>强奸</option>
                                        <option>盗窃</option>
                                        <option>杀人</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>案件级别</label>
                                <div class="select">
                                    <select class="form-control" required>
                                        <option value="" disabled selected hidden>请选择案件级别</option>
                                        <option>一般</option>
                                        <option>特大</option>
                                        <option>重大</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row inputBox">
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>简要案情</label>
                                <i class="required">*</i>
                                <textarea class="form-control" rows="3" placeholder="请输入简要案情"></textarea>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>其他说明</label>
                                <textarea class="form-control" rows="3" placeholder="请输入其他说明"></textarea>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row Modular">
        <div class="col-md-12">
            <div class="panel panel-default">
                <div class="panel-heading red">
                    <div>鉴定要求</div>
                    <button class="btn btn-blue checkboxAll">全选</button>
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="form-group">
                                <ul class="btn-checkbox clearfix">
                                    <li class="pull-left active">死亡原因</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row Modular">
        <div class="col-md-12">
            <div class="panel panel-default">
                <div class="panel-heading green">
                    <div>被鉴定人信息</div>
                    <button class="btn btn-blue">添加</button>
                </div>
                <div class="panel-body nopadding">
                    <table class="table table-hover table-bordered bigTable">
                        <thead>
                            <tr>
                                <th>序号</th>
                                <th>人员类型</th>
                                <th>人员名称</th>
                                <th>性别</th>
                                <th>年龄</th>
                                <th>住址</th>
                                <th>身份证号</th>
                                <th>亲缘关系</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>序号</td>
                                <td>人员类型</td>
                                <td>人员名称</td>
                                <td>性别</td>
                                <td>年龄</td>
                                <td>住址</td>
                                <td>身份证号</td>
                                <td>亲缘关系</td>
                                <td>
                                    <button class="btn-icon btn-icon-blue btn-icon-bianji-blue ">编辑</button>
                                    <button class="btn-icon btn-icon-red btn-icon-shanchu-red ">删除</button>
                                </td>
                            </tr>
                            <tr>
                                <td>序号</td>
                                <td>人员类型</td>
                                <td>人员名称</td>
                                <td>性别</td>
                                <td>年龄</td>
                                <td>住址</td>
                                <td>身份证号</td>
                                <td>亲缘关系</td>
                                <td>
                                    <button class="btn-icon btn-icon-blue btn-icon-bianji-blue ">编辑</button>
                                    <button class="btn-icon btn-icon-red btn-icon-shanchu-red ">删除</button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <div class="row Modular">
        <div class="col-md-12">
            <div class="panel panel-default">
                <div class="panel-heading blue">
                    <div>检材信息</div>
                    <button class="btn btn-blue">添加</button>
                </div>
                <div class="panel-body nopadding">
                    <table class="table table-hover table-bordered bigTable">
                        <thead>
                            <tr>
                                <th>序号</th>
                                <th>检材名称</th>
                                <th>数量</th>
                                <th>重量</th>
                                <th>计量单位</th>
                                <th>备注</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>序号</td>
                                <td>检材名称</td>
                                <td>数量</td>
                                <td>重量</td>
                                <td>计量单位</td>
                                <td>备注</td>
                                <td>
                                    <button class="btn-icon btn-icon-blue btn-icon-bianji-blue ">编辑</button>
                                    <button class="btn-icon btn-icon-red btn-icon-shanchu-red ">删除</button>
                                </td>
                            </tr>
                            <tr>
                                <td>序号</td>
                                <td>检材名称</td>
                                <td>数量</td>
                                <td>重量</td>
                                <td>计量单位</td>
                                <td>备注</td>
                                <td>
                                    <button class="btn-icon btn-icon-blue btn-icon-bianji-blue ">编辑</button>
                                    <button class="btn-icon btn-icon-red btn-icon-shanchu-red ">删除</button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <div class="row btn-box ">
        <div class="col-md-12">
            <button class="btn btn-blue btn-lang">提交</button>
            <button class="btn btn-blue-border btn-lang">取消</button>
        </div>
    </div>
    <%@ include file="../linkJs.jsp" %>

    <script>
        $(function() {
            $(".clientSelect").searchableSelect();
            $(".clientSelect").change(function() {
                if ($(this).val() == "other") {
                    $("#addClientBox").modal('show')
                    $("#addClientBox").find("input[name='index']").val($(this).parents(".col-md-11").index())
                }
            })
            $(".addClient").click(function() {
                var clientName = $("#addClientBox").find("input[name='clientName']").val()
                var clientDuties = $("#addClientBox").find("input[name='clientDuties']").val()
                var clientIdType = $("#addClientBox").find("select[name='clientIdType']").val()
                var clientId = $("#addClientBox").find("input[name='clientId']").val()
                var clientPhone = $("#addClientBox").find("input[name='clientPhone']").val()
                var index = $("#addClientBox").find("input[name='index']").val()

                $(".effect-1").parent(".col-md-11").children().eq(index).find(".clientSelect").find(
                    "option[value='other']").before("<option selected>" + clientName + "</option>")
                $(".effect-1").parent(".col-md-11").children().eq(index).find(".clientSelect").next().remove()
                $(".effect-1").parent(".col-md-11").children().eq(index).find(".clientPhone").html(
                    clientPhone)
                $(".effect-1").parent(".col-md-11").children().eq(index).find(".clientDuties").html(
                    clientDuties)
                $(".effect-1").parent(".col-md-11").children().eq(index).find(".clientIdType").html(
                    clientIdType)
                $(".effect-1").parent(".col-md-11").children().eq(index).find(".clientId").html(
                    clientId)
                $(".effect-1").parent(".col-md-11").children().eq(index).find(".clientSelect").searchableSelect();

                $("#addClientBox").modal('hide')
            })
            $('#addClientBox').on('hidden.bs.modal', function(e) {
                $("#addClientBox").find("input").val("")
                $("#addClientBox").find("select").val("")
            })
            $(".btn-checkbox").children().each(function(i) {
                if (i > 0 && $(this).position().left == 0) {
                    $(this).css("margin-left", '0')
                }
            })
            $(".btn-checkbox").children().click(function() {
                if ($(this).hasClass("active")) {
                    $(this).removeClass("active")
                } else {
                    $(this).addClass("active")
                }
            })
            $(".checkboxAll").click(function() {
                if ($(".btn-checkbox").children(".active").length == $(".btn-checkbox").children().length) {
                    $(".btn-checkbox").children(".active").removeClass("active")
                } else {
                    $(".btn-checkbox").children().addClass("active")
                }
            })
        })
    </script>
</body>

</html>