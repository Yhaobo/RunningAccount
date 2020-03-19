//加载导航栏
$.get("header.html", function (data) {
    $("#header").html(data);


    // //获取所有项目选项
    // $.get("setting/findProjects", function (data) {
    //     let projects = data.data;
    //     var html = '';
    //     if (data.flag) {
    //         for (var i = 0; i < projects.length; i++) {
    //             var item = projects[i];
    //             html += '<li><a href="monthly_list.html?p_id=' + item.id + '&title=' + item.name + '每月汇总">' + item.name + '</a></li>';
    //         }
    //         $("#header_project").html(html);
    //         $("#projectFilter").html(html);
    //     }
    // });
    // //获取所有分类选项
    // $.get("setting/findCategorys", function (data) {
    //     let categorys = data.data;
    //     var html = '';
    //     if (data.flag) {
    //         for (var i = 0; i < categorys.length; i++) {
    //             var item = categorys[i];
    //             html += '<li><a href="monthly_list.html?c_id=' + item.id + '&title=' + item.name + '每月汇总">' + item.name + '</a></li>';
    //         }
    //         $("#header_category").html(html);
    //         $("#categoryFilter").html(html);
    //     }
    // });
    // //获取所有部门选项
    // $.get("setting/findDepartments", function (data) {
    //     let departments = data.data;
    //     var html = '';
    //     if (data.flag) {
    //         for (var i = 0; i < departments.length; i++) {
    //             var item = departments[i];
    //             html += '<li><a href="monthly_list.html?dep_id=' + item.id + '&title=' + item.name + '每月汇总">' + item.name + '</a></li>';
    //         }
    //         $("#header_department").html(html);
    //         $("#departmentFilter").html(html);
    //     }
    // });
    // //获取所有账户选项
    // $.get("setting/findAccounts", function (data) {
    //     let accounts = data.data;
    //     var html = '';
    //     if (data.flag) {
    //         for (var i = 0; i < accounts.length; i++) {
    //             var item = accounts[i];
    //             html += '<li><a href="monthly_list.html?a_id=' + item.id + '&title=' + item.name + '每月汇总">' + item.name + '</a></li>';
    //         }
    //         $("#header_account").html(html);
    //         $("#accountFilter").html(html);
    //     }
    // });

});
// $("#header").load("header.html");




