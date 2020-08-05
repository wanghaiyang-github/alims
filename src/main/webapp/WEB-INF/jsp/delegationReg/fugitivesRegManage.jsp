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

        .bigTable.table-bordered, .bigTable.table-bordered > tbody > tr > td {
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
        }#personModal {
             text-align: center;
         }

        #personModal > div {
            width: 480px;
            margin: 0 auto;
            margin-top: 20px;
        }

        #personModal .modal-content {
            position: static;
            float: left;
        }

        #personModal .form-group {
            margin-bottom: 5px;
        }

        #personModal .modal-content {
            width: 480px;
            top: 20px;
            text-align: left;
        }

        #personModal .sampleBox {
            width: 720px;
            top: -519px;
            display: none;
        }

        #personModal .modal-content .modal-header {
            text-align: center;
        }

        #personModal .personBox .modal-header h4 {
            font-size: 17px;
            color: #007ef9;
            font-weight: 600;
        }

        #personModal .sampleBox .modal-header h4 {
            font-size: 17px;
            color: #ffa400;
            font-weight: 600;
        }

        #personModal .personBox .modal-body {
            padding: 13px 50px;
            padding-bottom: 0px;
            max-height: 492px;
            overflow-y: auto;
        }

        #personModal .sampleBox .modal-body {
            padding: 13px 50px;
        }

        #personModal .modal-content .modal-footer {
            text-align: center;
            border: none;
            padding-top: 0px;
        }

        #personModal .modal-content .modal-footer div {
            margin-bottom: 10px;
        }

    </style>
</head>

<body>
<ol class="breadcrumb">
    <li><a href="javascript:void(0);" id="pageHome">首页</a></li>
    <li class="active">在逃人员管理</li>
</ol>

<div class="row Modular">
    <div class="col-md-12">
        <div class="panel panel-default">
            <div class="panel-body">
                <form id="consignationForm" action="<%=path %>/delegate/fugitivesRegManage" class="form-horizontal tasi-form"
                      method="post">
                    <div class="row">
                        <div class="col-md-4 seachInputBox" style="width: 24.333333%;">
                            <div class="form-group">
                                <label>人员名称</label>
                                <input type="text" id="personName" name="entity.personName"
                                       class="form-control" placeholder="请输入人员名称">
                            </div>
                        </div>
                        <div class="col-md-4 seachInputBox" style="width: 24.333333%;">
                            <div class="form-group">
                                <label>身份证号</label>
                                <input type="text" id="personCard" name="personCard"
                                       class="form-control" placeholder="请输入身份证号">
                            </div>
                        </div>
                        <div class="col-md-4 seachInputBox" style="width: 24.333333%;">
                            <div class="form-group">
                                <label>逃犯编号</label>
                                <input type="text" id="fugitiveNo" name="entity.fugitiveNo"
                                       class="form-control" placeholder="请输入逃犯编号">
                            </div>
                        </div>
                        <div class="col-md-4 seachInputBox" style="width: 24.333333%;">
                            <div class="form-group">
                                <label>涉案编号</label>
                                <input type="text" id="involvedNo" name="entity.involvedNo"
                                       class="form-control" placeholder="请输入涉案编号">
                            </div>
                        </div>
                        <div class="col-md-4 seachInputBox" style="width: 24.333333%;">
                            <div class="form-group">
                                <label>涉案名称</label>
                                <input type="text" id="involvedName" name="entity.involvedName"
                                       class="form-control" placeholder="请输入涉案名称">
                            </div>
                        </div>
                        <div class="col-md-4 seachInputBox" style="width: 24.333333%;">
                            <div class="form-group seachButtonBox">
                                <button class="btn btn-blue" type="submit" id="searchBtn">查询</button>
                                <button type="button" name="reset" class="btn btn-blue-border">重置</button>
                            </div>
                        </div>
                        <div class="col-md-4 seachInputBox" style="width: 24.333333%;">
                            <div class="form-group seachButtonBox">
                                <button class="btn btn-blue" type="button" id="addBtn">添加</button>
                                <button class="btn btn-blue" type="button" id="importBtn">导入在逃人员</button>
                                <input type="file" name="upFile" id="upFile" multiple="multiple" class="hide"/>
                                <input type="hidden" name='textfield' id='textfield' readonly="readonly"/>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<!--添加在逃人员样本-->
