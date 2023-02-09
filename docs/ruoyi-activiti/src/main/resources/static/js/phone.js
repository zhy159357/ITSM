//电话事件单  点击挂机的挂机时间
function closeTel(){
    var date = new Date();
    var month = date.getMonth() + 1;//月比实际月份要少1
    var strDate = date.getDate();
    var hours = date.getHours();
    var minutes = date.getMinutes();
    var seconds = date.getSeconds();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    if(hours >= 0 && hours <= 9){
        hours = "0" + hours;
    }
    if(minutes >= 0 && minutes <= 9){
        minutes = "0" + minutes;
    }
    if(seconds >= 0 && seconds <= 9){
        seconds = "0" + seconds;
    }
    var closeTime = date.getFullYear()  +""+ month  +""+ strDate+""+ hours  +""+ minutes+""+ seconds;
    return closeTime;
}
