//加载导航栏
// $.get("header.html", function (data) {
//     $("#header").html(data);
// });
$("#header").load("header.html");

//日期格式化方法
// Date.prototype.format = function(fmt) {
//     var o = {
//         "M+" : this.getMonth()+1,                 //月份
//         "d+" : this.getDate(),                    //日
//         "H+" : this.getHours(),                   //小时
//         "m+" : this.getMinutes(),                 //分
//         "s+" : this.getSeconds(),                 //秒
//         "q+" : Math.floor((this.getMonth()+3)/3), //季度
//         "S"  : this.getMilliseconds()             //毫秒
//     };
//     if(/(y+)/.test(fmt)) {
//         fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
//     }
//     for(var k in o) {
//         if(new RegExp("("+ k +")").test(fmt)){
//             fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
//         }
//     }
//     return fmt;
// };


//日期格式化方法
function dateFormat(date, fmt) { //author: meizz
    var o = {
        "M+": date.getMonth() + 1,                 //月份
        "d+": date.getDate(),                    //日
        "H+": date.getHours(),                   //小时
        "m+": date.getMinutes(),                 //分
        "s+": date.getSeconds(),                 //秒
        "q+": Math.floor((date.getMonth() + 3) / 3), //季度
        "S": date.getMilliseconds()             //毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

//根据传递过来的参数name获取对应的值
function getParameter(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = location.search.substr(1).match(reg);
    if (r != null) return (r[2]);
    return null;
}

//根据传参name获取对应的键和值以及前面的& (&key=value)
function getArg(name) {
    var reg = new RegExp("(^|&)(" + name + "=([^&]*))", "i")
    var r = location.search.substr(1).match(reg);
    if (r) {
        return r[0];
    } else {
        return null;
    }
}