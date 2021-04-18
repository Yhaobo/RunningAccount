<template>
  <div>
    <el-card shadow="hover">
      <div slot="header">
        <el-input
            placeholder="输入关键字进行过滤" @input="$refs.tree.filter(filterText);"
            v-model="filterText" clearable>
        </el-input>
      </div>
      <el-tree class="filter-tree" :indent="30"
               @node-click="assignRoles"
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
              <el-link type="primary" @click.stop="assignRoles(data,node)" v-if="node.level===2&&data.id!=='1'"
                       :underline="false">分配角色</el-link>
            </span>
          </span>
      </el-tree>
    </el-card>
    <el-dialog :title="assignRolesDialog.title" :visible.sync="assignRolesDialog.visible" :close-on-click-modal="false">
      <el-transfer
          :titles="['未拥有角色', '已拥有角色']"
          :props="assignRolesDialog.props"
          filterable
          :filter-method="transferFilterMethod"
          filter-placeholder="输入关键字进行过滤"
          v-model="assignRolesDialog.assignRoleIds"
          :data="assignRolesDialog.allRoles">
      </el-transfer>
      <div slot="footer" class="dialog-footer">
        <el-button size="medium" @click="assignRolesDialog.visible = false">取 消</el-button>
        <el-button size="medium" type="primary" @click="submitAssignRoles()" :loading="assignRolesDialog.loading">确 定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import groupApi from "@/api/rbac/groupApi";
import userApi from "@/api/rbac/userApi";
import roleApi from "@/api/rbac/roleApi";

export default {
  name: "RoleAssign",
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
      if (dropNode.level === 2 && type === 'inner') {
        return true;
      } else {
        return false;
      }
    },
    treeAllowDrag(draggingNode) {
      return draggingNode.level === 3 && draggingNode.data.id !== '1';
    },
    assignRoles(data, node) {
      if (node.level === 2) {
        if (data.id !== '1') {
          groupApi.listRole(data.id).then(value => {
            this.assignRolesDialog.groupId = data.id
            this.assignRolesDialog.title = `为【${data.name}】分配角色`
            this.assignRolesDialog.assignRoleIds = value.data
          }).then(() => {
            roleApi.listAll().then(value => {
              this.assignRolesDialog.allRoles = value.data
            })
          }).then(() => {
            this.assignRolesDialog.visible = true
          });
        }
      }
    },
    submitAssignRoles() {
      this.assignRolesDialog.loading = true;
      groupApi.assignRoles(this.assignRolesDialog.assignRoleIds, this.assignRolesDialog.groupId).then(() => {
        this.$message.success({message: '分配角色成功', showClose: true})
        this.assignRolesDialog.visible = false
      }).finally(() => {
        this.assignRolesDialog.loading = false;
      })
    },
    transferFilterMethod(query, item) {
      return item.name.indexOf(query) > -1;
    },

    loadTreeData() {
      groupApi.listAll().then((result) => {
        this.tree.data[0].children = result.data.filter(group => {
          return group.id !== '1'
        });
      })
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