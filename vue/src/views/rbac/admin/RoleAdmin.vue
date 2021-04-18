<template>
  <div>
    <el-card shadow="hover">
      <div slot="header">
        <el-input
            placeholder="输入关键字进行过滤" @input="$refs.tree.filter(filterText);"
            v-model="filterText" clearable>
        </el-input>
      </div>
      <el-tree class="filter-tree" @node-click="alterOrCheckRole" :indent="30"
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
              <el-link type="success" @click.stop="addRole(data,node)" v-if="node.level===1"
                       :underline="false">新增角色</el-link>
              <el-link type="warning" @click.stop="alterOrCheckRole(data,node)" v-if="node.level===2"
                       :underline="false">修改此角色</el-link>
              <el-link type="danger" @click.stop="deleteRole(data,node)" v-if="node.level===2"
                       :underline="false">删除此角色</el-link>
            </span>
          </span>
      </el-tree>
    </el-card>
    <el-dialog title="新增角色" :visible.sync="addRoleDialog.visible" :close-on-click-modal="false"
               @closed="addRoleDialog.role.name='';addRoleDialog.role.description=''">
      <el-form :model="addRoleDialog.role">
        <el-form-item label="角色名" prop="name">
          <el-input v-model="addRoleDialog.role.name"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="addRoleDialog.role.description"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="medium" @click="addRoleDialog.visible = false">取 消</el-button>
        <el-button size="medium" type="primary" @click="submitAddRole()">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改或查看角色" :visible.sync="alterRoleDialog.visible" :close-on-click-modal="false"
               @closed="alterRoleDialog.role.name='';alterRoleDialog.role.description=''">
      <el-form :model="alterRoleDialog.role">
        <el-form-item label="角色名" prop="name">
          <el-input v-model="alterRoleDialog.role.name"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="alterRoleDialog.role.description"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="medium" @click="alterRoleDialog.visible = false">取 消</el-button>
        <el-button size="medium" type="primary" @click="submitAlterRole()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import roleApi from "@/api/rbac/roleApi";

export default {
  name: "RoleAdmin",
  data() {
    return {
      addRoleDialog: {
        role: {
          name: '',
          description: ''
        },
        visible: false
      },
      alterRoleDialog: {
        role: {
          id: '',
          name: '',
          description: ''
        },
        visible: false
      },
      filterText: "",
      tree: {
        data: [{name: '所有角色', children: []}],
        props: {
          children: 'children',
          label: 'name'
        }
      }
    };
  },
  created() {
    this.loadRoleData()
  },

  methods: {
    transferFilterMethod(query, item) {
      return item.name.indexOf(query) > -1;
    },
    submitAddRole() {
      roleApi.add(this.addRoleDialog.role).then(() => {
        this.$message.success({message: "新增角色成功", showClose: true})
        this.addRoleDialog.visible = false;
        this.loadRoleData()
      })
    },
    loadRoleData() {
      roleApi.listAll().then(value => {
        this.tree.data[0].children = value.data
      })
    },
    deleteRole(data) {
      this.$confirm('此操作将永久删除此角色, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        beforeClose: (action, instance, done) => {
          if (action === 'confirm') {
            instance.confirmButtonLoading = true;
            instance.confirmButtonText = '执行中...';
            roleApi.delete(data.id)
                .then(() => {
                  this.$message.success({message: '删除角色成功', showClose: true})
                  this.loadRoleData()
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
    addRole() {
      this.addRoleDialog.visible = true;
    },
    submitAlterRole() {
      roleApi.update(this.alterRoleDialog.role).then(() => {
        this.$message.success({message: '修改角色成功', showClose: true})
        this.alterRoleDialog.visible = false
        this.loadRoleData()
      })
    },
    alterOrCheckRole(data, node) {
      if (node.level === 2) {
        this.alterRoleDialog.role.id = data.id
        this.alterRoleDialog.role.name = data.name
        this.alterRoleDialog.role.description = data.description
        this.alterRoleDialog.visible = true
      }
    },
    filterTreeNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
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