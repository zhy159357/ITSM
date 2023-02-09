
function pinggu(){

    var enuParam =  "<h4 class=\"form-header h4\">评估详情</h4>\n" +
        "<div class=\"row\">\n" +
        "\t\t\t\t\t<div class=\"col-sm-12\">\n" +
        "\t\t\t\t\t\t<div class=\"form-group\">\n" +
        "\t\t\t\t\t\t\t<label class=\"col-xs-2 control-label is-required\">评估意见：</label>\n" +
        "\t\t\t\t\t\t\t<div class=\"col-xs-10\">\n" +
        "\t\t\t\t\t\t\t\t<textarea   name=\"params[comment]\" id=\"comment\" class=\"form-control\" type='text' placeholder=\'文本框内容不得超过64个字\' maxlength='64'></textarea>"+
        "\t\t\t\t\t\t\t</div>\n" +
        "\t\t\t\t\t\t</div>\n" +
        "\t\t\t\t\t</div>\n" +
        "\t\t\t\t</div>";
    $("#page-out").after(enuParam);
    var button1="<button type=\"button\" class=\"btn btn-sm btn-success\" onclick=\"submitPinggu(0)\"><i class=\"fa fa-check\"></i>评估</button>&nbsp;"
    var button2="<button type=\"button\" class=\"btn btn-sm btn-warning \" onclick=\"submitPinggu(1)\"><i class=\"fa fa-edit\"></i>退回</button>&nbsp;"
    $("#buttons-close").before(button1);
    $("#buttons-close").append(button2);
}
function submitPinggu(){
    if(!$.common.isEmpty($("#comment").val())){
        var data=$("#form-issue-view").serializeArray()
       $.operate.saveTab(ctx+"issueList/activiti/pinggu",data);
    }else {
        $.modal.alertWarning("请填写评估意见！");
    }
}
function shenhe(){
    var div = document.getElementById('buttons-view');
    div.className = 'col-sm-offset-4 col-sm-10';
    var enuParam =
        "<h4 class=\"form-header h4\">审核详情</h4>\n" +
        "<div class=\"row\">\n" +
        "\t\t\t\t\t<div class=\"col-sm-12\">\n" +
        "\t\t\t\t\t\t<div class=\"form-group\">\n" +
        "\t\t\t\t\t\t\t<label class=\"col-xs-2 control-label is-required\">审核意见：</label>\n" +
        "\t\t\t\t\t\t\t<div class=\"col-xs-10\">\n" +
        "\t\t\t\t\t\t\t\t<textarea   name=\"params[comment]\" id=\"comment\" class=\"form-control\" rows='5' type='text' placeholder=\'文本框内容不得超过500个字\' maxlength='500'></textarea>"+
        "\t\t\t\t\t\t\t</div>\n" +
        "\t\t\t\t\t\t</div>\n" +
        "\t\t\t\t\t</div>\n" +
        "\t\t\t\t</div>";
    $("#page-out").after(enuParam);
    var button="<button type=\"button\" class=\"btn btn-sm btn-primary\" onclick=\"submitshenhe(0)\"><i class=\"fa fa-check\"></i>通过</button>&nbsp;"+
        "<button type=\"button\" class=\"btn btn-sm btn-success\" onclick=\"submitshenhe(1)\"><i class=\"fa fa-reply-all\"></i>退回修改</button>&nbsp;"
    $("#buttons-close").before(button);

}
function submitshenhe(flag){
    if(!$.common.isEmpty($("#comment").val())){
        var data=$("#form-issue-view").serializeArray()
        var reCode={
            name:'params[reCode]',
            value:flag
        }
        data.push(reCode);
        $.operate.saveTab(ctx+"issueList/activiti/shenhe",data);
    }else {
        $.modal.alertWarning("请填写审核意见！");
    }
}
function fenfa(){

    var enuParam =  "<h4 class=\"form-header h4\">分发信息</h4>\n" +
        "<div class=\"row\">\n" +
        "\t\t\t\t\t<div class=\"col-sm-12\">\n" +
        "\t\t\t\t\t\t<div class=\"form-group\">\n" +
        "\t\t\t\t\t\t\t<label class=\"col-xs-2 control-label is-required\">分发意见：</label>\n" +
        "\t\t\t\t\t\t\t<div class=\"col-xs-10\">\n" +
        "\t\t\t\t\t\t\t\t<textarea   name=\"params[comment]\" id=\"comment\" class=\"form-control\" type='text' placeholder=\'文本框内容不得超过128个字\' maxlength='128'></textarea>"+
        "\t\t\t\t\t\t\t</div>\n" +
        "\t\t\t\t\t\t</div>\n" +
        "\t\t\t\t\t</div>\n" +
        "\t\t\t\t</div>";
    $("#page-out").after(enuParam);
    var button="<button type=\"button\" class=\"btn btn-sm btn-success\" onclick=\"submitFenfa()\"><i class=\"fa fa-check\"></i>确认分发</button>&nbsp;"
    $("#buttons-close").before(button);
}
function submitFenfa(){
    if(!$.common.isEmpty($("#comment").val())){
        var data=$("#form-issue-view").serializeArray()
        $.operate.saveTab(ctx+"issueList/activiti/fenfa",data);
    }else {
        $.modal.alertWarning("请填写分发意见！");
    }
}
function jscl(){
    var div = document.getElementById('buttons-view');
    div.className = 'col-sm-offset-3 col-sm-10';
    var button1="<button type=\"button\" class=\"btn btn-sm btn-success\" data-toggle=\"modal\" data-target=\"#myModal1\" ><i class=\"fa fa-check\"></i>受理</button>&nbsp;"
    var button2="<button type=\"button\" class=\"btn btn-sm btn-success\" data-toggle=\"modal\" data-target=\"#myModal6\" ><i class=\"fa fa-check\"></i>预解决</button>&nbsp;"
   var button3="<button type=\"button\" class=\"btn btn-sm btn-primary\"data-toggle=\"modal\" data-target=\"#myModal2\" ><i class=\"fa fa-reply-all\"></i>退回修改</button>&nbsp;"+
        "<button type=\"button\" class=\"btn btn-sm btn-warning\" data-toggle=\"modal\" data-target=\"#myModal3\"><i class=\"fa fa-reply-all\"></i>转发技术</button>&nbsp;"+
        "<button type=\"button\" class=\"btn btn-sm btn-info\" data-toggle=\"modal\" data-target=\"#myModal4\"><i class=\"fa fa-reply-all\"></i>转发业务</button>&nbsp;"+
        "<button type=\"button\" class=\"btn btn-sm btn-default\"data-toggle=\"modal\" data-target=\"#myModal5\"><i class=\"fa fa-reply-all\"></i>转问题经理</button>&nbsp;"
    if("chuli"==type){
        $("#buttons-close").before(button1+button3);
    }else {
        $("#buttons-close").before(button2+button3);
    }

    var divModel=
        "                    <div class=\"modal inmodal fade\" id=\"myModal1\" tabindex=\"-1\" role=\"dialog\" aria-hidden=\"true\">\n" +
        "                        <div class=\"modal-dialog modal-lg\">\n" +
        "                            <div class=\"modal-content\">\n" +
        "                                <div class=\"modal-header\">\n" +
        "                                    <button type=\"button\" class=\"close\" data-dismiss=\"modal\"><span aria-hidden=\"true\">&times;</span><span class=\"sr-only\">Close</span>\n" +
        "                                    </button>\n" +
        "                                    <h4 class=\"modal-title\">受理确认</h4>\n" +
        "                                    <small class=\"font-bold\">\n" +
        "                                </div>\n" +
        "                                <div class=\"modal-body\">\n" +
        "                                     <h4>受理功能是用于技术经理认领问题确属为所辖应用系统的问题，并进行问题的初步分析。</h4>\n" +
        "                                </div>\n" +
        "                                <div class=\"modal-footer\">\n" +
        "                                    <button type=\"button\" class=\"btn btn-white\" data-dismiss=\"modal\">关闭</button>\n" +
        "                                   <button type=\"button\" class=\"btn btn-primary\" onclick=\"submitjscl(0)\">受理</button>\n" +
        "                         </div>\n" +
        "                            </div>\n" +
        "                        </div>\n" +
        "                    </div>"+
        "                    <div class=\"modal inmodal fade\" id=\"myModal2\" tabindex=\"-1\" role=\"dialog\" aria-hidden=\"true\">\n" +
        "                        <div class=\"modal-dialog modal-lg\">\n" +
        "                            <div class=\"modal-content\">\n" +
        "                                <div class=\"modal-header\">\n" +
        "                                    <button type=\"button\" class=\"close\" data-dismiss=\"modal\"><span aria-hidden=\"true\">&times;</span><span class=\"sr-only\">Close</span>\n" +
        "                                    </button>\n" +
        "                                    <h4 class=\"modal-title\">退回修改</h4>\n" +
        "                                    <small class=\"font-bold\">\n" +
        "                                </div>\n" +
        "                                <div class=\"modal-body\">\n" +
        "                                     <h4>退回修改功能是用于问题单描述不清楚或缺少信息等情况下退回给提单人补全内容使用。</h4>\n" +
        "                                </div>\n" +
        "                                <div class=\"modal-footer\">\n" +
        "                                    <button type=\"button\" class=\"btn btn-white\" data-dismiss=\"modal\">关闭</button>\n" +
        "                                    <button type=\"button\" class=\"btn btn-primary\"  data-toggle=\"modal\" data-target=\"#myModaltuihui\">确认</button>\n" +
        "                                </div>\n" +
        "                            </div>\n" +
        "                        </div>\n" +
        "                    </div>"+
        "                    <div class=\"modal inmodal fade\" id=\"myModal3\" tabindex=\"-1\" role=\"dialog\" aria-hidden=\"true\">\n" +
        "                        <div class=\"modal-dialog modal-lg\">\n" +
        "                            <div class=\"modal-content\">\n" +
        "                                <div class=\"modal-header\">\n" +
        "                                    <button type=\"button\" class=\"close\" data-dismiss=\"modal\"><span aria-hidden=\"true\">&times;</span><span class=\"sr-only\">Close</span>\n" +
        "                                    </button>\n" +
        "                                    <h4 class=\"modal-title\">转发技术</h4>\n" +
        "                                    <small class=\"font-bold\">\n" +
        "                                </div>\n" +
        "                                <div class=\"modal-body\">\n" +
        "                                     <h4>转发技术功能是用于该应用问题解决牵涉关联系统共同分析、排查及配套改造，需对方技术经理给予协同处理。</h4>\n" +
        "                                </div>\n" +
        "                                <div class=\"modal-footer\">\n" +
        "                                    <button type=\"button\" class=\"btn btn-white\" data-dismiss=\"modal\">关闭</button>\n" +
        "                                    <button type=\"button\" class=\"btn btn-primary\" onclick=\"submitjscl(1)\">确认</button>\n" +
        "                                </div>\n" +
        "                            </div>\n" +
        "                        </div>\n" +
        "                    </div>"+
        "                    <div class=\"modal inmodal fade\" id=\"myModal4\" tabindex=\"-1\" role=\"dialog\" aria-hidden=\"true\">\n" +
        "                        <div class=\"modal-dialog modal-lg\">\n" +
        "                            <div class=\"modal-content\">\n" +
        "                                <div class=\"modal-header\">\n" +
        "                                    <button type=\"button\" class=\"close\" data-dismiss=\"modal\"><span aria-hidden=\"true\">&times;</span><span class=\"sr-only\">Close</span>\n" +
        "                                    </button>\n" +
        "                                    <h4 class=\"modal-title\">转发业务</h4>\n" +
        "                                    <small class=\"font-bold\">\n" +
        "                                </div>\n" +
        "                                <div class=\"modal-body\">\n" +
        "                                     <h4>转发业务功能是用于该问题单解决涉及业务部门在业务控制台上处理或需要总行业务指导分行业务进行处理，涉及信息系统的需求变更等，转业务，由总行对口业务部门给出意见。</h4>\n" +
        "                                </div>\n" +
        "                                <div class=\"modal-footer\">\n" +
        "                                    <button type=\"button\" class=\"btn btn-white\" data-dismiss=\"modal\">关闭</button>\n" +
        "                                    <button type=\"button\" class=\"btn btn-primary\" onclick=\"submitjscl(2)\">确认</button>\n" +
        "                                </div>\n" +
        "                            </div>\n" +
        "                        </div>\n" +
        "                    </div>"+
        "                    <div class=\"modal inmodal fade\" id=\"myModal5\" tabindex=\"-1\" role=\"dialog\" aria-hidden=\"true\">\n" +
        "                        <div class=\"modal-dialog modal-lg\">\n" +
        "                            <div class=\"modal-content\">\n" +
        "                                <div class=\"modal-header\">\n" +
        "                                    <button type=\"button\" class=\"close\" data-dismiss=\"modal\"><span aria-hidden=\"true\">&times;</span><span class=\"sr-only\">Close</span>\n" +
        "                                    </button>\n" +
        "                                    <h4 class=\"modal-title\">转问题经理</h4>\n" +
        "                                    <small class=\"font-bold\">\n" +
        "                                </div>\n" +
        "                                <div class=\"modal-body\">\n" +
        "                                     <h4>转问题经理功能是用于问题情况复杂，牵涉多个系统配合，多系统修改等问题，需由问题经理牵头协调解决。</h4>\n" +
        "                                </div>\n" +
        "                                <div class=\"modal-footer\">\n" +
        "                                    <button type=\"button\" class=\"btn btn-white\" data-dismiss=\"modal\">关闭</button>\n" +
        "                                    <button type=\"button\" class=\"btn btn-primary\" onclick=\"submitjscl(4)\">确认</button>\n" +
        "                                </div>\n" +
        "                            </div>\n" +
        "                        </div>\n" +
        "                    </div>\n"+
        "                    <div class=\"modal inmodal fade\" id=\"myModal6\" tabindex=\"-1\" role=\"dialog\" aria-hidden=\"true\">\n" +
        "                        <div class=\"modal-dialog modal-lg\">\n" +
        "                            <div class=\"modal-content\">\n" +
        "                                <div class=\"modal-header\">\n" +
        "                                    <button type=\"button\" class=\"close\" data-dismiss=\"modal\"><span aria-hidden=\"true\">&times;</span><span class=\"sr-only\">Close</span>\n" +
        "                                    </button>\n" +
        "                                    <h4 class=\"modal-title\">预解决</h4>\n" +
        "                                    <small class=\"font-bold\">\n" +
        "                                </div>\n" +
        "                                <div class=\"modal-body\">\n" +
        "                                     <h4>预解决功能是用于经过问题分析，找出解决问题的方案，并对问题有初步的计划解决时间。</h4>\n" +
        "                                </div>\n" +
        "                                <div class=\"modal-footer\">\n" +
        "                                    <button type=\"button\" class=\"btn btn-white\" data-dismiss=\"modal\">关闭</button>\n" +
        "                                    <button type=\"button\" class=\"btn btn-primary\" onclick=\"submitjscl(5)\">预解决</button>\n" +
        "                                </div>\n" +
        "                            </div>\n" +
        "                        </div>\n" +
        "                    </div>\n"+
        "                    <div class=\"modal inmodal fade\" id=\"myModaltuihui\" tabindex=\"-1\" role=\"dialog\" aria-hidden=\"true\">\n" +
        "                        <div class=\"modal-dialog modal-lg\">\n" +
        "                            <div class=\"modal-content\">\n" +
        "                                <div class=\"modal-header\">\n" +
        "                                    <button type=\"button\" class=\"close\" data-dismiss=\"modal\"><span aria-hidden=\"true\">&times;</span><span class=\"sr-only\">Close</span>\n" +
        "                                    </button>\n" +
        "                                    <h4 class=\"modal-title\">退回</h4>\n" +
        "                                    <small class=\"font-bold\">\n" +
        "                                </div>\n" +
        "                                <div class=\"modal-body\">\n" +
        "                                   <label class=\"col-xs-4 control-label is-required \" style='font-size: 20px;font-weight: bold'>退回意见：</label>\n" +
        "                                   <textarea   name=\"tuihuicomment\" id=\"tuihuicomment\" rows='5' class=\"form-control\" type='text' placeholder=\'文本框内容不得超过500个字\' maxlength='500'></textarea>\n"+
        "                                </div>\n" +
        "                                <div class=\"modal-footer\">\n" +
        "                                    <button type=\"button\" class=\"btn btn-white\" data-dismiss=\"modal\">关闭</button>\n" +
        "                                    <button type=\"button\" class=\"btn btn-primary\" data-dismiss=\"modal\" onclick=\"submitjscl(3)\">退回</button>\n" +
        "                                </div>\n" +
        "                            </div>\n" +
        "                        </div>\n" +
        "                    </div>\n"
    $("#buttons-view").after(divModel);
}
function submitjscl(flag){

    //受理
    if(flag==0){
        $.modal.open("受理页面",ctx+"issueList/activiti/goShouli/"+$("#issuefxId").val());
    }
    //退回
    if(flag==3){
        if($.common.isEmpty($("#tuihuicomment").val())){
            $.modal.alert("请填写退回意见！");
            return;
        }
        var data=$("#form-issue-view").serializeArray();
        var comment={
            name:'params[comment]',
            value:$("#tuihuicomment").val()
        };
        var  reCode={
            name:'params[reCode]',
            value:flag
        };
        data.push(comment);
        data.push(reCode);
       $.operate.saveTab(ctx+"issueList/activiti/wentichuli",data,closeItem);
    }
    if(flag==1){
        $.modal.open("转发技术",ctx+"issueList/activiti/goJscl/"+$("#issuefxId").val()+"/"+type);
    }
    if(flag==2){
        $.modal.open("转发业务",ctx+"issueList/activiti/goYwcl/"+$("#issuefxId").val()+"/"+type);
    }
    if(flag==4){
        $.modal.open("转发问题经理",ctx+"issueList/activiti/goWtjl/"+$("#issuefxId").val()+"/"+type);
    }
    if(flag==5){
        $.modal.open("预解决",ctx+"issueList/activiti/goYjj/"+$("#issuefxId").val()+"/"+type);
    }
}
function fuhe(){
    var enuParam =  "<h4 class=\"form-header h4\">处理信息</h4>\n" +
        "<div class=\"row\">\n" +
        "\t\t\t\t\t<div class=\"col-sm-12\">\n" +
        "\t\t\t\t\t\t<div class=\"form-group\">\n" +
        "\t\t\t\t\t\t\t<label class=\"col-xs-2 control-label is-required\">复核意见：</label>\n" +
        "\t\t\t\t\t\t\t<div class=\"col-xs-10\">\n" +
        "\t\t\t\t\t\t\t\t<textarea   name=\"params[comment]\" id=\"comment\" class=\"form-control\" type='text' placeholder=\'文本框内容不得超过64个字\' maxlength='64'></textarea>"+
        "\t\t\t\t\t\t\t</div>\n" +
        "\t\t\t\t\t\t</div>\n" +
        "\t\t\t\t\t</div>\n" +
        "\t\t\t\t</div>";
    $("#page-out").after(enuParam);
    var button="<button type=\"button\" class=\"btn btn-sm btn-success\" onclick=\"submitfuhe()\"><i class=\"fa fa-reply-all\"></i>提交</button>&nbsp;"
    $("#buttons-close").before(button);

}
function submitfuhe(){
    if(!$.common.isEmpty($("#comment").val())){
        var data=$("#form-issue-view").serializeArray()
        $.operate.saveTab(ctx+"issueList/activiti/pinggu",data);
    }else {
        $.modal.alertWarning("请填复核意见！");
        return;
    }
}
function jiejue(){
    $.modal.open("解决",ctx+"issueList/activiti/goYjj/"+$("#issuefxId").val());
}