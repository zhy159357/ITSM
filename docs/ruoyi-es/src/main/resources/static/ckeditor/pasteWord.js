var websocket = null; 
var _editor = "";
var flag = false;
//var postUrl = "http://90.19.2.19:9999/knowledgeApi/pwupload";

//wsCreate();

function wsCreate(){
	if(flag == false){
		//判断当前浏览器是否支持WebSocket 
		if ('WebSocket' in window) { 
			websocket = new WebSocket("ws://127.0.0.1:9833"); 
		} else { 
			console.log('当前浏览器 Not support websocket') ;
			return;
		} 
		wsInit();
	}else{
		alert("PasteWord服务端已启动");
	}
	
}
function wsInit(){
	//连接发生错误的回调方法 
	websocket.onerror = function() { 
		//if(confirm("连接发生错误，确定重新连接？")){
		//	wsCreate();
		//}
		console.log("WebSocket连接发生错误"); 
		flag=false;
	}; 
	//连接成功建立的回调方法 
	websocket.onopen = function() { 
		console.log("WebSocket连接成功"); 
		flag=true;
		var postUrl = "";
		$.ajax({
	        async:false,
			url: ctx+"knowledgeApi/getPostUrl",
	        type: "post",
	        success: function (result) {
	        	postUrl=result;
	        }
	    });
		websocket.send("{'postUrl':'"+postUrl+"'}");
	} 
	//接收到消息的回调方法 
	websocket.onmessage = function(event) { 
		//console.log(event.data); 
		handle( event.data );
	} 
	//连接关闭的回调方法 
	websocket.onclose = function() { 
		if(confirm("PasteWord客户端关闭或未启动，确定重新连接？")){
			wsCreate();
		}
		console.log("WebSocket连接关闭"); 
		flag=false;
	} 
	//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。 
	window.onbeforeunload = function() { 
		closeWebSocket(); 
	} 
	//关闭WebSocket连接 
	function closeWebSocket() { 
		websocket.close(); 
	} 

}



function handle(data){
	var json = eval("("+data+")");
	if(json.code=="html"){
		console.log(json.text);
		CKEDITOR.instances[_editor].insertHtml(json.text);
	}else if(json.code=="error"){
		alert(json.text);
	}else if(json.code=="wait"){
		showLoading();
	}else if(json.code=="complete"){
		completeLoading();
	}
}

function CKPasteWord(e){
	_editor = e;
	
	CKEDITOR.instances[e].on('instanceReady', function (event) {
		this.document.on("blur", function (event) {
			//文本框失焦，取消接管粘贴热键
			if(websocket != null){
				websocket.send("{'event':'blur'}");
			}
		})
		this.document.on("focus", function (event) {
			//文本框选中，接管粘贴热键
			if(websocket != null){
				websocket.send("{'event':'focus'}");
			}
		})
	});
}

//======================== Loading =======================
//在页面未加载完毕之前显示的loading Html自定义内容
var _LoadingHtml = '<div id="loadingDiv" style="display: none; "><div id="over" style=" position: absolute;top: 0;left: 0; width: 100%;height: 100%; background-color: #f5f5f5;opacity:0.5;z-index: 1000;"></div><div id="layout" style="position: absolute;top: 40%; left: 40%;width: 20%; height: 20%;  z-index: 1001;text-align:center;">处理中，请等待……</div></div>';
//呈现loading效果
document.write(_LoadingHtml);
 
//移除loading效果
function completeLoading() {  
		document.getElementById("loadingDiv").style.display="none";
}
//展示loading效果
function showLoading()
{
document.getElementById("loadingDiv").style.display="block";
}
