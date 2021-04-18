<template>
  <el-card class="login-card">
    <div slot="header">
      <span>欢迎使用企业财务流水管理系统</span>
    </div>
    <CanvasNest></CanvasNest>
    <div @keypress.enter="onSubmit">
      <el-form label-position="right" label-width="auto" :model="loginForm" class="transparent">
        <el-form-item label="用户名">
          <el-input v-model="loginForm.username" clearable autofocus></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="loginForm.password" show-password clearable></el-input>
        </el-form-item>
        <el-form-item label-width="0">
          <el-button type="success" @click="onSubmit" :loading="loading">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </el-card>
</template>

<script>
import CanvasNest from "@/components/CanvasNest";
import userApi from "@/api/rbac/userApi";

export default {
  name: 'login',
  components: {
    CanvasNest
  },
  data() {
    return {
      loginForm: {
        username: '',
        password: '',
      },
      loading: false,
    }
  },
  methods: {
    onSubmit() {
      this.loading = true;
      userApi.login({...this.loginForm})
          .then((result) => {
            sessionStorage.setItem("username", result.data.username)
            sessionStorage.setItem("groupId", result.data.groupId);
            let redirect = this.$route.query.redirect;
            if (redirect) {
              this.$router.replace({path: redirect + ''});
            } else {
              this.$router.replace({path: '/'});
            }
          })
          .catch(() => this.loading = false)
    }
  }
}

</script>

<style>
.login-card .el-card__header {
  font-size: 20px;
  color: white;

}

.login-card {
  width: 473px;
  margin: 15% auto;
  background: rgb(0, 0, 0, 0.6);
  /*color: black;*/
  text-align: center;
}

.transparent .el-input__inner, .transparent .el-form-item__label {
  background-color: transparent;
  color: white;
}

</style>