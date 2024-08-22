package com.esplai.flashcards.network;


import com.esplai.flashcards.model.AccesToken;
import com.esplai.flashcards.model.Card;
import com.esplai.flashcards.model.Collection;
import com.esplai.flashcards.model.LoginUser;
import com.esplai.flashcards.model.User;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface ApiService {
    @POST("/flashcards/user/register")
    Call<User> createUser(@Body User user);

    @POST("/flashcards/user/login")
    Call <AccesToken>loginUser(@Body LoginUser loginUser);

    @POST("/flashcards/collection")
    Call<Void> createCollection(@Header("Authorization") String token, @Body Collection request);

    @GET("/flashcards/collection")
    Call<List<Collection>> getCollections(@Header("Authorization") String token);

    @POST("/flashcards/card")
    Call<Void> createCard(@Header("Authorization") String token, @Body Card card);

}
