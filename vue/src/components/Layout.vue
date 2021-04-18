<template>
  <el-container>
    <el-header>
      <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" :router="true">
        <el-submenu index="brand" :show-timeout="0">
          <template slot="title"><span v-text="brand" style="font-size: 20px;vertical-align:baseline;"></span>
          </template>

          <el-menu-item v-if="currentUser.groupId<='2'" @click="updateBalance">
            <i class="el-icon-refresh"></i>重新计算并更新所有结存
          </el-menu-item>

          <el-menu-item @click="onLogout">
            <i class="el-icon-switch-button"></i>退出系统
          </el-menu-item>
        </el-submenu>
        <el-menu-item index="/detail">流水明细</el-menu-item>
        <el-menu-item index="/statistics">统计汇总</el-menu-item>
        <el-menu-item v-if="currentUser.groupId<='2'" style="float: right" @click="openOptionSettingDialog"
                      class="el-icon-setting">选项设置
        </el-menu-item>
        <el-submenu index="excel" v-if="currentUser.groupId<='2'" style="float: right" :show-timeout="0">
          <template slot="title">Excel 操作</template>
          <el-menu-item @click="importFromExcelDialog.visible=true"><i class="el-icon-upload2"></i>导入数据 从Excel
          </el-menu-item>
          <el-menu-item @click="openExportToExcelDialog"><i class="el-icon-download"></i>导出数据 为Excel</el-menu-item>
        </el-submenu>
        <el-menu-item v-if="currentUser.groupId==='1'" style="float: right" @click="$router.push({path: '/rbac'})">
          <i class="el-icon-user"></i>用户管理
        </el-menu-item>
      </el-menu>
    </el-header>


    <el-dialog title="导出数据为 Excel" :visible.sync="exportToExcelDialog.visible"
               @closed="exportToExcelDialog.accountName=null">
      <el-form>
        <el-form-item>
          <el-select v-model="exportToExcelDialog.accountName" placeholder="请选择要导出的账户">
            <el-option v-for="account in exportToExcelDialog.accountList" :key="account.id" :label="account.name"
                       :value="account.name"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" v-show="exportToExcelDialog.accountName">
        <el-button size="medium" @click="exportToExcelDialog.visible = false">取 消</el-button>
        <el-button size="medium" type="primary" @click="exportToExcel">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="导入数据从 Excel" :visible.sync="importFromExcelDialog.visible" @closed="onImportExcelDialogClosed">
      <el-tooltip class="item" effect="dark" content="请务必使用此模板来录入数据" placement="top">
        <el-button @click="getExcelTemplate" style="margin-bottom: 20px">获取最新的 Excel 模板</el-button>
      </el-tooltip>
      <upload ref="uploadExcel" :url="exportToExcelDialog.downloadUrl" :limit="1" :multiple="false" accept=".xlsx"
              :limit-size="1024"></upload>
    </el-dialog>

    <!--    <el-dialog title="用户管理" :visible.sync="userManagementDialog.visible"-->
    <!--               :close-on-click-modal="false"-->
    <!--               @closed="resetUserManagementDialog">-->
    <!--      <el-form ref="userForm" :model="userManagementDialog.user" :rules="userManagementDialog.rules"-->
    <!--               class="demo-ruleForm" status-icon label-width="70px">-->
    <!--        <el-form-item label-width="auto">-->
    <!--          <el-cascader :options="userManagementDialog.cascader.Options" clearable-->
    <!--                       :key="userManagementDialog.cascader.key"-->
    <!--                       :props="userManagementDialog.cascader.props" placeholder="请先选择账号类型"-->
    <!--                       v-model="userManagementDialog.cascader.value" @change="onSelectUsername"></el-cascader>-->
    <!--        </el-form-item>-->
    <!--        <div v-show="userManagementDialog.user.groupId">-->
    <!--          <el-form-item label="用户名" prop="username">-->
    <!--            <el-input v-model="userManagementDialog.user.username"></el-input>-->
    <!--          </el-form-item>-->
    <!--          <el-form-item label="密码" prop="password">-->
    <!--            <el-input type="password" v-model="userManagementDialog.user.password" autocomplete="off"></el-input>-->
    <!--          </el-form-item>-->
    <!--          <el-form-item label="确认密码" prop="checkPassword">-->
    <!--            <el-input type="password" v-model="userManagementDialog.user.checkPassword" autocomplete="off"></el-input>-->
    <!--          </el-form-item>-->
    <!--        </div>-->
    <!--      </el-form>-->
    <!--      <div slot="footer" class="dialog-footer" v-show="userManagementDialog.user.groupId">-->
    <!--        <el-button size="medium" type="warning" @click="updateUser">修 改</el-button>-->
    <!--        <el-button size="medium" v-show="userManagementDialog.user.groupId>0" type="success" @click="addUser">新 增-->
    <!--        </el-button>-->
    <!--        <el-button size="medium" style="float: left" v-show="userManagementDialog.user.groupId>0" type="danger"-->
    <!--                   @click="deleteUser">删 除-->
    <!--        </el-button>-->
    <!--      </div>-->
    <!--    </el-dialog>-->

    <el-dialog title="选项设置" :visible.sync="optionSettingDialog.visible" :close-on-click-modal="false" width="30%">
      <el-card shadow="never">
        <div slot="header">
          <el-input
              placeholder="输入关键字进行过滤" @input="$refs.tree.filter(optionSettingDialog.filterText);"
              v-model="optionSettingDialog.filterText" clearable>
          </el-input>
        </div>
        <el-tree class="filter-tree"
                 :data="optionSettingDialog.tree.data"
                 :props="optionSettingDialog.tree.props"
                 default-expand-all
                 :filter-node-method="filterTreeNode"
                 ref="tree"
                 :expand-on-click-node="false">
          <span class="custom-tree-node" slot-scope="{ node,data }">
            <span>{{ node.label }}</span>
            <span>
              <el-link type="success" @click="addOption(data,node)" v-if="node.level===1"
                       :underline="false">添加</el-link>
              <el-link type="warning" @click="updateOption(data,node)" v-if="node.level===2"
                       :underline="false">修改</el-link>
            </span>
          </span>
        </el-tree>
      </el-card>
    </el-dialog>

    <el-main>
      <router-view :key="routerViewKey"/>
    </el-main>

    <el-backtop :right="10" :visibility-height="1000"><i class="el-icon-s-promotion"></i></el-backtop>
    <el-divider>我是有底线的</el-divider>
  </el-container>

