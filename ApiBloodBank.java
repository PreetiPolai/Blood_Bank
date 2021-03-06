package com.android.BloodBank;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiBloodBank {

    //https://api.data.gov.in/resource/fced6df9-a360-4e08-8ca0-f283fc74ce15?api-key=579b464db66ec23bdd000001cdd3946e44ce4aad7209ff7b23ac571b&format=json&offset=0&limit=10&filters[__city]=Rewa
    private static final String BASE_URL = "https://api.data.gov.in/";
    private static ApiBloodBank sInstance;
    private final Retrofit retrofit;


    private ApiBloodBank() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiBloodBank getInstance() {
        if (sInstance == null) {
            sInstance = new ApiBloodBank();
        }
        return sInstance;
    }

    public Api getApi() {
        return retrofit.create(Api.class);
    }

}
