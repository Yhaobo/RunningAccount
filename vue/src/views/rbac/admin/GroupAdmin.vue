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
              <el-link type="success" @click.stop="addGroup(data,node)" v-if="node.level===1"
                       :underline="false">新增组</el-link>
              <el-link type="warning" @click.stop="alterGroup(data,node)" v-if="node.level===2&&data.id!=='1'"
                       :underline="false">修改此组</el-link>
              <el-link type="danger" @click.stop="deleteGroup(data,node)" v-if="node.level===2&&data.id!=='1'"
                       :underline="false">删除此组</el-link>
            </span>
          </span>
      </el-tree>
    </el-card>
    <el-dialog title="新增组" :visible.sync="addGroupDialog.visible" :close-on-click-modal="false"
               @closed="addGroupDialog.group.name='';addGroupDialog.group.description=''">
      <el-form :model="addGroupDialog.group">
        <el-form-item label="组名" prop="name">
          <el-input v-model="addGroupDialog.group.name"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="addGroupDialog.group.description"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="medium" @click="addGroupDialog.visible = false">取 消</el-button>
        <el-button size="medium" type="primary" @click="submitAddGroup()">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改或查看组" :visible.sync="alterGroupDialog.visible" :close-on-click-modal="false"
               @closed="alterGroupDialog.group.name='';alterGroupDialog.group.description=''">
      <el-form :model="alterGroupDialog.group">
        <el-form-item label="组名" prop="name">
          <el-input v-model="alterGroupDialog.group.name"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="alterGroupDialog.group.description"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="medium" @click="alterGroupDialog.visible = false">取 消</el-button>
        <el-button size="medium" type="primary" @click="submitAlterGroup()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import groupApi from "@/api/rbac/groupApi";

export default {
  name: "GroupAdmin",
  data() {
    return {
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
    deleteGroup(data) {
      if (data.id === '1') {
        this.$message.warning({message: "管理员组不可删除", showClose: true})
        return;
      }
      if (data.children) {
        this.$message.warning({message: "请先清空此组下的用户", showClose: true});
      } else {
        this.$confirm('此操作将永久删除此用户组, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          beforeClose: (action, instance, done) => {
            if (action === 'confirm') {
              instance.confirmButtonLoading = true;
              instance.confirmButtonText = '执行中...';
              groupApi.delete(data.id)
                  .then(() => {
                    this.$message.success({message: "删除组成功", showClose: true})
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
        });
      }
    },
    submitAlterGroup() {
      groupApi.update(this.alterGroupDialog.group).then(() => {
        this.$message.success({message: "修改组成功", showClose: true})
        this.alterGroupDialog.visible = false
        this.loadTreeData()
      })
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
    addGroup() {
      this.addGroupDialog.visible = true
    },
    submitAddGroup() {
      groupApi.add(this.addGroupDialog.group).then(() => {
        this.$message.success({message: '新增组成功', showClose: true})
        this.addGroupDialog.visible = false
        this.loadTreeData();
      });
    },

    alterGroup(data) {
      this.alterGroupDialog.group.id = data.id;
      this.alterGroupDialog.group.name = data.name;
      this.alterGroupDialog.group.description = data.description;
      this.alterGroupDialog.visible = true
    },
    alterOrCheck(data, node) {
      if (node.level === 2) {
        this.alterGroup(data);
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