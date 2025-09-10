<!-- src/views/ReserveCharts.vue -->
<template>
  <div>
    <!-- 每日挂号量柱状图 -->
    <div>
      <h2>每日挂号量</h2>
      <!-- 使用 ref 绑定到 DOM 元素，用于 ECharts 实例的挂载 -->
      <div ref="dailyChart" style="width: 100%; height: 400px;"></div>
    </div>

    <!-- 每月挂号量柱状图 -->
    <div style="margin-top: 50px;">
      <h2>每月挂号量</h2>
      <!-- 使用 ref 绑定到 DOM 元素，用于 ECharts 实例的挂载 -->
      <div ref="monthlyChart" style="width: 100%; height: 400px;"></div>
    </div>
  </div>
</template>

<script>
// 引入 ECharts 库，用于图表的创建和渲染
import * as echarts from 'echarts'

export default {
  name: 'ReserveCharts', // 组件名称
  data() {
    return {
      dailyChartInstance: null, // 存储每日挂号量图表的 ECharts 实例
      monthlyChartInstance: null, // 存储每月挂号量图表的 ECharts 实例
      dailyChartData: { // 存储每日挂号量的数据
        date: '', // 日期
        departmentNames: [], // 科室名称数组
        registrationCounts: [] // 各科室的挂号数量数组
      },
      monthlyChartData: [] // 存储每月挂号量的数据，假设是 ChartsDTO 类型的列表
    }
  },
  methods: {
    /**
     * 获取当天的挂号数据
     */
    fetchDailyChartData() {
      // 发送 GET 请求到 '/charts/dailyReserve' 端点
      this.$request.get('/charts/dailyReserve')
          .then(res => {
            if (res.code === "200") { // 检查响应码是否为 "200"
              this.dailyChartData = res.data; // 将响应数据存储到 dailyChartData
              this.renderDailyChart(); // 调用渲染每日图表的方法
            } else {
              // 如果响应码不是 "200"，显示错误消息
              this.$message.error(res.msg || '获取挂号数据失败');
            }
          })
          .catch(error => {
            // 捕捉请求错误，记录错误并显示错误消息
            console.error('获取挂号数据失败:', error);
            this.$message.error('获取挂号数据失败');
          });
    },
    /**
     * 获取每个月的挂号数据
     */
    fetchMonthlyChartData() {
      // 发送 GET 请求到 '/charts/monthlyReserve' 端点
      this.$request.get('/charts/monthlyReserve')
          .then(res => {
            if (res.code === "200") { // 检查响应码是否为 "200"
              this.monthlyChartData = res.data; // 将响应数据存储到 monthlyChartData
              this.renderMonthlyChart(); // 调用渲染每月图表的方法
            } else {
              // 如果响应码不是 "200"，显示错误消息
              this.$message.error(res.msg || '获取挂号数据失败');
            }
          })
          .catch(error => {
            // 捕捉请求错误，记录错误并显示错误消息
            console.error('获取挂号数据失败:', error);
            this.$message.error('获取挂号数据失败');
          });
    },
    /**
     * 渲染每日挂号量图表
     */
    renderDailyChart() {
      // 如果 dailyChartInstance 还未初始化，则初始化 ECharts 实例
      if (!this.dailyChartInstance) {
        this.dailyChartInstance = echarts.init(this.$refs.dailyChart);
      }

      // 定义图表的配置项
      const option = {
        title: {
          text: `各科室挂号数量（${this.dailyChartData.date}）`, // 图表标题，包含日期
          left: 'center' // 标题居中
        },
        tooltip: {
          trigger: 'axis', // 触发类型为轴触发
          axisPointer: {
            type: 'shadow' // 指示器类型为阴影
          }
        },
        xAxis: {
          type: 'category', // X 轴类型为类目轴
          data: this.dailyChartData.departmentNames, // X 轴数据为科室名称
          axisLabel: {
            interval: 0, // 标签不跳跃显示
            rotate: 30 // 标签旋转 30 度以避免重叠
          }
        },
        yAxis: {
          type: 'value', // Y 轴类型为数值轴
          name: '挂号数量' // Y 轴名称
        },
        series: [{
          name: '挂号数量', // 系列名称
          type: 'bar', // 系列类型为柱状图
          data: this.dailyChartData.registrationCounts, // 系列数据为挂号数量
          itemStyle: {
            color: '#3398DB' // 柱状图颜色
          }
        }]
      };

      // 设置图表的配置项
      this.dailyChartInstance.setOption(option);
      // 监听窗口大小变化，自动调整图表大小
      window.addEventListener('resize', this.dailyChartInstance.resize);
    },
    /**
     * 渲染每月挂号量图表
     */
    renderMonthlyChart() {
      // 如果 monthlyChartInstance 还未初始化，则初始化 ECharts 实例
      if (!this.monthlyChartInstance) {
        this.monthlyChartInstance = echarts.init(this.$refs.monthlyChart);
      }

      // 准备数据
      const allMonths = this.monthlyChartData.map(item => item.date); // 获取所有月份
      const departmentsSet = new Set(); // 使用 Set 收集唯一的科室名称

      // 收集所有科室名称
      this.monthlyChartData.forEach(item => {
        item.departmentNames.forEach(dept => departmentsSet.add(dept));
      });

      const departments = Array.from(departmentsSet).sort(); // 转换为数组并排序

      // 初始化数据结构，为每个科室创建一个系列
      const series = departments.map(dept => ({
        name: dept, // 系列名称为科室名称
        type: 'bar', // 系列类型为柱状图
        stack: '总量', // 使用 stack 实现堆叠效果
        data: [] // 初始化数据为空数组
      }));

      // 按月份填充数据
      allMonths.forEach(month => {
        const monthData = this.monthlyChartData.find(item => item.date === month); // 查找对应月份的数据
        departments.forEach((dept, index) => {
          if (monthData.departmentNames.includes(dept)) {
            const deptIndex = monthData.departmentNames.indexOf(dept); // 找到科室在当前月份中的索引
            const count = monthData.registrationCounts[deptIndex] || 0; // 获取挂号数量，如果未找到则为 0
            series[index].data.push(count); // 将挂号数量添加到对应系列的数据数组中
          } else {
            series[index].data.push(0); // 如果当前月份没有该科室的数据，则添加 0
          }
        });
      });

      // 定义图表的配置项
      const option = {
        title: {
          text: '每月各科室挂号数量', // 图表标题
          left: 'center' // 标题居中
        },
        tooltip: {
          trigger: 'axis', // 触发类型为轴触发
          axisPointer: {
            type: 'shadow' // 指示器类型为阴影
          }
        },
        legend: {
          data: departments, // 图例数据为科室名称
          top: 30 // 图例位置距离顶部 30px
        },
        xAxis: {
          type: 'category', // X 轴类型为类目轴
          data: allMonths, // X 轴数据为月份
          axisLabel: {
            interval: 0, // 标签不跳跃显示
            rotate: 30 // 标签旋转 30 度以避免重叠
          }
        },
        yAxis: {
          type: 'value', // Y 轴类型为数值轴
          name: '挂号数量' // Y 轴名称
        },
        series: series // 系列数据
      };

      // 设置图表的配置项
      this.monthlyChartInstance.setOption(option);
      // 监听窗口大小变化，自动调整图表大小
      window.addEventListener('resize', this.monthlyChartInstance.resize);
    }
  },
  mounted() {
    // 组件挂载后，调用方法获取数据并渲染图表
    this.fetchDailyChartData();
    this.fetchMonthlyChartData();
  },
  beforeDestroy() { // Vue 2 中的生命周期钩子，Vue 3 使用 beforeUnmount
    // 在组件销毁前，移除窗口大小变化的监听器并释放 ECharts 实例
    if (this.dailyChartInstance) {
      window.removeEventListener('resize', this.dailyChartInstance.resize);
      this.dailyChartInstance.dispose(); // 释放图表实例
    }
    if (this.monthlyChartInstance) {
      window.removeEventListener('resize', this.monthlyChartInstance.resize);
      this.monthlyChartInstance.dispose(); // 释放图表实例
    }
  }
}
</script>

<style scoped>
/* 可选样式，可以根据需要进行自定义 */
/* 例如，调整图表标题的字体、颜色等 */
</style>
