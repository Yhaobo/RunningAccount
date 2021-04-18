<template>
  <div>
    <el-card shadow="hover">
      <div slot="header">
        <el-input
            placeholder="输入关键字进行过滤" @input="$refs.tree.filter(filterText);"
            v-model="filterText" clearable>
        </el-input>
      </div>
      <el-tree class="filter-tree" @node-click="alterOrCheck" :indent="30"
               draggable
               :allow-drop="treeAllowDrop"
               :allow-drag="treeAllowDrag"
               @node-drop="handleTreeNodeDrop"
               :data="tree.data"
               :props="tree.props"
               default-expand-all
               :filter-node-method="filterTreeNode"
               ref="tree"
               :highlight-current="true"
               :expand-on-click-node="false">
          <span class="custom-tree-node" slot-scope="{ node,data }">
            <span slot="reference">{{ node.label }}</span>
            <span>
              <el-link type="success" @click.stop="addUser(data,node)" v-if="node.level===2"
                       :underline="false">新增用户</el-link>
              <el-link type="warning" @click.stop="alterUser(data,node)" v-if="node.level===3"
                       :underline="false">修改此用户</el-link>
              <el-link type="danger" @click.stop="deleteUser(data,node)" v-if="node.level===3&&data.id!=='1'"
                       :underline="false">删除此用户</el-link>
            </span>
          </span>
      </el-tree>
    </el-card>
    <el-dialog title="修改或查看用户" :visible.sync="alterUserDialog.visible" :close-on-click-modal="false"
               @closed="$refs.alterUserForm.resetFields();alterUserDialog.user.groupId='';">
      <el-form ref="alterUserForm" :model="alterUserDialog.user" :rules="addUserDialog.rules"
               class="demo-ruleForm" status-icon label-width="70px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="alterUserDialog.user.username"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="password">
          <el-input type="password" v-model="alterUserDialog.user.password" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPassword">
          <el-input type="password" v-model="alterUserDialog.user.checkPassword" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="medium" @click="alterUserDialog.visible = false">取 消</el-button>
        <el-button size="medium" type="primary" @click="submitAlterUser()">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="新增用户" :visible.sync="addUserDialog.visible" :close-on-click-modal="false"
               @closed="$refs.addUserForm.resetFields();addUserDialog.user.groupId='';">
      <el-form ref="addUserForm" :model="addUserDialog.user" :rules="addUserDialog.rules"
               class="demo-ruleForm" status-icon label-width="70px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="addUserDialog.user.username"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="addUserDialog.user.password" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPassword">
          <el-input type="password" v-model="addUserDialog.user.checkPassword" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="medium" @click="addUserDialog.visible = false">取 消</el-button>
        <el-button size="medium" type="primary" @click="submitAddUser()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import groupApi from "@/api/rbac/groupApi";
import userApi from "@/api/rbac/userApi";

