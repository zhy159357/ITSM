// 自定义validator

// maxlength2
// 区别于JQuery.validator.maxlength，汉字按两个字符计算
jQuery.validator.addMethod("maxlength2",function(value,element,param){
        	var length = 0;
        	var flag = false;
//        	switch(element.nodeName.toLowerCase()){
//        		case 'select':
//        			length = $("option:selected",element).length;
//        			flag = true;
//        		case 'input':
//        			if(this.chekcable(element)){
//        				length = this.findByName(element.name).filter(':checked').length;
//        				flag = true;
//        			}
//        	}
        	if(!flag){
        		length = value.length;
        		for(var i =0 ; i < value.length; i++){
        			if(value.charCodeAt(i) > 127){
        				length++;
        			}
        		}
        	}
        	return length <= param;
        },jQuery.validator.format("最大长度为{0}个字符(中文算2个字符)"));