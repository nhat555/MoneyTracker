package com.nhat.moneytracker.charts;

import com.nhat.moneytracker.entities.SoGiaoDich;
import com.nhat.moneytracker.modules.converts.IntegerToString;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class LabelTimeChart {

    public static void displayTimeLabel(BarChart barChart, ArrayList<Integer> listInteger) {
        ArrayList<String> strings = IntegerToString.convertIntegerToStringArrayList(listInteger);
        String[] labels = strings.toArray(new String[strings.size()]);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
    }
    
    public static ArrayList<Integer> getYearLabel(ArrayList<SoGiaoDich> soGiaoDiches) {
        Calendar calendar = Calendar.getInstance();
        ArrayList<Integer> labels = new ArrayList<>();
        for (int i = 0; i < soGiaoDiches.size(); i++) {
            calendar.setTime(soGiaoDiches.get(i).getNgayGiaoDich());
            int year = calendar.get(Calendar.YEAR);
            if(!labels.contains(year)) {
                labels.add(year);
            }
        }
        Collections.sort(labels);
        return labels;
    }
}
