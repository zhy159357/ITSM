//关联脚本执行情况
$(function () {
    var optionsqq = {
        url: prefix + "/autoList/" + $("#fmId").val(),
        id: "auto-table",
        showSearch: false,
        showRefresh: true,
        showToggle: false,
        showColumns: false,
        pagination: true,
        singleSelect: true,
        clickToSelect: true,
        pageSize: 5,
        toolbar: "toolbar3",
        modalName: "事件单脚本",
        columns: [{
            checkbox: true
        },
            {
                field: 'fbsId',
                title: '脚本记录表',
                visible: false
            },
            {
                field: 'flowId',
                title: '关联id',
                visible: false
            },
            {
                field: 'scriptName',
                title: '脚本名称'
            },
            {
                field: 'executorName',
                title: '执行人'
            },
            {
                field: 'executorGroupName',
                title: '执行工作组'
            },
            {
                field: 'executorTime',
                title: '执行时间',
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
                field: 'resultStatus',
                title: '执行状态'
            }]
    };
    $.table.init(optionsqq);
});

/*查看脚本执行情况详情*/
function detailAuto() {
    var ids = $.table.selectFirstColumns();
    var url = ctx + "system/script/detail/" + ids;
    $.modal.openTab("脚本执行详情", url);
}