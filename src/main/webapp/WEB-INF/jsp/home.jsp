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
    <%@ include file="linkCss.jsp" %>
    <style>
        body {
            padding: 0 15px;
            background: none;
        }

        .panel {
            margin-bottom: 0px;
            background-color: #fff;
            border: none;
            border-radius: 0px;
            box-shadow: none;
            margin-top: 20px;
        }

        .panel-default > .panel-heading {
            background-color: #fff;
            border: none;
            padding: 0px 15px;
            border-radius: 0px;
            font-weight: 600;
        }

        .panel-default > .panel-heading.yellow {
            border-left: 5px solid #ffb802;
            color: #ffb802
        }

        .panel-default > .panel-heading.blue {
            border-left: 5px solid #296fff;
            color: #296fff;
        }

        .panel-default > .panel-heading.green {
            border-left: 5px solid #00b39b;
            color: #00b39b;
        }

        .panel-default > .panel-heading.red {
            border-left: 5px solid #f54336;
            color: #f54336;
        }

        .panel-body {
            border: 1px solid #f2f2f2;
            box-shadow: 0px 0px 10px 5px #f3f3f3;
            border-radius: 5px;
            margin-top: 10px;
            padding: 0px;
        }

        .nav-tabs > li > a {
            margin-right: 2px;
            line-height: 1.42857143;
            border: 1px solid transparent;
            border-bottom: 1px solid transparent;
            border-radius: 4px 4px 0 0;
            height: 100%;
            color: #000;
        }

        .nav-tabs > li.active > a,
        .nav-tabs > li.active > a:focus,
        .nav-tabs > li.active > a:hover {
            color: #296fff;
            cursor: default;
            background-color: #fff;
            border: none;
            border-bottom: 5px solid #296fff;
            font-weight: 600;
        }

        .nav > li > a:focus,
        .nav > li > a:hover {
            border: 1px solid transparent;
            border-bottom: 1px solid transparent;
            text-decoration: none;
            background-color: #fff;
        }

        .nav-tabs > li > a > span:nth-child(1) {
            display: inline-block;
            margin: 0 5px;
        }

        .nav-tabs > li > a > span:nth-child(2) {
            line-height: 1.7;
            color: #fff;
            background-color: #ff5951;
            width: 18px;
            height: 18px;
            border-radius: 50%;
            padding: 0px;
            position: absolute;
            top: 0px;
            right: 50%;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            margin-right: -62px;
        }

        .nav-tabs > li {
            text-align: center;
            width: 25%;
        }

        .ulline {
            margin-bottom: 0px;
        }

        .ulline > li {
            padding: 20px 0px;
            padding-left: 50px;
            position: relative;
        }

        .ulline > li:hover {
            background: #f7f7f7
        }

        .ulline > li::before {
            position: absolute;
            content: "●";
            width: 0px;
            height: 50%;
            font-size: 25px;
            line-height: 12px;
            text-align: center;
            left: 30px;
            top: 21px;
            z-index: 1;
        }

        .ulline > li:nth-child(3n+1):before {
            color: #40c1ad;
        }

        .ulline > li:nth-child(3n+2):before {
            color: #ff7a75;
        }

        .ulline > li:nth-child(3n+3):before {
            color: #ffc64a;
        }

        .ulline > li:nth-child(n+2):after {
            position: absolute;
            content: "";
            width: 0px;
            height: 100%;
            border-left: 2px solid #f1f1f1;
            left: 100px;
            top: 21px;
            left: 37px;
            top: -71px;
        }

        .ulline > li .col-md-8 > p:nth-child(1) {
            color: #000;
        }

        .ulline > li .col-md-8 > p:nth-child(2) {
            color: #707070
        }

        #appraisalStatusChange > div {
            text-align: center;
            padding: 13px 0;
            border-top: 2px dashed #e8e8e8;
        }

        #messageHint .panel-body .ulline {
            height: 300px;
            overflow-y: auto;
        }

        #backlog .panel-body {
            height: 408px;
            padding: 40px 0;
        }

        #backlog .panel-body > .row {
            height: 100%
        }

        #backlog .panel-body .col-md-6:nth-child(1) {
            height: 100%;
            border-right: 2px dashed #e8e8e8;
        }

        #backlog .panel-body .col-md-6:nth-child(2) {
            height: 100%;
        }

        .statistics {
            padding-top: 20px;
        }

        .statistics > .col-md-4 > p {
            display: inline-block;
            padding: 10px 27px;
            background: #eaf1ff;
            color: #296fff;
            font-weight: 600;
            border: 1px dashed #296fff;
            margin-left: 70px;
            border-radius: 3px;
        }

        .statistics .navbar-nav {
            width: 100%;
            height: 54px;
            line-height: 42px;
        }

        .statistics .navbar-nav li {
            cursor: pointer
        }

        .statistics .navbar-nav > li {
            color: #666666
        }

        .statistics .navbar-nav > li:nth-child(n):nth-child(-n+5):hover,
        .statistics .navbar-nav > li:nth-child(n):nth-child(-n+5):active {
            border-bottom: 2px solid #296fff;
            color: #296fff;
        }

        .statistics .navbar-nav > li.active,
        .statistics .navbar-nav > li.active:hover,
        .statistics .navbar-nav > li.active:active {
            border-bottom: 2px solid #296fff;
            color: #296fff;
        }

        .statistics .navbar-nav > .dropdown.active > a,
        .statistics .navbar-nav > .dropdown.active > a:hover,
        .statistics .navbar-nav > .dropdown.active > a:active {
            border-bottom: 2px solid #296fff;
            color: #296fff;
        }

        .statistics .navbar-nav > .dropdown.active ul li {
            color: #666666;
        }

        .statistics .navbar-nav > .dropdown.active ul li.active,
        .statistics .navbar-nav > .dropdown.active ul li.active:hover,
        .statistics .navbar-nav > .dropdown.active ul li.active:active {
            color: #fff;
            background: #296fff;
        }

        .statistics .navbar-nav > li + li {
            margin-left: 9%;
        }

        .statistics .navbar-nav > .dropdown > a {
            line-height: 42px;
            padding: 0px;
            color: #666666
        }

        .statistics .navbar-nav > .dropdown > a > span {
            color: #d1d1d1
        }

        .statistics .nav .dropdown-toggle:hover,
        .statistics .nav .open > a,
        .statistics .nav .open > a:focus,
        .statistics .nav .open > a:hover {
            background-color: #fff;
            border: none;
            border-bottom: 2px solid #296fff;
            color: #296fff;
        }

        .statistics .dropdown-menu {
            padding: 0px;
        }

        .statistics .dropdown-menu li {
            text-align: center;
        }

        .statistics .dropdown-menu li:hover {
            background: #f7f7f7;
            color: #296fff;
        }

        #statisticsCharts {
            height: 318px;
        }

        .statistics .chartsTab {
            border-top: 2px solid #e9ebf0;
            margin-left: 70px;
        }

        .statistics .chartsTab li {
            float: left;
            width: 160px;
            height: 110px;
            text-align: left;
            font-size: 14px;
            color: #000;
            cursor: pointer;
            padding-left: 43px;
            padding-top: 25px;
        }

        .statistics .chartsTab li.active {
            background: #f7f7f7
        }

        .statistics .chartsTab li:hover {
            background: #f7f7f7
        }

        .statistics .chartsTab img {
            display: block;
            margin-top: 14px;
        }
    </style>
