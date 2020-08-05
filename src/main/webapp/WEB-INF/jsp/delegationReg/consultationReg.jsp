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
        .checkedBigBox .form-group {
            padding-top: 20px;
            border-top: 1px dashed #e8e8e8;
            margin-top: 20px;
        }
        
        .checkedBigBox button {
            margin-left: 10px;
        }
    </style>
</head>

<body>
    <ol class="breadcrumb">
        <li><a href="#">首页</a></li>
        <li><a href="#">案件委托登记</a></li>
        <li class="active">专家会诊委托</li>
    </ol>
    <div class="row Modular">
        <div class="col-md-12">
            <div class="panel panel-default">
                <div class="panel-heading blue">
                    <div>委托信息</div>
                </div>
                <div class="panel-body">
                    <div class="row inputBox">
                        <div class="col-md-12" style="font-size:17px;color:#ff5951;padding-bottom: 15px;">
                            申请指导/会诊单位（法医）— —<span>昌平分局刑侦支队</span>
                        </div>
                    </div>
                    <div class="row inputBox">
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>被鉴定人</label>
                                <i class="required">*</i>
                                <input type="text" class="form-control" placeholder="请输入姓名">
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>年龄</label>
                                <input type="text" class="form-control" placeholder="请输入年龄">
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group" style="margin-top: 30px;">
                                <label>性别</label>
                                <label class="radio-inline radio-sex">
                                    <input type="radio" class="sex sexman" value="男" name="sex">男性
                                </label>
                                <label class="radio-inline  radio-sex">
                                    <input type="radio" class="sex sexwoman" value="女" name="sex">女性
                                </label>
                            </div>
                        </div>
                        <div class="col-md-8">
                            <div class="form-group">
                                <label>简要案情</label>
                                <textarea class="form-control" rows="3" placeholder="请输入简要案情"></textarea>
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
                <div class="panel-heading yellow">
                    <div>要求指导/会诊项目</div>
                    <label class="radio-inline">
                        <input type="radio" class="rdo" name="expertConsultation" value="临床委托" checked>临床委托
                    </label>
                    <label class="radio-inline">
                        <input type="radio" class="rdo" name="expertConsultation" value="尸检指导">尸检指导
                    </label>
                </div>
                <div class="panel-body">
                    <div class="row inputBox clinical">
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>原鉴定结论</label>
                                <i class="required">*</i>
                                <textarea class="form-control" rows="3" placeholder="请描述"></textarea>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>临床室接案人员鉴定意见</label>
                                <textarea class="form-control" rows="3" placeholder="请描述"></textarea>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>专家会诊意见</label>
                                <textarea class="form-control" rows="3" placeholder="请描述"></textarea>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>会诊结果</label>
                                <textarea class="form-control" rows="3" placeholder="请描述"></textarea>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>备注</label>
                                <textarea class="form-control" rows="3" placeholder="请描述"></textarea>
                            </div>
                        </div>
                    </div>
                    <div class="row inputBox autopsy hidden">
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>申请法医姓名</label>
                                <i class="required">*</i>
                                <input type="text" class="form-control" placeholder="请输入姓名">
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>申请法医联系方式</label>
                                <input type="text" class="form-control" placeholder="请输入联系方式">
                            </div>
                        </div>
                    </div>
                    <div class="row inputBox autopsy checkedBigBox hidden">
                        <div class="col-md-12">
                            <label>指导要求 <button class="btn btn-blue-border checkboxAll">全选</button> </label>
                            <div class="form-group">
                                <ul class="btn-checkbox clearfix">
                                    <li class="pull-left">死因</li>
                                    <li class="pull-left">死亡方式</li>
                                    <li class="pull-left">损伤成因</li>
                                    <li class="pull-left">致伤物推断</li>
                                    <li class="pull-left">病理学检验</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="row inputBox autopsy checkedBigBox hidden">
                        <div class="col-md-12">
                            <label>送检材料 <button class="btn btn-blue-border checkboxAll">全选</button> </label>
                            <div class="form-group">
                                <ul class="btn-checkbox clearfix">
                                    <li class="pull-left">尸体</li>
                                    <li class="pull-left">病历</li>
                                    <li class="pull-left">现场照片</li>
                                    <li class="pull-left">尸检照片</li>
                                    <li class="pull-left">毒化报告</li>
                                    <li class="pull-left">X片</li>
                                    <li class="pull-left">CT片</li>
                                    <li class="pull-left">询问笔录</li>
                                    <li class="pull-left">视频监控</li>
                                    <li class="pull-left">调查访问情况</li>
                                    <li class="pull-left">情况</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="row inputBox  autopsy checkedBigBox hidden">
                        <div class="col-md-12">
                            <label>送检材料 <button class="btn btn-blue-border checkboxAll">全选</button> </label>
                            <div class="form-group">
                                <ul class="btn-checkbox clearfix">
                                    <li class="pull-left">尸体</li>
                                    <li class="pull-left">病历</li>
                                    <li class="pull-left">现场照片</li>
                                    <li class="pull-left">尸检照片</li>
                                    <li class="pull-left">毒化报告</li>
                                    <li class="pull-left">X片</li>
                                    <li class="pull-left">CT片</li>
                                    <li class="pull-left">询问笔录</li>
                                    <li class="pull-left">视频监控</li>
                                    <li class="pull-left">调查访问情况</li>
                                    <li class="pull-left">情况</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <%@ include file="../linkJs.jsp" %>
    <script>
        $(function() {

            $(".btn-checkbox").children().click(function() {
                if ($(this).hasClass("active")) {
                    $(this).removeClass("active")
                } else {
                    $(this).addClass("active")
                }
            })
            $(".checkboxAll").click(function() {
                if ($(this).parents(".col-md-12").eq(0).find(".btn-checkbox").children(".active").length ==
                    $(this).parents(".col-md-12").eq(0).find(".btn-checkbox").children().length) {
                    $(this).parents(".col-md-12").eq(0).find(".btn-checkbox").children(".active").removeClass(
                        "active")
                } else {
                    $(this).parents(".col-md-12").eq(0).find(".btn-checkbox").children().addClass(
                        "active")
                }
            })
            $("input[name='expertConsultation']").change(function() {
                if ($(this).val() == "尸检指导") {
                    $('.autopsy').removeClass("hidden")
                    $(".clinical").addClass("hidden")
                    $(".btn-checkbox").children().each(function(i) {
                        if (i > 0 && $(this).position().left == 0) {
                            $(this).css("margin-left", '0')
                        }
                    })
                } else {
                    $('.autopsy').addClass("hidden")
                    $(".clinical").removeClass("hidden")
                }
            })
        })
    </script>
</body>

</html>