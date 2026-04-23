package com.example.learningassistantapp;

public class Result {
    public String questionTitle;
    public String resultText;
    public boolean isLoading;

    public Result(String questionTitle, String resultText, boolean isLoading) {
        this.questionTitle = questionTitle;
        this.resultText = resultText;
        this.isLoading = isLoading;
    }
}
