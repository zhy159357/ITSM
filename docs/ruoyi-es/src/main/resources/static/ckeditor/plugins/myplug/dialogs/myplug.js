var downloadUrl=ctx+'ckeditor/plugins/myplug/pasteword.zip';
	//'http://20.200.89.7:8008/group1/M00/00/59/FMhZCWETQtOAaOxABH5K2AZXG5A711.zip';

(function () {
  function myplugDialog(editor) {
  return {
   title: '确定启动PasteWord功能', //窗口标题
   minWidth: 300,
   minHeight: 80,
   buttons: [/*{
    type: 'button',
    id: 'someButtonID',
    label: 'Button',
    onClick: function () {
     alert('This is a custome button');
    }
    //对话框底部的自定义按钮
   },*/
   CKEDITOR.dialog.okButton, //对话框底部的确定按钮
   CKEDITOR.dialog.cancelButton], //对话框底部的取消按钮
   contents:      //每一个contents在对话框中都是一个tab页
   [
    {
     id: 'tab1',   //contents的id
     label: 'tab1',
     title: 'tab1',
     elements:    //定义contents中的内容，我们这里放一个文本框，id是name
     [
      /*{
       id: 'name',
       type: 'text',
       style: 'width: 50%;',
       label: 'You name',
      }*/{
          id: 'name',
          type: 'html',
          html: '<p>在点击[确定]前请确保您已启动PasteWord客户端程序'
        	  +'<br/>如果您还没有PasteWord客户端，请点击 <a style="color:#00B2CE" href="'+downloadUrl+'">[这里]</a> 下载'
        	  +'<br/><br/>客户端的使用方法您可在解压后阅读《使用说明》文档'
        	  +'</p>'
         }
     ]
    }
   ],
   onLoad: function () {
    //alert('onLoad');
   },
   onShow: function () {
    //alert('onShow');
   },
   onHide: function () {
    //alert('onHide');
   },
   onOk: function () {
    //点击 确定 按钮之后触发的事件
    //var name = this.getValueOf( 'user', 'name' );
    //从界面上取值的方法，getValueOf( 'contents的id', '控件的id' )
    //editor.insertHtml('<p>' + name + ' : Hello world!' + '</p>');
    //将内容放入到editor
    //this.commitContent();
	   wsCreate();
   },
   onCancel: function () {
    //alert('onCancel');
   },
   resizable: CKEDITOR.DIALOG_RESIZE_HEIGHT
  };
 }
 CKEDITOR.dialog.add('myplugDialog', function (editor) {
  return myplugDialog(editor);
 });
})();