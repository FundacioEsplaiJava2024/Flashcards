package com.esplai.flashcards.network;


import com.esplai.flashcards.model.AccesToken;
import com.esplai.flashcards.model.Card;
import com.esplai.flashcards.model.Collection;
import com.esplai.flashcards.model.LoginUser;
import com.esplai.flashcards.model.User;
import com.esplai.flashcards.service.cardlogic.CardModel;
import com.esplai.flashcards.service.entities.Collection;


import java.util.List;

import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface ApiService {
    @POST("/flashcards/user/register")
    Call<User> createUser(@Body User user);

    @POST("/flashcards/user/login")
    Call <AccesToken>loginUser(@Body LoginUser loginUser);

    @GET("/flashcards/card/random")
    Call <List<CardModel>>getRandomCards(@Header("Authorization")String token);

    @GET("/flashcards/collection")
    Call <List<Collection>>getCollectionsFromUser(@Header("Authorization")String token);

    @GET("/flashcards/card/collection/{collection_id}")
    Call <List<CardModel>>getCollectionDetails(@Header("Authorization")String token,@Path("collection_id")int collectionId);

}
