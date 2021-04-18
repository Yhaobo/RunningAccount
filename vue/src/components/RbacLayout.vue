<template>
  <el-container>
    <el-aside>
      <el-menu
          :router="true"
          :default-active="activeRoute"
          class="el-menu-vertical-demo"
          @open="handleOpen"
          @close="handleClose"
          background-color="#545c64"
          text-color="#fff"
          active-text-color="#ffd04b"
          style="height:100vh">
        <el-submenu index="1">
          <template slot="title">
            <i class="el-icon-user-solid"></i>
            <span>权限认证管理</span>
          </template>
          <el-menu-item-group title="管理">
            <el-menu-item index="/userAdmin">用户管理</el-menu-item>
            <el-menu-item index="/groupAdmin">用户组管理</el-menu-item>
            <el-menu-item index="/roleAdmin">角色管理</el-menu-item>
          </el-menu-item-group>
          <el-menu-item-group title="分配">
            <el-menu-item index="/roleAssign">角色分配</el-menu-item>
            <el-menu-item index="/permissionAssign">权限分配</el-menu-item>
          </el-menu-item-group>
        </el-submenu>
        <el-submenu index="2">
          <template slot="title">
            <i class="el-icon-guide"></i>
            <span>页面跳转</span>
          </template>
          <el-menu-item @click="$router.push('/')">
            <i class="el-icon-s-home"></i>
            <span slot="title" >返回主页</span>
          </el-menu-item>
          <el-menu-item @click="logout()">
            <i class="el-icon-switch-button"></i>
            <span slot="title" v-text="logoutTitle"></span>
          </el-menu-item>
        </el-submenu>
      </el-menu>
    </el-aside>


    <el-main>
      <router-view :key="routerViewKey"/>
      <!--      <el-divider>我是有底线的</el-divider>-->
    </el-main>

    <el-backtop :right="10" :visibility-height="1000"><i class="el-icon-s-promotion"></i></el-backtop>
    <!--    <el-footer>-->

    <!--    </el-footer>-->

  </el-container>

</template>

<script>

import userApi from "@/api/rbac/userApi";

export default {
  components: {},
  data() {
    return {
      activeRoute: `${this.$route.path}`,
      routerViewKey: 0,
    };
  },
  computed: {
    logoutTitle() {
      return `登出系统（${sessionStorage.getItem("username")}）`
    }
  },
  created() {
  },

  methods: {
    logout() {
      sessionStorage.clear()
      userApi.logout().then(() => {
        this.$router.replace({path: '/login'})
      })
    },
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    }
  }
}
</script>

<style>
section {
  background-color: white;
}


/*.el-table .warning-row {*/
/*  background: oldlace;*/
/*}*/

/*.el-table .success-row {*/
/*  background: #f0f9eb;*/
/*}*/

/*.custom-tree-node {*/
/*  font-size: 16px;*/
/*}*/

/*.custom-tree-node a {*/
/*  margin-left: 10px;*/
/*}*/
</style>
