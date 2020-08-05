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
        .starBox .radio input[type=radio],
        .starBox .radio-inline input[type=radio] {
            margin-top: 11px;
        }
    </style>

</head>
<div class="modal fade popBox smallBox" id="appraisalRequirementsBox" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">其他鉴定要求</h4>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <input type="text" class="form-control">
                </div>
            </div>
            <div class="modal-footer clearfix">
                <button class="btn btn-lang pull-left btn-blue addAppraisalRequirements" data-toggle="modal" data-target="#appraisalRequirementsTips">确认</button>
                <button type="button" class="btn btn-lang pull-right btn-blue-border" data-dismiss="modal">取消</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade popBox tips" id="appraisalRequirementsTips" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-body">
                是否保存为常用项
            </div>
            <div class="modal-footer clearfix">
                <button class="btn btn-lang pull-left btn-blue" data-dismiss="modal">是</button>
                <button type="button" class="btn btn-lang pull-right btn-blue-border" data-dismiss="modal">否</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade popBox xsBox" id="authenticatorBox" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">被鉴定人信息</h4>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <label>人员类型</label>
                    <div class="select">
                        <select class="form-control" required name="personnelType">
                            <option value="" disabled selected hidden>请选择人员类型</option>
                            <option value="违法犯罪人员">违法犯罪人员</option>
                            <option value="嫌疑人">嫌疑人</option>
                            <option value="嫌疑人亲属">嫌疑人亲属</option>
                            <option value="前科人员">前科人员</option>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label>人员名称</label>
                    <input type="text" class="form-control" placeholder="请输入人员名称" name="personnelName">
                </div>
                <div class="form-group">
                    <label>性别</label>
                    <label class="radio-inline radio-sex">
                        <input type="radio" class="sex sexman" value="男" name="sex">男性
                    </label>
                    <label class="radio-inline  radio-sex">
                        <input type="radio" class="sex sexwoman" value="女" name="sex">女性
                    </label>
                </div>
                <div class="form-group">
                    <label>年龄</label>
                    <input type="text" class="form-control" placeholder="请输入年龄" name="year">
                </div>
                <div class="form-group">
                    <label>住址</label>
                    <input type="text" class="form-control" placeholder="请输入住址" name="address">
                </div>
                <div class="form-group">
                    <label>身份证号</label>
                    <div class="row">
                        <div class="col-md-8 nopadding">
                            <input type="text" class="form-control" placeholder="请输入身份证号" name="idCard">
                        </div>
                        <div class="col-md-4 nopadding">
                            <label class="custom-control checkbox-inline">
                                <input class="custom-control-input" type="checkbox" name="noIdCheck">
                                <span class="custom-control-label">无</span>
                            </label>
                        </div>
                    </div>
                    <input type="text" class="form-control hidden" placeholder="请输入无身份证号原因" style="margin-top: 5px;" name="noIdCard">
                </div>
                <div class="form-group">
                    <label>亲缘关系</label>
                    <div class="select">
                        <select class="form-control" required name="kinship">
                            <option value="" disabled selected hidden>请选择亲缘关系</option>
                            <option value="父母">父母</option>
                            <option value="配偶">配偶</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="modal-footer clearfix">
                <input type="hidden" name="index">
                <button class="btn btn-lang btn-blue pull-left addAuthenticator">确认</button>
                <button type="button" class="btn btn-lang pull-right btn-blue-border" data-dismiss="modal">取消</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade popBox xsBox" id="urineBox" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">尿液检材信息</h4>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <label>检材类型</label>
                    <label class="radio-inline">
                        <input type="radio" class="rdo" value="尿液" name="personnelType" checked>尿液
                    </label>
                </div>
                <div class="form-group">
                    <label>毒品使用情况</label>
                    <input type="text" class="form-control" placeholder="请输入毒品使用情况" name="drugUse">
                </div>
                <div class="form-group">
                    <label>取尿地点</label>
                    <input type="text" class="form-control" placeholder="请输入取尿地点" name="urineSite">
                </div>
                <div class="form-group">
                    <label>取尿时间</label>
                    <input type="text" class="form-control" placeholder="请输入取尿时间" name="urineTime">
                </div>
                <div class="form-group">
                    <label>符合市局包装</label>
                    <label class="radio-inline">
                        <input type="radio" class="rdo" value="是" name="packing">是
                    </label>
                    <label class="radio-inline">
                        <input type="radio" class="rdo" value="否" name="packing">否
                    </label>
                </div>
                <div class="form-group">
                    <label>备注</label>
                    <input type="text" class="form-control" placeholder="请输入备注" name="remarks">
                </div>
            </div>
            <div class="modal-footer clearfix">
                <input type="hidden" name="index">
                <button class="btn btn-lang btn-blue pull-left addUrine">确认</button>
                <button type="button" class="btn btn-lang pull-right btn-blue-border" data-dismiss="modal">取消</button>
            </div>
        </div>
    </div>
