package edu.sustech.cs209a.java2finalprojectdemo.visualization;

import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.general.*;

import java.util.List;
import java.util.Map;

public class PieChartDrawer {
    /*
    You can ignore this class since echarts would be finally used for visualization, not java
     */
    public static void drawPieChart(List<Map.Entry<String, Double>> dataList) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        for (Map.Entry<String, Double> entry : dataList) {
            String topic = entry.getKey();
            double heat = entry.getValue();
            dataset.setValue(topic, heat);
        }

        JFreeChart chart = ChartFactory.createPieChart(
            "Topic Heat Distribution",
            dataset,
            true,
            true,
            false
        );

        ChartFrame frame = new ChartFrame("Pie Chart", chart);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        List<Map.Entry<String, Double>> sortedList = List.of(
            Map.entry("Topic1", 20.0),
            Map.entry("Topic2", 10.0)
        );

        drawPieChart(sortedList);
    }
}
