
###项目描述:          
简单的企业财务流水管理系统; 前后端分离开发, 通过Restful风格来进行数据交互;  
前端为 Vue + Element-UI 构建的单页面应用SPA;  
后端为 SpringBoot + MyBatis-plus, 权限认证管理基于RBAC模型, 使用Shiro框架实现;  

功能上主要有 明细模块, 统计模块, Excel模块, 权限认证模块

####涉及技术: 
JDK1.8, SpringBoot, Shiro, MyBatis-plus, Vue, Axios, Element-UI, ECharts, Apache-POI等

####开发工具: 
IDEA, Maven, MySQL8.0, Navicat, Git, Vue-cli

####具体功能:

1.流水明细按条件筛选

2.与每条记录相关的查看,上传,删除图片信息功能

3.添加或修改记录后保证所有记录的结存一致性(包括修改记录日期)

4.统计汇总按条件筛选

5.统计汇总数据可视化(图表)

6.财务数据自动备份(用户退出系统时)

7.用户认证和权限管理, 基于RBAC模型（用户<一对多>用户组<多对多>角色<多对多>权限）

8.可导出流水记录为Excel, 或从Excel中导入新记录

9.可选择记录生成费用报销单(Excel格式)