<div class="modal fade" id="personModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="clearfix">
        <form action="">
            <div class="modal-content personBox">
                <div class="modal-header">
                    <h4 class="modal-title">在逃人员信息</h4>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label>人员名称</label>
                        <input type="text" class="form-control" placeholder="请输入人员名称"
                               name="personNameModel" id="personNameModel">
                    </div>
                    <div class="form-group">
                        <label>人员类型</label>
                        <div class="select">
                            <select class="form-control" required name="personTypeModel" id="personTypeModel">
                                <option value="" disabled selected hidden>请选择人员类型</option>
                                <c:forEach items="${personTypeList}" var="list">
                                    <option value="${list.dictCode}">${list.dictName}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label>性别</label>
                        <label class="radio-inline radio-sex">
                            <input type="radio" class="sex sexman" value="01" name="genderModel" checked>男性
                        </label>
                        <label class="radio-inline  radio-sex">
                            <input type="radio" class="sex sexwoman" value="02" name="genderModel">女性
                        </label>
                    </div>
                    <div class="form-group">
                        <label>身份证号</label>
                        <input type="text" class="form-control" placeholder="请输入身份证号" name="personCardModel" id="personCardModel">
                    </div>
                    <div class="form-group">
                        <label>年龄</label>
                        <input type="text" class="form-control" placeholder="请输入年龄" name="ageModel"
                               id="ageModel">
                    </div>
                    <div class="form-group">
                        <label>在逃编号</label>
                        <input type="text" class="form-control" placeholder="请输入在逃编号" name="fugitiveNoModel"
                               id="fugitiveNoModel">
                    </div>
                    <div class="form-group">
                        <label>涉案编号</label>
                        <input type="text" class="form-control" placeholder="请输入涉案编号" name="involvedNoModel"
                               id="involvedNoModel">
                    </div>
                    <div class="form-group">
                        <label>涉案名称</label>
                        <input type="text" class="form-control" placeholder="请输入在逃编号" name="involvedNameModel"
                               id="involvedNameModel">
                    </div>
                </div>
                <div class="modal-footer">
                    <input type="hidden" id="idModel" name="idModel">
                    <button type="button" class="btn btn-blue btn-lang addAuthenticator" name="confirmButton">确认</button>
                    <button type="button" class="btn btn-blue-border  btn-lang" data-dismiss="modal">取消</button>
                </div>
            </div>
        </form>
    </div>
</div>
<!--重复的在逃人员信息-->
<div class="modal fade" id="repeatPersonModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static">
    <div class="clearfix">
        <form action="">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">重复的在逃人员列表</h4>
                </div>
                <div class="modal-body">
                    <table class="table table-hover table-bordered bigTable">
                        <thead>
                        <tr>
                            <th>序号</th>
                            <th>人员名称</th>
                            <th>身份证号</th>
                            <th>涉案编号</th>
                            <th>涉案名称</th>
                        </tr>
                        </thead>
                        <tbody id="listRepeatTbody">
                        </tbody>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-blue btn-lang" id="repeatConfirmBtn">确 定</button>
                </div>
            </div>
        </form>
    </div>
