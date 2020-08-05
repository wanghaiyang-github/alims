$(".clientSelect").searchableSelect();

$(".personCard").find("button").click(function () {
    $("#addClientBox").modal('show')
    $("#addClientBox").find("input[name='index']").val($(this).parents(".personCard").parent().index())
})

$(".addClient").click(function () {
    var clientName = $("#addClientBox").find("input[name='clientName']").val()
    var delegatorPosition = $("#addClientBox").find("select[name='delegatorPosition']").val()
    var delegatorPhoneNumber = $("#addClientBox").find("input[name='delegatorPhoneNumber']").val()
    var delegatorPaperworkType = $("#addClientBox").find("input[name='delegatorPaperworkType']").val()
    var delegatorPaperworkNo = $("#addClientBox").find("input[name='delegatorPaperworkNo']").val()
    var index = $("#addClientBox").find("input[name='index']").val()

    $(".personCard").eq(index).find(".delegator1Id").append("<option selected>" + clientName + "</option>")
    $(".personCard").eq(index).find(".delegatorPosition").val(delegatorPosition)
    $(".personCard").eq(index).find(".delegatorPhoneNumber").val(delegatorPhoneNumber)
    $(".personCard").eq(index).find(".delegatorPaperworkType").val(delegatorPaperworkType)
    $(".personCard").eq(index).find(".delegatorPaperworkNo").val(delegatorPaperworkNo)
    //$(".personCard").eq(index).find(".clientSelect").next().remove()
    //$(".personCard").eq(index).find("input[name='clientPhone']").val(clientPhone)
    //$(".personCard").eq(index).find("input[name='clientDuties']").val(clientDuties)
    //$(".personCard").eq(index).find("input[name='clientIdType']").val(clientIdType)
    //$(".personCard").eq(index).find("input[name='clientId']").val(clientId)
    //$(".personCard").eq(index).find(".clientSelect").searchableSelect();
    $("#addClientBox").modal('hide')
})

$('#addClientBox').on('hidden.bs.modal', function (e) {
    $("#addClientBox").find("input").val("")
    $("#addClientBox").find("select").val("")
})

$("#saveform ").find(".btn-checkbox").children().each(function (i) {
    if (i > 0 && $(this).position().left == 0) {
        $(this).css("margin-left", '0')
    }
})

$(".checkboxAll").click(function () {
    if ($("#saveform ").find(".has-error").length > 0) {
        var form = $("#saveform ")
        form.data('bootstrapValidator').destroy();
        form.data('bootstrapValidator', null);
    }
    if ($("#saveform ").find(".btn-checkbox").children(".active").length == $("#saveform ").find(".btn-checkbox").children().length) {
        $("#saveform ").find(".btn-checkbox").children(".active").removeClass("active")
        $("#saveform ").find(".btn-checkbox").find("input").prop("checked", false)
    } else {
        $("#saveform ").find(".btn-checkbox").children().addClass("active")
        $("#saveform ").find(".btn-checkbox").find("input").prop("checked", true)
    }
})

$("#saveform ").find(".btn-checkbox").on("click", 'li', function () {
    if ($("#saveform ").find(".has-error").length > 0) {
        var form = $("#saveform ")
        form.data('bootstrapValidator').destroy();
        form.data('bootstrapValidator', null);
    }
    if ($(this).hasClass("active")) {
        $(this).removeClass("active")
    } else {
        $(this).addClass("active")
    }
})
$("#siteSurveyBox").find(".btn-checkbox").on("click", 'li', function () {
    if (!$(this).hasClass("active")) {
        $(this).addClass("active").siblings().removeClass("active")
        $(this).find("input").prop("checked", true)
        $(this).siblings().find("input").prop("checked", false)
    }
    $("#siteSurveyBox").find(".modal-footer").next().find("span").html("("+$(this).prop ('firstChild').nodeValue+")")
})


$("#authenticatorBox").find("input[name='noIdCheck']").change(function () {
    if ($(this).is(":checked")) {
        $("#authenticatorBox").find("input[name='idCard']").val("").prop("disabled",
            true)
        $("#authenticatorBox").find("input[name='noIdCard']").val("").removeClass(
            "hidden")
    } else {
        $("#authenticatorBox").find("input[name='idCard']").val("").prop("disabled",
            false)
        $("#authenticatorBox").find("input[name='noIdCard']").val("").addClass("hidden")
    }
})

////���֤��֤
//function isCardNo(card) {
//    var pattern = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
//    return pattern.test(card);
//}
//// ���������������
//$("#addAuthenticatorBig").find("input[name='idCard']").blur(function () {
//    if ($.trim($(this).val()).length > 0 && isCardNo($.trim($(this).val()))) {
//        var year = Number($(this).val().substring(6, 10))
//        var myDate = new Date();
//        var Nowyear = Number(myDate.getFullYear())
//        $("#authenticatorBox").find("input[name='year']").val(Nowyear - year)
//        alert($(this).val().length)
//        if($(this).val().length==18){
//            alert(Number($(this).val().substring(17, 1)))
//        }
//    }
//})

$("#authenticatorBox").find("select[name='personnelType']").change(function () {
    if ($(this).val().indexOf("����") != -1) {
        var headerHtml = $(this).val().substring(0, $(this).val().indexOf("����"))
        $("#authenticatorBox").find("select[name='kinship']").parents(".form-group").removeClass(
            "hidden").find("label").html("��" + headerHtml + "��Ե��ϵ")
    } else {
        $("#authenticatorBox").find("select[name='kinship']").val("").parents(
            ".form-group").addClass("hidden")
    }
})