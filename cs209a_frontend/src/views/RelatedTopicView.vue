<template>
  <el-row :gutter="20">
    <el-col :span="4"
      ><el-input v-model="searchTopic" placeholder="Enter topic"
    /></el-col>
    <el-col :span="12">
      <el-button type="primary" @click="fetchData">Search</el-button>
    </el-col>
  </el-row>
  <el-row :gutter="20">
    <el-col :span="24"><div ref="wordCloudChart" class="chart" /></el-col>
  </el-row>
</template>

<script setup>
import * as echarts from "echarts";
import "echarts-wordcloud";
import { onMounted, ref } from "vue";

const baseurl = "http://127.0.0.1:8090";
const searchTopic = ref(null);

const wordCloudChart = ref(null);
function drawWordCloudChart(chartData) {
  console.log(chartData);
  const chart = echarts.init(wordCloudChart.value);
  chart.resize({ height: "400px" });
  chart.setOption({
    title: {
      text: "相关话题",
      left: "center",
    },
    tooltip: {
      trigger: "item",
      formatter: function (params) {
        return `${params.seriesName}<br/>${params.marker}${params.name}: (${params.value}%)`;
      },
    },
    series: [
      {
        name: "相关性",
        type: "wordCloud",
        shape: "circle",
        left: "center",
        top: "center",
        width: "80%",
        height: "90%",
        right: null,
        bottom: null,
        sizeRange: [12, 60],
        rotationRange: [-90, 90],
        rotationStep: 45,
        gridSize: 8,
        drawOutOfBound: false,
        shrinkToFit: true,
        layoutAnimation: true,

        // Global text style
        textStyle: {
          fontFamily: "sans-serif",
          fontWeight: "bold",
          // Color can be a callback function or a color string
          color: function () {
            // Random color
            return (
              "rgb(" +
              [
                Math.round(Math.random() * 160),
                Math.round(Math.random() * 160),
                Math.round(Math.random() * 160),
              ].join(",") +
              ")"
            );
          },
        },
        emphasis: {
          focus: "self",
          textStyle: {
            textShadowBlur: 10,
            textShadowColor: "#333",
          },
        },

        // Data is an array. Each array item must have name and value property.
        data: chartData,
      },
    ],
  });
}

const fetchData = async () => {
  try {
    if (searchTopic.value != null) {
      const response = await fetch(
        `${baseurl}/related/search?topic=${searchTopic.value}`
      );
      const responseData = await response.json();

      const formattedData = ref(
        responseData
          .map((item) => {
            const key = Object.keys(item)[0];
            const value = item[key];
            return {
              value: value,
              name: key,
            };
          })
          .filter((item) => item.value >= 0.1)
      );

      drawWordCloudChart(formattedData.value);
    }
    // drawPieChart(transformErrorMap(responseData), total);
  } catch (error) {
    console.error("Error:", error);
  }
};

const transformErrorMap = (map) => {
  return Object.entries(map).map(([name, value]) => {
    return { value, name };
  });
};
</script>

<style scoped>
.chart {
  width: 100%; /* Sets the width to take the full container width. Adjust as necessary. */
}
.el-row {
  margin-bottom: 20px;
}
.el-row:last-child {
  margin-bottom: 0;
}
.el-col {
  border-radius: 4px;
}

.grid-content {
  border-radius: 4px;
  min-height: 36px;
}

/* Additional styling can be added as needed, such as margins or alignment */
</style>