</head>

<body>
<div class="row">
    <div class="col-md-6" id="messageHint">
        <div class="panel panel-default">
            <div class="panel-heading yellow">消息提示</div>
            <div class="panel-body">
                <ul class="nav nav-tabs" role="tablist">
                    <li role="presentation" class="active">
                        <a href="#appraisalStatusChange" aria-controls="home" role="tab" data-toggle="tab">鉴定状态变更(<span>0</span><span
                                class="badge">0</span>)</a>
                    </li>
                    <li role="presentation">
                        <a href="#notificationOfTest" aria-controls="profile" role="tab" data-toggle="tab">检测结果通知(<span>0</span><span
                                class="badge">0</span>)</a>
                    </li>
                    <li role="presentation">
                        <a href="#appraisalNotice" aria-controls="messages" role="tab"
                           data-toggle="tab">鉴定领取通知(<span>0</span><span
                                class="badge">0</span>)</a>
                    </li>
                    <li role="presentation">
                        <a href="#personnelNum" aria-controls="messages" role="tab"
                           data-toggle="tab">入库编号推送(<span>0</span><span class="badge">0</span>)</a>
                    </li>
                </ul>
                <div class="tab-content">
                    <div role="tabpanel" class="tab-pane active" id="appraisalStatusChange">
                        <ul class="ulline">

                            <li>
                                <div class="row">
                                    <div class="col-md-8">
                                        <p>您还没有消息</p>
                                    </div>

                                </div>
                            </li>
                            <%--
                                                        <li>
                                                            <div class="row">
                                                                <div class="col-md-8">
                                                                    <p>案件FYB1900003-2019WZ00003领取通知</p>
                                                                    <p>鉴定领取通知已于2019-01-16出来</p>
                                                                </div>
                                                                <div class="col-md-3 col-md-offset-1">
                                                                    <input type="hidden" name="caseNo" value="FYB1900003-2019WZ00003">
                                                                    <input type="hidden" name="caseName" value="刘登福交通肇事案">
                                                                    <input type="hidden" name="orgName" value="东城分局">
                                                                    <input type="hidden" name="delegatorName" value="马波">
                                                                    <input type="hidden" name="delegatorNo" value="06041">
                                                                    <button class="btn btn-blue" name="receiveWord">打印领取单</button>
                                                                </div>
                                                            </div>
                                                        </li>
                                                        <li>
                                                            <div class="row">
                                                                <div class="col-md-8">
                                                                    <p>案件FYB1900002-2019WZ00002领取通知</p>
                                                                    <p>鉴定领取通知已于2018-05-13出来</p>
                                                                </div>
                                                                <div class="col-md-3 col-md-offset-1">
                                                                    <button class="btn btn-gray">已领取</button>
                                                                </div>
                                                            </div>
                                                        </li>
                                                        <li>
                                                            <div class="row">
                                                                <div class="col-md-8">
                                                                    <p>案件FYB1900001-2019WZ00001领取通知</p>
                                                                    <p>鉴定领取通知已于2018-05-13出来</p>
                                                                </div>
                                                                <div class="col-md-3 col-md-offset-1">
                                                                    <button class="btn btn-gray">已领取</button>
                                                                </div>
                                                            </div>
                                                        </li>
                            --%>
                        </ul>
                        <div>
                            <button class="btn btn-gray-border">已领取记录</button>
                        </div>
                    </div>
                    <div role="tabpanel" class="tab-pane" id="notificationOfTest">
                        <ul class="ulline">
                            <li>
                                <div class="row">
                                    <div class="col-md-8">
                                        <p>您还没有消息</p>
                                    </div>
                                </div>
                            </li>
                            <%--
                                                        <li>
                                                            <div class="row">
                                                                <div class="col-md-8">
                                                                    <p>案件FYB1900001-2019WZ00001比中</p>
                                                                    <p>00001-01号（现场血斑）与BJ19SHD0100001（张）比中</p>
                                                                </div>
                                                            </div>
                                                        </li>
                            --%>
                        </ul>
                    </div>
                    <div role="tabpanel" class="tab-pane" id="appraisalNotice">
                        <ul class="ulline">

                            <li>
                                <div class="row">
                                    <div class="col-md-8">
                                        <p>您还没有消息</p>
                                    </div>
                                </div>
                            </li>
                            <%--
                                                        <li>
                                                            <div class="row">
                                                                <div class="col-md-8">
                                                                    <p>案件FYB1900003-2019WZ00003领取通知</p>
                                                                    <p>鉴定领取通知已于2019-01-16出来</p>
                                                                </div>
                                                                <div class="col-md-3 col-md-offset-1">
                                                                    <input type="hidden" name="caseNo" value="FYB1900003-2019WZ00003">
                                                                    <input type="hidden" name="caseName" value="刘登福交通肇事案">
                                                                    <input type="hidden" name="orgName" value="东城分局">
                                                                    <input type="hidden" name="delegatorName" value="马波">
                                                                    <input type="hidden" name="delegatorNo" value="06041">
                                                                    <button class="btn btn-blue" name="receiveWord">打印领取单</button>
                                                                </div>
                                                            </div>
                                                        </li>
                                                        <li>
                                                            <div class="row">
                                                                <div class="col-md-8">
                                                                    <p>案件FYB1900002-2019WZ00002领取通知</p>
                                                                    <p>鉴定领取通知已于2018-05-13出来</p>
                                                                </div>
                                                                <div class="col-md-3 col-md-offset-1">
                                                                    <button class="btn btn-gray">已领取</button>
                                                                </div>
                                                            </div>
                                                        </li>
                                                        <li>
                                                            <div class="row">
                                                                <div class="col-md-8">
                                                                    <p>案件FYB1900001-2019WZ00001领取通知</p>
                                                                    <p>鉴定领取通知已于2018-05-13出来</p>
                                                                </div>
                                                                <div class="col-md-3 col-md-offset-1">
                                                                    <button class="btn btn-gray">已领取</button>
                                                                </div>
                                                            </div>
                                                        </li>
                            --%>
                        </ul>
                    </div>
                    <div role="tabpanel" class="tab-pane" id="personnelNum">
                        <ul class="ulline">

                            <li>
                                <div class="row">
                                    <div class="col-md-8">
                                        <p>您还没有消息</p>
                                    </div>
                                </div>
                            </li>
                            <%--
                                                        <li>
                                                            <div class="row">
                                                                <div class="col-md-8">
                                                                    <p>00001-01号（现场血斑）入库成功！编号：P001232343222343</p>
                                                                </div>
                                                            </div>
                                                        </li>
                            --%>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="col-md-6" id="backlog">
        <div class="panel panel-default">
            <div class="panel-heading blue">待办事项</div>
            <div class="panel-body">
                <div class="row">
                    <div class="col-md-6" id="backlogCharts1"></div>
                    <div class="col-md-6" id="backlogCharts2"></div>
                </div>
            </div>
        </div>
    </div>
    <div class="col-md-12">
        <div class="panel panel-default">
            <div class="panel-heading green">年度案件数量</div>
            <div class="panel-body">
                <div class="row statistics">
                    <div class="col-md-4">
                        <p>
                            DNA类型全年${monthMap.C1+monthMap.C2+monthMap.C3+monthMap.C4+monthMap.C5+monthMap.C6+monthMap.C7+monthMap.C8+monthMap.C9+monthMap.C10+monthMap.C11+monthMap.C12}件</p>
                    </div>
                    <div class="col-md-5 col-md-offset-3">
                        <ul class="nav navbar-nav navbar-right ">
                            <li class="active"></li>
                            <li></li>
                            <li></li>
                            <li></li>
                            <li></li>
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button"
                                   aria-haspopup="true" aria-expanded="false">更多<span class="caret"></span>
                                </a>
                                <ul class="dropdown-menu">
                                    <li></li>
                                    <li></li>
                                    <li></li>
                                    <li></li>
                                    <li></li>
                                </ul>
                            </li>
                        </ul>
                    </div>
                    <div class="col-md-12" id="statisticsCharts"></div>
                    <div class="col-md-12">
                        <ul class="chartsTab">
                            <li class="active">
                                <p>DNA<img src="<%=path%>/img/redcharts..png" alt=""></p>
                            </li>
                            <%--
                            <li>
                                <p>毒化<img src="../img/bluecharts.png" alt=""></p>
                            </li>
                            <li>
                                <p>临床<img src="../img/yellowcharts..png" alt=""></p>
                            </li>
                            <li>
                                <p>病理<img src="../img/greencharts..png" alt=""></p>
                            </li>
                            --%>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="linkJs.jsp" %>
