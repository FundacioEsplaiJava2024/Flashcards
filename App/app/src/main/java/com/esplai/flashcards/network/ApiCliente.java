package com.esplai.flashcards.network;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


//llamada a la api
public class ApiCliente {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8181/")   // URL base de tu API
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
