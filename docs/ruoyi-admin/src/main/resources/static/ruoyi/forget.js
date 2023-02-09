
$(function() {
    validateRule();
});

$.validator.setDefaults({
    submitHandler: function() {
        forgetPassword();
    }
});

function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#signupForm").validate({
        rules: {
            username: {
                required: true
            },
            password: {
                required: true
            }
        },
        messages: {
            username: {
                required: icon + "请输入您的用户名或手机号",
            },
            password: {
                required: icon + "请输入您的密码",
            }
        }
    })
}

function sendnumber(){
    var counts = 60;
    var loginName = $.common.trim($("input[name='username']").val());
    var phoneNumber = $.common.trim($("input[name='phoneNumber']").val());
    if (null == loginName || "" == loginName) {
        alert('请填写用户名！');
        return;
    }
    if (null == phoneNumber || "" == phoneNumber) {
        alert('请填写手机号！');
        return;
    }
    var btn = document.getElementById("sendcode");
    btn.disabled = 'disabled';
    $.ajax({
        type: "post",
        url: ctx + "sendcode",
        data: {
            "loginName": loginName,
            "phoneNumber": phoneNumber,
            "loginFlag": "0"// 忘记密码默认用用户名查询，防止手机号查询出现重复数据
        },
        success: function(r) {
           if(r == '-2'){
                alert("短信发送失败，请联系管理员！");
               btn.removeAttribute("disabled");
               $("input[name='username']").val("");
               $("input[name='phoneNumber']").val("");
                return;
           }
            if(r == '-1'){
                alert("该用户没有设置电话号码!");
                btn.removeAttribute("disabled");
                $("input[name='username']").val("");
                $("input[name='phoneNumber']").val("");
                return;
            }
            if(r == '-3'){
                alert("该用户不存在!");
                btn.removeAttribute("disabled");
                $("input[name='username']").val("");
                $("input[name='phoneNumber']").val("");
                return;
            }
            if(r == '-5'){
                alert("输入手机号与用户注册的手机号不匹配！");
                btn.removeAttribute("disabled");
                $("input[name='username']").val("");
                $("input[name='phoneNumber']").val("");
                return;
            }
            if(r == '-6'){
                alert("短信验证码间隔时间（1分钟）内不可重复获取！");
                $("input[name='username']").val("");
                $("input[name='phoneNumber']").val("");
                return;
            }else{
                // 验证码正常获取需要读秒，防止连续点击
                settime(btn,counts);
            }
        }
    });
}
function clean() {
    var inputcode = $("#telvalidateCode").val().trim();
    if (inputcode == "请输入手机验证码") {
        $("#telvalidateCode").setValue("");
    }
}
function settime(btn,counts) {
    var num = 0;
    if(counts == 0){
        btn.value = "获取验证码";
        btn.removeAttribute("disabled");
        return;
    }
    btn.setAttribute("disabled", true);
    btn.value = "等待" + counts + "s";
    counts--;

    setTimeout(function() {
        settime(btn,counts);
    }, 1000);
}

// 忘记密码
function forgetPassword() {
    var loginName = $.common.trim($("input[name='username']").val());
    var phoneNumber = $.common.trim($("input[name='phoneNumber']").val());
    var telvalidateCode = $.common.trim($("input[name='telvalidateCode']").val());
    if (null == loginName || "" == loginName) {
        alert('请填写用户名！');
        return;
    }
    if (null == phoneNumber || "" == phoneNumber) {
        alert('请填写手机号！');
        return;
    }
    if (null == telvalidateCode || "" == telvalidateCode) {
        alert('请填写短信验证码！');
        return;
    }
    $.modal.loading($("#forgetPassword").data("loading"));
    $.ajax({
        type: "post",
        url: ctx + "forget",
        data: {
            "username": loginName,
            "phoneNumber": phoneNumber,
            "telvalidateCode": telvalidateCode
        },
        success: function(result) {
            if (result.code == 0) {
                var userId = result.msg;
                $.modal.openNoColse("修改密码", ctx + 'forgetEditPassword/' + userId);
                $.modal.closeLoading();
            } else {
                $.modal.closeLoading();
                $.modal.msg(result.msg);
            }
        }
    });
}