<script>
    $(function () {
        //获取当前年份
        var date = new Date()
        $(".navbar-right").children().each(function (i) {
            if (!$(this).hasClass("dropdown")) {
                $(this).html((Number(date.getFullYear()) - i) + "年")
            }
        })
        $(".dropdown-menu").children().each(function (i) {
            var year = (Number(date.getFullYear()) - Number($(".navbar-right").children().length - 1)) - i
            $(this).html(year + "年")
        })
        $(".navbar-right").children(".dropdown").click(function () {
            $(this).addClass("active").siblings().removeClass("active")
        })
        $(".navbar-right").children(".dropdown").siblings().click(function () {
            $(".dropdown-menu").children().removeClass("active")
            var year = $(this).html().substring(0, 4)
            $(this).addClass("active").siblings().removeClass("active")
            $.ajax({
                url: "<%=path%>/main/getCount",
                type: "get",
                data: {year: year},
                dataType: "json",
                success: function (data) {
                    var data = [data.monthMap.C1, data.monthMap.C2, data.monthMap.C3, data.monthMap.C4, data.monthMap.C5, data.monthMap.C6, data.monthMap.C7, data.monthMap.C8, data.monthMap.C9, data.monthMap.C10, data.monthMap.C11, data.monthMap.C12,];
                    var xdata = ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'];
                    var lineColor = ['#f54336', '#296fff', '#ffc108', '#49c6b6']
                    var statisticsCharts = echarts.init(document.getElementById('statisticsCharts'));
                    var statisticsChartsOption = {
                        grid: {
                            left: 30,
                            right: 50,
                            top: 10,
                            bottom: 30,
                            containLabel: true
                        },
                        xAxis: [{
                            type: 'category',
                            data: xdata,
                            axisLine: {
                                show: true,
                                lineStyle: {
                                    color: lineColor[0], //X下轴颜色
                                    type: 'dashed'
                                }
                            },
                            axisTick: {
                                show: false
                            },
                            axisLabel: {
                                color: '#616161', //X下轴字体颜色
                            },
                            boundaryGap: false,
                        }, {
                            type: 'category',
                            data: data,
                            axisLine: {
                                show: false
                            },
                            axisTick: {
                                show: false
                            },
                            splitLine: {
                                show: true,
                                lineStyle: {
                                    color: '#999', //x轴辅助线颜色
                                    type: 'dashed'
                                }
                            },
                            axisLabel: {
                                color: '#616161', //X上轴字体颜色
                            },
                            boundaryGap: false,
                        },],
                        yAxis: {
                            type: 'value',
                            splitLine: {
                                show: false,
                                lineStyle: {
                                    type: 'dashed'
                                }
                            },
                            axisLine: {
                                show: false
                            },
                            axisTick: {
                                show: false
                            },
                        },
                        tooltip: {
                            trigger: 'axis',
                            showContent: false,
                            axisPointer: {
                                type: 'cross',
                                lineStyle: {
                                    type: 'dashed',
                                    color: lineColor[0], //滑动X轴辅助线颜色
                                }
                            }
                        },
                        series: [{
                            data: data,
                            type: 'line',
                            symbol: 'emptyCircle',
                            symbolSize: 5,
                            color: lineColor[0], //拐点颜色
                            lineStyle: {
                                color: lineColor[0] //折线颜色
                            },
                            label: {
                                show: false,
                            },
                            areaStyle: {
                                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                    offset: 0.22,
                                    color: lineColor[0]
                                }, {
                                    offset: 0.98,
                                    color: '#fff'
                                }]), //折线区域颜色
                                opacity: 0.5
                            }
                        }]
                    };
                    statisticsCharts.setOption(statisticsChartsOption);
                },
                error: function (e) {
                    alert(e);
                }
            });
        })
        $(".dropdown-menu").children().click(function () {
            $(this).addClass("active").siblings().removeClass("active")
            var year = $(this).html().substring(0, 4)
            $.ajax({
                url: "<%=path%>/main/getCount",
                type: "get",
                data: {year: year},
                dataType: "json",
                success: function (data) {

                    var data = [data.monthMap.C1, data.monthMap.C2, data.monthMap.C3, data.monthMap.C4, data.monthMap.C5, data.monthMap.C6, data.monthMap.C7, data.monthMap.C8, data.monthMap.C9, data.monthMap.C10, data.monthMap.C11, data.monthMap.C12,];
                    var xdata = ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'];
                    var lineColor = ['#f54336', '#296fff', '#ffc108', '#49c6b6']
                    var statisticsCharts = echarts.init(document.getElementById('statisticsCharts'));
                    var statisticsChartsOption = {
                        grid: {
                            left: 30,
                            right: 50,
                            top: 10,
                            bottom: 30,
                            containLabel: true
                        },
                        xAxis: [{
                            type: 'category',
                            data: xdata,
                            axisLine: {
                                show: true,
                                lineStyle: {
                                    color: lineColor[0], //X下轴颜色
                                    type: 'dashed'
                                }
                            },
                            axisTick: {
                                show: false
                            },
                            axisLabel: {
                                color: '#616161', //X下轴字体颜色
                            },
                            boundaryGap: false,
                        }, {
                            type: 'category',
                            data: data,
                            axisLine: {
                                show: false
                            },
                            axisTick: {
                                show: false
                            },
                            splitLine: {
                                show: true,
                                lineStyle: {
                                    color: '#999', //x轴辅助线颜色
                                    type: 'dashed'
                                }
                            },
                            axisLabel: {
                                color: '#616161', //X上轴字体颜色
                            },
                            boundaryGap: false,
                        },],
                        yAxis: {
                            type: 'value',
                            splitLine: {
                                show: false,
                                lineStyle: {
                                    type: 'dashed'
                                }
                            },
                            axisLine: {
                                show: false
                            },
                            axisTick: {
                                show: false
                            },
                        },
                        tooltip: {
                            trigger: 'axis',
                            showContent: false,
                            axisPointer: {
                                type: 'cross',
                                lineStyle: {
                                    type: 'dashed',
                                    color: lineColor[0], //滑动X轴辅助线颜色
                                }
                            }
                        },
                        series: [{
                            data: data,
                            type: 'line',
                            symbol: 'emptyCircle',
                            symbolSize: 5,
                            color: lineColor[0], //拐点颜色
                            lineStyle: {
                                color: lineColor[0] //折线颜色
                            },
                            label: {
                                show: false,
                            },
                            areaStyle: {
                                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                    offset: 0.22,
                                    color: lineColor[0]
                                }, {
                                    offset: 0.98,
                                    color: '#fff'
                                }]), //折线区域颜色
                                opacity: 0.5
                            }
                        }]
                    };
                    statisticsCharts.setOption(statisticsChartsOption);
                },
                error: function (e) {
                    alert(e);
                }
            });
        })
