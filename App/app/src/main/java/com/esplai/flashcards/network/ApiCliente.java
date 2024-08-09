package com.esplai.flashcards.network;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


//llamada a la api
public class ApiCliente {
    private static Retrofit retrofit;
    public static Retrofit getClient(){
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://localhost:8181/flashcards")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
