<template>
  <div>
    <el-row type="flex" align="middle" :gutter="10">
      <el-col :span="5.5">
        <el-date-picker
            @change="loadData"
            v-model="queryCondition.dateRange"
            type="daterange"
            align="right"
            unlink-panels
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :picker-options="queryCondition.datePickerOptions"
            :default-time="['00:00:00', '23:59:59']"
        >
        </el-date-picker>
      </el-col>
      <el-col :span="3">
        <el-select v-model="queryCondition.projectId" placeholder="所有项目" @change="loadData" clearable>
          <el-option
              v-for="item of selectionData.projectOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id">
          </el-option>
        </el-select>
      </el-col>
      <el-col :span="3">
        <el-select v-model="queryCondition.accountId" placeholder="所有银行账户" @change="loadData" clearable>
          <el-option
              v-for="item of selectionData.accountOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id">
          </el-option>
        </el-select>
      </el-col>
      <el-col :span="3">
        <el-select v-model="queryCondition.departmentId" placeholder="所有部门" @change="loadData" clearable>
          <el-option
              v-for="item of selectionData.departmentOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id">
          </el-option>
        </el-select>
      </el-col>
      <el-col :span="3">
        <el-select v-model="queryCondition.categoryId" placeholder="所有类别" @change="loadData" clearable>
          <el-option
              v-for="item of selectionData.categoryOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id">
          </el-option>
        </el-select>
      </el-col>
      <el-col :span="3">
        <el-input v-model="queryCondition.digest" placeholder="摘要关键字" @change="loadData" clearable></el-input>
      </el-col>
    </el-row>

    <el-table ref="table" :data="tableData.data" style="width: 100%;margin-top: 10px" @row-click="onTableRowClick" height="90vh"
              v-loading="tableData.loading" :element-loading-text="loadingText" :row-class-name="tableRowClassName"
              :select-on-indeterminate="false" @select="onTableRowSelect" :border="true"
              @select-all="onTableSelectAll">
      <el-table-column type="expand">
        <template slot-scope="scope">
          <el-collapse value="digest">
            <el-collapse-item title="摘要" name="digest">
              <div>{{ scope.row.digest }}</div>
            </el-collapse-item>
          </el-collapse>
          <el-collapse @change="showPicture($event,scope.row.id,scope.$index)" value="picture">
            <el-collapse-item :name="scope.row.pictures?'picture':''" :disabled="!scope.row.hasPicture">
              <template slot="title">
                <span v-show="!scope.row.hasPicture">暂无图片信息</span>
                <span v-show="scope.row.hasPicture">图片信息</span>
                <el-button style="margin-left: 10px" size="mini" type="success" v-if="currentUserGroupId<='1'"
                           @click.stop="openAddPictureDialog(scope.row.id,scope.$index,true)">上传
                </el-button>
              </template>
              <div :element-loading-text="loadingText" v-loading="pictureCollapse.loading" style="height:inherit">
                <el-tag v-for="picture of scope.row.pictures" :key="picture.id" type="danger" effect="plain"
                        :disable-transitions="true" style="height: max-content;margin: 5px;" closable
                        @close="deletePicture(picture,scope.row)">
                  <el-image style="width: 300px" :previewSrcList="scope.row.pictures | previewSrcListFilter"
                            :src="picture.url"
                  ></el-image>
                </el-tag>
              </div>
            </el-collapse-item>
          </el-collapse>
        </template>
      </el-table-column>
      <el-table-column label="日期" :sortable="true" :sort-method="sort">
        <template slot-scope="scope">
          <span>{{ dateFormat(scope.row.date, 'yyyy-MM-dd') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="项目" prop="project.name"></el-table-column>
      <el-table-column label="银行账户" prop="account.name"></el-table-column>
      <el-table-column label="部门" prop="department.name"></el-table-column>
      <el-table-column label="具体类别" prop="category.name"></el-table-column>
      <el-table-column label="收入" prop="earning" :sortable="true">
        <template slot-scope="scope">{{ '￥' + scope.row.earning }}</template>
      </el-table-column>
      <el-table-column label="支出" prop="expense" :sortable="true">
        <template slot-scope="scope">{{ '￥' + scope.row.expense }}</template>
      </el-table-column>
      <el-table-column label="结存" prop="balance">
        <template slot-scope="scope">{{ '￥' + scope.row.balance }}</template>
      </el-table-column>
      <el-table-column label="操作" width="157" v-if="currentUserGroupId<='1'">
        <template slot="header">
          操作
          <el-button v-show="tableData.multipleSelectionData.data.length===0" size="small" type="success"
                     style="margin-left: 10px" @click="addDialog.visible = true;">添加记录
          </el-button>
          <el-button v-show="tableData.multipleSelectionData.data.length>0" size="small" type="primary"
                     style="margin-left: 0" @click="generateExpenseClaimForm"
                     :loading="tableData.multipleSelectionData.loading">生成报销凭证
          </el-button>
        </template>
        <template slot-scope="scope">
          <el-button size="mini" @click.stop="openUpdateDetailDialog(scope.$index, scope.row)">修改
          </el-button>
          <el-button size="mini" type="danger" @click.stop="deleteDetail(scope.$index, scope.row)">删除
          </el-button>
        </template>
      </el-table-column>
      <el-table-column v-if="currentUserGroupId<='1'" type="selection" width="40"></el-table-column>
    </el-table>
    <el-pagination @size-change="handlePageSizeChange"
                   @current-change="handleCurrentPageChange"
                   :current-page="pageData.currentPage"
                   :page-sizes="[25, 50, 100, 500]"
                   :page-size="pageData.pageSize"
                   layout="total, sizes, prev, pager, next, jumper"
                   :total="pageData.total"
                   :pager-count="9"
                   background
                   style="text-align: left;margin-top: 20px;"
    ></el-pagination>


    <el-dialog title="修改记录" :visible.sync="updateDetailDialog.visible" width="30%" :close-on-click-modal="false">
      <el-form label-width="70px" :model="updateDetailDialog.detail"
               style="width: max-content;margin: auto">
        <el-form-item label="日期时间">
          <el-date-picker
              v-model="updateDetailDialog.detail.date"
              type="datetime"
              placeholder="选择日期时间" :clearable="false">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="项目">
          <el-select v-model="updateDetailDialog.detail.project.id" placeholder="请选择">
            <el-option
                v-for="item of selectionData.projectOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="银行账户">
          <el-select v-model="updateDetailDialog.detail.account.id" placeholder="请选择">
            <el-option
                v-for="item of selectionData.accountOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="部门">
          <el-select v-model="updateDetailDialog.detail.department.id" placeholder="请选择">
            <el-option
                v-for="item of selectionData.departmentOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="具体类别">
          <el-select v-model="updateDetailDialog.detail.category.id" placeholder="请选择">
            <el-option
                v-for="item of selectionData.categoryOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="收入" prop="earning">
          <el-input-number v-model="updateDetailDialog.detail.earning" :precision="2" :step="1"
                           :controls="false" style="width: inherit"></el-input-number>
        </el-form-item>
        <el-form-item label="支出" prop="expense">
          <el-input-number v-model="updateDetailDialog.detail.expense" :precision="2" :step="1"
                           :controls="false" style="width: inherit"></el-input-number>
        </el-form-item>
        <el-form-item label="摘要">
          <el-input
              type="textarea"
              placeholder="请输入内容"
              v-model="updateDetailDialog.detail.digest"
              maxlength="255"
              autosize
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="medium" @click="updateDetailDialog.visible = false">取 消</el-button>
        <el-button size="medium" type="primary" @click="updateDetail">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="addDialog.visible" :close-on-click-modal="false" @closed="onAddDialogClosed">
      <el-steps :active="addDialog.step" finish-status="success" process-status="finish" :align-center="true">
        <el-step title="添加记录"></el-step>
        <el-step title="上传图片信息"></el-step>
      </el-steps>
      <el-form :key="addDialog.detailForm.key" ref="addDetailForm" label-width="84px"
               :model="addDialog.detailForm.data"
               v-show="addDialog.step===0"
               style="width: max-content;margin: 10px auto" :rules="addDialog.rules">
        <el-form-item label="日期时间" prop="date">
          <el-date-picker v-model="addDialog.detailForm.data.date" type="datetime" placeholder="选择日期时间"
                          default-time="12:00:00">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="项目" prop="project">
          <el-select v-model="addDialog.detailForm.data.project.id" placeholder="请选择">
            <el-option
                v-for="item of selectionData.projectOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="银行账户" prop="account">
          <el-select v-model="addDialog.detailForm.data.account.id" placeholder="请选择">
            <el-option
                v-for="item of selectionData.accountOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="部门" prop="department">
          <el-select v-model="addDialog.detailForm.data.department.id" placeholder="请选择">
            <el-option
                v-for="item of selectionData.departmentOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="具体类别" prop="category">
          <el-select v-model="addDialog.detailForm.data.category.id" placeholder="请选择">
            <el-option
                v-for="item of selectionData.categoryOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="收入" prop="earning">
          <el-input-number v-model="addDialog.detailForm.data.earning" :precision="2" :step="1"
                           :controls="false" style="width: inherit"></el-input-number>
        </el-form-item>
        <el-form-item label="支出" prop="expense">
          <el-input-number v-model="addDialog.detailForm.data.expense" :precision="2" :step="1"
                           :controls="false" style="width: inherit"></el-input-number>
        </el-form-item>
        <el-form-item label="摘要" prop="digest">
          <el-input
              type="textarea"
              placeholder="请输入内容"
              v-model="addDialog.detailForm.data.digest"
              maxlength="255"
              autosize
          ></el-input>
        </el-form-item>
      </el-form>
      <upload :url="addDialog.pictureData.uploadUrl" ref="pictureUpload" list-type="picture" :limit-size="512"
              accept=".jpg,.jpeg,.png,.gif,.bmp,.JPG,.JPEG,.PNG,.GIF,.BMP" v-show="addDialog.step===1"
              tip-text="只能上传图片文件，且不超过 512KB"
      ></upload>
      <span slot="footer" class="dialog-footer">
        <el-button size="medium" type="primary" @click="addDetail" v-show="addDialog.step===0"
                   :loading="addDialog.loading">下一步</el-button>
        <el-button size="medium" @click="addDialog.step=0" v-show="addDialog.step===1&&!addDialog.justAddPicture"
                   style="float:left;">上一步</el-button>
        <el-button size="medium" type="primary" @click="addDialog.visible=false"
                   v-show="addDialog.step===1">完 成</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import detailApi from "@/api/detailApi";
import MyUitls from "@/utils/MyUitls";
import upload from "@/components/Upload";
import optionApi from "@/api/optionApi";
import excelApi from "@/api/excelApi";

export default {
  components: {
    upload
  },
  data() {
    return {
      currentUserGroupId: sessionStorage.getItem('groupId'),
      loadingText: '玩命加载中...',
      baseUrl: process.env.VUE_APP_BaseURL,
      pageData: {
        currentPage: 1,
        pageSize: 25,
        total: 0
      },
      selectionData: {
        projectOptions: [],
        accountOptions: [],
        departmentOptions: [],
        categoryOptions: [],
      },
      addDialog: {
        visible: false,
        loading: false,
        step: 0,
        justAddPicture: false,
        detailForm: {
          key: 0,
          data: {
            project: {},
            account: {},
            department: {},
            category: {}
          },
        },
        pictureData: {
          detailId: null,
          index: null,//临时存放table.data数组的下标(对应当前选中记录)
          uploadUrl: ''
        },
        rules: {
          date: [
            {type: 'date', required: true, message: '请选择日期时间', trigger: 'blur'},
          ],
          department: [
            {validator: this.validateSelection, required: true, trigger: 'change'}
          ],
          project: [
            {validator: this.validateSelection, required: true, trigger: 'change'}
          ],
          account: [
            {validator: this.validateSelection, required: true, trigger: 'change'}
          ],
          category: [
            {validator: this.validateSelection, required: true, trigger: 'change'}
          ],
        }
      },
      updateDetailDialog: {
        visible: false,
        detail: {
          project: {},
          account: {},
          department: {},
          category: {}
        },
        index: null, //临时存放table.data数组的下标(对应当前选中记录)
      },
      pictureCollapse: {
        loading: true,
      },

      queryCondition: {
        dateRange: [],
        projectId: null,
        accountId: null,
        departmentId: null,
        categoryId: null,
        digest: null,
        datePickerOptions: {
          shortcuts: [{
            text: '最近一个月',
            onClick(picker) {
              const end = new Date();
              end.setHours(23, 59, 59, 999)
              const start = new Date();
              start.setHours(0, 0, 0, 0);
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date();
              end.setHours(23, 59, 59, 999)
              const start = new Date();
              start.setHours(0, 0, 0, 0);
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近半年',
            onClick(picker) {
              const end = new Date();
              end.setHours(23, 59, 59, 999)
              const start = new Date();
              start.setHours(0, 0, 0, 0);
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30 * 6);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近一年',
            onClick(picker) {
              const end = new Date();
              end.setHours(23, 59, 59, 999)
              const start = new Date();
              start.setHours(0, 0, 0, 0);
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30 * 12);
              picker.$emit('pick', [start, end]);
            }
          },]
        },
      },
      tableData: {
        data: [],
        loading: true,
        generateExpenseClaimFormLoading: false,
        expandRow: null,
        multipleSelectionData: {
          loading: false,
          data: [],
          maxSelectedNum: 7
        },
      }
    }
  },
  created() {
    this.loadData()
    this.loadSelectionData()
  },

  methods: {
    generateExpenseClaimForm() {
      this.tableData.multipleSelectionData.loading = true;
      excelApi.generateExpenseClaimForm(this.tableData.multipleSelectionData.data)
          .then((result) => {
            window.open(this.baseUrl + `/excel/expenseClaimForm?formId=${result.data}`)
            this.tableData.multipleSelectionData.data.forEach(value => {
              //修改标记为已报销
              value.reimbursement = true
            })

          })
          .finally(() => this.tableData.multipleSelectionData.loading = false);
    },
    onAddDialogClosed() {
      if (this.$refs.pictureUpload.successUploadNum > 0) {
        //上传成功之后更新数据
        this.refreshPictureData()
      }
      //重置上传列表
      this.$refs.pictureUpload.reset();
      //置空数据
      this.addDialog.detailForm.data = {
        project: {},
        account: {},
        department: {},
        category: {}
      }
      this.addDialog.justAddPicture = false
      this.addDialog.step = 0
      this.addDialog.detailForm.key++
    },
    validateSelection(rule, value, callback) {
      if (!value.id) {
        callback(new Error('请选择'));
      } else {
        callback();
      }
    },
    addDetail() {
      if (this.addDialog.step === 0) {
        this.$refs.addDetailForm.validate().then(() => {
          this.addDialog.loading = true
          const detail = this.addDialog.detailForm.data;
          if (!detail.id) {
            //添加
            detailApi.add(detail)
                .then((result) => {
                  this.$message.success({message: '添加记录成功', showClose: true})
                  this.loadData()
                  this.addDialog.detailForm.data.id = result.data
                  this.addDialog.loading = false
                  this.openAddPictureDialog(this.addDialog.detailForm.data.id, null)
                })
                .catch(() => this.addDialog.loading = false)
          } else {
            //修改
            detailApi.update(detail)
                .then(() => {
                  this.loadData()
                  this.addDialog.loading = false
                  this.openAddPictureDialog(this.addDialog.detailForm.data.id, null)
                })
                .catch(() => this.addDialog.loading = false)
          }
        });
      }
    },
    handleCurrentPageChange(currentPage) {
      this.pageData.currentPage = currentPage
      this.loadData()
      // window.scrollTo(0, 80)
      this.$refs.table.bodyWrapper.scrollTop = 0;
    },
    handlePageSizeChange(pageSize) {
      this.pageData.pageSize = pageSize
      this.loadData()
      // window.scrollTo(0, 80)
      this.$refs.table.bodyWrapper.scrollTop = 0;
    },
    loadData() {
      this.tableData.loading = true;
      //处理属性
      let queryCondition = {...this.queryCondition, ...this.pageData};
      const dateRange = queryCondition.dateRange;
      if (dateRange) {
        queryCondition.beginDate = dateRange[0]
        queryCondition.endDate = dateRange[1]
      }
      queryCondition.dateRange = null;
      queryCondition.total = null
      queryCondition.datePickerOptions = null
      detailApi.list(queryCondition)
          .then((result) => {
            this.tableData.data = result.data.list;
            this.pageData.total = result.data.total
            this.tableData.loading = false;
          })
          .catch(() => this.tableData.loading = false)
    },
    updateDetail() {
      this.$confirm('此操作将永久修改这条记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        beforeClose: (action, instance, done) => {
          if (action === 'confirm') {
            instance.confirmButtonLoading = true;
            instance.confirmButtonText = '执行中...';
            const detail = this.updateDetailDialog.detail;
            detailApi.update(detail)
                .then(() => {
                  this.$message.success({message: "修改记录成功", showClose: true})
                  this.loadData()
                  this.updateDetailDialog.visible = false
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
    loadSelectionData() {
      optionApi.getAll().then((result) => {
        this.selectionData = result.data;
      })
    },
    openUpdateDetailDialog(index, row) {
      this.loadSelectionData()
      detailApi.get(row.id).then((result) => {
        this.updateDetailDialog.index = index
        this.updateDetailDialog.detail = result.data
        this.updateDetailDialog.visible = true
      })
    },
    deleteDetail(index, row) {
      this.$confirm('此操作将永久删除这条记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        beforeClose: (action, instance, done) => {
          if (action === 'confirm') {
            instance.confirmButtonLoading = true;
            instance.confirmButtonText = '执行中...';
            detailApi.delete(row)
                .then(() => {
                  this.loadData()
                  this.$message.success({message: '删除记录成功', showClose: true})
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

    deletePicture(picture, detail) {
      this.$confirm('此操作将永久删除此图片, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        beforeClose: (action, instance, done) => {
          if (action === 'confirm') {
            instance.confirmButtonLoading = true;
            instance.confirmButtonText = '执行中...';
            detailApi.deletePicture(picture.id)
                .then(() => {
                  //删除本地数据
                  const pictures = detail.pictures;
                  pictures.splice(pictures.indexOf(picture), 1);
                  if (pictures.length === 0) {
                    detail.hasPicture = false
                  }
                  this.$refs.table.toggleRowExpansion(detail, false);
                  this.$refs.table.toggleRowExpansion(detail, true);
                  this.$message.success({message: "删除图片成功", showClose: true});
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
    tableRowClassName({row}) {
      if (row.earning - row.expense < 0) {
        return 'warning-row';
      } else if (row.earning - row.expense > 0) {
        return 'success-row';
      } else {
        return ''
      }
    },

    onTableRowClick(row) {
      if (this.tableData.expandRow === row) {
        //关闭
        this.$refs.table.toggleRowExpansion(row, false);
        this.tableData.expandRow = null;
      } else if (this.tableData.expandRow) {
        //关闭之前的, 展开现在的
        this.$refs.table.toggleRowExpansion(this.tableData.expandRow, false);
        this.$refs.table.toggleRowExpansion(row, true);
        this.tableData.expandRow = row;
      } else {
        //展开
        this.$refs.table.toggleRowExpansion(row, true);
        this.tableData.expandRow = row;
      }
    },
    onTableRowSelect(selection, row) {
      if (selection.length > this.tableData.multipleSelectionData.maxSelectedNum) {
        this.$message.info({
          message: `最多只能选中${this.tableData.multipleSelectionData.maxSelectedNum}条记录`,
          showClose: true
        });
        selection.splice(this.tableData.multipleSelectionData.maxSelectedNum);
        this.$refs.table.toggleRowSelection(row, false);
      } else if (row.reimbursement && selection.indexOf(row) >= 0) {
        this.tableData.multipleSelectionData.data = selection;
        this.$confirm('此记录已生成过费用报销单, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          beforeClose: (action, instance, done) => {
            if (action === 'confirm') {
              done();
            } else {
              this.tableData.multipleSelectionData.data.splice(this.tableData.multipleSelectionData.data.indexOf(row))
              this.$refs.table.toggleRowSelection(row, false);
              done();
            }
          }
        })
      } else {
        this.tableData.multipleSelectionData.data = selection;
      }
    },
    onTableSelectAll(selection) {
      if (selection.length === 0) {
        this.tableData.multipleSelectionData.data = selection
      } else {
        //取消全选功能
        selection.splice(0);
      }
    },
    sort(a, b) {
      return a.date > b.date ? 1 : (a.data < b.date ? -1 : 0)
    },

    dateFormat(date, fmt) {
      return MyUitls.dateFormat(new Date(date), fmt)
    },

    showPicture(event, detailId, index) {
      const detail = this.tableData.data[index];
      if (detail.hasPicture && event.length && !(detail.pictures && detail.pictures.length > 0)) {
        //有图片,没图片数据,且是打开折叠面板
        this.pictureCollapse.loading = true
        detailApi.listPicture(detailId)
            .then((result) => {
              result.data.forEach((i) => {
                //生成url
                i.url = `${this.baseUrl}/detail/picture/img/${i.uri}`;
                i.uri = null
              });
              detail.pictures = result.data
              this.pictureCollapse.loading = false
            })
            .catch(() => this.pictureCollapse.loading = false)
      }
    },
    openAddPictureDialog(detailId, index, justAddPicture) {
      this.addDialog.pictureData.index = index;
      this.addDialog.pictureData.detailId = detailId;
      this.addDialog.pictureData.uploadUrl = `${this.baseUrl}/detail/picture/${this.addDialog.pictureData.detailId}`
      this.addDialog.step = 1
      this.addDialog.justAddPicture = justAddPicture
      this.addDialog.visible = true
    },
    refreshPictureData() {
      if (this.addDialog.pictureData.index != null) {
        //已有记录添加图片成功
        this.pictureCollapse.loading = true;
        detailApi.listPicture(this.addDialog.pictureData.detailId)
            .then((result) => {
              //处理url
              result.data.forEach((i) => {
                i.url = `${this.baseUrl}/detail/picture/img/${i.uri}`;
                i.uri = null
              });
              this.tableData.data[this.addDialog.pictureData.index].hasPicture = true;
              this.tableData.data[this.addDialog.pictureData.index].pictures = result.data; //搞不懂这里为啥是响应式
              this.pictureCollapse.loading = false
            })
            .catch(() => this.pictureCollapse.loading = false)
      } else {
        //新记录添加图片成功
        let tableDataIndex = this.handleTableDataIndex(this.addDialog.pictureData.index);
        if (tableDataIndex != null) {
          this.tableData.data[tableDataIndex].hasPicture = true;
        }
      }
    },
    handleTableDataIndex(tableDataIndex) {
      //处理空值
      if (tableDataIndex == null) {
        this.tableData.data.forEach((value, index) => {
          if (value.id === this.addDialog.pictureData.detailId) {
            tableDataIndex = index
            return false
          }
        });
      }
      return tableDataIndex
    }
  }
  ,
  filters: {
    previewSrcListFilter(pictures) {
      let previewSrcList = []
      pictures.forEach((item) => {
        previewSrcList.push(item.url)
      })
      return previewSrcList;
    }
  }
}
</script>
