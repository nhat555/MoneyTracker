package com.nhat.moneytracker.dbs;

public class ChiTietNganSachDBS {

    private static final String TABLE_NAME = "ChiTietNganSach";

    private static final String TRANS = "maGiaoDich";
    private static final String BUDGET = "maNganSach";

    public static String createTable(){
        return "CREATE TABLE " + TABLE_NAME + "(" +
                TRANS + " TEXT, " +
                BUDGET + " TEXT" +
                ")";
    }

    public static String deleteTable(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
