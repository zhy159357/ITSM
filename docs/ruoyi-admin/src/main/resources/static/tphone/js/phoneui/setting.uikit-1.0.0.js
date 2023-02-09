/**
 * 电话条UI工具库
 *
 * @param {object} options - UiKit选项参数设置，传递对象为DOM对象
 * @constructor
 */
var settingUiKit = window.settingUiKit || {};
var SettingUiKit = function () {



  // 变量替换
  var self = this
  self.options = {
    uimode: 'onpage',
    parentid: "main-container"
  }
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
    settingUiKit.options.parentid = obj.parentid
    settingUiKit.options.uimode = obj.uimode
    settingUiKit.agent = obj.agent
    settingUiKit.independent = obj.independent

    var egooSettingPag = "<div id=\"egooSettingPage"+obj.agent.agentId+"\" class=\"egooSettingPage\" style=\"top: 10%; left: 20%; cursor: move; position: absolute;\">\n" +
      "      <div style=\"position: absolute;text-align: left;color: #B99999;width: 515px;height: 39px;margin-left: 20px;border-bottom: 1px solid #DADADA;font-size: 16px;font-family: Microsoft YaHei;font-weight: bold;color: #0076F1;line-height: 40px\">\n" +
      "        \t设置\n" +
      "      </div>\n" +
      "      <div title=\"关闭\" class=\"close\" onclick=\"settingUiKit.closeSettingPage()\" style=\"position: absolute;width: 40px;text-align: right;height: 30px;text-align: center;line-height: 40px;right: 0px;cursor: pointer;color: #CCCCCC;\">\n" +
      "      <span class=\"icon iconfont iconguanbi\" style=\"font-size: 12px\"></span>\n" +
      "      </div>\n" +
      "      \n" +
      "    <div style=\"width: 530px;height:420px;margin-top: 50px;\">\n" +
      "\t\t<div style=\"float: left;border-right:1px solid #E9E9E9;height: 100%;\">\n" +
      "\t\t\t<ul class=\"J_tabs tab_but cu_li\" id=\"egooSettinglei\">\n" +
      "\t\t\t    <li class=\"current\" name=\"line\">线路设置</li>\n" +
/*      "\t\t\t    <li name=\"answer\">接听设置</li>\n" +*/
      "\t\t\t</ul>\n" +
      "\t\t</div>\n" +
      "\t\t<div style=\"margin-left: 180px;width: 480px;\" class=\"settingContent line\">\n" +
      "\t\t\t<div style=\"padding: 5px;\">\n" +
      "\t\t\t\t<label class=\"labelTitle\">线&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;路: </label>\n" +
      "\t\t\t\t<select class=\"egooSettingPagSuppliers\" onchange=\"settingUiKit.getCurrentLineInfo()\">\n" +
      "\t\t\t\t</select>\n" +
      "\t\t\t</div>\n" +
      "\t\t\t<div style=\"padding: 5px;\">\n" +
      "\t\t\t\t<label class=\"labelTitle\">外线方式: </label>\n" +
      "\t\t\t\t<select class=\"egooSettingPagAppearance\" onchange=\"settingUiKit.showAppearanceNum()\" >\n" +
      "\t\t\t\t  <option value=\"numberPoolAppearance\">随机号码</option>\n" +
      "\t\t\t\t  <option value=\"fixedAppearance\">固定号码</option>\n" +
      "\t\t\t\t</select>\n" +
      "\t\t\t</div>\n" +
      "\t\t\t<div style=\"padding: 5px;display: none\">\n" +
      "\t\t\t\t<label class=\"labelTitle\">外线号码: </label>\n" +
      "\t\t\t\t<select class=\"egooSettingPagAppearanceNum\" onchange=\"settingUiKit.curAppearanceInfo()\">\n" +
      "\t\t\t\t</select>\n" +
      "\t\t\t</div>\n" +
/*      "\t\t\t<div style=\"padding: 5px;width: 350px;text-align: center;\">\n" +
      "\t\t\t\t<div style=\"width: 100px;height: 30px;border:1px solid #FFFFFF;background-color: #FFFFFF;float: left;line-height: 30px;border-radius:5px ;\">取消</div>\n" +
      "\t\t\t\t<div style=\"width: 100px;height: 30px;border:1px solid #1AAD19;background-color: #1AAD19;float: left;line-height: 30px;color: #FFFFFF;border-radius:5px ;margin-left: 20px;\">确定</div>\n" +
      "\t\t\t</div>\n" +*/
      "\t\t</div>\n" +
      "\t\t<div style=\"float: left;display: none;\" class=\"settingContent answer\">接听设置</div>\n" +
      "    </div>\n" +
      "  </div>"
    $("#"+obj.parentid).append(egooSettingPag)
    $("#egooSettinglei li").click(function() {
      $(this).siblings('li').removeClass('current');  // 删除其他兄弟元素的样式
      $(this).addClass('current');                            // 添加当前元素的样式
    })
    $("li").click(function(){
      $(".settingContent").hide()
      var option=$(this).attr("name");
      $("."+option).show()
    });
    $("#egooSettingPage"+obj.agent.agentId).hide()
    settingUiKit.getSuppliers();
  }
  this.getCurrentLineInfo = function () {
    try {
      if(!CONFIG.internalManagement){
        return
      }
      var supplier = $(".egooSettingPagSuppliers") .val()
      var url = CONFIG.internalManagement+"/eurm/if/data/transfer/list.ptc"
      var param = {
        "dataType": "NumberPool",
        "filters": [
          {
            "name": "department",
            "type": "0",
            "value": settingUiKit.agent.dept
          },
          {
            "name": "supplier",
            "type": "0",
            "value": supplier
          }
        ],
        "offset": 0,
        "limit": 200
      }
      CommonApi.httpPostRequest(url,param,function (data) {
        console.log(JSON.stringify(data))
        if(data.head.errorCode == 0){
          var dataArr = data.info.data
          CONFIG.LINEINFO.lines = dataArr
          $(".egooSettingPagAppearanceNum").empty()
          for(var i=0;i<dataArr.length;i++){
            var option = "<option value=\""+dataArr[i].UUID+"\">"+dataArr[i].fixedAppearance+"</option>"
            $(".egooSettingPagAppearanceNum").append(option)
          }
          settingUiKit.getLineInfo()
        }
      },function () {
        console.log("error")
      })
    }catch (e) {

    }
  }
  this.getSuppliers = function () {
      $(".egooSettingPagSuppliers").empty()
      var suppliers = CONFIG.LINEINFO.suppliers
      for(var i=0;i<suppliers.length;i++){
        var option = "<option value=\""+suppliers[i].id+"\">"+suppliers[i].supplierName+"</option>"
        $(".egooSettingPagSuppliers").append(option)
        if(i==0){
          CONFIG.curSupplier = suppliers[i].id
          settingUiKit.getCurrentLineInfo(settingUiKit.agent.curSupplier);
        }
      }
      $(".egooSettingPagAppearance").val(CONFIG.LINEINFO.appearanceType)
      settingUiKit.showAppearanceNum()
  }
  this.showAppearanceNum = function(){
    CONFIG.LINEINFO.appearanceType = $(".egooSettingPagAppearance").val()
    if($(".egooSettingPagAppearance").val() == "fixedAppearance"){
      $(".egooSettingPagAppearanceNum").parent().show()
    }else{
      $(".egooSettingPagAppearanceNum").parent().hide()
    }
    settingUiKit.getLineInfo()
  }
  this.curAppearanceInfo = function(){
    var curUUID = $(".egooSettingPagAppearanceNum").val()
    for(var i=0;i<CONFIG.LINEINFO.lines.length;i++){
        if(CONFIG.LINEINFO.lines[i].UUID == curUUID){
          CONFIG.LINEINFO.fixedAppearanceInfo = CONFIG.LINEINFO.lines[i]
          return
        }
    }
  }
  this.getLineInfo = function(){
    var obj = {}
    var item = {}
      //外显
      if(CONFIG.LINEINFO.appearanceType == "fixedAppearance"){
        var curUUID = $(".egooSettingPagAppearanceNum").val()
        for(var i=0;i<CONFIG.LINEINFO.lines.length;i++){
          if(CONFIG.LINEINFO.lines[i].UUID == curUUID){
            CONFIG.LINEINFO.fixedAppearanceInfo = CONFIG.LINEINFO.lines[i]
            item =  CONFIG.LINEINFO.fixedAppearanceInfo
            obj.appearance = item.fixedAppearance
          }
        }
      }else{
        item = CONFIG.LINEINFO.lines[Math.floor(Math.random()*CONFIG.LINEINFO.lines.length)];
        obj.appearance = item.numberPoolAppearance
      }
      //前缀
      obj.prefix = item.prefix
    return obj
  }
  this.closeSettingPage = function () {
    $("#egooSettingPage"+settingUiKit.agent.agentId).hide()
  }
  this.showSettingPage = function () {
    $("#egooSettingPage"+settingUiKit.agent.agentId).show()
  }

}
