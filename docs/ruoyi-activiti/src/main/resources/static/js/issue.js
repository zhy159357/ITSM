$(function() {
//附件
    var optionsfj = {
        url: ctx+"pub/attachment/list",
        id:'file-table',
        createUrl: ctx+"pub/attachment/add",
        updateUrl: ctx+"pub/attachment/download/{id}",
        removeUrl: ctx+"pub/attachment/remove",
        sortName: "fileTime",
        sortOrder: "desc",
        toolbar:"toolbar1",
        showSearch: false,
        showRefresh: true,
        showToggle: false,
        showColumns: false,
        singleSelect:true,
         clickToSelect:true,
        queryParams:queryParams,
        modalName: "附件列表",
        columns: [{
            checkbox: true
        },
            {
                field : 'atId',
                title : '附件ID',
                visible : false
            },
            {
                field : 'fileName',
                title : '文件名称'
            },
            {
                field : 'person.pName',
                title : '上传人'
            },
            {
                field : 'size',
                title : '文件大小'
            },
            {
                field : 'memo',
                title : '文件描述'
            },
            {
                field : 'fileTime',
                title : '上传时间',
                 formatter: function (value, row, index) {
                    var startTime = '';
                    if (value != '' && value != null) {
                        var pattern = /(\d{4})(\d{2})(\d{2})(\d{2})(\d{2})(\d{2})/;
                        startTime = value.replace(pattern, '$1-$2-$3 $4:$5:$6');
                    }
                    return startTime;
                }
            }]
    };
    $.table.init(optionsfj);
});
//业务事件单
$(function (){

    var optionsyw = {
        url: prefix + "/ywList",
        id:"yw-table",
        createUrl: prefix + "/add",
        removeUrl:ctx+"system/relation/remove",
        showSearch: false,
        showRefresh: true,
        showToggle: false,
        showColumns: false,
        pagination: true,
         clickToSelect:true,
        singleSelect:true,
        toolbar:"toolbar2",
        uniqueId: "params.relationId",
        queryParams:queryParamsyw,
        modalName: "业务事件单",
        columns: [{
            checkbox: true
        },{
            field : 'params.relationId',
            visible: false
        },
            {
                field : 'fmId',
                title : '主键ID',
                visible: false
            },
            {
                title: "序号",
                formatter: function (value, row, index) {
                    return $.table.serialNumber(index);
                }
            },
            {
                field : 'faultNo',
                title : '事件编号'
            },
            {
                field : 'faultDecriptSummary',
                title : '事件标题',
                formatter: function (value, row, index) {
                    return $.table.tooltip(value);
                }
            },
            {
                field : 'ogSys.caption',
                title : '所属系统',
                formatter: function (value, row, index) {
                    return $.table.tooltip(value);
                }
            },
            {
                field: 'currentState',
                title: '当前状态',
                formatter: function (value) {
                    return selectPubParaValueData(datas, value);
                }
            } ]
    };
    $.table.init(optionsyw);

});
//监控事件单
$(function() {
    var optionsRun = {
        url: prefix + "/runList",
        removeUrl:ctx+"system/relation/remove",
        modalName: "监控事件单",
        showSearch: false,
        showRefresh: true,
        showToggle: false,
        showColumns: false,
         clickToSelect:true,
        pagination: true,
        singleSelect:true,
        uniqueId: "params.relationId",
        toolbar:"toolbar3",
        queryParams:queryParamsRun,
        id:"run-table",
        columns: [{
            checkbox: true
        },{
            field : 'params.relationId',
            visible: false
        },
            {
                field: 'eventId',
                title: '事件单id',
                visible: false
            },
            {
                title: "序号",
                formatter: function (value, row, index) {
                    return $.table.serialNumber(index);
                }
            },
            {
                field : 'eventTitle',
                title : '事件标题',
                formatter: function (value, row, index) {
                    return $.table.tooltip(value);
                }
            },
            {
                field : 'eventNo',
                title : '单号'
            },
            {
                field : 'createTime',
                title : '创建时间'
            },
            {
                field : 'eventSource',
                title : '来源',
                formatter:function (value) {
                    return $.table.selectPubParaValueData(eventSource,value);
                }
            },
            {
                field : 'appSystemName',
                title : '应用系统名称',
                formatter: function (value, row, index) {
                    return $.table.tooltip(value);
                }
            },
            {
                field : 'eventLevel',
                title : '事件等级',
                formatter:function (value) {
                    return $.table.selectPubParaValueData(eventLevel,value);

                }
            },
            {
                field : 'eventType',
                title : '事件类型',
                formatter:function (value) {
                    return $.table.selectPubParaValueData(eventType,value);

                }
            },
            {
                field : 'status',
                title : '事件状态',
                formatter:function (value) {
                    return $.table.selectPubParaValueData(eventStatus,value);

                }
            }
        ]
    };
    $.table.init(optionsRun);
});
//数据变更
$(function() {
    var optionsCm = {
        url: prefix + "/jlauditlist",
        removeUrl:ctx+"system/relation/remove",
        modalName: "数据变更单",
        showSearch: false,
        showRefresh: true,
        showToggle: false,
        showColumns: false,
        pagination: true,
         clickToSelect:true,
        singleSelect:true,
        toolbar:"toolbar4",
        uniqueId: "params.relationId",
        queryParams:queryParamsCm,
        id:"cm-table",
        columns: [{
            checkbox: true
        },{
            field : 'params.relationId',
            visible: false
        },
            {
                field: 'changeSingleId',
                title: '数据变更单Id',
                visible:false
            },
            {
                title: "序号",
                formatter: function (value, row, index) {
                    return $.table.serialNumber(index);
                }
            },
            {
                field: 'changeCode',
                title: '单号'
            },
            {
                field: 'fmTitle',
                title: '标题',
                formatter: function (value, row, index) {
                    return $.table.tooltip(value);
                }
            },
            {
                field: 'changeApplicantName',
                title: '创建人'
            },
            {
                field: 'createTimeText',
                title: '申请时间',
                align: 'center'
            },
            {
                field: 'changeCategoryId',
                title: '数据变更类别',
                formatter: function (value) {
                    return selectPubParaValueData(changeCategoryId_datas, value);
                }
            },
            {
                field: 'changeSingleStatus',
                title: '状态',
                formatter: function (value) {
                    return selectPubParaValueData(changeSingleStatus, value);
                }
            }]
    };
    $.table.init(optionsCm);
});
//资源变更
 $(function() {
            var optionsCheck = {
                url: prefix + "/CmBizListAll",
                removeUrl:ctx+"system/relation/remove",
                 showSearch: false,
                 showRefresh: true,
                 showToggle: false,
                 showColumns: false,
                 pagination: true,
                 singleSelect:true,
                 clickToSelect:true,
                 toolbar:"toolbar5",
                 uniqueId: "params.relationId",
                 id:"CmBiz-table",
                modalName: "资源变更单",
                queryParams:queryParamsCB,
                columns: [{
                    checkbox: true
                },
                {
                    field : 'params.relationId',
                    visible: false
                },
                {
                    field : 'changeId',
                    title : 'id',
                    visible: false
                },
                    {
                        title: "序号",
                        formatter: function (value, row, index) {
                            return $.table.serialNumber(index);
                        }
                    },
                {
                    field : 'changeCode',
                    title : '单号'
                },
                    {
                        field: 'changeSingleName',
                        title: '变更标题',
                        formatter:function (value,row,index) {
                            return $.table.tooltip(value);
                        }
                    },
                    {
                        field: 'applicantName',
                        title: '申请人'
                    },
                    {
                        field: 'expectStartTime',
                        title: '计划变更时间',
                        formatter: function (value, row, index) {
                            var startTime = '';
                            if (value != '' && value != null) {
                                var pattern = /(\d{4})(\d{2})(\d{2})(\d{2})(\d{2})(\d{2})/;
                                startTime = value.replace(pattern, '$1-$2-$3 $4:$5:$6');
                            }
                            return startTime;
                        }
                    },
                    {
                        field: 'changeCategoryName',
                        width: 100,
                        title: '变更分类'
                    },
                    {
                        field: 'changeType',
                        title: '类型',
                        formatter: function (value, row, index) {
                            return $.table.selectPubParaValueData(changeTypeV, value);
                        }
                    },
                    {
                        field: 'changeSingleStauts',
                        title: '状态',
                        formatter: function (value, row, index) {
                            return $.table.selectPubParaValueData(changeStatusV, value);
                        }
                    }]
            };
            $.table.init(optionsCheck);
        });
