
$(function() {
    validateKickout();
    validateRule();
    $('.imgcode').click(function() {
        var url = ctx + "captcha/captchaImage?type=" + captchaType + "&s=" + Math.random();
        $(".imgcode").attr("src", url);
    });
});

$.validator.setDefaults({
    submitHandler: function() {
        login();
    }
});

function login() {
    var username = $.common.trim($("input[name='username']").val());
    var password = $.common.trim($("input[name='password']").val());
    var validateCode = $("input[name='validateCode']").val();
    // var telvalidateCode = $.common.trim($("input[name='telvalidateCode']").val());
    var rememberMe = $("input[name='rememberme']").is(':checked');
    var loginFlag = $("input[name='loginFlag']:checked").val();
    // if(telvalidateCode == null || telvalidateCode == ""){
    //     alert("请输入短信验证码！");
    //     return;
    // }


    $.modal.loading($("#btnSubmit").data("loading"));
    $.ajax({
        type: "post",
        url: ctx + "login",
        data: {
            "username": username,
            "password": password,
            //"validateCode": validateCode,
           // "telvalidateCode": telvalidateCode,
            "rememberMe": rememberMe,
            "loginFlag": loginFlag
        },
        success: function(result) {
            if (result.code == 0) {
                location.href = ctx + 'index';
            } else {
            	$.modal.closeLoading();
            	$('.imgcode').click();
                $(".code").val("");
            	$.modal.msg(result.msg);
            }
        }
    });
}

function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#signupForm").validate({
        rules: {
            username: {
                required: true
            },
            password: {
                required: true
            },
            telvalidateCode: {
                required: true
            }
        },
        messages: {
            username: {
                required: icon + "请输入您的用户名或手机号",
            },
            password: {
                required: icon + "请输入您的密码",
            },
            /*telvalidateCode: {
                required: icon + "请输入短信验证码",
            }*/
        }
    })
}

function validateKickout() {
    if (getParam("kickout") == 1) {
        layer.alert("<font color='red'>您已在别处登录，请您修改密码或重新登录</font>", {
            icon: 0,
            title: "系统提示"
        },
        function(index) {
            //关闭弹窗
            layer.close(index);
            if (top != self) {
                top.location = self.location;
            } else {
                var url  =  location.search;
                if (url) {
                    var oldUrl  = window.location.href;
                    var newUrl  = oldUrl.substring(0,  oldUrl.indexOf('?'));
                    self.location  = newUrl;
                }
            }
        });
    }
}

function getParam(paramName) {
    var reg = new RegExp("(^|&)" + paramName + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return decodeURI(r[2]);
    return null;
}

function sendnumber(){
    var counts = 60;
    var loginName = $.common.trim($("input[name='username']").val());
    var password = $.common.trim($("input[name='password']").val());
    var loginFlag = $("input[name='loginFlag']:checked").val();
    if (null == loginName || "" == loginName) {
        alert('请填写登录用户名！');
        return;
    }
    if (null == password || "" == password) {
        alert('请填写密码！');
        return;
    }
    var btn = document.getElementById("sendcode");
    btn.disabled = 'disabled';
    $.ajax({
        type: "post",
        url: ctx + "sendcode",
        data: {
            "loginName": loginName,
            "password": password,
            "loginFlag": loginFlag
        },
        success: function(r) {
           if(r == '-2'){
                alert("短信发送失败，请联系管理员！");
                btn.removeAttribute("disabled");
                $("input[name='password']").val("");
                return;
            }
            if(r == '-1'){
                alert("该用户没有设置电话号码！");
                btn.removeAttribute("disabled");
                $("input[name='password']").val("");
                return;
            }
            if(r == '-3'){
                alert("密码错误或账号信息不存在！");
                btn.removeAttribute("disabled");
                $("input[name='password']").val("");
                return;
            }
            if(r == '-4'){
                alert("通过手机号查询到多个用户信息，请选择用户名登录方式获取验证码!");
                btn.removeAttribute("disabled");
                $("input[name='password']").val("");
                return;
            }
            if(r == '-6'){
                alert("短信验证码间隔时间（1分钟）内不可重复获取！");
                btn.removeAttribute("disabled");
                $("input[name='password']").val("");
                return;
            } else {
               // 验证码正常获取需要读秒，防止连续点击
                settime(btn,counts);
            }
        }
    });
}
function clean() {
    var inputcode = $("#telvalidateCode").val().trim();
    if (inputcode == "请输入手机验证码") {
        $("#telvalidateCode").val("");
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

// 跳转到忘记密码页面
function forgetHtml() {
    location.href = ctx + 'forget';
}
