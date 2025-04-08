<template>
  <div class="wordcloud-container">
    <!-- 搜索区域 -->
    <el-card shadow="never" class="search-card">
      <div class="search-wrapper">
        <el-input
            v-model="searchTopic"
            placeholder="请输入主题关键词"
            clearable
            @keyup.enter="fetchData"
            class="search-input"
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
          <el-icon class="el-icon--right"><Right /></el-icon>
        </el-button>
      </div>
      <div class="search-tips" v-if="!searchTopic">
        <el-icon><InfoFilled /></el-icon>
        <span>请输入主题关键词查看相关话题词云</span>
      </div>
    </el-card>

    <!-- 词云区域 -->
    <el-card shadow="hover" class="wordcloud-card">
      <div ref="wordCloudChart" class="chart"></div>
      <div class="chart-empty" v-if="!hasData">
        <el-empty description="暂无数据，请输入关键词搜索" />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import * as echarts from "echarts";
import "echarts-wordcloud";
import { onMounted, ref, watch } from "vue";
import { Search, Right, InfoFilled } from '@element-plus/icons-vue'

const baseurl = "http://127.0.0.1:8090";
const searchTopic = ref("");
const wordCloudChart = ref(null);
const loading = ref(false);
const hasData = ref(false);

// 预定义颜色方案
const colorPalette = [
  '#5470c6', '#91cc75', '#fac858', '#ee6666',
  '#73c0de', '#3ba272', '#fc8452', '#9a60b4',
  '#ea7ccc', '#17b3a3', '#6e7074', '#61a0a8'
];

function drawWordCloudChart(chartData) {
  const chart = echarts.init(wordCloudChart.value);

  const option = {
    title: {
      text: "相关话题热度词云",
      left: "center",
      textStyle: {
        fontSize: 18,
        fontWeight: 'bold',
        color: '#333'
      },
      subtext: searchTopic.value ? `与"${searchTopic.value}"相关的热门话题` : '相关话题热度',
      subtextStyle: {
        fontSize: 14,
        color: '#666'
      }
    },
    tooltip: {
      trigger: "item",
      formatter: function (params) {
        return `<div style="font-weight:bold">${params.name}</div>
                <div>相关性: ${(params.value * 100).toFixed(1)}%</div>`;
      },
      backgroundColor: 'rgba(50,50,50,0.9)',
      borderColor: '#333',
      textStyle: {
        color: '#fff'
      }
    },
    series: [
      {
        name: "相关性",
        type: "wordCloud",
        shape: "circle",
        left: "center",
        top: "center",
        width: "90%",
        height: "90%",
        sizeRange: [16, 72],
        rotationRange: [-45, 45],
        rotationStep: 15,
        gridSize: 10,
        drawOutOfBound: false,
        shrinkToFit: true,
        layoutAnimation: true,
        textStyle: {
          fontFamily: "Microsoft YaHei, sans-serif",
          fontWeight: "bold",
          color: function () {
            return colorPalette[Math.floor(Math.random() * colorPalette.length)];
          }
        },
        emphasis: {
          focus: "self",
          textStyle: {
            shadowBlur: 8,
            shadowColor: '#333'
          }
        },
        data: chartData
      }
    ]
  };

  chart.setOption(option);
  hasData.value = chartData && chartData.length > 0;

  // 窗口大小变化时重绘
  window.addEventListener('resize', function() {
    chart.resize();
  });
}

const fetchData = async () => {
  if (!searchTopic.value.trim()) {
    ElMessage.warning('请输入搜索关键词');
    return;
  }

  try {
    loading.value = true;
    const response = await fetch(
        `${baseurl}/related/search?topic=${encodeURIComponent(searchTopic.value)}`
    );
    const responseData = await response.json();

    const formattedData = responseData
        .map((item) => {
          const key = Object.keys(item)[0];
          const value = item[key];
          return {
            value: value,
            name: key
          };
        })
        .filter((item) => item.value >= 0.1)
        .sort((a, b) => b.value - a.value)
        .slice(0, 50); // 限制显示数量

    drawWordCloudChart(formattedData);
  } catch (error) {
    console.error("Error:", error);
    ElMessage.error('获取数据失败');
    hasData.value = false;
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  // 初始化一个空图表
  drawWordCloudChart([]);
});
</script>

<style scoped>
.wordcloud-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.search-card {
  margin-bottom: 24px;
  border-radius: 8px;
  border: 1px solid #ebeef5;
}

.search-wrapper {
  display: flex;
  gap: 12px;
  margin-bottom: 8px;
}

.search-input {
  flex: 1;
}

.search-button {
  width: 120px;
}

.search-tips {
  font-size: 13px;
  color: #909399;
  display: flex;
  align-items: center;
}

.search-tips .el-icon {
  margin-right: 6px;
}

.wordcloud-card {
  border-radius: 8px;
  transition: all 0.3s;
}

.wordcloud-card:hover {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1) !important;
}

.chart {
  width: 100%;
  height: 500px;
}

.chart-empty {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 400px;
}

@media (max-width: 768px) {
  .search-wrapper {
    flex-direction: column;
  }

  .search-button {
    width: 100%;
  }

  .chart {
    height: 350px;
  }
}
</style>