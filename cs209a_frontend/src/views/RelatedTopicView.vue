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
function drawPieChart(chartData, total) {
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
        console.log(params);
        const value = params.data.value;
        const percent = ((value / total) * 100).toFixed(2);
        return `${params.seriesName}<br/>${params.marker}${params.name}: (${percent}%)`;
      },
    },
    series: [
      {
        name: "相关性",
        type: "wordCloud",

        // The shape of the "cloud" to draw. Can be any polar equation represented as a
        // callback function, or a keyword present. Available presents are circle (default),
        // cardioid (apple or heart shape curve, the most known polar equation), diamond (
        // alias of square), triangle-forward, triangle, (alias of triangle-upright, pentagon, and star.

        shape: "circle",

        // Folllowing left/top/width/height/right/bottom are used for positioning the word cloud
        // Default to be put in the center and has 75% x 80% size.

        left: "center",
        top: "center",
        width: "70%",
        height: "80%",
        right: null,
        bottom: null,

        // Text size range which the value in data will be mapped to.
        // Default to have minimum 12px and maximum 60px size.

        sizeRange: [12, 60],

        // Text rotation range and step in degree. Text will be rotated randomly in range [-90, 90] by rotationStep 45

        rotationRange: [-90, 90],
        rotationStep: 45,

        // size of the grid in pixels for marking the availability of the canvas
        // the larger the grid size, the bigger the gap between words.

        gridSize: 8,

        // set to true to allow word to be drawn partly outside of the canvas.
        // Allow word bigger than the size of the canvas to be drawn
        // This option is supported since echarts-wordcloud@2.1.0
        drawOutOfBound: false,

        // if the font size is too large for the text to be displayed,
        // whether to shrink the text. If it is set to false, the text will
        // not be rendered. If it is set to true, the text will be shrinked.
        // This option is supported since echarts-wordcloud@2.1.0
        shrinkToFit: false,

        // If perform layout animation.
        // NOTE disable it will lead to UI blocking when there is lots of words.
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
    const response = await fetch(
      `${baseurl}/related/search?topic=${searchTopic.value}`
    );
    const responseData = await response.json();
    console.log(responseData);
    let total = 0;

    Object.values(responseData).forEach((value) => {
      total += value;
    });

    drawPieChart(transformErrorMap(responseData), total);
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
