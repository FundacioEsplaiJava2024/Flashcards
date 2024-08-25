package com.esplai.flashcards.network;


import com.esplai.flashcards.model.AccesToken;

import com.esplai.flashcards.model.LoginUser;
import com.esplai.flashcards.model.User;
import com.esplai.flashcards.service.cardlogic.CardModel;
import com.esplai.flashcards.service.entities.Collection;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.*;



public interface ApiService {
    @POST("/flashcards/user/register")
    Call<User> createUser(@Body User user);

    @POST("/flashcards/user/login")
    Call <AccesToken>loginUser(@Body LoginUser loginUser);

    @POST("/flashcards/collection")
    Call<Void> createCollection(@Header("Authorization") String token, @Body Collection request);

    @POST("/flashcards/card")
    Call<Void> createCard(@Header("Authorization") String token, @Body CardModel card);

    @GET("/flashcards/card/random")
    Call <List<CardModel>>getRandomCards(@Header("Authorization")String token);

    @GET("flashcards/card/hashtag")
    Call<List<CardModel>> getCardsByHashtag(@Header("Authorization") String token, @Query("hashtag") String hashtag);


    @GET("/flashcards/collection")
    Call <List<Collection>>getCollectionsFromUser(@Header("Authorization")String token);

    @GET("/flashcards/collection/random")
    Call <List<Collection>>getRandomCollections(@Header("Authorization")String token);

    @GET("/flashcards/card/collection/{collection_id}")
    Call <List<CardModel>>getCollectionDetails(@Header("Authorization")String token,@Path("collection_id")int collectionId);

    @POST("/flashcards/collection/saved/{id}")
    Call <Collection>saveCollection(@Header("Authorization")String token,@Path("id")int id);

    @GET("/flashcards/collection/saved")
    Call <List<Collection>>getSavedCollections(@Header("Authorization")String token);
}
