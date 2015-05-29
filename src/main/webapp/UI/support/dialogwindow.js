/**
 * 显示对话框
 */
function dialog(url, arg, sfeature) {

    // openWin(url);
    // return;

    var sfeature = sfeature;
    var date = new Date();
    // 设置URL扩展，避免IE没有设置每次刷新，导致显示旧页面内容
    var time = getDateString(date) + " " + getTimeString(date);
    if (url.indexOf("?") > 0) {
        url += "&showDialogTime=" + time;
    } else {
        url += "?&showDialogTime=" + time;
    }
    // alert(url);
    return window.showModalDialog(url, arg, sfeature);
    // return window.open(url);
}

/**
 * 获取日期
 */
function getDateString(date) {
    var years = date.getFullYear();
    var months = date.getMonth() + 1;
    var days = date.getDate();

    if (months < 10)
        months = "0" + months;
    if (days < 10)
        days = "0" + days;

    return years + "-" + months + "-" + days;
}

/**
 * 获取时间
 */
function getTimeString(date) {
    var hours = date.getHours();
    var minutes = date.getMinutes();
    var seconds = date.getSeconds();

    if (hours < 10)
        hours = "0" + hours;
    if (minutes < 10)
        minutes = "0" + minutes;
    if (seconds < 10)
        seconds = "0" + seconds;

    return hours + ":" + minutes + ":" + seconds;
}
