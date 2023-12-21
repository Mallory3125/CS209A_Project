<template>
  <el-row :gutter="20">
    <el-col :span="24"><div ref="runtimeBarChart" class="chart" /></el-col>
  </el-row>
  <el-row :gutter="20">
    <el-col :span="24"><div ref="checkedBarChart" class="chart" /></el-col>
  </el-row>
  <el-row :gutter="20">
    <el-col :span="24"><div ref="fatalBarChart" class="chart" /></el-col>
  </el-row>
  <el-row :gutter="20">
    <el-col :span="24"><div ref="otherBarChart" class="chart" /></el-col>
  </el-row>
  <el-row :gutter="20">
    <el-col :span="12"><div ref="runtimeChart" class="chart" /></el-col>
    <el-col :span="12"><div ref="betweenPieChart" class="chart" /></el-col>
  </el-row>
</template>

<script setup>
import * as echarts from "echarts";
// import { pa } from "element-plus/es/locale";
import { onMounted, ref } from "vue";


const betweenPieChart = ref(null);
const runtimeBarChart = ref(null);
const fatalBarChart = ref(null);
const checkedBarChart = ref(null);
const otherBarChart = ref(null);

const baseurl = "http://127.0.0.1:8090";

function drawPieChart(chartData) {
  const chart = echarts.init(betweenPieChart.value);
  chart.resize({ height: "400px" });
  chart.setOption({
    title: {
      text: "不同类bug热度信息",
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

function drawbarChart(chartRef,chartData,type) {
  const chart = echarts.init(chartRef.value);
  const option = {
    dataZoom : [
          {
            type: 'inside',
            show: true,
            start: 0,
            end: 30,
            xAxisIndex: [0],
          },
        ],
    title: {
      text: type+"热度",
      left: "center",
    },
    tooltip: {trigger: "axis",
    formatter: "{a} <br/>{b} : {c} ",
}, 
    grid: {
        x: 40,  //左
        y: 30,  //上
        x2: 40, //右
        y2: 200  //下
    },
    xAxis: {
      type: "category",
      data: Object.keys(chartData),
      axisLabel: {
        show: true, // 是否显示刻度标签，默认显示
        interval: 0, // 坐标轴刻度标签的显示间隔，在类目轴中有效；默认会采用标签不重叠的策略间隔显示标签；可以设置成0强制显示所有标签；如果设置为1，表示『隔一个标签显示一个标签』，如果值为2，表示隔两个标签显示一个标签，以此类推。
        rotate: -60, // 刻度标签旋转的角度，在类目轴的类目标签显示不下的时候可以通过旋转防止标签之间重叠；旋转的角度从-90度到90度
        inside: false, // 刻度标签是否朝内，默认朝外
        margin: 6, // 刻度标签与轴线之间的距离
      },
    },
    yAxis: {
      type: "log",
    },
    series: [
      {
        name:"热度",
        data: Object.values(chartData),
        type: "bar",
      },
    ],
  };

  chart.setOption(option);
  chart.resize({ height: "400px" });
}

onMounted(async () => {
  try {
   
    const response = await fetch(baseurl + "/bug/comparebetween");
    const responseData = await response.json();
    drawPieChart(transformErrorMap(responseData));

    comparebetween("/bug/comparewithin?type=runtime",runtimeBarChart,"RuntimeException")
    comparebetween("/bug/comparewithin?type=fatal",fatalBarChart,"FatalError")
    comparebetween("/bug/comparewithin?type=other",otherBarChart,"OtherError")
    comparebetween("/bug/comparewithin?type=checked",checkedBarChart,"CheckedException")
    // const response2 = await fetch(baseurl + "/bug/comparewithin?type=runtime");
    // const responseData2 = await response2.json();
    // drawbarChart(runtimeBarChart,responseData2,"RuntimeException");

    // const response3 = await fetch(baseurl + "/bug/comparewithin?type=fatal");
    // const responseData3 = await response3.json();
    // drawbarChart(fatalBarChart,responseData3,"FatalError");
  } catch (error) {
    console.error("Error:", error);
  }
});

async function comparebetween(path,chart,type){ 
    const response = await fetch(baseurl + path);
    const responseData = await response.json();
    drawbarChart(chart,responseData,type);
}

// beforeUnmount(async () => {
//   try {
//     await fetch(baseurl);
//   } catch (error) {
//     console.error("Error:", error);
//   }
// });

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
