package com.example.learningassistantapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SubmissionApi {

    @GET("getExplanation")
    Call<String> getExplanation(@Query("query") String explanation);
}