</template>

<script>
import userApi from "@/api/rbac/userApi";
import detailApi from "@/api/detailApi";
import optionApi from "@/api/optionApi";
import upload from "@/components/Upload";

export default {
  components: {upload},
  data() {
    return {
      routerViewKey: 0,//修改此值实现刷新效果
      currentUser: {
        groupId: sessionStorage.getItem('groupId'),
        username: sessionStorage.getItem('username')
      },
      activeIndex: `${this.$route.path}`,
      optionSettingDialog: {
        visible: false,
        filterText: '',
        tree: {
          data: [
            {name: '项目', children: []},
            {name: '银行账户', children: []},
            {name: '部门', children: []},
            {name: '类别', children: []},
          ],
          props: {
            children: 'children',
            label: 'name'
          }
        }
      },
      importFromExcelDialog: {
        visible: false,
      },
      exportToExcelDialog: {
        visible: false,
        accountName: '',
        accountList: [],
        downloadUrl: `${process.env.VUE_APP_BaseURL}/excel/`,
      },
      // userManagementDialog: {
      //   user: {
      //     id: null,
      //     groupId: '',
      //     username: '',
      //     password: '',
      //     checkPassword: ''
      //   },
      //   rules: {
      //     username: [
      //       {validator: this.validateInput, trigger: ['blur', 'change']}
      //     ],
      //     password: [
      //       {validator: this.validateInput, trigger: ['blur', 'change']}
      //     ],
      //     checkPassword: [
      //       {validator: this.validateInput, trigger: 'blur'}
      //     ]
      //   },
      //   visible: false,
      //   cascader: {
      //     key: 0,
      //     value: null,
      //     Options: [
      //       {
      //         value: '0',
      //         label: '管理员',
      //       },
      //       {
      //         value: '1',
      //         label: '用户',
      //       },
      //       {
      //         value: '2',
      //         label: '参观者',
      //       },
      //     ],
      //     props: {
      //       lazy: true,
      //       lazyLoad: this.cascaderLazyLoad
      //     }
      //   },
      // }
    };
  },
  computed: {
    brand() {
      let info = '';
      switch (this.currentUser.groupId) {
        case '1':
          info = '管理员';
          break
        case '2':
          info = '用户'
          break
        case '3':
          info = '参观者'
      }
      return `财务流水管理系统 (${info})`
    }
  },
  created() {
  },

  methods: {
    onImportExcelDialogClosed() {
      const excelUpload = this.$refs.uploadExcel;
      if (excelUpload.successUploadNum > 0) {
        //上传成功之后更新数据
        this.routerViewKey++
      }
      excelUpload.reset()
    },
    getExcelTemplate() {
      window.open(this.exportToExcelDialog.downloadUrl + 'template')
    },
    updateOption(data, node) {
      this.$prompt('请修改选项名', '修改选项', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputValue: data.name
      }).then(({value}) => {
        let promise
        switch (node.parent.label) {
          case '项目':
            promise = optionApi.updateProject({id: data.id, name: value})
            break;
          case '银行账户':
            promise = optionApi.updateAccount({id: data.id, name: value})
            break;
          case '部门':
            promise = optionApi.updateDepartment({id: data.id, name: value})
            break;
          case '类别':
            promise = optionApi.updateCategory({id: data.id, name: value})
            break;
        }
        promise.then(() => {
          this.routerViewKey++
          data.name = value;
          this.$message.success({message: '修改选项成功', showClose: true})
        })
      })
    },
    addOption(data, node) {
      this.$prompt('请输入选项名', '添加选项', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
      }).then(({value}) => {
        let promise
        switch (data.name) {
          case '项目':
            promise = optionApi.addProject(value)
            break;
          case '银行账户':
            promise = optionApi.addAccount(value)
            break;
          case '部门':
            promise = optionApi.addDepartment(value)
            break;
          case '类别':
            promise = optionApi.addCategory(value)
            break;
        }
        promise.then((result) => {
          this.routerViewKey++
          this.$refs.tree.append({id: result.data, name: value}, node)
          this.$message.success({message: '添加选项成功', showClose: true})
        })
      })
    },
    filterTreeNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },

    openOptionSettingDialog() {
      optionApi.getAll().then((result) => {
        const data = this.optionSettingDialog.tree.data;
        data[0].children = result.data.projectOptions
        data[1].children = result.data.accountOptions
        data[2].children = result.data.departmentOptions
        data[3].children = result.data.categoryOptions
      })
      this.optionSettingDialog.visible = true
    },

    onLogout() {
      sessionStorage.clear()
      userApi.logout().then(() => {
        this.$router.replace({path: '/login'})
      })
    },

    updateBalance() {
      this.$confirm('正常情况下结存会自动计算, 此操作用于特殊情况, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        beforeClose: (action, instance, done) => {
          if (action === 'confirm') {
            instance.confirmButtonLoading = true;
            instance.confirmButtonText = '执行中...';
            detailApi.updateBalance()
                .then(() => {
                  this.routerViewKey++
                  this.$message({
                    type: 'success',
                    message: '结存更新成功!'
                  });
                  instance.confirmButtonLoading = false;
                  done();
                })
                .catch(() => {
                  instance.confirmButtonLoading = false
                  done();
                })
          } else {
            done();
          }
        }
      })
    },
    openExportToExcelDialog() {
      optionApi.listAccount().then((result) => {
        this.exportToExcelDialog.accountList = result.data;
        this.exportToExcelDialog.visible = true;
      })
    },

    exportToExcel() {
      const exportToExcelDialog = this.exportToExcelDialog;
      window.open(exportToExcelDialog.downloadUrl + '?accountName=' + encodeURIComponent(exportToExcelDialog.accountName));
      exportToExcelDialog.visible = false;
    },

    // cascaderLazyLoad(node, resolve) {
    //   const {value} = node;
    //   userApi.listUsername(value)
    //       .then((result) => {
    //         const userList = result.data;
    //         let nodes = userList.map((user) => ({
    //           value: user,
    //           label: user.username,
    //           leaf: true
    //         }))
    //         // 通过调用resolve将子节点数据返回，通知组件数据加载完成
    //         resolve(nodes);
    //       })
    //       .catch(() => resolve())
    // },
    //用户管理中选中账号后的操作
    // onSelectUsername() {
    //   let cascaderValue = this.userManagementDialog.cascader.value;
    //   this.userManagementDialog.user.groupId = cascaderValue[0]
    //   this.userManagementDialog.user.username = cascaderValue[1].username;
    //   this.userManagementDialog.user.id = cascaderValue[1].id;
    // },
    //
    // validateInput(rule, value, callback) {
    //   switch (rule.field) {
    //     case 'username':
    //       if (!value) {
    //         callback(new Error('用户名不能为空'));
    //       } else if (value.length < 3) {
    //         callback(new Error('用户名不能少于3位'))
    //       } else if (!/^[a-zA-Z][a-zA-Z0-9_]{2,31}$/.test(value)) {
    //         callback(new Error('用户名只允许[字母,数字,下划线],且不超过32位'));
    //       } else {
    //         callback()
    //       }
    //       break;
    //     case 'password':
    //       if (!value) {
    //         callback(new Error('密码不能为空'));
    //       } else if (value.length < 6) {
    //         callback(new Error('密码不能少于6位'));
    //       } else if (!/^[a-zA-Z0-9_ ]{6,32}$/.test(value)) {
    //         callback(new Error('密码只允许[字母,数字,下划线,空格],且不超过32位'));
    //       } else {
    //         callback()
    //       }
    //       break
    //     case 'checkPassword':
    //       if (!value) {
    //         callback(new Error('请输入密码'));
    //       } else if (value !== this.userManagementDialog.user.password) {
    //         callback(new Error('两次输入密码不一致!'));
    //       } else {
    //         callback();
    //       }
    //   }
    // },
    //
    // updateUser() {
    //   this.$refs.userForm.validate().then(() => {
    //     this.$confirm('此操作将修改此账号, 是否继续?', '提示', {
    //       confirmButtonText: '确定',
    //       cancelButtonText: '取消',
    //       type: 'warning',
    //       beforeClose: (action, instance, done) => {
    //         if (action === 'confirm') {
    //           instance.confirmButtonLoading = true;
    //           instance.confirmButtonText = '执行中...';
    //           this.userManagementDialog.updateLoading = true
    //           userApi.update({...this.userManagementDialog.user})
    //               .then(() => {
    //                 this.$message.success({message: '修改账号成功', showClose: true});
    //                 this.userManagementDialog.visible = false
    //                 instance.confirmButtonLoading = false;
    //                 done();
    //               })
    //               .catch(() => {
    //                 instance.confirmButtonLoading = false
    //                 done();
    //               })
    //         } else {
    //           done();
    //         }
    //       }
    //     })
    //   })
    // },
    //
    // addUser() {
    //   this.$refs.userForm.validate().then(() => {
    //     this.$confirm('此操作将新增一个账号, 是否继续?', '提示', {
    //       confirmButtonText: '确定',
    //       cancelButtonText: '取消',
    //       type: 'warning',
    //       beforeClose: (action, instance, done) => {
    //         if (action === 'confirm') {
    //           instance.confirmButtonLoading = true;
    //           instance.confirmButtonText = '执行中...';
    //           userApi.add({...this.userManagementDialog.user})
    //               .then(() => {
    //                 this.userManagementDialog.visible = false
    //                 this.$message.success({message: '新增账号成功', showClose: true});
    //                 instance.confirmButtonLoading = false;
    //                 done();
    //               })
    //               .catch(() => {
    //                 instance.confirmButtonLoading = false
    //                 done();
    //               })
    //         } else {
    //           done();
    //         }
    //       }
    //     })
    //   })
    // },
    // deleteUser() {
    //   this.$confirm('此操作将永久删除此账号, 是否继续?', '提示', {
    //     confirmButtonText: '确定',
    //     cancelButtonText: '取消',
    //     type: 'warning',
    //     beforeClose: (action, instance, done) => {
    //       if (action === 'confirm') {
    //         instance.confirmButtonLoading = true;
    //         instance.confirmButtonText = '执行中...';
    //         userApi.delete(this.userManagementDialog.user)
    //             .then(() => {
    //               this.$message.success({message: "删除账号成功", showClose: true})
    //               this.userManagementDialog.visible = false
    //               instance.confirmButtonLoading = false;
    //               done();
    //             })
    //             .catch(() => {
    //               instance.confirmButtonLoading = false
    //               done();
    //             })
    //       } else {
    //         done();
    //       }
    //     }
    //   })
    // },
    // resetUserManagementDialog() {
    //   this.$refs.userForm.resetFields();
    //   this.userManagementDialog.cascader.value = null;
    //   this.userManagementDialog.user.groupId = null
    //   //组件key值自增(为了刷新缓存)
    //   ++this.userManagementDialog.cascader.key
    // },
  }
}
</script>

<style>
section {
  background-color: white;
}

/*.transparent .el-input__inner, .transparent .el-range-input {*/
/*  background-color: transparent;*/
/*  color: white !important;*/
/*}*/

/*.transparent .el-range-separator {*/
/*  color: #909399 !important;*/
/*}*/

.el-table .warning-row {
  background: oldlace;
}

.el-table .success-row {
  background: #f0f9eb;
}

.custom-tree-node {
  font-size: 16px;
}

.custom-tree-node a {
  margin-left: 10px;
}
</style>
