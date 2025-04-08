<template>
  <div class="dashboard-container">
    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :xs="24" :sm="12" :md="12">
        <el-card shadow="hover" class="chart-card">
          <div ref="exceptionPieChart" class="chart" />
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="12">
        <el-card shadow="hover" class="chart-card">
          <div ref="errorPieChart" class="chart" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-row :gutter="20" class="search-row">
      <el-col :xs="24" :sm="18" :md="16" :lg="12">
        <el-card shadow="never" class="search-card">
          <div class="search-container">
            <el-input
                v-model="searchBug"
                placeholder="请输入Bug类型进行搜索"
                clearable
                @keyup.enter="fetchData"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
            <el-button
                type="primary"
                :loading="loading"
                @click="fetchData"
                class="search-button"
            >
              搜索
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 柱状图区域 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="24">
        <el-card shadow="hover" class="chart-card">
          <div ref="bugBarChart" class="chart" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import * as echarts from "echarts";
import { onMounted, ref } from "vue";
import { Search } from '@element-plus/icons-vue'

const searchBug = ref(null);
const errorPieChart = ref(null);
const exceptionPieChart = ref(null);
const bugBarChart = ref(null);
const loading = ref(false);

const baseurl = "http://127.0.0.1:8090";

function drawPieChart(chartData, mychart, type) {
  const piechart = echarts.init(mychart.value);
  piechart.setOption({
    title: {
      text: `不同类${type}热度信息`,
      left: "center",
      textStyle: {
        fontSize: 16,
        fontWeight: 'bold'
      }
    },
    tooltip: {
      trigger: "item",
      formatter: "{a} <br/>{b} : {c} ({d}%)",
    },
    legend: {
      orient: 'vertical',
      right: 10,
      top: 'center'
    },
    series: [
      {
        name: "热度",
        type: "pie",
        radius: ["40%", "70%"],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: true,
          formatter: '{b}: {d}%'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '18',
            fontWeight: 'bold'
          }
        },
        data: chartData,
      },
    ],
    color: ['#36a2eb', '#ff6384', '#ffcd56', '#4bc0c0', '#9966ff', '#ff9f40']
  });
}

function drawBarChart(chartData, type) {
  const chart = echarts.init(bugBarChart.value);
  const option = {
    dataZoom: [
      {
        type: 'inside',
        show: true,
        start: 0,
        end: 30,
        xAxisIndex: [0],
      },
    ],
    title: {
      text: `${type}热度`,
      left: "center",
      textStyle: {
        fontSize: 16,
        fontWeight: 'bold'
      }
    },
    tooltip: {
      trigger: "axis",
      formatter: "{a} <br/>{b} : {c}",
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '15%',
      top: '15%',
      containLabel: true
    },
    xAxis: {
      type: "category",
      data: Object.keys(chartData),
      axisLabel: {
        interval: 0,
        rotate: -60,
        margin: 6,
        fontSize: 12
      },
    },
    yAxis: {
      type: "value",
      axisLabel: {
        fontSize: 12
      }
    },
    series: [
      {
        name: "热度",
        data: Object.values(chartData),
        type: "bar",
        itemStyle: {
          color: '#36a2eb',
          borderRadius: [4, 4, 0, 0]
        },
        barWidth: '60%',
        emphasis: {
          itemStyle: {
            color: '#1e88e5'
          }
        }
      },
    ],
  };

  chart.setOption(option);
}

const fetchData = async () => {
  if (!searchBug.value) {
    ElMessage.warning('请输入搜索内容');
    return;
  }

  try {
    loading.value = true;
    const response = await fetch(
        `${baseurl}/bug/compare/within-category?type=${searchBug.value}`
    );
    const responseData = await response.json();
    drawBarChart(responseData, searchBug.value);
  } catch (error) {
    console.error("Error:", error);
    ElMessage.error('数据加载失败');
  } finally {
    loading.value = false;
  }
};

onMounted(async () => {
  try {
    let response = await fetch(baseurl + "/bug/compare/between-categories?type=error");
    let responseData = await response.json();
    drawPieChart(transformErrorMap(responseData), errorPieChart, "error");

    response = await fetch(baseurl + "/bug/compare/between-categories?type=exception");
    responseData = await response.json();
    drawPieChart(transformErrorMap(responseData), exceptionPieChart, "exception");
  } catch (error) {
    console.error("Error:", error);
    ElMessage.error('初始化图表失败');
  }
});

const transformErrorMap = (map) => {
  return Object.entries(map).map(([name, value]) => {
    return { value, name };
  });
};
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
}

.chart-row {
  margin-bottom: 24px;
}

.search-row {
  margin-bottom: 24px;
}

.chart-card {
  transition: all 0.3s ease;
  border-radius: 8px;
}

.chart-card:hover {
  box-shadow: 0 6px 16px -8px rgba(0, 0, 0, 0.16) !important;
}

.search-card {
  border: none;
  background-color: #f8fafc;
}

.search-container {
  display: flex;
  gap: 12px;
}

.search-button {
  flex-shrink: 0;
}

.chart {
  width: 100%;
  height: 400px;
  transition: all 0.3s ease;
}

@media (max-width: 768px) {
  .search-container {
    flex-direction: column;
  }

  .search-button {
    width: 100%;
  }

  .chart {
    height: 300px;
  }
}
</style>