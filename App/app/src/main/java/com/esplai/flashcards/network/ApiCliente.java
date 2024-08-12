package com.esplai.flashcards.network;


import java.net.URL;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


//llamada a la api
public class ApiCliente {
    private static Retrofit retrofit = null;
    private static final String url="http://10.0.2.2:8181/";
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)  // URL base de tu API
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
