package com.nhat.moneytracker.charts;

import android.content.Context;

import com.nhat.moneytracker.R;
import com.nhat.moneytracker.entities.DanhMuc;
import com.nhat.moneytracker.entities.SoGiaoDich;
import com.nhat.moneytracker.helper.DBHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class BarDataChart {
    private static final String DOANHTHU = "doanhthu";
    private static final String KHOANCHI = "khoanchi";

    public static void setBarData(BarChart barChart, ArrayList<SoGiaoDich> soGiaoDiches, Context context, DBHelper dbHelper) {
        BarDataSet barDataSetKT = setDataKhoanThu(soGiaoDiches, context, dbHelper);
        BarDataSet barDataSetKC = setDataKhoanChi(soGiaoDiches, context, dbHelper);

        BarData data = new BarData(barDataSetKT, barDataSetKC);

        barChart.setData(data);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setText("");
        barChart.setMaxVisibleValueCount(50);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);
        barChart.animateY(2000);
        barChart.setDragEnabled(true);
        barChart.setVisibleXRangeMaximum(3);

        float barSpace = 0.04f;
        float groupSpace = 0.02f;
        data.setBarWidth(0.45f);

        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * 8);
        barChart.getAxisLeft().setAxisMinimum(0);

        barChart.groupBars(0, groupSpace, barSpace);
        barChart.invalidate();
    }

    private static BarDataSet setDataKhoanChi(ArrayList<SoGiaoDich> soGiaoDiches, Context context, DBHelper dbHelper) {
        BarDataSet barDataSet = new BarDataSet(barEntriesKhoanChi(soGiaoDiches, dbHelper),
                context.getResources().getString(R.string.spend_money));
        barDataSet.setColor(context.getResources().getColor(R.color.colorRed));
        barDataSet.notifyDataSetChanged();
        return barDataSet;
    }

    private static BarDataSet setDataKhoanThu(ArrayList<SoGiaoDich> soGiaoDiches, Context context, DBHelper dbHelper) {
        BarDataSet barDataSet = new BarDataSet(barEntriesDoanhThu(soGiaoDiches, dbHelper),
                context.getResources().getString(R.string.recharge_money));
        barDataSet.setColor(context.getResources().getColor(R.color.colorLightBlue));
        barDataSet.notifyDataSetChanged();
        return barDataSet;
    }

    private static ArrayList<BarEntry> barEntriesDoanhThu(ArrayList<SoGiaoDich> soGiaoDiches, DBHelper dbHelper) {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < soGiaoDiches.size(); i++) {
            SoGiaoDich soGiaoDich = soGiaoDiches.get(i);
            DanhMuc danhMuc = dbHelper.getByID_DanhMuc(soGiaoDiches.get(i).getMaDanhMuc());
            if(danhMuc.getLoaiDanhMuc().equals(DOANHTHU)) {
                barEntries.add(new BarEntry(i, (float) soGiaoDich.getSoTien()));
            }
        }
        return barEntries;
    }

    private static ArrayList<BarEntry> barEntriesKhoanChi(ArrayList<SoGiaoDich> soGiaoDiches, DBHelper dbHelper) {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < soGiaoDiches.size(); i++) {
            SoGiaoDich soGiaoDich = soGiaoDiches.get(i);
            DanhMuc danhMuc = dbHelper.getByID_DanhMuc(soGiaoDiches.get(i).getMaDanhMuc());
            if(danhMuc.getLoaiDanhMuc().equals(KHOANCHI)) {
                barEntries.add(new BarEntry(i, (float) soGiaoDich.getSoTien()));
            }
        }
        return barEntries;
    }
}
