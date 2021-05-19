package com.nhat.moneytracker.charts;

import com.nhat.moneytracker.entities.SoGiaoDich;

import java.util.ArrayList;
import java.util.Calendar;

public class BarDataRevenue {

    private static final String DOANHTHU = "doanhthu";

    public static ArrayList<SoGiaoDich> getSoGiaoDichsByYear(ArrayList<SoGiaoDich> soGiaoDiches, ArrayList<Integer> years) {
        ArrayList<SoGiaoDich> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (Integer year : years) {
            for (SoGiaoDich soGiaoDich : soGiaoDiches) {
                calendar.setTime(soGiaoDich.getNgayGiaoDich());
                int yearRs = calendar.get(Calendar.YEAR);
                if(yearRs == year) {
                    list.add(soGiaoDich);
                }
            }
        }
        return list;
    }
}
