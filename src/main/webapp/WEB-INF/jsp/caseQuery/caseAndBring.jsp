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
        .bu {
            background: #fddddb;
            color: #fc5a56;
            padding: 5px;
            border-radius: 50%;
            font-size: 10px;
            font-weight: 600;
        }

        /*.bigTable.table-bordered, .bigTable.table-bordered > tbody > tr > td {*/
            /*overflow: hidden;*/
            /*white-space: nowrap;*/
            /*text-overflow: ellipsis;*/
        /*}*/

    </style>
</head>

<body>
    <ol class="breadcrumb">
        <li><a href="javascript:void(0);" id="pageHome">首页</a></li>
        <li class="active">案件补送登记</li>
    </ol>

    <div class="row Modular">
        <div class="col-md-12">
            <div class="panel panel-default">
                <div class="panel-body">
                    <form id="consignationForm" action="<%=path %>/caseQuery/caseAndBring" class="form-horizontal tasi-form"
                          method="post">
                        <div class="row">
                        <div class="col-md-4 seachInputBox">
                            <div class="form-group">
                                <label>案件名称</label>
                                <input type="text" id="caseName" name="entity.caseName" value="${query.entity.caseName}" class="form-control" placeholder="请输入案件名称">
                            </div>
                        </div>
                        <div class="col-md-4 seachInputBox">
                            <div class="form-group">
                                <label>案件性质</label>
                                <%--<input type="text" class="form-control" placeholder="请输入案件性质">--%>
                                <div class="select">
                                    <select id="caseProperty" name="entity.caseProperty" value="${query.entity.caseProperty}" class="form-control">
                                        <option value="">全部</option>
                                        <c:forEach items="${casePropertyList}" var="list" varStatus="cs">
                                            <option value="${list.dictCode}"
                                                    <c:if test="${list.dictCode eq query.entity.caseProperty}">selected</c:if>>${list.dictName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4 seachInputBox" style="display: none">
                            <div class="form-group">
                                <label>案件类型</label>
                                <%--<input type="text" class="form-control" placeholder="请输入案件类型">--%>
                                <div class="select">
                                    <select id="caseType" name="entity.caseType" value="${query.entity.caseType}" class="form-control">
                                        <option value="">全部</option>
                                        <c:forEach items="${caseTypeList}" var="list" varStatus="cs">
                                            <option value="${list.dictCode}"
                                                    <c:if test="${list.dictCode eq query.entity.caseType}">selected</c:if>>${list.dictName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4 seachInputBox">
                            <div class="form-group">
                                <label>案件编号</label>
                                <input type="text" id="caseNo" name="entity.caseNo" value="${query.entity.caseNo}" class="form-control" placeholder="请输入案件编号">
                            </div>
                        </div>
                        <div class="col-md-4 seachInputBox">
                            <div class="form-group">
                                <label>委托开始时间</label>
                                <input type="text" name="delegateStartDatetime" id="delegateStartDatetime" value="<fmt:formatDate value="${query.delegateStartDatetime}" pattern="yyyy-MM-dd"/>" class="form-control form_datetime" placeholder="请输入委托开始时间" readonly>
                            </div>
                        </div>
                        <div  class="col-md-4 seachInputBox">
                            <div class="form-group">
                                <label>委托结束时间</label>
                                <input type="text" class="form-control form_datetime" id="delegateEndDatetime" name="delegateEndDatetime" value="<fmt:formatDate value="${query.delegateEndDatetime}" pattern="yyyy-MM-dd"/>" placeholder="请输入委托结束时间" readonly>
                            </div>
                        </div>
                        <div class="col-md-4 seachInputBox">
                            <div class="form-group">
                                <label>委托人</label>
                                <input type="text" id="delegator1Name" name="delegator1Name" value="${query.delegator1Name}" class="form-control" placeholder="请输入委托人">
                            </div>
                        </div>
                        <div class="col-md-4 seachInputBox">
                            <div class="form-group">
                                <label>现场勘验号</label>
                                <input type="text" id="caseXkNo" name="entity.caseXkNo" value="${query.entity.caseXkNo}" class="form-control" placeholder="请输入现场勘验号">
                            </div>
                        </div>
                        <div class="col-md-4 seachInputBox">
                            <div class="form-group">
                                <label>受理状态</label>
                                <div class="select">
                                    <select id="caseStatus" name="status" value="${query.status}" class="form-control">
                                        <option value="02" <c:if test="${query.status eq '02'}">selected</c:if>>在检验</option>
                                        <option value="03" <c:if test="${query.status eq '03'}">selected</c:if>>已完成</option>

                                    </select>
                                </div>

                            </div>
                        </div>
                        <div class="col-md-4 seachInputBox">
                            <div class="form-group">
                                <label>鉴定中心</label>
                                <div class="select">
                                    <select id="acceptOrgId" name="acceptOrgId" value="${query.acceptOrgId}" class="form-control">
                                        <option value="">全部</option>
                                        <c:forEach items="${orgInfos}" var="list" varStatus="cs">
                                            <option value="${list.orgCode}"
                                                    <c:if test="${list.orgCode eq query.acceptOrgId}">selected</c:if>>${list.orgQualification}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4 seachInputBox">
                            <div class="form-group seachButtonBox">
                                <button class="btn btn-blue" type="submit" id="addInformant">查询</button>
                                <button type="button" name="reset" class="btn btn-blue-border">重置</button>
                            </div>
                        </div>
                    </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div class="row Modular Accepted">
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
                                <th>案件名称</th>
                                <th>案件编号</th>
                                <%--<th>案件类别</th>--%>
                                <th>案件性质</th>
                                <th>案发时间</th>
                                <th>委托时间</th>
                                <th>案件状态</th>
                                <th>受理时间</th>
                                <th>委托人</th>
                                <th>鉴定中心</th>
                                <th>补送</th>
                                <th>下载</th>
                            </tr>
                        </thead>
                        <tbody id="consignatioInfoListTbody1">
                            <c:forEach items="${caseInfoList}" var="caseInfo" varStatus="s">
                                <tr>
                                    <td>${s.count}</td>
                                    <td>${caseInfo.entity.caseName}</td>
                                    <td>${caseInfo.entity.caseNo}</td>
                                    <%--<td>${caseInfo.caseTypeName}</td>--%>
                                    <td>${caseInfo.casePropertyName}</td>
                                    <td><fmt:formatDate value="${caseInfo.entity.caseDatetime}" pattern="yyyy-MM-dd"/></td>
                                    <td><fmt:formatDate value='${caseInfo.delegateDatetime}' pattern='yyyy-MM-dd'/></td>
                                    <td>${caseInfo.caseStatusName}</td>
                                    <td><fmt:formatDate value='${caseInfo.acceptDatetime}' pattern='yyyy-MM-dd'/></td>
                                    <td>${caseInfo.delegator1Name},${caseInfo.delegator2Name}</td>
                                    <td>${caseInfo.orgQualification}</td>
                                    <td>
                                        <input type="hidden" name="consignmentId" value="${caseInfo.consignmentId}">
                                        <input type="hidden" name="caseId" value="${caseInfo.entity.caseId}">
                                        <input type="hidden" name="caseXkNo" value="${caseInfo.entity.caseXkNo}">
                                        <input type="hidden" id="entrustStatus" name="entrustStatus" value="${caseInfo.entity.entrustStatus}">
                                        <%--<a href="replacement.html" target="ifm" class="btn-icon btn-icon-blue btn-icon-addBusong-blue ">添加补送</a>--%>
                                        <%--<button class="btn-icon btn-icon-blue btn-icon-addBusong-blue " type="button" name="replacementBtn">补送</button>
                                        <button type="button" name="queryBtn" class="btn-icon btn-icon-yellow btn-icon-busong-yellow replacementTableShow">补送记录</button>--%>
                                        <button class="btn-font btn-font-blue" name="replacementBtn">补送</button>
                                        <button class="btn-font btn-font-blue" name="queryBtn">补送记录
                                            <c:choose>
                                                <c:when test="${not empty caseInfo.entity.replacementNum}">
                                                    <i class="fa bu">(${caseInfo.entity.replacementNum})</i>
                                                </c:when>
                                                <c:otherwise>
                                                    <i class="fa bu">(0)</i>
                                                </c:otherwise>
                                            </c:choose>
                                        </button>
                                        <div class="replacementTable" id="lsit">
                                            <div class="triangle"></div>
                                        </div>
                                    </td>
                                    <td>
                                        <button class="btn-font btn-font-green" name="delegateDocBtn">鉴定委托书</button>
                                        <button class="btn-font btn-font-green" name="employDocBtn">聘请书</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <div class="row" style="padding: 0px">
                        <div class="col-md-12">
                            <div id="kkpager"></div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
    <%@ include file="../linkJs.jsp" %>
    <script src="<%=path%>/js/entrustCurrency.js"></script>
    <script>
        $(function() {


            kkpager.generPageHtml({
                pno: ${pageInfo.page},
                //总页码
                total: ${pageInfo.pageCount},
                //总数据条数
                totalRecords: ${pageInfo.allRecordCount},
                //链接前部
                hrefFormer: '<%=path%>/caseQuery/caseAndBring',
                //链接尾部
                //hrefLatter: '.html',
                getLink: function (page) {
                    return this.hrefFormer + this.hrefLatter + "?" + "page=" + page + "&" + $("#consignationForm").serialize();
                }
                , lang: {
                    firstPageText: '首页',
                    firstPageTipText: '首页',
                    lastPageText: '尾页',
                    lastPageTipText: '尾页',
                    prePageText: '上一页',
                    prePageTipText: '上一页',
                    nextPageText: '下一页',
                    nextPageTipText: '下一页',
                    totalPageBeforeText: '共',
                    totalPageAfterText: '页',
                    currPageBeforeText: '当前第',
                    currPageAfterText: '页',
                    totalInfoSplitStr: '/',
                    totalRecordsBeforeText: '共',
                    totalRecordsAfterText: '条数据',
                    gopageBeforeText: '&nbsp;转到',
                    gopageButtonOkText: '确定',
                    gopageAfterText: '页',
                    buttonTipBeforeText: '第',
                    buttonTipAfterText: '页'
                }
            });


            $(".replacementTableShow").click(function() {
                $(this).siblings(".replacementTable").slideToggle("slow")
            })

            $('.form_datetime').datetimepicker({
                format: 'yyyy-mm-dd',
                language: 'zh',
                weekStart: 1,
                todayBtn: 1,
                minView: "month",
                autoclose: true,
                todayHighlight: true,
                forceParse: 0,
                showMeridian: 1
            }).on('changeDate', function (ev) {
                $(this).datetimepicker('hide');
            });

            //删除
            $("button[name='delBtn']", "#consignatioInfoListTbody").on("click", function () {
                var consignmentId = $("input[name='consignmentId']", $(this).parent("td")).val();
                var caseId = $("input[name='caseId']", $(this).parent("td")).val();
                $.ajax({
                    url: "<%=path%>/caseQuery/delCaseAndBring",
                    type: "post",
                    data: {
                        consignmentId: consignmentId,
                        caseId: caseId
                    },
                    dataType: "json",
                    success: function (data) {
                        if (data.code == 0) {
                            alert(data.message)
                            window.location.reload();
                        }
                    },
                    error: function (e) {
                        alert(e);
                    }
                });
            });

            //修改委托登记
            $("button[name='editBtn']", "#consignatioInfoListTbody").on("click", function () {
                var consignmentId = $("input[name='consignmentId']", $(this).parent("td")).val();
                var caseId = $("input[name='caseId']", $(this).parent("td")).val();
                location.href = "<%=path%>/caseQuery/editCaseAndBring?consignmentId=" + consignmentId + "&caseId=" + caseId;
            });

            //委托书下载
            $("button[name='delegateDocBtn']", "#consignatioInfoListTbody").on("click",function(){
                var consignmentId = $("input[name='consignmentId']", $(this).parent("td").parent("tr")).val();
                location.href="<%=path%>/DocDownload/delegateDoc?consignmentId=" + consignmentId;
            });

            //聘请书下载
            $("button[name='employDocBtn']", "#consignatioInfoListTbody").on("click",function(){
                var consignmentId = $("input[name='consignmentId']", $(this).parent("td").parent("tr")).val();
                location.href="<%=path%>/DocDownload/employDoc?consignmentId=" + consignmentId;
            });

            //合同书下载已受理
            $("button[name='delegateDocBtn']", "#consignatioInfoListTbody1").on("click",function(){
                var consignmentId = $("input[name='consignmentId']", $(this).parent("td").parent("tr")).val();
                location.href="<%=path%>/DocDownload/delegateDoc?consignmentId=" + consignmentId;
            });

            //聘请书下载已受理
            $("button[name='employDocBtn']", "#consignatioInfoListTbody1").on("click",function(){
                var consignmentId = $("input[name='consignmentId']", $(this).parent("td").parent("tr")).val();
                location.href="<%=path%>/DocDownload/employDoc?consignmentId=" + consignmentId;
            });

            //添加补送
            $("button[name='replacementBtn']").on("click",function(){
                var caseId = $("input[name='caseId']", $(this).parent("td").parent("tr")).val();
                var caseXkNo = $("input[name='caseXkNo']", $(this).parent("td").parent("tr")).val();
                var entrustStatus = $("input[name='entrustStatus']", $(this).parent("td").parent("tr")).val();
                /*if(!caseXkNo){
                    location.href="<%=path%>/caseQuery/replacementNonCaseByCaseId?caseId=" + caseId;
                }else{
                    location.href="<%=path%>/caseQuery/replacementByCaseId?caseId=" + caseId;
                }*/

                if (entrustStatus == '0') {
                    //现场委托
                    location.href="<%=path%>/caseQuery/replacementByCaseId?caseId=" + caseId;
                }else if(entrustStatus == '1') {
                    //非现场委托
                    location.href="<%=path%>/caseQuery/replacementNonCaseByCaseId?caseId=" + caseId;
                }else if (entrustStatus == '2') {
                    //在逃人员委托
                    location.href="<%=path%>/caseQuery/replacementFugitivesCaseByCaseId?caseId=" + caseId;
                }else {
                    if(!caseXkNo){
                        location.href="<%=path%>/caseQuery/replacementNonCaseByCaseId?caseId=" + caseId;
                    }else{
                        location.href="<%=path%>/caseQuery/replacementByCaseId?caseId=" + caseId;
                    }
                }
            })

            $("body").click(function(){
                $(".replacementTable").slideUp("slow")
            })
            //点击补送记录按钮 （查询补送记录信息）
            $("button[name='queryBtn']").on("click",function(){
                var caseId = $("input[name='caseId']", $(this).parent("td").parent("tr")).val();
                var that = $(this)
                $.ajax({
                    url: "<%=path%>/caseQuery/queryReplacementRecord",
                    type: "post",
                    data: {
                        caseId: caseId
                    },
                    dataType: "json",
                    success: function (data) {
                        //返回补送记录信息  foreac循环拼接数据
                        var $div=$('.replacementTable');
                        var html='<div class="triangle"></div>';
                        if (data.code == 0) {
                            if(data.limsCaseInfoVoList.length>0) {
                                $.each(data.limsCaseInfoVoList, function (index, obj){
                                    html += '<ul class="clearfix">';
                                    html += '<li>' + '<span>'+(index + 1)+'</span>' + '</li>'
                                    /*html += '<li>' + "补送类型:"+'<span>' + "" +'</span>' + '</li>'*/
                                    html += '<li>' + "补送人:"+'<span>' + obj.delegator1Name + "," + obj.delegator2Name +'</span>' + '</li>'
                                    html += '<li>' + "补送时间:"+'<span>'+obj.delegateDatetime+'</span>' + '</li>'
                                    html += '<li>' + "受理状态:"+'<span>'+obj.caseStatusName+'</span>' + '</li>'
                                    html += '<li>' + '<input type="hidden" id="consignmentId'+index+'" name="consignmentId" value="' + obj.consignmentId + '"/></li>'
                                    html += '<li>' + '<input type="hidden" id="caseId'+index+'" name="caseId" value="' + obj.entity.caseId + '"/></li>'
                                    html += '<li>' + '<input type="hidden" id="caseXkNo'+index+'" name="caseXkNo" value="' + obj.entity.caseXkNo + '"/></li>'
                                    html += '<li>' + '<button type="button" onclick="queryDetail('+index+');" class="btn btn-blue">查看详情</button>' + '</li>'
                                    html += '</ul>';
                                });
                                $div.html(html);
                            }else{
                                html += '<ul class="clearfix">';
                                html += '<li>' +"暂无补送记录"+'</li>'
                                html += '</ul>';
                                $div.html(html);
                            }
                            that.siblings(".replacementTable").slideToggle("slow")
                        }
                    },
                    error: function (e) {
                        alert(e);
                    }
                });
            })

            //重置
            $("button[name='reset']").on("click",function(){
                location.href = "<%=path%>/caseQuery/caseAndBring";
            })

        })
        //查询补送记录详情
        function queryDetail(index){
            var consignmentId = $("#consignmentId"+index).val();
            var caseId = $("#caseId"+index).val();
            var caseXkNo = $("#caseXkNo"+index).val();
            if(caseXkNo=="null"){
                location.href = "<%=path%>/caseQuery/queryNonCaseReplacementRecordDetail?consignmentId=" + consignmentId + "&caseId=" + caseId;
            }else{
                location.href = "<%=path%>/caseQuery/queryReplacementRecordDetail?consignmentId=" + consignmentId + "&caseId=" + caseId;
            }
        }

        $("#pageHome").on("click",function () {
            window.location="<%=path%>/main/home";
            parent.$("#bs-example-navbar-collapse-1").children().eq(1).children().eq(0).addClass("active").siblings().removeClass("active")
        });
    </script>
</body>

</html>