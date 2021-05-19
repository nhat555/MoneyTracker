package com.nhat.moneytracker.modules.others;

public class CategoryNameModule {

    public static String getLabelByText(String text) {
        if(text.equals("Khoản thu")) {
            return "doanhthu";
        }
        return "khoanchi";
    }
}