</div>
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
        <li class="active">毒化鉴定</li>
    </ol>
    <div class="row Modular">
        <div class="col-md-6 starBox">
            <p class="starP">
                <i class="fa fa-star star" aria-hidden="true"></i>鉴定类别:<span>毒化鉴定</span>
            </p>
        </div>
        <div class="col-md-6 starBox" style="text-align: center;">
            <div class="form-group">
                <label class="radio-inline" style="margin-right: 60px !important;">
                    <input type="radio" class="rdo" name="poisonAppraisa" value="毒品">毒品类委托
                </label>
                <label class="radio-inline">
                    <input type="radio" class="rdo" name="poisonAppraisa" value="毒物">毒物类委托
                </label>
            </div>
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
                                    <li class="pull-left active">毒品检验（定性分析）</li>
                                    <li class="pull-left other" data-toggle="modal" data-target="#appraisalRequirementsBox">其他</li>
                                    <!-- <li class="pull-left">DNA检验及亲缘关系鉴定</li>
                                    <li class="pull-left">DNA检验，同一认定及亲缘关系鉴定</li>
                                    <li class="pull-left">DNA检验入库比对</li>
                                    <li class="pull-left">DNA检验鉴定与鉴定文书比对</li> -->
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
                    <button class="btn btn-blue" data-toggle="modal" data-target="#authenticatorBox">添加</button>
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
                        <tbody id="authenticatorTbody">

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
                    <div>尿液检材信息</div>
                    <button class="btn btn-blue" data-toggle="modal" data-target="#urineBox">添加</button>
                </div>
                <div class="panel-body nopadding">
                    <table class="table table-hover table-bordered bigTable">
                        <thead>
                            <tr>
                                <th>序号</th>
                                <th>检材类型</th>
                                <th>毒品使用情况</th>
                                <th>取尿地点</th>
                                <th>取尿时间</th>
                                <th>是否符合市局规定包装</th>
                                <th>备注</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>序号</td>
                                <td>检材类型</td>
                                <td>毒品使用情况</td>
                                <td>取尿地点</td>
                                <td>取尿时间</td>
                                <td><i class="fa fa-check-circle" aria-hidden="true" style="color:#00b39b"></i></td>
                                <td>备注</td>
                                <td>
                                    <button class="btn-icon btn-icon-blue btn-icon-bianji-blue ">编辑</button>
                                    <button class="btn-icon btn-icon-red btn-icon-shanchu-red ">删除</button>
                                </td>
                            </tr>
                            <tr>
                                <td>序号</td>
                                <td>检材类型</td>
                                <td>毒品使用情况</td>
                                <td>取尿地点</td>
                                <td>取尿时间</td>
                                <td><i class="fa fa-times-circle" aria-hidden="true" style="color:#ff5951"></i></td>
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
    <div class="row Modular">
        <div class="col-md-12">
            <div class="panel panel-default">
                <div class="panel-heading yellow">
                    <div>可疑物检材信息</div>
                    <button class="btn btn-blue">添加</button>
                </div>
                <div class="panel-body nopadding">
                    <table class="table table-hover table-bordered bigTable">
                        <thead>
                            <tr>
                                <th>序号</th>
                                <th>检材类型</th>
                                <th>颜色</th>
                                <th>份数</th>
                                <th>包装</th>
                                <th>性状</th>
                                <th>备注</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>序号</td>
                                <td>检材类型</td>
                                <td>颜色</td>
                                <td>份数</td>
                                <td>包装</td>
                                <td>性状</td>
                                <td>备注</td>
                                <td>
                                    <button class="btn-icon btn-icon-blue btn-icon-bianji-blue ">编辑</button>
                                    <button class="btn-icon btn-icon-red btn-icon-shanchu-red ">删除</button>
                                </td>
                            </tr>
                            <tr>
                                <td>序号</td>
                                <td>检材类型</td>
                                <td>颜色</td>
                                <td>份数</td>
                                <td>包装</td>
                                <td>性状</td>
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
                //鉴定要求其他保存
            $(".addAppraisalRequirements").click(function() {
                    $("#appraisalRequirementsBox").find("input").val()
                    $(".btn-checkbox").find(".other").before('<li class="pull-left active">' + $(
                        "#appraisalRequirementsBox").find("input").val() + '</li>')
                    $(".btn-checkbox").children().each(function(i) {
                        if (i > 0 && $(this).position().left == 0) {
                            $(this).css("margin-left", '0')
                        }
                    })
                    $('#appraisalRequirementsBox').modal('hide')
                })
                //鉴定要求其他关闭清空
            $('#appraisalRequirementsBox').on('hidden.bs.modal', function(e) {
                    $(".btn-checkbox").find(".other").removeClass("active")
                    $("#appraisalRequirementsBox").find("input").val("")
                })
                // 被鉴定人身份证切换
            $("#authenticatorBox").find("input[name='noIdCheck']").change(function() {
                    if ($(this).is(":checked")) {
                        $("#authenticatorBox").find("input[name='idCard']").val("").prop("disabled", true)
                        $("#authenticatorBox").find("input[name='noIdCard']").val("").removeClass("hidden")
                    } else {
                        $("#authenticatorBox").find("input[name='idCard']").val("").prop("disabled", false)
                        $("#authenticatorBox").find("input[name='noIdCard']").val("").addClass("hidden")
                    }
                })
                // 被鉴定人修改
            $("#authenticatorTbody").on('click', '.edit', function() {
                    var personnelType = $(this).parents("tr").find('input[name="personnelType"]').val(),
                        personnelName = $(this).parents("tr").find('input[name="personnelName"]').val(),
                        sex = $(this).parents("tr").find('input[name="sex"]').val(),
                        year = $(this).parents("tr").find('input[name="year"]').val(),
                        address = $(this).parents("tr").find('input[name="address"]').val(),
                        idCard = $(this).parents("tr").find('input[name="idCard"]').val(),
                        noIdCard = $(this).parents("tr").find('input[name="noIdCard"]').val(),
                        kinship = $(this).parents("tr").find('input[name="kinship"]').val(),
                        index = $(this).parents('tr').index();
                    if (noIdCard) {
                        $("#authenticatorBox").find("input[type='checkbox']").prop("checked", true)
                        $("#authenticatorBox").find("input[name='noIdCard']").removeClass("hidden")
                    }
                    $("#authenticatorBox").find('select[name="personnelType"]').val(personnelType)
                    $("#authenticatorBox").find('input[name="personnelName"]').val(personnelName)
                    $("#authenticatorBox").find('input[name="sex"][value="' + sex + '"]').prop("checked",
                        true)
                    $("#authenticatorBox").find('input[name="year"]').val(year)
                    $("#authenticatorBox").find('input[name="address"]').val(address)
                    $("#authenticatorBox").find('input[name="idCard"]').val(idCard)
                    $("#authenticatorBox").find('input[name="noIdCard"]').val(noIdCard)
                    $("#authenticatorBox").find('select[name="kinship"]').val(kinship)
                    $("#authenticatorBox").find('input[name="index"]').val(index)
                    $("#authenticatorBox").modal('show')
                })
                // 被鉴定人保存
            $(".addAuthenticator").click(function() {
                    var personnelType = $("#authenticatorBox").find('select[name="personnelType"]').val(),
                        personnelName = $("#authenticatorBox").find('input[name="personnelName"]').val(),
                        sex = $("#authenticatorBox").find('input[name="sex"]:checked').val(),
                        year = $("#authenticatorBox").find('input[name="year"]').val(),
                        address = $("#authenticatorBox").find('input[name="address"]').val(),
                        idCard = $("#authenticatorBox").find('input[name="idCard"]').val(),
                        noIdCard = $("#authenticatorBox").find('input[name="noIdCard"]').val(),
                        kinship = $("#authenticatorBox").find('select[name="kinship"]').val(),
                        index = $("#authenticatorBox").find('input[name="index"]').val(),
                        length = $("#authenticatorTbody").children().length,
                        nowId = '';
                    if (noIdCard !== "") {
                        nowId = '无身份证号(' + noIdCard + ')'
                    } else {
                        nowId = idCard
                    }
                    if (index != "") {
                        // 修改
                        var tr = $("#authenticatorTbody").children("tr").eq(index)
                        tr.find('input[name="personnelType"]').val(personnelType)
                        tr.find('input[name="personnelName"]').val(personnelName)
                        tr.find('input[name="sex"]').val(sex)
                        tr.find('input[name="year"]').val(year)
                        tr.find('input[name="address"]').val(address)
                        tr.find('input[name="idCard"]').val(idCard)
                        tr.find('input[name="noIdCard"]').val(noIdCard)
                        tr.find('input[name="kinship"]').val(kinship)
                        tr.children("td").each(function() {
                            if ($(this).children("input").length > 0) {
                                var tdInput = $(this).children("input").clone()
                                if (tdInput.length > 1) {
                                    if (tdInput.eq(0).val()) {
                                        $(this).html(tdInput.eq(0).val())
                                    } else {
                                        $(this).html('无身份证号(' + tdInput.eq(1).val() + ')')
                                    }
                                } else {
                                    $(this).html(tdInput.val())
                                }
                                $(this).append(tdInput)
                            }
                        })
                    } else {
                        // 新添
                        var newTr = '<tr>'
                        newTr += '<td>' + (length + 1) + '</td>'
                        newTr += '<td>' + personnelType +
                            '<input type="hidden" name="personnelType" value=' + personnelType + '></td>'
                        newTr += '<td>' + personnelName +
                            '<input type="hidden" name="personnelName" value=' + personnelName + '></td>'
                        newTr += '<td>' + sex + '<input type="hidden" name="sex" value=' + sex + '></td>'
                        newTr += '<td>' + year + '<input type="hidden" name="year" value=' + year +
                            '></td>'
                        newTr += '<td>' + address + '<input type="hidden" name="address" value=' + address +
                            '></td>'
                        newTr += '<td>' + nowId + '<input type="hidden" name="idCard" value=' + idCard +
                            '><inputtype = "hidden"name = "noIdCard"value = ' + noIdCard + '></td>'
                        newTr += '<td>' + kinship + '<input type="hidden" name="kinship" value=' + kinship +
                            '></td>'
                        newTr += '<td>'
                        newTr +=
                            '<button class="btn-icon btn-icon-blue btn-icon-bianji-blue edit">编辑</button>'
                        newTr +=
                            '<button class="btn-icon btn-icon-red btn-icon-shanchu-red remove">删除</button>'
                        newTr +=
                            '<button class="btn-icon btn-icon-yellow  btn-icon-chakan-yellow showTable">查看检材</button>'
                        newTr += '</td>'
                        newTr += '</tr>'
                        $("#authenticatorTbody").append(newTr)
                    }
                    $("#authenticatorBox").modal('hide')
                })
                //被鉴定人关闭清空
            $('#authenticatorBox').on('hidden.bs.modal', function(e) {
                    $("#authenticatorBox").find("input[type='text']").val("")
                    $("#authenticatorBox").find("input[type='hidden']").val("")
                    $("#authenticatorBox").find("input[type='radio']").prop("checked", false)
                    $("#authenticatorBox").find("input[type='checkbox']").prop("checked", false)
                    $("#authenticatorBox").find("input[name='noIdCard']").addClass("hidden")
                    $("#authenticatorBox").find("select").val("")
                })
                // 被鉴定人修改
            $("#authenticatorTbody").on('click', '.edit', function() {
                    var personnelType = $(this).parents("tr").find('input[name="personnelType"]').val(),
                        personnelName = $(this).parents("tr").find('input[name="personnelName"]').val(),
                        sex = $(this).parents("tr").find('input[name="sex"]').val(),
                        year = $(this).parents("tr").find('input[name="year"]').val(),
                        address = $(this).parents("tr").find('input[name="address"]').val(),
                        idCard = $(this).parents("tr").find('input[name="idCard"]').val(),
                        noIdCard = $(this).parents("tr").find('input[name="noIdCard"]').val(),
                        kinship = $(this).parents("tr").find('input[name="kinship"]').val(),
                        index = $(this).parents('tr').index();
                    if (noIdCard) {
                        $("#authenticatorBox").find("input[type='checkbox']").prop("checked", true)
                        $("#authenticatorBox").find("input[name='noIdCard']").removeClass("hidden")
                    }
                    $("#authenticatorBox").find('select[name="personnelType"]').val(personnelType)
                    $("#authenticatorBox").find('input[name="personnelName"]').val(personnelName)
                    $("#authenticatorBox").find('input[name="sex"][value="' + sex + '"]').prop("checked",
                        true)
                    $("#authenticatorBox").find('input[name="year"]').val(year)
                    $("#authenticatorBox").find('input[name="address"]').val(address)
                    $("#authenticatorBox").find('input[name="idCard"]').val(idCard)
                    $("#authenticatorBox").find('input[name="noIdCard"]').val(noIdCard)
                    $("#authenticatorBox").find('select[name="kinship"]').val(kinship)
                    $("#authenticatorBox").find('input[name="index"]').val(index)
                    $("#authenticatorBox").modal('show')
                })
                // 被鉴定人保存
            $(".addAuthenticator").click(function() {
                    var personnelType = $("#authenticatorBox").find('select[name="personnelType"]').val(),
                        personnelName = $("#authenticatorBox").find('input[name="personnelName"]').val(),
                        sex = $("#authenticatorBox").find('input[name="sex"]:checked').val(),
                        year = $("#authenticatorBox").find('input[name="year"]').val(),
                        address = $("#authenticatorBox").find('input[name="address"]').val(),
                        idCard = $("#authenticatorBox").find('input[name="idCard"]').val(),
                        noIdCard = $("#authenticatorBox").find('input[name="noIdCard"]').val(),
                        kinship = $("#authenticatorBox").find('select[name="kinship"]').val(),
                        index = $("#authenticatorBox").find('input[name="index"]').val(),
                        length = $("#authenticatorTbody").children().length,
                        nowId = '';
                    if (noIdCard !== "") {
                        nowId = '无身份证号(' + noIdCard + ')'
                    } else {
                        nowId = idCard
                    }
                    if (index != "") {
                        // 修改
                        var tr = $("#authenticatorTbody").children("tr").eq(index)
                        tr.find('input[name="personnelType"]').val(personnelType)
                        tr.find('input[name="personnelName"]').val(personnelName)
                        tr.find('input[name="sex"]').val(sex)
                        tr.find('input[name="year"]').val(year)
                        tr.find('input[name="address"]').val(address)
                        tr.find('input[name="idCard"]').val(idCard)
                        tr.find('input[name="noIdCard"]').val(noIdCard)
                        tr.find('input[name="kinship"]').val(kinship)
                        tr.children("td").each(function() {
                            if ($(this).children("input").length > 0) {
                                var tdInput = $(this).children("input").clone()
                                if (tdInput.length > 1) {
                                    if (tdInput.eq(0).val()) {
                                        $(this).html(tdInput.eq(0).val())
                                    } else {
                                        $(this).html('无身份证号(' + tdInput.eq(1).val() + ')')
                                    }
                                } else {
                                    $(this).html(tdInput.val())
                                }
                                $(this).append(tdInput)
                            }
                        })
                    } else {
                        // 新添
                        var newTr = '<tr>'
                        newTr += '<td>' + (length + 1) + '</td>'
                        newTr += '<td>' + personnelType +
                            '<input type="hidden" name="personnelType" value=' + personnelType + '></td>'
                        newTr += '<td>' + personnelName +
                            '<input type="hidden" name="personnelName" value=' + personnelName + '></td>'
                        newTr += '<td>' + sex + '<input type="hidden" name="sex" value=' + sex + '></td>'
                        newTr += '<td>' + year + '<input type="hidden" name="year" value=' + year +
                            '></td>'
                        newTr += '<td>' + address + '<input type="hidden" name="address" value=' + address +
                            '></td>'
                        newTr += '<td>' + nowId + '<input type="hidden" name="idCard" value=' + idCard +
                            '><inputtype = "hidden"name = "noIdCard"value = ' + noIdCard + ' ></td>'
                        newTr += '<td>' + kinship + '<input type="hidden" name="kinship" value=' + kinship +
                            '></td>'
                        newTr += '<td>'
                        newTr +=
                            '<button class="btn-icon btn-icon-blue btn-icon-bianji-blue edit">编辑</button>'
                        newTr +=
                            '<button class="btn-icon btn-icon-red btn-icon-shanchu-red remove">删除</button>'
                        newTr +=
                            '<button class="btn-icon btn-icon-yellow  btn-icon-chakan-yellow showTable">查看检材</button>'
                        newTr += '</td>'
                        newTr += '</tr>'
                        $("#authenticatorTbody").append(newTr)
                    }
                    $("#authenticatorBox").modal('hide')
                })
                //被鉴定人关闭清空
            $('#authenticatorBox').on('hidden.bs.modal', function(e) {
                $("#authenticatorBox").find("input[type='text']").val("")
                $("#authenticatorBox").find("input[type='hidden']").val("")
                $("#authenticatorBox").find("input[type='radio']").prop("checked", false)
                $("#authenticatorBox").find("input[type='checkbox']").prop("checked", false)
                $("#authenticatorBox").find("input[name='noIdCard']").addClass("hidden")
                $("#authenticatorBox").find("select").val("")
            })
        })
    </script>
</body>

</html>