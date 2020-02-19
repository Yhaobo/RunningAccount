//加载导航栏
$.get("header.html", function (data) {
    $("#header").html(data);
    //如果为收支明细页面,则显示添加按钮
    if (location.href.indexOf("detail_list.html") >= 0) {
        $('#header_addBtn').removeClass('hidden');
    }
});
// $("#header").load("header.html");




