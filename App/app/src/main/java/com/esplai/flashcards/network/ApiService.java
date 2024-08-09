package com.esplai.flashcards.network;


import com.esplai.flashcards.model.User;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface ApiService{
    @POST("/user/register")
    Call<User> createUser(@Body User user);
}
