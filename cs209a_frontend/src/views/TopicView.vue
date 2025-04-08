<template>
  <div class="topic-container">
    <el-card shadow="hover" class="chart-card">
      <div class="chart-header">
        <h2>Topic 热度分析</h2>
        <div class="chart-toggle">
          <el-radio-group v-model="chartType" size="large" @change="toggleChart">
            <el-radio-button label="bar">柱状图</el-radio-button>
            <el-radio-button label="pie">饼图</el-radio-button>
          </el-radio-group>
        </div>
      </div>

      <div ref="topicChart" class="chart" v-loading="loading"></div>

      <div class="chart-footer" v-if="dataCount > 0">
        <span>共显示 {{ dataCount }} 个 topic</span>
        <el-tag type="info" v-if="chartType === 'bar'">数据按热度降序排列</el-tag>
      </div>

      <el-empty v-if="dataCount === 0" description="暂无数据" :image-size="200" />
    </el-card>
  </div>
</template>

<script setup>
import * as echarts from "echarts";
import { onMounted, ref } from "vue";

const baseurl = "http://127.0.0.1:8090";
const topicChart = ref(null);
const chartType = ref("bar");
const loading = ref(true);
const dataCount = ref(0);
const chartData = ref({
  bar: { xAxisData: [], seriesData: [] },
  pie: []
});

// 颜色集合
const colorPalette = [
  '#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de',
  '#3ba272', '#fc8452', '#9a60b4', '#ea7ccc', '#17b3a3'
];

function initChart() {
  const chart = echarts.init(topicChart.value);
  chart.resize({ height: "500px" });
  return chart;
}

function drawPieChart() {
  const chart = initChart();
  chart.setOption({
    title: {
      text: "Topic 热度分布",
      left: "center",
      textStyle: {
        fontSize: 18,
        fontWeight: 'bold',
        color: '#333'
      }
    },
    tooltip: {
      trigger: "item",
      formatter: params => {
        return `${params.seriesName}<br/>
                ${params.marker}${params.name}<br/>
                热度: ${params.value}<br/>
                占比: ${params.percent}%`;
      },
      backgroundColor: 'rgba(50,50,50,0.9)',
      borderColor: '#333',
      textStyle: {
        color: '#fff'
      }
    },
    legend: {
      orient: 'vertical',
      right: 20,
      top: 'middle',
      textStyle: {
        color: '#666'
      }
    },
    series: [
      {
        name: "热度",
        type: "pie",
        radius: ['40%', '70%'],
        avoidLabelOverlap: true,
        itemStyle: {
          borderRadius: 6,
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
            fontSize: '16',
            fontWeight: 'bold'
          }
        },
        data: chartData.value.pie,
        color: colorPalette
      }
    ]
  });
}

function drawBarChart() {
  const chart = initChart();
  const option = {
    title: {
      text: "Topic 热度排行",
      left: "center",
      textStyle: {
        fontSize: 18,
        fontWeight: 'bold',
        color: '#333'
      }
    },
    tooltip: {
      trigger: "axis",
      formatter: params => {
        return `${params[0].marker}${params[0].name}<br/>
                热度: ${params[0].value}`;
      },
      backgroundColor: 'rgba(50,50,50,0.9)',
      borderColor: '#333',
      textStyle: {
        color: '#fff'
      }
    },
    grid: {
      left: '15%',
      right: '5%',
      top: '15%',
      bottom: '15%',
      containLabel: true
    },
    yAxis: {
      type: "category",
      data: chartData.value.bar.xAxisData,
      axisLabel: {
        interval: 0,
        fontSize: 12,
        color: '#666'
      },
      axisLine: {
        lineStyle: {
          color: '#ddd'
        }
      }
    },
    xAxis: {
      type: "value",
      axisLine: {
        lineStyle: {
          color: '#ddd'
        }
      },
      splitLine: {
        lineStyle: {
          type: 'dashed'
        }
      }
    },
    series: [
      {
        name: "热度",
        data: chartData.value.bar.seriesData,
        type: "bar",
        itemStyle: {
          color: params => {
            return colorPalette[params.dataIndex % colorPalette.length];
          },
          borderRadius: [0, 4, 4, 0]
        },
        barWidth: '60%',
        label: {
          show: true,
          position: 'right',
          formatter: '{c}',
          color: '#666'
        }
      }
    ]
  };

  chart.setOption(option);
}

function toggleChart() {
  if (chartType.value === 'bar') {
    drawBarChart();
  } else {
    drawPieChart();
  }
}

onMounted(async () => {
  try {
    const response = await fetch(baseurl + "/topic/");
    const responseData = await response.json();

    // 处理柱状图数据
    responseData.forEach(item => {
      for (const key in item) {
        chartData.value.bar.xAxisData.push(key);
        chartData.value.bar.seriesData.push(item[key]);
      }
    });

    // 处理饼图数据
    chartData.value.pie = responseData.map(item => {
      const key = Object.keys(item)[0];
      return {
        value: item[key],
        name: key
      };
    });

    dataCount.value = responseData.length;

    // 默认显示柱状图
    drawBarChart();
  } catch (error) {
    console.error("Error:", error);
    ElMessage.error('数据加载失败');
  } finally {
    loading.value = false;
  }

  // 窗口大小变化时重绘
  window.addEventListener('resize', function() {
    if (topicChart.value) {
      const chart = echarts.getInstanceByDom(topicChart.value);
      if (chart) chart.resize();
    }
  });
});
</script>

<style scoped>
.topic-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.chart-card {
  border-radius: 12px;
  transition: all 0.3s;
}

.chart-card:hover {
  box-shadow: 0 6px 16px -8px rgba(0, 0, 0, 0.16) !important;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.chart-header h2 {
  margin: 0;
  font-size: 20px;
  color: #333;
}

.chart {
  width: 100%;
  height: 500px;
}

.chart-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #eee;
  font-size: 14px;
  color: #888;
}

@media (max-width: 768px) {
  .chart-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .chart {
    height: 400px;
  }
}
</style>