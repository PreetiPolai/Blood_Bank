package com.android.BloodBank.Notifications;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


//basically register as a client
public class Client {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String url){
        //singleton pattern
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;

    }
}
