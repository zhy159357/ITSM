// gggggg Tech 扩展 v1.0
/**
 * 计算时间差，单位秒
 * @param startDateStr
 * @param endDateStr
 */
function calcTotalSecond(startDateStr, endDateStr) {
  var date1 = new Date(startDateStr);             // 开始时间
  var date2 = new Date(endDateStr);               // 结束时间
  var timeSub = date2.getTime() - date1.getTime();  // 时间差毫秒
  return timeSub / 1000;
}

/**
 * 计算出相差天数
 * @param secondSub
 */
function formatTotalDateSub (secondSub) {
  var days = Math.floor(secondSub / (24 * 3600));     // 计算出小时数
  var leave1 = secondSub % (24*3600) ;                // 计算天数后剩余的毫秒数
  var hours = Math.floor(leave1 / 3600);              // 计算相差分钟数
  var leave2 = leave1 % (3600);                       // 计算小时数后剩余的毫秒数
  var minutes = Math.floor(leave2 / 60);              // 计算相差秒数
  var leave3 = leave2 % 60;                           // 计算分钟数后剩余的毫秒数
  var seconds = Math.round(leave3);
  return days + " 天 " + hours + " 时 " + minutes + " 分 " + seconds + ' 秒';
}

/* 查看审批历史 */
function showHistoryDialog(instanceId) {
  var url = ctx + 'process/historyList/' + instanceId;
  $.modal.open("查看审批历史", url, null, null, null, true);
}

function showProcessImgDialog(instanceId) {
  var url = ctx + 'process/processImg/' + instanceId;
  $.modal.open("查看流程图", url, null, null, null, true);
}

/* 选择用户 */
function selectUser(taskId) {
  var url = ctx + 'user/authUser/selectUser?taskId=' + taskId;
  $.modal.open("关联系统用户", url);
}

function showFormDialog(instanceId, module) {
  var url = ctx + module + "/showFormDialog/" + instanceId;
  $.modal.open('申请详情', url, null, null, null, true);
}
var businessKey='';
function puFolwHistory(id){
  businessKey=id;
  var optionshistory = {
    url:  ctx + "system/log/list",
    id:"history-table",
    showSearch: false,
    showRefresh: false,
    showToggle: false,
    showColumns: false,
    pagination: true,
    pageSize:5,
    queryParams:queryParamsHistory,

    modalName: "流程历史",
    columns: [
      {
        field:'serialNum',
        width:10,
        title:'序号'
      },
      {
        field : 'logId',
        title : '日志id',
        visible: false
      },
      {
        field : 'taskName',
        width:60,
        title : '任务名称',
        formatter:function (value,row,index) {
          var a="<a  onclick=\"viewFlowLog('"+row.logId+"')\">" +value;
          return a;
        }
      },
      {
        field : 'performName',
        width:60,
        title : '操作名称'
      },
      {
        field : 'performDesc',
        title : '处理意见',
        width:100
      },
      {
        field : 'performerName',
        width:40,
        title : '处理人'
      },
      {
        field : 'performerTel',
        width:60,
        title : '联系电话'
      },
      {
        field : 'nextTaskName',
        width:60,
        title : '下一步名称'
      },
      {
        field : 'nextPerformerDesc',
        width:70,
        title : '下一步操作',
        formatter:function (value,row,index) {
          if(value==null||value==""){
            return "-";
          }
          var a="<a  onclick=\"viewFlowLog('"+row.logId+"')\">" +value;
          return a;
        }
      },
      {
        field: 'performerTime',
        width:100,
        title: '操作时间',
        formatter: function (value, row, index) {
          var startTime = '';
          if(value!=''&&value!=null){
            var pattern = /(\d{4})(\d{2})(\d{2})(\d{2})(\d{2})(\d{2})/;
            startTime = value.replace(pattern, '$1-$2-$3 $4:$5:$6');
          }
          return startTime;
        }

      } ]
  };
  $.table.init(optionshistory);
}
function puFolwHistory2(id){
  businessKey=id;
  var optionshistory = {
    url:  ctx + "system/log/list",
    id:"history-table",
    showSearch: false,
    showRefresh: false,
    showToggle: false,
    showColumns: false,
    pagination: true,
    pageSize:5,
    queryParams:queryParamsHistory,

    modalName: "流程历史",
    columns: [
      {
        field:'serialNum',
        title:'序号'
      },
      {
        field : 'logId',
        title : '日志id',
        visible: false
      },
      {
        field : 'taskName',
        title : '任务名称',
        formatter:function (value,row,index) {
          var a="<a  onclick=\"viewFlowLog('"+row.logId+"')\">" +value;
          return a;
        }
      },
      {
        field : 'performName',
        title : '操作名称'
      },
      {
        field : 'performDesc',
        title : '处理意见',
        width:200,
        formatter: function(value, row, index) {
          return $.table.tooltip(value);
        }
      },
      {
        field : 'performerName',
        title : '处理人'
      },
      {
        field:'dutyAccount',
        title:'值班账号'
      },
      {
        field : 'performerTel',
        title : '联系电话'
      },
      {
        field : 'nextTaskName',
        title : '下一步名称'
      },
      {
        field : 'nextPerformerDesc',
        title : '下一步操作',
        formatter:function (value,row,index) {
          if(value==null||value==""){
            return "-";
          }
          var a="<a  onclick=\"viewFlowLog('"+row.logId+"')\">" +value;
          return a;
        }
      },
      {
        field: 'performerTime',
        title: '操作时间',
        formatter: function (value, row, index) {
          var startTime = '';
          if(value!=''&&value!=null){
            var pattern = /(\d{4})(\d{2})(\d{2})(\d{2})(\d{2})(\d{2})/;
            startTime = value.replace(pattern, '$1-$2-$3 $4:$5:$6');
          }
          return startTime;
        }

      },
       {
        field:'sysResidenceTime',
        title:"操作耗时",
        formatter:function(value,row,index){
            if(0==value||null==value){
             return "-";
            }
            var day=Math.floor(value/(24*3600));
            var hour=Math.floor((value-day*24*3600)/3600);
            var minute=Math.floor((value-day*24*3600-hour*3600)/60);
            var second=value-day*24*3600-hour*3600-minute*60;
            var reTime="";
            if(day!=0){
                reTime+=day+"天";
            }
            if(hour!=0){
                reTime+=hour+"小时";
            }
            if(minute!=0){
                reTime+=minute+"分钟";
            }
            if(second!=0){
                reTime+=second+"秒";
            }
            return reTime;
            }
       }
       ]
  };
  $.table.init(optionshistory);
}
function queryParamsHistory(params){
  var curParams = {
    // 传递参数查询参数
    pageSize:       params.limit,
    pageNum:        params.offset / params.limit + 1,
    searchValue:    params.search,
    orderByColumn:  params.sort,
    isAsc:          params.order
  };
  curParams.bizId = businessKey;
  return curParams;
}
function  viewFlowLog(flowLogId) {
  var url=ctx+"system/log/view/"+flowLogId;
  $.modal.openNoBtn("流程日志",url);
}