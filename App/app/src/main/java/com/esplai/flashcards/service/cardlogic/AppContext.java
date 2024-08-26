package com.esplai.flashcards.service.cardlogic;

import android.content.Context;

public class AppContext {
    private static Context context;

    public static void init(Context context) {
        AppContext.context = context.getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