function queryParams(params) {
    var search = $.table.queryParams(params);
    search.ownerId = $("#issuefxId").val();
    return search;
}

function queryParamsyw(params) {
    var search = $.table.queryParams(params);
    search.fmId=$("#issuefxId").val();
    return search;
}
function queryParamsRun(params) {
    var search = $.table.queryParams(params);
    search.eventId=$("#issuefxId").val();
    return search;
}
function queryParamsCm(params) {
    var search = $.table.queryParams(params);
    search.changeSingleId=$("#issuefxId").val();
    return search;
}
function queryParamsCB(params) {
    var search = $.table.queryParams(params);
    search.changeId=$("#issuefxId").val();
    return search;
}
/*查看业务事件单详情*/
function detailyw(){
    var selects = $("#yw-table").bootstrapTable('getSelections');
    var ids = $.table.selectColumns("fmId");
    if($.common.isEmpty(selects)){
        return $.modal.alertWarning("请选择！");
    }
    var url = ctx+"fmbiz/detail/" + selects[0].fmId;
    $.modal.openTab("业务事件单详情",url);
}
/*查看运行事件单详情*/
function detailRun(){
    var selects = $("#run-table").bootstrapTable('getSelections');
    var ids = $.table.selectColumns("eventId");
    if($.common.isEmpty(selects)){
        return $.modal.alertWarning("请选择！");
    }
    var url = ctx+"activiti/run/view/" + selects[0].eventId;
    $.modal.openTab("监控事件单详情",url);
}
/*查看数据变更单详情*/
function detailCm(){
    var selects = $("#cm-table").bootstrapTable('getSelections');
    var ids = $.table.selectColumns("changeSingleId");
    if($.common.isEmpty(ids)){
        return $.modal.alertWarning("请选择！");
    }
    var url = ctx+"bg/sjbg/details/" + selects[0].changeSingleId;
    $.modal.openTab("数据变更单详情",url);
}
/*查看隐患排查详情*/
function detailCmBiz(){
    var selects = $("#CmBiz-table").bootstrapTable('getSelections');
    var ids = $.table.selectColumns("changeId");
    if($.common.isEmpty(selects)){
        return $.modal.alertWarning("请选择！");
    }
    var url = ctx+"system/single/detail/" + selects[0].changeId;
    $.modal.openTab("资源变更单详情",url);
}
// 回显数据字典
function selectPubParaValueData(datas, value) {
    var actions = [];
    $.each(datas, function(index, pubParaValue) {
        if (pubParaValue.value == ('' + value)) {
            actions.push($.common.sprintf("<span class='%s'>%s</span>", "", pubParaValue.valueDetail));
            return false;
        }
    });
    return actions.join('');
}
// 附件上传页面
function upload() {

    var url = ctx+"pub/attachment/upload/" + $("#issuefxId").val();
    $.modal.open("问题单附件上传", url);
}
// 附件下载
function downloadFile() {
    var rows = $.table.selectFirstColumns();
    var atId = rows[0];
    var ownerId = $("#issuefxId").val();
    var url = ctx + "pub/attachment/download/"+ownerId+"/"+atId;
    window.location.href = url;
}