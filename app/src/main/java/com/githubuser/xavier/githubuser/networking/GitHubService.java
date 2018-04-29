package com.githubuser.xavier.githubuser.networking;

import com.githubuser.xavier.githubuser.object.UserObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitHubService {
    @GET("users")
    Call<List<UserObject>> users(@Query("since") int id,
                                 @Query("per_page") int perPage);

    @GET("users/{user}")
    Call<UserObject> userDetail(@Path("user") String user);
}