//        $(".chartsTab").children().click(function () {
//            $(this).addClass("active").siblings().removeClass("active")
//        })
        var backlogCharts1 = echarts.init(document.getElementById('backlogCharts1'));
        var backlogCharts1Option = {
            title: {
                text: '      ',
                x: "center",
                y: "72%",
                textStyle: {
                    width: "43px",
                    height: "0px",
                },
                padding: [0, 0, 0, 0,],
                borderColor: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                    offset: 0.15,
                    color: '#ffb802'
                }, {
                    offset: 0.15,
                    color: 'transparent'
                }]),
                borderWidth: 8,
            },
            series: [{
                type: 'liquidFill',
                center: ['50%', '35%'],
                data: [0.6, 0.5, 0.4],
                color: ['#ffeec8', '#ffe6b0 ', '#ffd885 '],
                itemStyle: {
                    normal: {
                        shadowBlur: 0,
                        opacity: 0.6
                    }
                },
                backgroundStyle: {
                    color: '#fff'
                },
                outline: {
                    borderDistance: 0,
                    itemStyle: {
                        borderWidth: 8,
                        borderColor: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                            offset: 0.42,
                            color: '#ededed'
                        }, {
                            offset: 0.42,
                            color: '#ffb802'
                        }]),
                        shadowBlur: 20,
                    }
                },
                label: {
                    normal: {
                        formatter: [
                            '{textBorder| ${cnt1}件}',
                            <%--'{textBorder|' + ${cnt1} + '件}',--%>
                            '{titles|待送检案件数}'
                        ].join('\n'),
                        rich: {
                            textBorder: {
                                fontSize: 28,
                                color: '#a86f00',
                                insideColor: '#a86f00',
                                fontWeight: "bold"
                            },
                            titles: {
                                color: '#000',
                                fontSize: 18,
                                x: "center",
                                y: "80%",
                                padding: [-280, 0, 0, 0]
                            }
                        }
                    }
                }
            }]
        };
        backlogCharts1.setOption(backlogCharts1Option);
        var backlogCharts2 = echarts.init(document.getElementById('backlogCharts2'));
        var backlogCharts2Option = {
            title: {
                text: '      ',
                x: "center",
                y: "72%",
                textStyle: {
                    width: "43px",
                    height: "0px",
                },
                padding: [0, 0, 0, 0,],
                borderColor: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                    offset: 0.15,
                    color: '#007ef9'
                }, {
                    offset: 0.15,
                    color: 'transparent'
                }]),
                borderWidth: 8,
            },
            series: [{
                type: 'liquidFill',
                center: ['50%', '35%'],
                data: [0.6, 0.5, 0.4],
                color: ['#c1dffd', '#a5d0fe', '#75b8fd'],
                itemStyle: {
                    normal: {
                        shadowBlur: 0,
                        opacity: 0.6
                    }
                },
                backgroundStyle: {
                    color: '#fff'
                },
                outline: {
                    borderDistance: 0,
                    itemStyle: {
                        borderWidth: 8,
                        borderColor: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                            offset: 0.42,
                            color: '#ededed'
                        }, {
                            offset: 0.42,
                            color: '#007ef9'
                        }]),
                        shadowBlur: 20,
                    }
                },
                label: {
                    normal: {
                        formatter: [
                            <%--'{textBorder|${cnt3}件}',--%>
                            '{textBorder|1件}',
                            '{titles|待领取鉴定数}'
                        ].join('\n'),
                        rich: {
                            textBorder: {
                                fontSize: 28,
                                color: '#0065aa',
                                insideColor: '#0065aa',
                                fontWeight: "bold"
                            },
                            titles: {
                                color: '#000',
                                fontSize: 18,
                                x: "center",
                                y: "80%",
                                padding: [-280, 0, 0, 0]
                            }
                        }
                    }
                }
            }]
        };
        backlogCharts2.setOption(backlogCharts2Option);

        var data = [${monthMap.C1}, ${monthMap.C2}, ${monthMap.C3}, ${monthMap.C4}, ${monthMap.C5}, ${monthMap.C6}, ${monthMap.C7}, ${monthMap.C8}, ${monthMap.C9}, ${monthMap.C10}, ${monthMap.C11}, ${monthMap.C12},];
        var xdata = ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'];
        var lineColor = ['#f54336', '#296fff', '#ffc108', '#49c6b6']
        var statisticsCharts = echarts.init(document.getElementById('statisticsCharts'));
        var statisticsChartsOption = {
            grid: {
                left: 30,
                right: 50,
                top: 10,
                bottom: 30,
                containLabel: true
            },
            xAxis: [{
                type: 'category',
                data: xdata,
                axisLine: {
                    show: true,
                    lineStyle: {
                        color: lineColor[0], //X下轴颜色
                        type: 'dashed'
                    }
                },
                axisTick: {
                    show: false
                },
                axisLabel: {
                    color: '#616161', //X下轴字体颜色
                },
                boundaryGap: false,
            }, {
                type: 'category',
                data: data,
                axisLine: {
                    show: false
                },
                axisTick: {
                    show: false
                },
                splitLine: {
                    show: true,
                    lineStyle: {
                        color: '#999', //x轴辅助线颜色
                        type: 'dashed'
                    }
                },
                axisLabel: {
                    color: '#616161', //X上轴字体颜色
                },
                boundaryGap: false,
            },],
            yAxis: {
                type: 'value',
                splitLine: {
                    show: false,
                    lineStyle: {
                        type: 'dashed'
                    }
                },
                axisLine: {
                    show: false
                },
                axisTick: {
                    show: false
                },
            },
            tooltip: {
                trigger: 'axis',
                showContent: false,
                axisPointer: {
                    type: 'cross',
                    lineStyle: {
                        type: 'dashed',
                        color: lineColor[0], //滑动X轴辅助线颜色
                    }
                }
            },
            series: [{
                data: data,
                type: 'line',
                symbol: 'emptyCircle',
                symbolSize: 5,
                color: lineColor[0], //拐点颜色
                lineStyle: {
                    color: lineColor[0] //折线颜色
                },
                label: {
                    show: false,
                },
                areaStyle: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                        offset: 0.22,
                        color: lineColor[0]
                    }, {
                        offset: 0.98,
                        color: '#fff'
                    }]), //折线区域颜色
                    opacity: 0.5
                }
            }]
        };
        statisticsCharts.setOption(statisticsChartsOption);

        $(".chartsTab").children().click(function () {
            var year = $("#statisticsCharts").prev().find(".active").html().substring(0, 4)
            $.ajax({
                url: "<%=path%>/main/getCount",
                type: "get",
                data: {year: year},
                dataType: "json",
                success: function (data) {
                    var data = [data.monthMap.C1, data.monthMap.C2, data.monthMap.C3, data.monthMap.C4, data.monthMap.C5, data.monthMap.C6, data.monthMap.C7, data.monthMap.C8, data.monthMap.C9, data.monthMap.C10, data.monthMap.C11, data.monthMap.C12,];
                    var xdata = ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'];
                    var lineColor = ['#f54336', '#296fff', '#ffc108', '#49c6b6']
                    var statisticsCharts = echarts.init(document.getElementById('statisticsCharts'));
                    var statisticsChartsOption = {
                        grid: {
                            left: 30,
                            right: 50,
                            top: 10,
                            bottom: 30,
                            containLabel: true
                        },
                        xAxis: [{
                            type: 'category',
                            data: xdata,
                            axisLine: {
                                show: true,
                                lineStyle: {
                                    color: lineColor[0], //X下轴颜色
                                    type: 'dashed'
                                }
                            },
                            axisTick: {
                                show: false
                            },
                            axisLabel: {
                                color: '#616161', //X下轴字体颜色
                            },
                            boundaryGap: false,
                        }, {
                            type: 'category',
                            data: data,
                            axisLine: {
                                show: false
                            },
                            axisTick: {
                                show: false
                            },
                            splitLine: {
                                show: true,
                                lineStyle: {
                                    color: '#999', //x轴辅助线颜色
                                    type: 'dashed'
                                }
                            },
                            axisLabel: {
                                color: '#616161', //X上轴字体颜色
                            },
                            boundaryGap: false,
                        },],
                        yAxis: {
                            type: 'value',
                            splitLine: {
                                show: false,
                                lineStyle: {
                                    type: 'dashed'
                                }
                            },
                            axisLine: {
                                show: false
                            },
                            axisTick: {
                                show: false
                            },
                        },
                        tooltip: {
                            trigger: 'axis',
                            showContent: false,
                            axisPointer: {
                                type: 'cross',
                                lineStyle: {
                                    type: 'dashed',
                                    color: lineColor[0], //滑动X轴辅助线颜色
                                }
                            }
                        },
                        series: [{
                            data: data,
                            type: 'line',
                            symbol: 'emptyCircle',
                            symbolSize: 5,
                            color: lineColor[0], //拐点颜色
                            lineStyle: {
                                color: lineColor[0] //折线颜色
                            },
                            label: {
                                show: false,
                            },
                            areaStyle: {
                                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                    offset: 0.22,
                                    color: lineColor[0]
                                }, {
                                    offset: 0.98,
                                    color: '#fff'
                                }]), //折线区域颜色
                                opacity: 0.5
                            }
                        }]
                    };
                    statisticsCharts.setOption(statisticsChartsOption);
                }
            })
        })

        $("button[name='receiveWord']").on("click", function () {
            var caseNo = $("input[name='caseNo']").val();
            var caseName = $("input[name='caseName']").val();
            var orgName = $("input[name='orgName']").val();
            var delegatorName = $("input[name='delegatorName']").val();
            var delegatorNo = $("input[name='delegatorNo']").val();
            location.href = "<%=path%>/main/receiveWord?caseNo=" + caseNo + "&caseName=" + caseName
                    + "&orgName=" + orgName + "&delegatorName=" + delegatorName + "&delegatorNo=" + delegatorNo;
        });

    })

</script>
</body>

</html>
