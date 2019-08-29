package com.android.blessed.androidsurfeducation.network;

import com.android.blessed.androidsurfeducation.models.LoginRequest;
import com.android.blessed.androidsurfeducation.models.LoginResponse;
import com.android.blessed.androidsurfeducation.models.Meme;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ServerAPI {
    @POST(ServerUrls.LOGIN)
    Call<LoginResponse> loginUser(@Body LoginRequest request);

    @GET(ServerUrls.GET_MEMES)
    Call<List<Meme>> getMemes();

    @POST(ServerUrls.LOGOUT)
    Call<Void> logoutUser();
}
