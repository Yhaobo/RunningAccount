<template>
  <div>
    <el-row :gutter="10" type="flex" align="middle" style="margin-bottom: 20px">
      <el-col :span="5.5">
        <el-date-picker
            @change="loadData(false)"
            v-model="queryCondition.dateRange"
            type="daterange"
            align="right"
            unlink-panels
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :picker-options="queryCondition.datePickerOptions"
            :default-time="['00:00:00', '23:59:59']"
        ></el-date-picker>
      </el-col>
      <el-col :span="3">
        <el-select v-model="queryCondition.groupPolicy" @change="loadData(false)">
          <el-option label="按月汇总" value="month"></el-option>
          <el-option label="按年汇总" value="year"></el-option>
        </el-select>
      </el-col>
      <el-col :span="3">
        <el-select v-model="queryCondition.accountId" @change="loadData(false)">
          <el-option
              v-for="item of selectionData.accountOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id">
          </el-option>
        </el-select>
      </el-col>
      <el-col :span="3">
        <el-select v-model="queryCondition.projectId" placeholder="所有项目" @change="loadData(false)" clearable>
          <el-option
              v-for="item of selectionData.projectOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id">
          </el-option>
        </el-select>
      </el-col>
      <el-col :span="3">
        <el-select v-model="queryCondition.departmentId" placeholder="所有部门" @change="loadData(false)" clearable>
          <el-option
              v-for="item of selectionData.departmentOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id">
          </el-option>
        </el-select>
      </el-col>
      <el-col :span="3">
        <el-select v-model="queryCondition.categoryId" placeholder="所有类别" @change="loadData(false)" clearable>
          <el-option
              v-for="item of selectionData.categoryOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id">
          </el-option>
        </el-select>
      </el-col>
    </el-row>
    <el-tabs type="border-card" v-loading="tabsData.loading" :element-loading-text="loadingText"
             @tab-click="onTabClick">
      <el-tab-pane label="表格数据">
        <el-table ref="table" :data="tabsData.data" style="width: 100%;" @row-click="onRowClick"
                  :summary-method="handleSummaries" show-summary :row-class-name="tableRowClassName">
          <template slot="empty" v-if="typeof queryCondition.accountId ==='string'"><span style="font-size: 18px"
                                                                                          class="el-icon-top">请先选择 <strong>银行账户</strong></span>
          </template>
          <!--          <el-table-column type="expand">-->
          <!--            <template>-->
          <!--              <el-collapse value="">-->
          <!--                <el-collapse-item title="" name="">-->
          <!--                  功能开发中... 敬请期待!-->
          <!--                </el-collapse-item>-->
          <!--              </el-collapse>-->
          <!--            </template>-->
          <!--          </el-table-column>-->
          <el-table-column label="日期" min-width="50" :sortable="true" :sort-method="sort">
            <template slot-scope="scope">
              <span>{{
                  queryCondition.groupPolicy === 'month' ? dateFormat(scope.row.date, 'yyyy-MM') : dateFormat(scope.row.date, 'yyyy')
                }}</span>
            </template>
          </el-table-column>
          <el-table-column label="收入" prop="earning" min-width="50" :sortable="true">
            <template slot-scope="scope">{{ '￥' + scope.row.earning }}</template>
          </el-table-column>
          <el-table-column label="支出" prop="expense" min-width="50" :sortable="true">
            <template slot-scope="scope">{{ '￥' + scope.row.expense }}</template>
          </el-table-column>
          <el-table-column label="结存" prop="balance" min-width="50">
            <template slot-scope="scope">{{ '￥' + scope.row.balance }}</template>
          </el-table-column>
        </el-table>
        <el-pagination @size-change="handlePageSizeChange"
                       @current-change="handleCurrentPageChange"
                       :current-page="pageData.currentPage"
                       :page-sizes="[25, 50, 100, 500, 1000]"
                       :page-size="pageData.pageSize"
                       layout="total, sizes, prev, pager, next, jumper"
                       :total="pageData.total"
                       :pager-count="9"
                       background
                       style="text-align: left;margin-top: 20px;background-color: white;"
        ></el-pagination>
      </el-tab-pane>
      <el-tab-pane label="收支变化图表" name="moneyChangedChart"
                   :disabled="typeof queryCondition.accountId==='string'">
        <div ref="moneyChangedChart" style="width: 100%;height:95vh"></div>
      </el-tab-pane>
      <el-tab-pane label="类别支出占比图表" name="categoryProportionChart"
                   :disabled="typeof queryCondition.accountId==='string'">
        <div ref="categoryProportionChart" style="width: 100%;height:95vh"></div>
      </el-tab-pane>
      <el-tab-pane label="部门支出占比图表" name="departmentProportionChart"
                   :disabled="typeof queryCondition.accountId==='string'">
        <div ref="departmentProportionChart" style="width: 100%;height:95vh"></div>
      </el-tab-pane>

    </el-tabs>
  </div>
