package com.android.blessed.androidsurfeducation.network;

import com.android.blessed.androidsurfeducation.models.LoginRequest;
import com.android.blessed.androidsurfeducation.models.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ServerAPI {
    @HTTP(method = "POST", path = "/auth/login", hasBody = true)
    Call<LoginResponse> loginUser(@Body LoginRequest request);
}
