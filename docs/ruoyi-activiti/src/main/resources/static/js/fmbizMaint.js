$(function () {
 //alert("哈哈哈");
});

function isPoneAvailable(phone) {
    var reg = /^[1][3,4,5,7,8][0-9]{9}$/;
    if (!reg.test(phone)) {
        return false;
    } else {
        return true;
    }
}

/**
 * 根据客户名称or手机号码 查询数据库中是否存在，如果存在多条名称相同的先暂时取第一条数据的相关信息回显
 */
function checkCustInfo(){
    var cName = $("#customerName").val();
    var cPhone = $("#cPhone").val();
    if(cPhone!=""){
        if(!isPoneAvailable(cPhone)){
            $.modal.alertWarning("手机号不合法，请重新输入");
            return;
        }
    }

    $("#cDept").val("");
    $("#cPost").val("");
    $("#cAddress").val("");

    if(cName!="" || cPhone!=""){
        $.ajax({
            cache: true,
            type: "POST",
            url: prefix_cust + "/checkCustInfo",
            data: {
                "cName": cName,
                "cPhone": cPhone
            },
            async: false,
            success: function (data) {
                console.log(data);
                if(data.length>0){
                    if(cName==""){
                        $("#customerName").val(data[0].cName);
                    }
                    if(cPhone==""){
                        $("#cPhone").val(data[0].cPhone);
                    }
                    $("#cDept").val(data[0].cDept);
                    $("#cPost").val(data[0].cPost);
                    $("#cAddress").val(data[0].cAddress);
                }
            }
        });
    }
}


//事件类型触发
function checkType(){
    var recordType =  $("#recordType").val();
    if(recordType==2){
        $("#tj1").hide();
        $("#zc").hide();
        $("#tj2").show();
    }else{
        $("#tj1").show();
        $("#zc").show();
        $("#tj2").hide();
    }
}

//标题监听事件
var btBsSuggest = $("#faultDecriptSummary").bsSuggest({
    url: ctx + "fmbizmaint/selectList/1",
    idField: "mTitle",    //id字段
    keyField: "mDetail",   //key字段
    allowNoKeyword: false, //是否允许无关键字时请求数据
    effectiveFields:["mTitle","mDetail"],
    effectiveFieldsAlias:{mTitle: "事件标题",mDetail:"事件描述"},
    searchingTip: '搜索中...',
    hideOnSelect: true, // 鼠标从列表单击选择了值时，是否隐藏选择列表
    inputWarnColor: '#fff', // 输入框内容不是下拉列表选择时的警告色
    autoMinWidth: true,  // 是否自动最小宽度，设为 false 则最小宽度不小于输入框宽度
    listAlign: 'auto',  // 提示列表对齐位置，left/right/auto
    showBtn: false,
    indexId: 2,
    indexKey: 1
}).on('onDataRequestSuccess', function (e, result) {
    // console.log('onDataRequestSuccess: ', result);
}).on('onSetSelectValue', function (e, keyword,data) { //data是后台传过来的字段
    // console.log('onSetSelectValue: ', keyword,data);
    $("#faultDecriptSummary").val(data.mTitle);
    $("#faultDecriptDetail").val(data.mDetail);
}).on('onUnsetSelectValue', function (e) {
    // console.log("onUnsetSelectValue");
});

var btBsSuggest2 = $("#faultDecriptDetail").bsSuggest({
    url: ctx + "fmbizmaint/selectList/2",
    allowNoKeyword: false, //是否允许无关键字时请求数据
    searchingTip: '搜索中...',
    hideOnSelect: true, // 鼠标从列表单击选择了值时，是否隐藏选择列表
    inputWarnColor: '#fff', // 输入框内容不是下拉列表选择时的警告色
    autoMinWidth: true,  // 是否自动最小宽度，设为 false 则最小宽度不小于输入框宽度
    listAlign: 'auto',  // 提示列表对齐位置，left/right/auto
    showBtn: false,
    indexId: 2,
    indexKey: 2 //查询出来的第几个字段
}).on('onDataRequestSuccess', function (e, result) {
    // console.log('onDataRequestSuccess: ', result);
}).on('onSetSelectValue', function (e, keyword) {
    // console.log('onSetSelectValue: ', keyword);
}).on('onUnsetSelectValue', function (e) {
    // console.log("onUnsetSelectValue");
});