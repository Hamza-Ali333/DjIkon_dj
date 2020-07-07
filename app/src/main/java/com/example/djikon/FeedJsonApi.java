package com.example.djikon;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FeedJsonApi {

    @GET ("blog")
    Call<List<Blog>> getBlogs();
}
