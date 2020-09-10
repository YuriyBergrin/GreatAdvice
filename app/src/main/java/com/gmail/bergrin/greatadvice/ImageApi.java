package com.gmail.bergrin.greatadvice;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ImageApi {
    @GET("data/frontpage/_default/{imageNumber}.jpg")
    Call<ResponseBody> getImage(@Path("imageNumber") String imageNumber);
}
