<template>
  <div  ref="topicChart" class="chart"></div>
</template>

<style scoped>
.chart {
  width: 80%;
  position: relative;
  top: 40%; /*偏移*/
  transform: translateY(50%);
  text-align: center;
  margin-left: 10%;
}
</style>

<script setup>
import * as echarts from "echarts";
import { onMounted, ref } from "vue";

const baseurl = "http://127.0.0.1:8090";
const topicChart = ref(null);
function drawPieChart(chartData) {
  const chart = echarts.init(topicChart.value);
  chart.resize({ height: "400px" });
  chart.setOption({
    title: {
      text: "不同topic热度信息",
      left: "center",
    },
    tooltip: {
      trigger: "item",
      formatter: "{a} <br/>{b} : {c} ({d}%)",
    },
    series: [
      {
        name: "热度",
        type: "pie",
        radius: "55%",
        data: chartData,
      },
    ],
  });
}

function drawbarChart(chartData) {
  const xAxisDataReversed = [...chartData.value.xAxisData].reverse();
  const seriesDataReversed = [...chartData.value.seriesData].reverse();
  const chart = echarts.init(topicChart.value);
  const option = {
    title: {
      text: "不同topic热度信息",
      left: "center",
    },
    tooltip: { trigger: "axis", formatter: "{a} <br/>{b} : {c} " },
    grid: {
      x: '10%', //左
      y: '10%', //上
      x2: '10%', //右
      y2: '10%', //下
    },
    yAxis: {
      type: "category",
      data: xAxisDataReversed,//chartData.value.xAxisData,
     
    },
    xAxis: {
      type: "value",
    },
    series: [
      {
        name: "热度",
        data: seriesDataReversed,//chartData.value.seriesData,
        type: "bar",
      },
    ],
  };

  chart.setOption(option);
  chart.resize({ height: "400px" });
}

const barchartData = ref({
  xAxisData: [],
  seriesData: [],
});

// 将数据格式化为 ECharts 能够理解的格式

onMounted(async () => {
  try {
    const response = await fetch(baseurl + "/topic/");
    const responseData = await response.json();
//barchart:
    responseData.forEach((item) => {
      for (const key in item) {
        barchartData.value.xAxisData.push(key);
        barchartData.value.seriesData.push(item[key]);
      }
    });
      drawbarChart(barchartData)

//pie chart:
    // const formattedData = ref(
    //   responseData.map((item) => {
    //     const key = Object.keys(item)[0];
    //     const value = item[key];
    //     return {
    //       value: value,
    //       name: key,
    //     };
    //   })
    // );
    // drawPieChart(formattedData.value);
  } catch (error) {
    console.error("Error:", error);
  }
});
</script>


