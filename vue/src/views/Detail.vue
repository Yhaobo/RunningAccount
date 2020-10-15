<template>
  <div>
    <el-row type="flex" align="middle" class="transparent" :gutter="10" style="margin-bottom: 20px">
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
        <el-input v-model="queryCondition.description" placeholder="摘要关键字" @change="loadData" clearable></el-input>
      </el-col>
      <el-col :span="1">
        <el-tooltip effect="light" content="仅显示无凭证的记录" placement="top">
          <el-switch
              @change="loadData"
              v-model="queryCondition.justNoVoucher"
              active-color="#13ce66"
              inactive-color="#ff4949">
          </el-switch>
        </el-tooltip>
      </el-col>
    </el-row>

    <el-table ref="table" :data="tableData.data" style="width: 100%;" @row-click="onRowClick"
              v-loading="tableData.loading" :element-loading-text="loadingText" :row-class-name="tableRowClassName">
      >
      <el-table-column type="expand">
        <template slot-scope="scope">
          <el-collapse value="digest">
            <el-collapse-item title="摘要" name="digest">
              <div>{{ scope.row.description }}</div>
            </el-collapse-item>
          </el-collapse>
          <el-collapse @change="showVoucher($event,scope.row.id,scope.$index)" value="voucher">
            <el-collapse-item :name="scope.row.vouchers?'voucher':''" :disabled="!scope.row.hasVoucher">
              <template slot="title">
                <span v-show="!scope.row.hasVoucher">暂无凭证</span>
                <span v-show="scope.row.hasVoucher">图片凭证</span>
                <el-button style="margin-left: 10px" size="mini" type="success"
                           @click.stop="openAddVoucherDialog(scope.row.id,scope.$index,true)">添加
                </el-button>
              </template>
              <div :element-loading-text="loadingText" v-loading="voucherCollapse.loading" style="height:inherit">
                <el-tag v-for="voucher of scope.row.vouchers" :key="voucher.id" type="danger" effect="plain"
                        :disable-transitions="true" style="height: max-content;margin: 5px;" closable
                        @close="deleteVoucher(voucher,scope.row)">
                  <el-image style="width: 300px" :previewSrcList="scope.row.vouchers | previewSrcListFilter"
                            :src="voucher.url"
                  ></el-image>
                </el-tag>
              </div>
            </el-collapse-item>
          </el-collapse>
        </template>
      </el-table-column>
      <el-table-column label="日期" min-width="50" :sortable="true" :sort-method="sort">
        <template slot-scope="scope">
          <span>{{ dateFormat(scope.row.date, 'yyyy-MM-dd') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="项目" prop="project.name" min-width="50"></el-table-column>
      <el-table-column label="银行账户" prop="account.name" min-width="50"></el-table-column>
      <el-table-column label="部门" prop="department.name" min-width="50"></el-table-column>
      <el-table-column label="具体类别" prop="category.name" min-width="50"></el-table-column>
      <el-table-column label="收入" prop="earning" min-width="50" :sortable="true">
        <template slot-scope="scope">{{ '￥' + scope.row.earning }}</template>
      </el-table-column>
      <el-table-column label="支出" prop="expense" min-width="50" :sortable="true">
        <template slot-scope="scope">{{ '￥' + scope.row.expense }}</template>
      </el-table-column>
      <el-table-column label="结存" prop="balance" min-width="50">
        <template slot-scope="scope">{{ '￥' + scope.row.balance }}</template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template slot="header">
          操作
          <el-button size="small" type="success" style="margin-left: 10px" @click="addDialog.visible = true;">添加
          </el-button>
        </template>
        <template slot-scope="scope">
          <el-button size="mini" @click.stop="openUpdateDetailDialog(scope.$index, scope.row)">修改
          </el-button>
          <el-button size="mini" type="danger" @click.stop="deleteDetail(scope.$index, scope.row)">删除
          </el-button>
        </template>
      </el-table-column>
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
                   style="text-align: left;margin-top: 20px;background-color: white;"
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
              v-model="updateDetailDialog.detail.description"
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
    <el-dialog :visible.sync="addDialog.visible" :close-on-click-modal="false" @closed="handleAddDialogClosed">
      <el-steps :active="addDialog.step" finish-status="success" process-status="finish" :align-center="true">
        <el-step title="添加记录"></el-step>
        <el-step title="上传凭证"></el-step>
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
        <el-form-item label="摘要" prop="description">
          <el-input
              type="textarea"
              placeholder="请输入内容"
              v-model="addDialog.detailForm.data.description"
              maxlength="255"
              autosize
          ></el-input>
        </el-form-item>
      </el-form>
      <upload :url="addDialog.voucherData.uploadUrl" ref="voucherUpload" list-type="picture" :limit-size="500"
              accept=".jpg,.jpeg,.png,.gif,.bmp,.JPG,.JPEG,.PNG,.GIF,.BMP" v-show="addDialog.step===1"
      ></upload>
      <span slot="footer" class="dialog-footer">
        <el-button size="medium" type="primary" @click="addDetail" v-show="addDialog.step===0"
                   :loading="addDialog.loading">下一步</el-button>
        <el-button size="medium" @click="addDialog.step=0" v-show="addDialog.step===1&&!addDialog.justAddVoucher"
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

export default {
  components: {
    upload
  },
  data() {
    return {
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
        justAddVoucher: false,
        detailForm: {
          key: 0,
          data: {
            project: {},
            account: {},
            department: {},
            category: {}
          },
        },
        voucherData: {
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
      voucherCollapse: {
        loading: true,
      },

      queryCondition: {
        dateRange: [],
        projectId: null,
        accountId: null,
        departmentId: null,
        categoryId: null,
        description: null,
        justNoVoucher: false,
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
        loading: true,
        expandRow: null,
      }
    }
  },
  created() {
    this.loadData().then(() => {
      this.loadSelectionData()
    })
  },

  methods: {
    handleAddDialogClosed() {
      if (this.$refs.voucherUpload.successUploadNum > 0) {
        //上传成功之后更新数据
        this.refreshVoucherData()
      }
      //重置上传列表
      this.$refs.voucherUpload.reset();
      //置空数据
      this.addDialog.detailForm.data = {
        project: {},
        account: {},
        department: {},
        category: {}
      }
      this.addDialog.justAddVoucher = false
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
            detailApi.add(detail).then((result) => {
              this.loadData()
              this.addDialog.detailForm.data.id = result.data
              this.addDialog.loading = false
              this.openAddVoucherDialog(this.addDialog.detailForm.data.id, null)
            });
          } else {
            //修改
            detailApi.update(detail).then(() => {
              this.loadData()
              this.addDialog.loading = false
              this.openAddVoucherDialog(this.addDialog.detailForm.data.id, null)
            })
          }
        });
      }
    },
    handleCurrentPageChange(currentPage) {
      this.pageData.currentPage = currentPage
      this.loadData()
      window.scrollTo(0, 70)
    },
    handlePageSizeChange(pageSize) {
      this.pageData.pageSize = pageSize
      this.loadData()
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
      return detailApi.list(queryCondition).then((result) => {
        this.tableData.data = result.data.list;
        this.pageData.total = result.data.total
        this.tableData.loading = false;
      })
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

    deleteVoucher(voucher, detail) {
      this.$confirm('此操作将永久删除此凭证, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        beforeClose: (action, instance, done) => {
          if (action === 'confirm') {
            instance.confirmButtonLoading = true;
            instance.confirmButtonText = '执行中...';
            detailApi.deleteVoucher(voucher.id)
                .then(() => {
                  //删除本地数据
                  const vouchers = detail.vouchers;
                  vouchers.splice(vouchers.indexOf(voucher), 1);
                  if (vouchers.length === 0) {
                    detail.hasVoucher = false
                  }
                  this.$refs.table.toggleRowExpansion(detail, false);
                  this.$refs.table.toggleRowExpansion(detail, true);
                  this.$message.success({message: "删除凭证成功", showClose: true});
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

    onRowClick(row) {
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

    sort(a, b) {
      return a.date > b.date ? 1 : (a.data < b.date ? -1 : 0)
    },

    dateFormat(date, fmt) {
      return MyUitls.dateFormat(new Date(date), fmt)
    },

    showVoucher(event, detailId, index) {
      const detail = this.tableData.data[index];
      if (detail.hasVoucher && event.length && !(detail.vouchers && detail.vouchers.length > 0)) {
        //有凭证,没凭证数据,且是打开折叠面板
        this.voucherCollapse.loading = true
        detailApi.listVoucher(detailId).then((result) => {
          result.data.forEach((i) => {
            //生成url
            i.url = `${this.baseUrl}/detail/voucher/img/${i.uri}`;
            i.uri = null
          });
          detail.vouchers = result.data
          this.voucherCollapse.loading = false
        })
      }
    },
    openAddVoucherDialog(detailId, index,justAddVoucher) {
      this.addDialog.voucherData.index = index;
      this.addDialog.voucherData.detailId = detailId;
      this.addDialog.voucherData.uploadUrl = `${this.baseUrl}/detail/voucher/${this.addDialog.voucherData.detailId}`
      this.addDialog.step = 1
      this.addDialog.justAddVoucher = justAddVoucher
      this.addDialog.visible = true
    },
    refreshVoucherData() {
      if (this.addDialog.voucherData.index != null) {
        //已有记录添加凭证成功
        this.voucherCollapse.loading = true;
        detailApi.listVoucher(this.addDialog.voucherData.detailId).then((result) => {
          //处理url
          result.data.forEach((i) => {
            i.url = `${this.baseUrl}/detail/voucher/img/${i.uri}`;
            i.uri = null
          });
          this.tableData.data[this.addDialog.voucherData.index].hasVoucher = true;
          this.tableData.data[this.addDialog.voucherData.index].vouchers = result.data; //搞不懂这里为啥是响应式
          this.voucherCollapse.loading = false
        });
      } else {
        //新记录添加凭证成功
        let tableDataIndex = this.handleTableDataIndex(this.addDialog.voucherData.index);
        if (tableDataIndex != null) {
          this.tableData.data[tableDataIndex].hasVoucher = true;
        }
      }
    },
    handleTableDataIndex(tableDataIndex) {
      //处理空值
      if (tableDataIndex == null) {
        this.tableData.data.forEach((value, index) => {
          if (value.id === this.addDialog.voucherData.detailId) {
            tableDataIndex = index
            return false
          }
        });
      }
      return tableDataIndex
    }
  },
  filters: {
    previewSrcListFilter(vouchers) {
      let previewSrcList = []
      vouchers.forEach((item) => {
        previewSrcList.push(item.url)
      })
      return previewSrcList;
    }
  }
}
</script>
