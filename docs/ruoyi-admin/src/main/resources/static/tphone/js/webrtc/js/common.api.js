/**
 *  通用API请求接口
 **/
const CommonApi = {

  ajaxRequestTimeout: 10000,
  ajaxAsync:true,

  // 通用查询ajax请求
  httpGetRequest: function (requestUrl, successHandler, errorHandler) {
    console.log('request url: ' + requestUrl)
    const ajaxGetRequest = $.ajax({
      url: requestUrl,
      type: 'get',
      dataType: 'json',
      async: CommonApi.ajaxAsync,
      timeout: CommonApi.ajaxRequestTimeout,
      beforeSend: function (request) {
        request.setRequestHeader('Content-Type', 'application/json');
      },
      // xhrFields: {
      //   withCredentials: true
      // },
      success: function (dbResponse) {
        successHandler(dbResponse)
      },
      error: function (XMLHttpRequest, status, errorThrown) {
        if (status !== 'timeout') {
          console.log(new Date() + '[log info,请求异常 : ' + status + ':' + errorThrown + ']')
        }
        if (errorHandler) {
          errorHandler()
        }
      },
      complete: function (XMLHttpRequest, status) {
        if (status === 'timeout') {
          ajaxGetRequest.abort()
        }
      }
    })
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
    console.log('request url: ' + url + ', data: ' + data)
    if (!data || !url) {
      return
    }

    const ajaxPostRequest = $.ajax({
      data: JSON.stringify(data),
      url: url,
      type: 'post',
      dataType: 'json',
      async: CommonApi.ajaxAsync,
      timeout: CommonApi.ajaxRequestTimeout,
      beforeSend: function (request) {
        request.setRequestHeader('Content-Type', 'application/json');
        request.setRequestHeader('tenantId', Utils.cookie.readCookie('tenantId') || window.CONFIG.tenantId);
      },
      success: function (data) {
        successHandler(data)
      },
      error: function (XMLHttpRequest, status, errorThrown) {
        if (status !== 'timeout') {
          console.log(new Date() + '[log info,请求异常 : ' + status + ':' + errorThrown + ']')
        }
        if (errorHandler) {
          errorHandler()
        }
      },
      complete: function (XMLHttpRequest, status) {
        if (status === 'timeout') {
          ajaxPostRequest.abort()
        }
      }
    })
  }
}