export default {
  name: "UserAdmin",
  data() {
    return {
      assignRolesDialog: {
        loading: false,
        groupId: '',
        allRoles: [],
        assignRoleIds: [],
        title: '分配角色',
        visible: false,
        props: {
          key: 'id',
          label: 'name'
        }
      },
      alterUserDialog: {
        user: {
          id: "",
          username: "",
          password: "",
          checkPassword: "",
          groupId: ""
        },
        visible: false
      },
      addUserDialog: {
        user: {
          username: "",
          password: "",
          checkPassword: "",
          groupId: ""
        },
        rules: {
          username: [
            {validator: this.validateInput, trigger: ['blur', 'change']}
          ],
          password: [
            {validator: this.validateInput, trigger: ['blur', 'change']}
          ],
          checkPassword: [
            {validator: this.validateInput, trigger: 'blur'}
          ]
        },
        visible: false
      },
      addGroupDialog: {
        group: {
          name: "",
          description: ""
        },
        visible: false
      },
      alterGroupDialog: {
        group: {
          id: "",
          name: "",
          description: ""
        },
        visible: false
      },
      filterText: "",
      tree: {
        data: [{name: '所有组', children: []}],
        props: {
          children: 'children',
          label: 'name'
        }
      }
    };
  },
  created() {
    this.loadTreeData();
  },
  methods: {
    handleTreeNodeDrop(draggingNode, dropNode) {
      //共四个参数，依次为：被拖拽节点对应的 Node、结束拖拽时最后进入的节点、被拖拽节点的放置位置（before、after、inner）、event
      let userId = draggingNode.data.id;
      let groupId = dropNode.data.id;
      userApi.update({id: userId, groupId: groupId}).then(() => {
        this.$message.success({message: `成功将【${draggingNode.data.name}】加入【${dropNode.data.name}】`, showClose: true})
      }).catch(() => {
        this.loadTreeData()
      })
    },
    treeAllowDrop(draggingNode, dropNode, type) {
      //type 参数有三种情况：'prev'、'inner' 和 'next'，分别表示放置在目标节点前、插入至目标节点和放置在目标节点后
      return dropNode.level === 2 && type === 'inner';
    },
    treeAllowDrag(draggingNode) {
      return draggingNode.level === 3 && draggingNode.data.id !== '1';
    },


    transferFilterMethod(query, item) {
      return item.name.indexOf(query) > -1;
    },
    deleteUser(data) {
      if (data.id === '1') {
        this.$message.warning({message: "此用户为初始管理员，不可删除", showClose: true})
        return;
      }
      this.$confirm('此操作将永久删除此用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        beforeClose: (action, instance, done) => {
          if (action === 'confirm') {
            instance.confirmButtonLoading = true;
            instance.confirmButtonText = '执行中...';
            userApi.delete(data.id)
                .then(() => {
                  this.$message.success({message: "删除用户成功", showClose: true})
                  this.loadTreeData()
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
    submitAlterUser() {
      this.$refs.alterUserForm.validate().then(() => {
        userApi.update({...this.alterUserDialog.user}).then(() => {
          this.alterUserDialog.visible = false
          this.$message.success({message: "修改用户成功", showClose: true})
          this.loadTreeData();
        })
      })
    },
    alterUser(data,node) {
      this.alterUserDialog.user.id = data.id
      this.alterUserDialog.user.username = data.name
      this.alterUserDialog.user.groupId = node.parent.data.id;
      this.alterUserDialog.visible = true
    },
    validateInput(rule, value, callback) {
      switch (rule.field) {
        case 'username':
          if (!value) {
            callback(new Error('用户名不能为空'));
          } else if (value.length < 3) {
            callback(new Error('用户名不能少于3位'))
          } else if (!/^[a-zA-Z][a-zA-Z0-9_]{2,31}$/.test(value)) {
            callback(new Error('用户名只允许[字母,数字,下划线],且不超过32位'));
          } else {
            callback()
          }
          break;
        case 'password':
          if (!value) {
            callback(new Error('密码不能为空'));
          } else if (value.length < 6) {
            callback(new Error('密码不能少于6位'));
          } else if (!/^[a-zA-Z0-9_ ]{6,32}$/.test(value)) {
            callback(new Error('密码只允许[字母,数字,下划线,空格],且不超过32位'));
          } else {
            callback()
          }
          break
        case 'checkPassword':
          if (!value) {
            callback(new Error('请再次输入密码'));
          } else if (!(value === this.addUserDialog.user.password || value === this.alterUserDialog.user.password)) {
            callback(new Error('两次输入的密码不一致!'));
          } else {
            callback();
          }
      }
    },
    submitAddUser() {
      this.$refs.addUserForm.validate().then(() => {
        userApi.add({...this.addUserDialog.user}).then(() => {
          this.addUserDialog.visible = false
          this.$message.success({message: "新增用户成功", showClose: true})
          this.loadTreeData()
        })
      })
    },


    loadTreeData() {
      groupApi.listGroupAndUser().then((result) => {
        this.tree.data[0].children = result.data
      })
    },
    filterTreeNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },


    addUser(data) {
      this.addUserDialog.user.groupId = data.id
      this.addUserDialog.visible = true;
    },

    alterOrCheck(data, node) {
      if (node.level === 3) {
        this.alterUser(data);
      }
    }
  }
}
</script>

<style scoped>
.custom-tree-node a {
  margin-left: 10px;
}

.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 16px;
  padding-right: 8px;
}
</style>