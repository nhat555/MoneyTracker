package com.nhat.moneytracker.modules.icons;

import android.content.Context;

public class IconsDrawableModule {

    public static int getResourcesDrawble(Context context, String name) {
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }
}
