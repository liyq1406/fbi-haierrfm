/**
 * ��ʾ�Ի���
 */
function dialog(url, arg, sfeature) {

    // openWin(url);
    // return;

    var sfeature = sfeature;
    var date = new Date();
    // ����URL��չ������IEû������ÿ��ˢ�£�������ʾ��ҳ������
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
 * ��ȡ����
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
 * ��ȡʱ��
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