</template>

<script>
import echarts from 'echarts'
import statisticsApi from "@/api/statisticsApi";
import optionApi from "@/api/optionApi";
import MyUitls from "@/utils/MyUitls";

export default {
  name: "statistics",
  data() {
    return {
      loadingText: '玩命加载中...',
      selectionData: {
        projectOptions: [],
        accountOptions: [],
        departmentOptions: [],
        categoryOptions: [],
      },
      echartsData: {
        moneyChangedChart: {
          instance: null,
          option: {
            title: {
              text: '收支结存变化'
            },
            tooltip: {
              trigger: 'axis'
            },
            legend: {
              data: ['收入', '支出', '结存']
            },
            grid: {
              // left: '2%',
              // right: '3%',
              // bottom: '3%',
              containLabel: true
            },
            yAxis: {
              type: 'value'
            },
          },
        },
        categoryProportionChart: {
          instance: null,
          option: {
            title: {
              text: '各类别支出占比',
              left: 'center',
            },

            tooltip: {
              trigger: 'item',
              formatter: '{b} : {c} ({d}%)'
            },
            legend: {
              orient: 'vertical',
              left: 'left',
              data: []
            },
            series: [
              {
                type: 'pie',
                radius: '55%',
                center: ['50%', '50%'],
                data: [],
                emphasis: {
                  itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                  }
                }
              }
            ]
          },
        },
        departmentProportionChart: {
          instance: null,
          option: {
            title: {
              text: '各部门支出占比',
              left: 'center',
            },
            tooltip: {
              trigger: 'item',
              formatter: '{b} : {c} ({d}%)'
            },
            legend: {
              orient: 'vertical',
              left: 'left',
              data: []
            },
            series: [
              {
                type: 'pie',
                radius: '55%',
                center: ['50%', '50%'],
                data: [],
                emphasis: {
                  itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                  }
                }
              }
            ]
          },
        },
      },
      pageData: {
        currentPage: 1,
        pageSize: 25,
        total: 0
      },
      tabsData: {
        loading: false,
        data: [],
        tableData: {
          expandRow: null
        }
      },
      queryCondition: {
        groupPolicy: 'month',
        dateRange: [],
        projectId: null,
        accountId: '--请先选择银行账户--',
        departmentId: null,
        categoryId: null,
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
    }
  },
  created() {
    this.loadSelectionData()
  },

  methods: {
    handleSummaries({columns, data}) {
      const sums = [];
      columns.forEach((column, index) => {
        if (index === 3) {
          return;
        }
        if (index === 0) {
          sums[index] = '合计';
          return;
        }
        const values = data.map(item => Number(item[column.property]));
        if (!values.every(value => isNaN(value))) {
          sums[index] = values.reduce((prev, curr) => {
            const value = Number(curr);
            if (!isNaN(value)) {
              return (prev * 100 + curr * 100) / 100;
            } else {
              return prev;
            }
          }, 0);
          switch (index) {
            case 1:
              sums[index] = '收入合计: ￥' + sums[index];
              break;
            case 2:
              sums[index] = '支出合计: ￥' + sums[index];
              break
            default:
              sums[index] = '￥ ' + sums[index];
          }
        } else {
          sums[index] = 'N/A';
        }
      });

      return sums;
    },
    onTabClick(instance) {
      switch (instance.name) {
        case 'moneyChangedChart':
          if (!this.echartsData.moneyChangedChart.instance) {
            setTimeout(() => {
              this.echartsData.moneyChangedChart.instance = echarts.init(this.$refs.moneyChangedChart, null, {renderer: 'canvas'});
              this.renderChart(this.echartsData.moneyChangedChart.instance);
            }, 200);
          }
          break;
        case 'categoryProportionChart':
          if (!this.echartsData.categoryProportionChart.instance) {
            setTimeout(() => {
              this.echartsData.categoryProportionChart.instance = echarts.init(this.$refs.categoryProportionChart, null, {renderer: 'canvas'});
              this.echartsData.categoryProportionChart.instance.setOption(this.echartsData.categoryProportionChart.option, true);
            }, 200);
          }
          break;
        case 'departmentProportionChart':
          if (!this.echartsData.departmentProportionChart.instance) {
            setTimeout(() => {
              this.echartsData.departmentProportionChart.instance = echarts.init(this.$refs.departmentProportionChart, null, {renderer: 'canvas'});
              this.echartsData.departmentProportionChart.instance.setOption(this.echartsData.departmentProportionChart.option, true);
            }, 200);
          }
          break;
      }
    },
    dateFormat(date, fmt) {
      return MyUitls.dateFormat(new Date(date), fmt)
    },
    handleCurrentPageChange(currentPage) {
      this.pageData.currentPage = currentPage
      this.loadData(true)
      window.scrollTo(0, 70)
    },
    handlePageSizeChange(pageSize) {
      this.pageData.pageSize = pageSize
      this.loadData(true)
    },
    sort(a, b) {
      return a.date > b.date ? 1 : (a.data < b.date ? -1 : 0)
    },
    onRowClick() {
      // if (this.tabsData.tableData.expandRow === row) {
      //   //关闭
      //   this.$refs.table.toggleRowExpansion(row, false);
      //   this.tabsData.tableData.expandRow = null;
      // } else if (this.tabsData.tableData.expandRow) {
      //   //关闭之前的, 展开现在的
      //   this.$refs.table.toggleRowExpansion(this.tabsData.tableData.expandRow, false);
      //   this.$refs.table.toggleRowExpansion(row, true);
      //   this.tabsData.tableData.expandRow = row;
      // } else {
      //   //展开
      //   this.$refs.table.toggleRowExpansion(row, true);
      //   this.tabsData.tableData.expandRow = row;
      // }
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
    loadSelectionData() {
      return optionApi.getAll().then((result) => {
        this.selectionData = result.data;
      })
    },
    loadData(isPageChanging) {
      if (typeof this.queryCondition.accountId === "number") {
        this.tabsData.loading = true;
        //处理属性
        let queryCondition = {...this.queryCondition, ...this.pageData};
        const dateRange = queryCondition.dateRange;
        if (dateRange) {
          queryCondition.beginDate = dateRange[0]
          queryCondition.endDate = dateRange[1]
        }
        queryCondition.dateRange = null;
        queryCondition.total = null;
        queryCondition.datePickerOptions = null;

        statisticsApi.money(queryCondition)
            .then((result) => {
                  this.tabsData.data = result.data.list;
                  this.pageData.total = result.data.total
                  if (isPageChanging) {
                    //分页变化
                    this.renderChart(this.echartsData.moneyChangedChart);
                  } else {
                    //过滤条件变化
                    this.renderChart('all');
                  }
                  this.tabsData.loading = false;
                }
            ).catch(() => this.tabsData.loading = false);
      } else {
        this.$message.warning({message: "请先选择银行账户!", showClose: true});
      }
    },
    // 渲染图表
    renderChart(chartInstance) {
      const all = 'all';
      switch (chartInstance) {
        case all:
          //为 'all' 则全部执行(case穿透)
          // eslint-disable-next-line no-fallthrough
        case this.echartsData.moneyChangedChart.instance:
          this.handleMoneyChangedChartOption()
          if (this.echartsData.moneyChangedChart.instance) {
            // 第二个参数的意思为是否重新绘制(默认合并上一次绘制)
            this.echartsData.moneyChangedChart.instance.setOption(this.echartsData.moneyChangedChart.option, true);
          }
          if (chartInstance !== all) {
            break;
          }
          // eslint-disable-next-line no-fallthrough
        case this.echartsData.categoryProportionChart.instance:
          this.handleCategoryProportionChartOption().then(() => {
            if (this.echartsData.categoryProportionChart.instance) {
              // 第二个参数的意思为是否重新绘制(默认合并上一次绘制)
              this.echartsData.categoryProportionChart.instance.setOption(this.echartsData.categoryProportionChart.option, true);
            }
          })
          if (chartInstance !== all) {
            break;
          }
          // eslint-disable-next-line no-fallthrough
        case this.echartsData.departmentProportionChart.instance:
          this.handleDepartmentProportionChartOption().then(() => {
            if (this.echartsData.departmentProportionChart.instance) {
              // 第二个参数的意思为是否重新绘制(默认合并上一次绘制)
              this.echartsData.departmentProportionChart.instance.setOption(this.echartsData.departmentProportionChart.option, true);
            }
          })
          if (chartInstance !== all) {
            break;
          }
      }
    },
    handleDepartmentProportionChartOption() {
      const queryCondition = {...this.queryCondition};
      const dateRange = queryCondition.dateRange;
      if (dateRange) {
        queryCondition.beginDate = dateRange[0]
        queryCondition.endDate = dateRange[1]
      }
      return statisticsApi.getDepartmentProportionData(queryCondition).then((result) => {
        const data = result.data
        this.echartsData.departmentProportionChart.option.series[0].data = data
        const names = [];
        data.forEach(data => {
          names.push(data.name);
        })
        this.echartsData.departmentProportionChart.option.legend.data = names
      });
    },
    handleCategoryProportionChartOption() {
      const queryCondition = {...this.queryCondition};
      const dateRange = queryCondition.dateRange;
      if (dateRange) {
        queryCondition.beginDate = dateRange[0]
        queryCondition.endDate = dateRange[1]
      }
      return statisticsApi.getCategoryProportionData(queryCondition).then((result) => {
        const data = result.data
        const names = [];
        this.echartsData.categoryProportionChart.option.series[0].data = data
        data.forEach(data => {
          names.push(data.name);
        })
        this.echartsData.categoryProportionChart.option.legend.data = names
      })
    },
    handleMoneyChangedChartOption() {
      //处理数据
      const earnings = [];
      const expenses = [];
      const balances = [];
      const dates = [];
      for (let i = this.tabsData.data.length - 1; i >= 0; i--) {
        const e = this.tabsData.data[i]
        earnings.push(e.earning);
        expenses.push(e.expense);
        balances.push(e.balance);
        let date = null;
        if (this.queryCondition.groupPolicy === 'month') {
          date = this.dateFormat(new Date(e.date), "yyyy-MM");
        } else if (this.queryCondition.groupPolicy === 'year') {
          date = this.dateFormat(new Date(e.date), "yyyy");
        }
        dates.push(date);
      }

      const option = this.echartsData.moneyChangedChart.option
      option.xAxis = {
        type: 'category',
        boundaryGap: true,
        data: dates
      }
      option.series = [
        {
          name: '收入',
          type: 'line',
          color: '#67C23A',
          data: earnings
        },
        {
          name: '支出',
          type: 'line',
          color: '#F56C6C',
          data: expenses
        },
        {
          name: '结存',
          type: 'bar',
          color: '#FFD700',
          // barMaxWidth: "50%",
          data: balances
        }
      ]
    }
  }
}
</script>

<style scoped>

</style>