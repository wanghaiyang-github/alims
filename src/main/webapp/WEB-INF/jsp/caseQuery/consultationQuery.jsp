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

<body>
    <ol class="breadcrumb">
        <li><a href="#">首页</a></li>
        <li><a href="#">案件委托登记</a></li>
        <li class="active">案件查询和补送</li>
    </ol>

    <div class="row Modular">
        <div class="col-md-12">
            <div class="panel panel-default">
                <div class="panel-body">
                    <div class="row">
                        <div class="col-md-4 seachInputBox">
                            <div class="form-group">
                                <label>项目类型</label>
                                <input type="text" class="form-control" placeholder="请输入项目类型">
                            </div>
                        </div>
                        <div class="col-md-4 seachInputBox">
                            <div class="form-group">
                                <label>被鉴定人</label>
                                <input type="text" class="form-control" placeholder="请输入被鉴定人">
                            </div>
                        </div>
                        <div class="col-md-4 seachInputBox">
                            <div class="form-group">
                                <label>委托时间</label>
                                <input type="text" class="form-control" placeholder="请输入委托时间">
                            </div>
                        </div>
                        <div class="col-md-4 seachInputBox">
                            <div class="form-group">
                                <label>受理状态</label>
                                <div class="select">
                                    <select class="form-control" required name="acceptingState">
                                        <option value="" disabled selected hidden>请选择受理状态</option>
                                        <option value="未受理" selected>未受理</option>
                                        <option value="已受理">已受理</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4 seachInputBox">
                            <div class="form-group seachButtonBox">
                                <button class="btn btn-blue">查询</button>
                                <button class="btn btn-blue-border">重置</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row Modular Accepted hidden">
        <div class="col-md-12">
            <div class="panel panel-default">
                <div class="panel-heading blue">
                    <div>已受理案件</div>
                </div>
                <div class="panel-body">
                    <table class="table table-hover table-bordered bigTable">
                        <thead>
                            <tr>
                                <th>序号</th>
                                <th>项目</th>
                                <th>委托时间</th>
                                <th>被鉴定人</th>
                                <th>简要案情</th>
                                <th>操作</th>
                                <th>下载</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>序号</td>
                                <td>项目</td>
                                <td>委托时间</td>
                                <td>被鉴定人</td>
                                <td>简要案情</td>
                                <td>
                                    <a href="../delegationRegistration/expertConsultation.html" target="ifm" class="btn-icon btn-icon-blue btn-icon-chakan-blue-bg  ">查看会诊结果</a>
                                </td>
                                <td>
                                    <button class="btn-font btn-font-green">合同表</button>
                                    <button class="btn-font btn-font-green">聘请书</button>
                                </td>
                            </tr>
                            <tr>
                                <td>序号</td>
                                <td>项目</td>
                                <td>委托时间</td>
                                <td>被鉴定人</td>
                                <td>简要案情</td>
                                <td>
                                    <a href="../delegationRegistration/expertConsultation.html" target="ifm" class="btn-icon btn-icon-blue btn-icon-chakan-blue-bg  ">查看会诊结果</a>
                                </td>
                                <td>
                                    <button class="btn-font btn-font-green">合同表</button>
                                    <button class="btn-font btn-font-green">聘请书</button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <div class="row Modular notAccepted">
        <div class="col-md-12">
            <div class="panel panel-default">
                <div class="panel-heading blue">
                    <div>未受理案件</div>
                </div>
                <div class="panel-body">
                    <table class="table table-hover table-bordered bigTable">
                        <thead>
                            <tr>
                                <th>序号</th>
                                <th>项目</th>
                                <th>委托时间</th>
                                <th>被鉴定人</th>
                                <th>简要案情</th>
                                <th>操作</th>
                                <th>下载</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>序号</td>
                                <td>项目</td>
                                <td>委托时间</td>
                                <td>被鉴定人</td>
                                <td>简要案情</td>
                                <td>
                                    <a href="../delegationRegistration/expertConsultation.html" target="ifm" class="btn-icon btn-icon-blue btn-icon-bianji-blue ">编辑</a>
                                    <button class="btn-icon btn-icon-red btn-icon-shanchu-red ">删除</button>
                                </td>
                                <td>
                                    <button class="btn-font btn-font-green">合同表</button>
                                    <button class="btn-font btn-font-green">聘请书</button>
                                </td>
                            </tr>
                            <tr>
                                <td>序号</td>
                                <td>项目</td>
                                <td>委托时间</td>
                                <td>被鉴定人</td>
                                <td>简要案情</td>
                                <td>
                                    <a href="../delegationRegistration/expertConsultation.html" target="ifm" class="btn-icon btn-icon-blue btn-icon-bianji-blue ">编辑</a>
                                    <button class="btn-icon btn-icon-red btn-icon-shanchu-red ">删除</button>
                                </td>
                                <td>
                                    <button class="btn-font btn-font-green">合同表</button>
                                    <button class="btn-font btn-font-green">聘请书</button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <%@ include file="../linkJs.jsp" %>
    <script>
        $(function() {
            $(".replacementTableShow").click(function() {
                $(this).siblings(".replacementTable").slideToggle("slow")
            })
            $("select[name='acceptingState']").change(function() {
                if ($(this).val() == "已受理") {
                    $(".notAccepted").addClass("hidden")
                    $(".Accepted").removeClass("hidden")
                } else {
                    $(".notAccepted").removeClass("hidden")
                    $(".Accepted").addClass("hidden")
                }
            })
        })
    </script>
</body>

</html>