/**
 * 电话条UI工具库
 *
 * @param {object} options - UiKit选项参数设置，传递对象为DOM对象
 * @constructor
 */
var callListUiKit = window.callListUiKit || {};
var CallListUiKit = function () {



  // 变量替换
  var self = this
  self.options = {
    uimode: 'onpage',
    parentid: "main-container"
  }
  self.count = 0
  self.page = 1
  self.size = 10
  self.days = 1
  self.annexList = new Array();
  self.agent = {}
  /**
   * 事件分发器
   * @type {null}
   */
  this.ee = null

  //************************************************************
  //*************************** 公有方法 ************************
  //************************************************************
  /**
   * 初始化按钮事件
   *
   */
  this.init = function (obj) {
    //initsdk
    var options = {
      debug: CONFIG.debug
    }
    callListUiKit.options.parentid = obj.parentid
    callListUiKit.options.uimode = obj.uimode
    callListUiKit.size = obj.size
    callListUiKit.agent = obj.agent
    callListUiKit.days = obj.days
    callListUiKit.independent = obj.independent
    if($("#egooCallList"+obj.agent.agentId).length>0){
      $("#egooCallList"+obj.agent.agentId).show()
      return
    }

    var styleStr = CONFIG.pageStyle.calllistPostion
    var calllistUI = "  <div id=\"egooCallList"+obj.agent.agentId+"\" class=\"egooCallList\" style="+styleStr+">\n" +
      "      <div style=\"position: absolute;text-align: left;color: #B99999;width: 762px;height: 39px;margin-left: 20px;border-bottom: 1px solid #DADADA;font-size: 16px;font-family: Microsoft YaHei;font-weight: bold;color: #0076F1;line-height: 40px\">\n" +
      "        \t外呼历史\n" +
      "      </div>\n" +
      "      <div title=\"关闭\" class=\"close\" onclick=\"callListUiKit.hideCallList()\" style=\"position: absolute;width: 40px;text-align: right;height: 30px;text-align: center;line-height: 40px;right: 0px;cursor: pointer;color: #CCCCCC;\">\n" +
      "      <span class=\"icon iconfont iconguanbi\" style=\"font-size: 12px\"></span>\n" +
      "      </div>\n" +
      "    <div style=\"width: 762px;padding-top: 60px;padding-left: 19px\">\n" +
      "      <label class=\"searchLable\">时间: </label>\n" +
      "      <input class=\"startTime Wdate timeInput\" value=\"\"  onClick=\"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})\" placeholder=\"开始时间\"/>\n" +
      "      <input class=\"endTime Wdate timeInput\" value=\"\" onClick=\"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})\" placeholder=\"结束时间\"/>\n" +
      "      <label class=\"searchLable\">电话号码: </label>\n" +
      "      <input class=\"phonenum\" value=\"\" placeholder=\"电话号码\" onclick=\"this.focus();\">\n" +
      "      <button type=\"button\" onclick=\"callListUiKit.paging()\" class=\"searchBtn\">查询</button>\n" +
      "    </div>\n" +
      "    <table class=\"table table-bordered\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"width: 757px;margin-left: 20px;margin-top: 20px\">\n" +
      "      <tr>\n" +
      "        <th style=\"border-right: 1px solid #dadada\">\n" +
      "          电话号码\n" +
      "        </th>\n" +
      "        <th style=\"border-right: 1px solid #dadada\">\n" +
      "          呼叫时间\n" +
      "        </th>\n" +
      "        <th  style=\"border-right: 1px solid #dadada\">\n" +
      "          通话时长\n" +
      "        </th>\n" +
      "        <th  style=\"border-right: 1px solid #dadada\">\n" +
      "          呼叫方向\n" +
      "        </th>\n" +
      "        <th>\n" +
      "          是否接通\n" +
      "        </th>\n" +
      "      </tr>\n" +
      "      <tbody class=\"annexTbody\">\n" +
      "      <!--动态生成-->\n" +
      "      </tbody>\n" +
      "    </table>\n" +
      "    <div class=\"page\">\n" +
      "        共<span id=\"totalCount"+obj.agent.agentId+"\">0</span>条\n" +
      "        第<span id=\"currentPage"+obj.agent.agentId+"\">1</span>页\n" +
      "        <button type=\"button\" id=\"prePage"+obj.agent.agentId+"\"  onclick=\"callListUiKit.prePage()\" disabled>上一页</button>\n" +
      "        <button type=\"button\" id=\"nextPage"+obj.agent.agentId+"\" onclick=\"callListUiKit.nextPage()\">下一页</button>\n" +
      "    </div>\n" +
      "  </div>"
    $("#"+obj.parentid).append(calllistUI)
    var calllistSmall = "  <div style=\"display: none;"+styleStr+"\" ondblclick=\"callListUiKit.showCallList();\" class=\"egooCallListSmall\" id=\"egooCallList-small"+obj.agent.agentId+"\">\n" +
      "  <em></em>"+
      "  </div>"
    $("#"+obj.parentid).append(calllistSmall)
    if(obj.uimode == "onpage"){
      document.getElementById("egooCallList"+obj.agent.agentId).onmouseover = function () {
        CommonApi.moveObj(this)
      }
      document.getElementById("egooCallList-small"+obj.agent.agentId).onmouseover = function () {
        CommonApi.moveObj(this)
      }
    }
    var curDate = new Date();
    var startTime = new Date(curDate.getTime() - callListUiKit.days*24*60*60*1000).format("yyyy-MM-dd hh:mm:ss");
    var endTime = curDate.format("yyyy-MM-dd hh:mm:ss");
    $(".startTime").val(startTime)
    $(".endTime").val(endTime)
    callListUiKit.paging();
    if(CONFIG.pageStyle.calllistMode == "minimize" && callListUiKit.independent){
      callListUiKit.hideCallList()
    }
    CommonApi.logDebugMessage('calllist version 1.0.0 by tianrui');
  }
  this.hideCallList = function () {
    $("#egooCallList"+callListUiKit.agent.agentId).hide()
    if(callListUiKit.independent){
      $("#egooCallList-small"+callListUiKit.agent.agentId).show()
    }
  }
  this.showCallList = function () {
    $("#egooCallList-small"+callListUiKit.agent.agentId).hide()
    $("#egooCallList"+callListUiKit.agent.agentId).show()
  }
  this.prePage = function () {
    if(callListUiKit.page == 0){
      return
    }
    callListUiKit.page--;
    callListUiKit.paging();
  }
  this.nextPage = function () {
    callListUiKit.page++;
    callListUiKit.paging();
  }
  this.paging = function () {
    var annexHtml ="";
    $(".annexTbody").empty()
    //当前页数
    var startTime = $(".startTime").val()
    var endTime = $(".endTime").val()
    var phone = $(".phonenum").val()
    var url = CONFIG.dnpooServer+"/v1/platform/data/history/getHistoryRecord/"+callListUiKit.agent.tenantId+"/"+callListUiKit.agent.agentId+"?pageNumble="+callListUiKit.page+"&pageSize="+callListUiKit.size+"&ringStartTime="+encodeURI(startTime)+"&ringEndTime="+encodeURI(endTime)+"&phone="+phone
    CommonApi.httpGetRequest(url,function (data) {
      if(data.retCode == 1){
        if(data.data.list && data.data.list.length>0){
          $("#totalCount"+callListUiKit.agent.agentId).text(data.data.totalNumble)
          $("#currentPage"+callListUiKit.agent.agentId).text(data.data.currPage)
          if (data.data.currentPage <= data.data.totalPage) {
            //第一页显示的数量
            $("#nextPage"+callListUiKit.agent.agentId).attr("disabled","disabled");
          } else {
            $("#nextPage"+callListUiKit.agent.agentId).removeAttr("disabled");
          }
          for(var i=0;i<data.data.list.length;i++){
            //获取值
            var phone = data.data.list[i].phone;
            var calltime = data.data.list[i].ringdialtimestamp;
            var handletime = CommonApi.formatSeconds(data.data.list[i].handletime);
            var isEstablished = data.data.list[i].establishedtimestamp?"已接通":"未接通";
            var direction = "呼入";
            if(data.data.list[i].direction == 2){
              direction = "呼出"
            }
            //拼接tr数据
            var className = "oddRows"
            if(i%2){
              className = "evenRows"
            }
            var annexHtml =
              "<tr class='dataline "+className+"'>" +
              "<td style='text-align: center'>"+ phone + "</td>" +
              "<td style='text-align: center'>" + calltime + "</td>" +
              "<td style='text-align: center'>" + handletime +"</td>" +
              "<td style='text-align: center;'>"+direction+"</td>" +
              "<td style='text-align: center;border-right: 0px;'>"+isEstablished+"</td>" +
              "</tr>";
            $(".annexTbody").append(annexHtml);
          }
        }
        $("#currentPage"+callListUiKit.agent.agentId).text(callListUiKit.page);
        if(callListUiKit.page == data.data.totalPage) {
          //到了最后一页不可以点击
          $("#nextPage"+callListUiKit.agent.agentId ).attr("disabled","disabled");
        } else {
          //恢复点击
          $("#nextPage"+callListUiKit.agent.agentId ).removeAttr("disabled");
        }
        if(callListUiKit.page == 1) {
          //到了第一页不可以点击
          $("#prePage"+callListUiKit.agent.agentId ).attr("disabled","disabled");
        } else {
          $("#prePage"+callListUiKit.agent.agentId ).removeAttr("disabled");
        }

      }
    },function () {
      alert("通话记录查询失败")
    })
  }
}
