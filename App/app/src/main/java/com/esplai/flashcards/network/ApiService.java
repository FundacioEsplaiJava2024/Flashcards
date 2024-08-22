package com.esplai.flashcards.network;


import com.esplai.flashcards.model.AccesToken;
import com.esplai.flashcards.model.Card;
import com.esplai.flashcards.model.Collection;
import com.esplai.flashcards.model.LoginUser;
import com.esplai.flashcards.model.User;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface ApiService {
    @POST("/flashcards/user/register")
    Call<User> createUser(@Body User user);

    @POST("/flashcards/user/login")
    Call <AccesToken>loginUser(@Body LoginUser loginUser);

    @POST("/flashcards/collection")
    Call<Void> createCollection(@Header("Authorization") String token, @Body Collection request);

    @POST("/flashcards/card")
    Call<Void> createCard(@Header("Authorization") String token, @Body Card card);

}
