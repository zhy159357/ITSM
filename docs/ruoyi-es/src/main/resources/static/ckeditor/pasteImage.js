//CKEDITOR支持粘贴截图的方法
function CKPasteImage(ckname) {
　　CKEDITOR.instances[ckname].on('instanceReady', function (event) {
    	$("a.cke_button.cke_button__image.cke_button_off").hide();//隐藏工具条中的[图像上传]按钮
	
	　　this.document.on("paste", function (e) {//重写该ckeditor实例的粘贴事件
		　　var items = e.data.$.clipboardData.items;//获取该ckeditor实例的所有剪切板数据
		　　for (var i = 0; i < items.length; ++i) {//循环该数据并只获取类型为image/png格式的数据
		　　　　var item = items[i];
		　　　　if (item.kind == 'file' && item.type == 'image/png') {
		　　　　　　var imgFile = item.getAsFile();
		　　　　　　if (!imgFile) {
		　　　　　　　　return true;
		　　　　　　}
		　　　　　　var reader = new FileReader();
		　　　　　　reader.readAsDataURL(imgFile);//转化为base64格式
		　　　　　　reader.onload = function (e) {//在控件中插入该图片
		　　　　　　		CKEDITOR.instances[ckname].insertHtml('<img src="' + this.result + '" alt="" />');
		　　　　	   }
		　　	   	   return false;
		　　	   }
		　　}
	　　});
　　});
}

//CKEDITOR处理超链接跳转的方法
function CKAddLinks(ckname){
	CKEDITOR.instances[ckname].on('instanceReady', function (event) {
		this.document.on("click", function(event) {
			var e = event.data.$.target;
			if(e.nodeName == "A"){
				var url = e.href;
				if(url.indexOf("knowledge:")!=-1){			
					var id = url.substring(10);
					$.modal.open("知识详情", ctx+"es/detail/"+id);
				}else if(url.indexOf("http:")!=-1 || url.indexOf("https:")!=-1 || url.indexOf("ftp:")!=-1){
					window.open(url, "_blank");
				}	
			}
		})
	})
}

//CKEDITOR高度自适应的方法
function CKFitHeight(ckname){
	var editor = CKEDITOR.instances[ckname];
	editor.on('instanceReady', function (event) {
		var height = editor.window.$.document.body.clientHeight + 60;
		var content = $('#' + editor.container.$.id).find('.cke_contents');
		content.css({height: height});
	});
	editor.on('change', function (event) {
		var height = editor.window.$.document.body.clientHeight + 60;
		var content = $('#' + editor.container.$.id).find('.cke_contents');
		content.css({height: height});
	});
	
}