</div>
<div class="row Modular notAccepted">
    <div class="col-md-12">
        <div class="panel panel-default">
            <div class="panel-heading blue">
                <div>在逃人员列表</div>
            </div>
            <div class="panel-body">
                <table class="table table-hover table-bordered bigTable">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>人员名称</th>
                        <th>人员类型</th>
                        <th>性别</th>
                        <th>年龄</th>
                        <th>身份证号</th>
                        <th>逃犯编号</th>
                        <th>涉案编号</th>
                        <th>涉案名称</th>
                        <th>创建时间</th>
                        <th>创建人</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody id="listTbody">
                    <c:forEach items="${fugitivesInfoVoList}" var="fugitivesInfoVo" varStatus="s">
                        <tr>
                            <td>${s.count}</td>
                            <td>${fugitivesInfoVo.entity.personName}</td>
                            <td>${fugitivesInfoVo.personTypeName}</td>
                            <td>${fugitivesInfoVo.personGenderName}</td>
                            <td>${fugitivesInfoVo.entity.personAge}</td>
                            <td>${fugitivesInfoVo.entity.personCard}</td>
                            <td>${fugitivesInfoVo.entity.fugitiveNo}</td>
                            <td>${fugitivesInfoVo.entity.involvedNo}</td>
                            <td>${fugitivesInfoVo.entity.involvedName}</td>
                            <td><fmt:formatDate value="${fugitivesInfoVo.entity.createDatetime}" pattern="yyyy-MM-dd"/></td>
                            <td>${fugitivesInfoVo.entity.createPerson}</td>
                            <td>
                                <input type="hidden" name="id" value="${fugitivesInfoVo.entity.id}">
                                <input type="hidden" name="personName" value="${fugitivesInfoVo.entity.personName}">
                                <input type="hidden" name="personType" value="${fugitivesInfoVo.entity.personType}">
                                <input type="hidden" name="personGender" value="${fugitivesInfoVo.entity.personGender}">
                                <input type="hidden" name="personAge" value="${fugitivesInfoVo.entity.personAge}">
                                <input type="hidden" name="personCard" value="${fugitivesInfoVo.entity.personCard}">
                                <input type="hidden" name="fugitiveNo" value="${fugitivesInfoVo.entity.fugitiveNo}">
                                <input type="hidden" name="involvedNo" value="${fugitivesInfoVo.entity.involvedNo}">
                                <input type="hidden" name="involvedName" value="${fugitivesInfoVo.entity.involvedName}">
                                <button type="button" name="editBtn"
                                        class="btn-icon btn-icon-blue btn-icon-bianji-blue ">编辑
                                </button>
                                <button type="button" name="delBtn"
                                        class="btn-icon btn-icon-red btn-icon-shanchu-red ">删除
                                </button>
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
<script>
    $(function () {
        $('#personCardModel').blur(function() {
            if ($(this).val().length == 18) {
                var year = Number($(this).val().substring(6, 10))
                var myDate = new Date();
                var Nowyear = Number(myDate.getFullYear())
                $("#ageModel").val(Nowyear - year).change()
            } else if ($(this).val().length == 15) {
                var year = Number("19" + $(this).val().substring(6, 8))
                var myDate = new Date();
                var Nowyear = Number(myDate.getFullYear())
                $("#ageModel").val(Nowyear - year).change()
            }
        });

        kkpager.generPageHtml({
            pno: ${pageInfo.page},
            //总页码
            total: ${pageInfo.pageCount},
            //总数据条数
            totalRecords: ${pageInfo.allRecordCount},
            //链接前部
            hrefFormer: '<%=path%>/delegate/fugitivesRegManage',
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

        //关闭重复列表弹框
        $("#repeatConfirmBtn").on("click", function(){
            location.href = "<%=path%>/delegate/fugitivesRegManage";
        })

        //导入在逃人员
        $("#importBtn").on("click", function(){
            $("#upFile").click();
        })

        //检查上传文件后缀
        $("#upFile").change(function () {
            $("#textfield").val($(this).val());
            if(checkFileSuffix()) {
                //上传文件
                upload()
            }
        });

        function upload() {
            $.ajaxFileUpload({
                cache:false,
                url:"<%=path%>/delegate/uploadFugitives",
                secureuri:false,
                fileElementId:["upFile"],
                type : 'post',
                dataType: 'json',
                success: function (data) {
                    if(data.success || data.success == true || data.success == "true") {
                        if(data.repeatFugitivesInfoList!= null && data.repeatFugitivesInfoList.length > 0) {
                            $("#repeatPersonModal").modal()
                            $("#listRepeatTbody").empty();

                            var list = data.repeatFugitivesInfoList;
                            var person;
                            var newRowHtml;
                            for (var i = 0; i < data.repeatFugitivesInfoList.length; i++) {
                                person = list[i];
                                newRowHtml = "";
                                newRowHtml += "<tr>";
                                newRowHtml += "<td>" + (i+1) + "</td>";
                                newRowHtml += "<td>" + person.personName + "</td>";
                                newRowHtml += "<td>" + person.personCard + "</td>";
                                newRowHtml += "<td>" + person.involvedNo + "</td>";
                                newRowHtml += "<td>" + person.involvedName + "</td>";
                                newRowHtml += "</tr>";
                                $("#listRepeatTbody").append(newRowHtml);
                            }
                        }else {
                            location.href = "<%=path%>/delegate/fugitivesRegManage";
                        }
                    }
                },
                error: function (data,status,e) {
                    alert("上传失败！" + e);
                }
            });
        }

        //检查多个上传文件后缀
        function checkFileSuffix() {
            var fileName = "";
            var str = document.getElementById("upFile");
            if (str.files.length > 0) {
                for(var i=0; i< str.files.length;i++){
                    fileName = str.files[i].name;
                    //获得文件后缀名
                    var photoExt = fileName.substr(fileName.lastIndexOf(".")).toLowerCase();
                    if (photoExt == '.xls' || photoExt == '.xlsx') {

                    } else {
                        alert("请上传后缀名为xls或xlsx的文件")
                        $("#textfield").val("");
                        return false;
                    }
                }
            }else {
                alert("请选择要上传的文件！")
                return false;
            }

            return true;
        }

        //添加
        $("#addBtn").on("click", function () {
            $("#personModal").children().width(480);
            $("#personModal").find(".modal-content").eq(1).css({
                "height": 542.6 + 2,
            }).fadeIn(800);
            //添加在逃人员，默认在逃人员
            $("#personTypeModel").val("07");
            $("#idModel").val("");
            $("#personNameModel").val("");
            $("input[name='genderModel']").val("01");
            $("#personCardModel").val("");
            $("#ageModel").val("");
            $("#fugitiveNoModel").val("");
            $("#involvedNoModel").val("");
            $("#involvedNameModel").val("");
            $("#personModal").modal("show");
        })

        //添加在逃人员信息
       //全局变量标识
        var CLICKTAG = 0;
        $(".addAuthenticator").click(function () {
            if (CLICKTAG == 0) {
                CLICKTAG = 1;
                //this.disabled=true;
                $("button[name='confirmButton']").attr("disabled", true)
                // 等待2s后重置按钮可用
                setTimeout(function () {
                    CLICKTAG = 0;
                    $("button[name='confirmButton']").removeAttr("disabled")
                }, 2000);
            }

            var form = $("#personModal").find("form");
            form.bootstrapValidator({
                live: 'disabled',
                message: 'This value is not valid',
                fields: {
                    ageModel: {
                        trigger: 'change',
                        validators: {
                            notEmpty: {
                                message: "不能为空"
                            },
                            regexp: {
                                regexp: /^[0-9]*$/,
                                message: '格式有误.'
                            },
                        },
                    },
                    personCardModel: {
                        validators: {
                            notEmpty: {
                                message: "不能为空"
                            },
                            regexp: {
                                regexp: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/,
                                message: '身份证输入有误'
                            },
                        }
                    },
                    personTypeModel: {
                        validators: {
                            notEmpty: {
                                message: "不能为空"
                            }
                        }
                    },
                    personNameModel: {
                        validators: {
                            notEmpty: {
                                message: "不能为空"
                            }
                        }
                    },
                    fugitiveNoModel: {
                        trigger: 'change',
                        validators: {
                            notEmpty: {
                                message: "不能为空"
                            }
                        }
                    }
                }
            });
            form.bootstrapValidator('validate');
            if (form.data('bootstrapValidator').isValid()) {
                form.data('bootstrapValidator').destroy();
                form.data('bootstrapValidator', null);

                var age = $("#ageModel").val();
                if(getByteLen(age) > 3) {
                    alert("请检查年龄是否正确！")
                    $("#ageModel").val("");
                    $("#ageModel").focus();
                    return false;
                }

                $.ajax({
                    url : "<%=path%>/delegate/operateFugitives",
                    type:"post",
                    data : JSON.stringify(fugitives()),
                    contentType:  "application/json; charset=utf-8",
                    dataType : "json",
                    success : function(data) {
                        if(data.success || data.success == true || data.success == "true") {
                            if (data.repeat == "repeat") {
                                alert("已存在此人员信息!");
                            }else {
                                location.href = "<%=path%>/delegate/fugitivesRegManage";
                            }
                        }else {
                            alert("添加失败!");
                        }
                    },
                    error: function(data,e){
                        alert("添加失败!");
                    }
                });
                $('#personModal').modal('hide');
            }
        })

        function fugitives() {
            var fugitives = {};

            fugitives.id = $("#idModel").val();
            fugitives.personName = $("#personNameModel").val();
            fugitives.personType = $("#personTypeModel").val();
            fugitives.personGender = $("input[name='genderModel']:checked").val();
            fugitives.personCard = $("#personCardModel").val();
            fugitives.personAge = $("#ageModel").val();
            fugitives.fugitiveNo = $("#fugitiveNoModel").val();
            fugitives.involvedNo = $("#involvedNoModel").val();
            fugitives.involvedName = $("#involvedNameModel").val();

            return fugitives;
        }
        //获取字符串长度（汉字算两个字符，字母数字算一个）
        function getByteLen(val) {
            var len = 0;
            for (var i = 0; i < val.length; i++) {
                var a = val.charAt(i);
                if (a.match(/[^\x00-\xff]/ig) != null) {
                    len += 2;
                }
                else {
                    len += 1;
                }
            }
            return len;
        }

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
        $("button[name='delBtn']", "#listTbody").on("click", function () {
            var res = confirm("您确定要删除这条记录吗！");
            if (res == true) {
                var id = $("input[name='id']", $(this).parent()).val();
                $.ajax({
                    url: "<%=path%>/delegate/delFugitivesInfo",
                    type: "post",
                    data: {
                        id: id
                    },
                    dataType: "json",
                    success: function (data) {
                        if(data.success || data.success == true || data.success == "true") {
                            location.href = "<%=path%>/delegate/fugitivesRegManage";
                        }else {
                            alert("删除失败!");
                        }
                    },
                    error: function (e) {
                        alert("删除失败!" + e);
                    }
                });
            }
        });

        //修改委托登记
        $("button[name='editBtn']", "#listTbody").on("click", function () {
            var id = $("input[name='id']", $(this).parent()).val();
            var personName = $("input[name='personName']", $(this).parent()).val();
            var personType = $("input[name='personType']", $(this).parent()).val();
            var personGender = $("input[name='personGender']", $(this).parent()).val();
            var personAge = $("input[name='personAge']", $(this).parent()).val();
            var personCard = $("input[name='personCard']", $(this).parent()).val();
            var fugitiveNo = $("input[name='fugitiveNo']", $(this).parent()).val();
            var involvedNo = $("input[name='involvedNo']", $(this).parent()).val();
            var involvedName = $("input[name='involvedName']", $(this).parent()).val();

            $("#idModel").val(id);
            $("#personNameModel").val(personName);
            $("#personTypeModel").find("option[value='"+personType+"']").attr("selected",true);
            $("input[name='genderModel'][value='"+personGender+"']").attr("checked",true);
            $("#ageModel").val(personAge);
            $("#personCardModel").val(personCard);
            $("#fugitiveNoModel").val(fugitiveNo);
            $("#involvedNoModel").val(involvedNo);
            $("#involvedNameModel").val(involvedName);

            $('#personModal').modal('show');
        });

        //重置
        $("button[name='reset']").on("click", function () {
            location.href = "<%=path%>/delegate/fugitivesRegManage";
        })

    })
</script>
</body>

</html>