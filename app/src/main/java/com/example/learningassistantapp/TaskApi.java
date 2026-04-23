package com.example.learningassistantapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TaskApi {
    // To reduce API overhead and token cost, we pass the delimited interest string
    // AI will separate it for us, and return a response in a structured JSON format

    @GET("getTasks")
    Call<TaskListResponse> getTasks(@Query("query") String interest);
}