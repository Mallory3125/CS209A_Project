<template>
    <el-row :gutter="20">
      <el-col :span="12"><div ref="exceptionPieChart" class="chart" /></el-col>
      <el-col :span="12"><div ref="errorPieChart" class="chart" /></el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="4"
        ><el-input v-model="searchBug" placeholder="please enter a type of bug"
      /></el-col>
      <el-col :span="12">
        <el-button type="primary" @click="fetchData">Search</el-button>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="24"><div ref="bugBarChart" class="chart" /></el-col>
    </el-row>
  </template>
  
  <script setup>
  import * as echarts from "echarts";
  import { onMounted, ref } from "vue";
  
  const searchBug = ref(null);
  const errorPieChart = ref(null);
  const exceptionPieChart = ref(null);
  const bugBarChart = ref(null);
  
  const baseurl = "http://127.0.0.1:8090";
  
  function drawPieChart(chartData,mychart,type) {
    const piechart = echarts.init(mychart.value);
    piechart.resize({ height: "400px" });
    piechart.setOption({
      title: {
        text: "不同类"+type+"热度信息",
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
  function drawbarChart(chartData,type) {
    const chart = echarts.init(bugBarChart.value);
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
        type: "value",
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

  const fetchData = async () => {
    try {
      const response = await fetch(
        `${baseurl}/bug/compare/within-category?type=${searchBug.value}`
      );
      const responseData = await response.json();
      drawbarChart(responseData,searchBug.value);
    } catch (error) {
      console.error("Error:", error);
    }
  };

  
  onMounted(async () => {
    try {
     
      var response = await fetch(baseurl + "/bug/compare/between-categories?type=error");
      var responseData = await response.json();
      drawPieChart(transformErrorMap(responseData),errorPieChart,"error");
      response = await fetch(baseurl + "/bug/compare/between-categories?type=exception");
      responseData = await response.json();
      drawPieChart(transformErrorMap(responseData),exceptionPieChart,"exception");
    } catch (error) {
      console.error("Error:", error);
    }
  });
  
  
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
  
