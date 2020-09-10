package com.gmail.bergrin.greatadvice;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AdviceApi {
    @GET("random/")
    Call<Advice> getRandomAdvice();

}
