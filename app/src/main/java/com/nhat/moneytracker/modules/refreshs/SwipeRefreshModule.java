package com.nhat.moneytracker.modules.refreshs;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class SwipeRefreshModule {
    private static final int TIME_SLEEP = 200;

    public static void eventRefresh(final SwipeRefreshLayout swipeRefresh, final Runnable runnable) {
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                runnable.run();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(TIME_SLEEP);
                            swipeRefresh.setRefreshing(false);
                        } catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                };
                new Thread(runnable).start();
            }
        });
    }
}
