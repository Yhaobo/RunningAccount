<template>
  <div>
    <el-card shadow="hover">
      <div slot="header">
        <el-input
            placeholder="输入关键字进行过滤" @input="$refs.tree.filter(filterText);"
            v-model="filterText" clearable>
        </el-input>
      </div>
      <el-tree class="filter-tree" @node-click="assignPermission" :indent="30"
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
              <el-link type="primary" @click.stop="assignPermission(data,node)" v-if="node.level===2"
                       :underline="false">分配权限</el-link>
            </span>
          </span>
      </el-tree>
    </el-card>
    <el-dialog :title="assignPermissionDialog.title" :visible.sync="assignPermissionDialog.visible" :close-on-click-modal="false">
      <el-transfer
          :titles="['未拥有权限', '已拥有权限']"
          :props="assignPermissionDialog.props"
          filterable
          :filter-method="transferFilterMethod"
          filter-placeholder="输入关键字进行过滤"
          v-model="assignPermissionDialog.assignPermissionIds"
          :data="assignPermissionDialog.allPermissions">
      </el-transfer>
      <div slot="footer" class="dialog-footer">
        <el-button size="medium" @click="assignPermissionDialog.visible = false">取 消</el-button>
        <el-button size="medium" type="primary" @click="submitAssignPermission()"
                   :loading="assignPermissionDialog.loading">确 定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import roleApi from "@/api/rbac/roleApi";
import permissionApi from "@/api/rbac/permissionApi";

export default {
  name: "PermissionAssign",
  data() {
    return {
      assignPermissionDialog: {
        loading: false,
        roleId: '',
        allPermissions: [],
        assignPermissionIds: [],
        title: '分配权限',
        visible: false,
        props: {
          key: 'id',
          label: 'name'
        }
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
    assignPermission(data,node) {
      if (node.level === 2) {
        roleApi.listPermission(data.id).then(value => {
          this.assignPermissionDialog.roleId = data.id
          this.assignPermissionDialog.title = `为【${data.name}】分配权限`
          this.assignPermissionDialog.assignPermissionIds = value.data
        }).then(() => {
          permissionApi.listAll().then(value => {
            this.assignPermissionDialog.allPermissions = value.data
          })
        }).then(() => {
          this.assignPermissionDialog.visible = true
        });
      }
    },
    submitAssignPermission() {
      this.assignPermissionDialog.loading = true;
      roleApi.assignPermissions(this.assignPermissionDialog.assignPermissionIds, this.assignPermissionDialog.roleId).then(() => {
        this.$message.success({message: '分配权限成功', showClose: true})
        this.assignPermissionDialog.visible = false
      }).finally(() => {
        this.assignPermissionDialog.loading = false;
      })

    },
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
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 16px;
  padding-right: 8px;
}
</style>