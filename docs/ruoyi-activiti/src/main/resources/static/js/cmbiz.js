//调度事件单
$(function () {
    var optionsdd = {
        url: prefix + "/fmddList/" + $("#changeId").val(),
        removeUrl: ctx + "system/relation/remove",
        id: "fmdd-table",
        showSearch: false,
        showRefresh: true,
        showToggle: false,
        showColumns: false,
        pagination: true,
        singleSelect: true,
        clickToSelect: true,
        pageSize: 10,
        toolbar: "toolbar2",
        uniqueId: "params.relationId",
        modalName: "调度事件单",
        columns: [{
            checkbox: true
        }, {
            field: 'params.relationId',
            visible: false
        },
            {
                field: 'fmddId',
                title: '事件单ID',
                visible: false
            },
            {
                field: 'faultNo',
                title: '事件单号',
                formatter: aFormatterFmdd
            },
            {
                field: 'createName',
                title: '创建人'
            },
            {
                field: 'createTime',
                title: '创建时间',
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
    $.table.init(optionsdd);
});
//关联事务事件单初始化
$(function () {
    var optionssw = {
        url: prefix + "/fmswList/" + $("#changeId").val(),
        removeUrl: ctx + "system/relation/remove",
        id: "fmsw-table",
        showSearch: false,
        showRefresh: true,
        showToggle: false,
        showColumns: false,
        pagination: true,
        singleSelect: true,
        clickToSelect: true,
        pageSize: 10,
        toolbar: "toolbar3",
        uniqueId: "params.relationId",
        modalName: "事务事件单",
        columns: [{
            checkbox: true
        }, {
            field: 'params.relationId',
            visible: false
        },
            {
                field: 'fmSwId',
                title: '事件单ID',
                visible: false
            },
            {
                field: 'faultNo',
                title: '事件单号',
                formatter: aFormatterFmsw
            },
            {
                field: 'faultTitle',
                title: '标题'
            },
            {
                field: 'createName',
                title: '创建人'
            },
            {
                field: 'createTime',
                title: '创建时间',
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
    $.table.init(optionssw);
});
//关联问题单初始化
$(function () {
    var optionswt = {
        url: prefix + "/imList/" + $("#changeId").val(),
        removeUrl: ctx + "system/relation/remove",
        id: "im-table",
        showSearch: false,
        showRefresh: true,
        showToggle: false,
        showColumns: false,
        pagination: true,
        singleSelect: true,
        clickToSelect: true,
        pageSize: 10,
        toolbar: "toolbar4",
        uniqueId: "params.relationId",
        modalName: "问题单",
        columns: [{
            checkbox: true
        }, {
            field: 'params.relationId',
            visible: false
        },
            {
                field: 'issuefxId',
                title: '问题单ID',
                visible: false
            },
            {
                field: 'issuefxNo',
                title: '问题单号',
                formatter: aFormatterIm
            },
            {
                field: 'issuefxName',
                title: '标题'
            },
            {
                field: 'reportname',
                title: '创建人'
            },
            {
                field: 'creatTime',
                title: '创建时间',
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
                field: 'currentState',
                title: '状态',
                sortable: true,
                formatter: function (value, row, index) {
                    var re = "";
                    if ("401" == row.hanguptask) {
                        re = "待业务复核;"
                    }
                    return re + selectPubParaValueData(allstatus, value);
                }
            }]
    };
    $.table.init(optionswt);
});
//关联变更请求单初始化
$(function () {
    var optionsqq = {
        url: prefix + "/cmqqList/" + $("#changeId").val(),
        removeUrl: ctx + "system/relation/remove",
        id: "cmqq-table",
        showSearch: false,
        showRefresh: true,
        showToggle: false,
        showColumns: false,
        pagination: true,
        singleSelect: true,
        clickToSelect: true,
        pageSize: 10,
        toolbar: "toolbar5",
        uniqueId: "params.relationId",
        modalName: "变更请求单",
        columns: [{
            checkbox: true
        }, {
            field: 'params.relationId',
            visible: false
        },
            {
                field: 'changeId',
                title: '变更请求单ID',
                visible: false
            },
            {
                field: 'changeCode',
                title: '变更单号',
                formatter: aFormatterBgqq
            },
            {
                field: 'params.createName',
                title: '创建人'
            },
            {
                field: 'addTime',
                title: '创建时间',
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
    $.table.init(optionsqq);
});

/*查看调度事件单详情*/
function detaildd() {
    var selects = $("#fmdd-table").bootstrapTable('getSelections');
    var ids = $.table.selectColumns("fmddId");
    if (ids.length > 1) {
        $.modal.alertWarning("请选择一条记录");
        return;
    }
    var url = ctx + "dispatch/mydispatch/detail/" + ids[0];
    $.modal.openTab("调度事件单详情", url);
}

/*查看事务事件单详情*/
function detailsw() {
    var selects = $("#fmsw-table").bootstrapTable('getSelections');
    var ids = $.table.selectColumns("fmSwId");
    if (ids.length > 1) {
        $.modal.alertWarning("请选择一条记录");
        return;
    }
    var url = ctx + "trans/sw/details/" + ids[0];
    $.modal.openTab("事务事件单详情", url);
}

/*查看问题单详情*/
function detailwt() {
    var selects = $("#im-table").bootstrapTable('getSelections');
    var ids = $.table.selectColumns("issuefxId");
    if (ids.length > 1) {
        $.modal.alertWarning("请选择一条记录");
        return;
    }
    var url = ctx + "issueList/build/view/" + ids[0];
    $.modal.openTab("问题单详情", url);
}

/*查看变更请求单详情*/
function detailqq() {
    var selects = $("#cmqq-table").bootstrapTable('getSelections');
    var ids = $.table.selectColumns("changeId");
    if (ids.length > 1) {
        $.modal.alertWarning("请选择一条记录");
        return;
    }
    var url = ctx + "activiti/qingqiu/detail/" + ids[0];
    $.modal.openTab("变更请求单详情", url);
}

// 回显数据字典
function selectPubParaValueData(datas, value) {
    var actions = [];
    $.each(datas, function (index, pubParaValue) {
        if (pubParaValue.value == ('' + value)) {
            actions.push($.common.sprintf("<span class='%s'>%s</span>", "", pubParaValue.valueDetail));
            return false;
        }
    });
    return actions.join('');
}

//调度事件单号超链接跳转到任务对应处理页面
function aFormatterFmdd(value, row, index) {
    var url = ctx + "dispatch/mydispatch/detail/" + row.fmddId;
    return '<a class="btn btn-warning btn-xs" href="javascript:void(0)" onclick="selectDetailFmdd(\'' + url + '\')">' + row.faultNo + '</a>';
}

function selectDetailFmdd(url) {
    $.modal.openTab("调度事件单详情", url);
}

//事务事件单号超链接跳转到任务对应处理页面
function aFormatterFmsw(value, row, index) {
    var url = ctx + "trans/sw/details/" + row.fmSwId;
    return '<a class="btn btn-warning btn-xs" href="javascript:void(0)" onclick="selectDetailFmsw(\'' + url + '\')">' + row.faultNo + '</a>';
}

function selectDetailFmsw(url) {
    $.modal.openTab("事务事件单详情", url);
}

//問題单号超链接跳转到任务对应处理页面
function aFormatterIm(value, row, index) {
    var url = ctx + "issueList/build/view/" + row.issuefxId;
    return '<a class="btn btn-warning btn-xs" href="javascript:void(0)" onclick="selectDetailIm(\'' + url + '\')">' + row.issuefxNo + '</a>';
}

function selectDetailIm(url) {
    $.modal.openTab("问题单详情", url);
}

//变更请求单号超链接跳转到任务对应处理页面
function aFormatterBgqq(value, row, index) {
    var url = ctx + "activiti/qingqiu/detail/" + row.changeId;
    return '<a class="btn btn-warning btn-xs" href="javascript:void(0)" onclick="selectDetailBgqq(\'' + url + '\')">' + row.changeCode + '</a>';
}

function selectDetailBgqq(url) {
    $.modal.openTab("变更请求单详情", url);
}