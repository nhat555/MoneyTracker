package com.nhat.moneytracker.charts;

import android.content.Context;
import android.widget.TextView;

import com.nhat.moneytracker.entities.SoGiaoDich;
import com.nhat.moneytracker.helper.DBHelper;
import com.github.mikephil.charting.charts.BarChart;

import java.util.ArrayList;

public class DisplayChart {

    public static void createChart(BarChart barChart, ArrayList<SoGiaoDich> soGiaoDiches, Context context, DBHelper dbHelper, TextView textViewTime) {
        String time = textViewTime.getText().toString();
        switch (time.length()) {
            case 0:
                chartByAll(barChart, soGiaoDiches, context, dbHelper);
            case 4:
                chartByYear();
            case 7:
                chartByMonth();
            case 10:
                chartByDay();
            default:
                chartByCustom();
        }
    }

    private static void chartByCustom() {

    }

    private static void chartByDay() {

    }

    private static void chartByMonth() {

    }

    private static void chartByYear() {

    }

    private static void chartByAll(BarChart barChart, ArrayList<SoGiaoDich> soGiaoDiches, Context context, DBHelper dbHelper) {
        ArrayList<Integer> years = LabelTimeChart.getYearLabel(soGiaoDiches);
        ArrayList<SoGiaoDich> listSGD = BarDataRevenue.getSoGiaoDichsByYear(soGiaoDiches, years);
        BarDataChart.setBarData(barChart, soGiaoDiches, context, dbHelper);
        LabelTimeChart.displayTimeLabel(barChart, years);
    }

}
