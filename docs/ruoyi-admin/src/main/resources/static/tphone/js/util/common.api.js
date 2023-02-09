/*jslint browser: true, devel: true*/
/*global $ window*/
/**
 *  通用API请求接口
 **/
var CommonApi = {

    ajaxRequestTimeout : 60000,
  // 通用查询ajax请求
  httpGetRequest : function (requestUrl, successHandler, errorHandler) {
    CommonApi.logDebugMessage('request url: ' + requestUrl);
    jQuery.support.cors=true;
    var ajaxGetRequest = $.ajax({
      url: requestUrl,
      type: 'get',
      dataType: 'json',
      async: true,
      timeout: CommonApi.ajaxRequestTimeout,
      beforeSend: function (request) {
        request.setRequestHeader('Content-Type', 'application/json');
      },
      success: function (dbResponse) {
        successHandler(dbResponse);
      },
      error: function (XMLHttpRequest, status, errorThrown) {
        CommonApi.logDebugMessage( '[log info,exception : ' + status + ':' + errorThrown + ']');
        if(errorHandler) {
          errorHandler();
        }
      },
      complete: function (XMLHttpRequest, status) {
        if ('timeout' === status) {
          ajaxGetRequest.abort();
        }
      }
    });
  },
  /**
   * 通用查询post请求
   *
   * @param url
   * @param data
   * @param successHandler
   * @param errorHandler
   */
  httpPostRequest: function (url, data, successHandler, errorHandler) {
    CommonApi.logDebugMessage('request url: ' + url + ', data: ' + JSON.stringify(data));
    if (!data || !url) {
      return;
    }
    if (!data || !url) {
      return;
    }
    jQuery.support.cors = true;
    var ajaxPostRequest = $.ajax({
      data: JSON.stringify(data),
      url: url,
      type: 'post',
      dataType: 'json',
      timeout: CommonApi.ajaxRequestTimeout,
      beforeSend: function (request) {
        request.setRequestHeader('Content-Type', 'application/json');
      },
      success: function (data) {
        CommonApi.logDebugMessage("res："+JSON.stringify(data));
        successHandler(data);
      },
      error: function (XMLHttpRequest, status, errorThrown) {
        if ('timeout' != status) {
          CommonApi.logDebugMessage("exception："+JSON.stringify(data));
        }
        if(errorHandler) {
          errorHandler();
        }
      },
      complete: function (XMLHttpRequest, status) {
        if ('timeout' === status) {
          ajaxPostRequest.abort();
        }
      }
    });
  },
    /**
     * 浮动
     *
     */
    moveObj: function (obj) {
        obj.style.cursor = "move";
        obj.onmousedown = function(e) {
          obj.style.position = "absolute";
          var top = obj.offsetTop;
          var left = obj.offsetLeft;
          var drag_x = document.all ? event.clientX : e.pageX;
          var drag_y = document.all ? event.clientY : e.pageY;
          var selection = CommonApi.disableSelection(document.body);
          var move = function(e) {
            if(left + (document.all ? event.clientX : e.pageX) - drag_x>0){
              obj.style.left = left + (document.all ? event.clientX : e.pageX) - drag_x + "px";
            }
            if(top + (document.all ? event.clientY : e.pageY) - drag_y>0){
              obj.style.top  = top + (document.all ? event.clientY : e.pageY) - drag_y + "px";
            }
            //obj.style.left = left + (document.all ? event.clientX : e.pageX) - drag_x + "px";
            //obj.style.top  = top + (document.all ? event.clientY : e.pageY) - drag_y + "px";
          };
          var stop = function(e) {
            CommonApi.removeEvent(document, "mousemove", move);
            CommonApi.removeEvent(document, "mouseup", stop);
            if(selection) {
              selection();
              delete selection;
            }
          };
          CommonApi.addEvent(document, "mousemove", move);
          CommonApi.addEvent(document, "mouseup", stop);
          selection();
        }
    },
    addEvent: function (target, type, listener, capture) {
        return CommonApi.eventHandler(target, type, listener, true, capture); 
    },

    removeEvent: function (target, type, listener, capture) {
        return CommonApi.eventHandler(target, type, listener, false, capture); 
    },

    eventHandler: function (target, type, listener, add, capture) {
         type = type.substring(0, 2) === "on" ? type.substring(2) : type;
        if(document.all) {
          add ? target.attachEvent("on"+type, listener) : target.detachEvent("on"+type, listener);
        } else {
          capture = (typeof capture === "undefined" ? true : (capture === "false" ? false : ((capture === "true") ? true : (capture ? true : false))));
          add ? target.addEventListener(type, listener, capture) : target.removeEventListener(type, listener, capture);
        }
    },

    disableSelection: function (target) {
        var disable = true;
        var oCursor = document.all ? target.currentStyle["cursor"] : window.getComputedStyle(target, null).getPropertyValue("cursor");
        var returnFalse = function(e) {
          e.stopPropagation();
          e.preventDefault();
          return false;
        };
        var returnFalseIE = function() { return false; };
        var selection = function() {
          if(document.all) {
            disable ? CommonApi.addEvent(target, "selectstart", returnFalseIE) : CommonApi.removeEvent(target, "selectstart", returnFalseIE);
          } else {
            disable ? CommonApi.addEvent(target, "mousedown", returnFalse, false) : CommonApi.removeEvent(target, "mousedown", returnFalse, false);
          }
          target.style.cursor = disable ? "default" : oCursor;
          disable = !disable;
        };
        return selection;
    },
    getBrowserInfo : function () {
      var agent = navigator.userAgent.toLowerCase();
      //var isIE = agent.indexOf("compatible") > -1 && agent.indexOf("msie" > -1); //判断是否IE<11浏览器
      var isIE =  !!window.ActiveXObject || "ActiveXObject" in window
      if (isIE) {
        var reIE = new RegExp("msie (\\d+\\.\\d+);");
        reIE.test(agent);
        return true;
      } //isIE end
      if (!isIE) {
        return false;
      }
    },
    logDebugMessage: function (msg) {
      var date = new Date();
      if(CommonApi.getBrowserInfo()){
        var fso = new ActiveXObject("Scripting.FileSystemObject");
        var month = date.getMonth()+1
        var filename =date.getFullYear()+"-"+month+"-"+date.getDate();
        if(fso.FolderExists(CONFIG.logDir)){
          if(!fso.FileExists(CONFIG.logDir+"\\"+filename+".log")){
            fso.CreateTextFile(CONFIG.logDir+"\\"+filename+".log");
          }
        }else{
          fso.CreateFolder(CONFIG.logDir);
          fso.CreateTextFile(CONFIG.logDir+"\\"+filename+".log")
        }
        var file = fso.OpenTextFile(CONFIG.logDir+"\\"+filename+".log", 8, false);
        file.WriteLine(new Date +"----->"+msg)
        file.Close()
      }else{
        console.log(
          '%c' + new Date +"----->" + '[debug] ' + (msg || ''),
          'background-color: #90ff1e'
        )
      }
    },
  formatSeconds: function (result) {
    var h = Math.floor(result / 3600) < 10 ? '0'+Math.floor(result / 3600) : Math.floor(result / 3600);
    var m = Math.floor((result / 60 % 60)) < 10 ? '0' + Math.floor((result / 60 % 60)) : Math.floor((result / 60 % 60));
    var s = Math.floor((result % 60)) < 10 ? '0' + Math.floor((result % 60)) : Math.floor((result % 60));
    return result = h + ":" + m + ":" + s;
    },
    includeLinkStyle:function (url) {
      var link = document.createElement("link");
      link.rel = "stylesheet";
      link.type = "text/css";
      link.href = url;
      document.getElementsByTagName("head")[0].appendChild(link);
    }
};
if (!window.JSON) {
  window.JSON = {
    parse: function(jsonStr) {
      return eval('(' + jsonStr + ')');
    },
    stringify: function(jsonObj) {
      var result = '',
        curVal;
      if (jsonObj === null) {
        return String(jsonObj);
      }
      switch (typeof jsonObj) {
        case 'number':
        case 'boolean':
          return String(jsonObj);
        case 'string':
          return '"' + jsonObj + '"';
        case 'undefined':
        case 'function':
          return undefined;
      }

      switch (Object.prototype.toString.call(jsonObj)) {
        case '[object Array]':
          result += '[';
          for (var i = 0, len = jsonObj.length; i < len; i++) {
            curVal = JSON.stringify(jsonObj[i]);
            result += (curVal === undefined ? null : curVal) + ",";
          }
          if (result !== '[') {
            result = result.slice(0, -1);
          }
          result += ']';
          return result;
        case '[object Date]':
          return '"' + (jsonObj.toJSON ? jsonObj.toJSON() : jsonObj.toString()) + '"';
        case '[object RegExp]':
          return "{}";
        case '[object Object]':
          result += '{';
          for (i in jsonObj) {
            if (jsonObj.hasOwnProperty(i)) {
              curVal = JSON.stringify(jsonObj[i]);
              if (curVal !== undefined) {
                result += '"' + i + '":' + curVal + ',';
              }
            }
          }
          if (result !== '{') {
            result = result.slice(0, -1);
          }
          result += '}';
          return result;

        case '[object String]':
          return '"' + jsonObj.toString() + '"';
        case '[object Number]':
        case '[object Boolean]':
          return jsonObj.toString();
      }
    }
  };
}

function escapeXml(unsafe) {
  return unsafe.replace(/[<>&'"]/g, function (c) {
    switch (c) {
      case '<': return '&lt;';
      case '>': return '&gt;';
      case '&': return '&amp;';
      case '\'': return '&apos;';
      case '"': return '&quot;';
    }
  });
}
Date.prototype.format = function (format) {
  var args = {
    "M+": this.getMonth() + 1,
    "d+": this.getDate(),
    "h+": this.getHours(),
    "m+": this.getMinutes(),
    "s+": this.getSeconds(),
    "q+": Math.floor((this.getMonth() + 3) / 3), //quarter

    "S": this.getMilliseconds()
  };
  if (/(y+)/.test(format)) format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
  for (var i in args) {
    var n = args[i];

    if (new RegExp("(" + i + ")").test(format)) format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? n : ("00" + n).substr(("" + n).length));
  }
  return format;
};
