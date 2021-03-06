package com.android.BloodBank;

import com.android.BloodBank.Model.BloodBankPOJO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {



    //base URL
    //    //https://api.data.gov.in/resource/fced6df9-a360-4e08-8ca0-f283fc74ce15?api-key=579b464db66ec23bdd000001cdd3946e44ce4aad7209ff7b23ac571b&format=json&offset=0&limit=10&filters[__city]=Rewa
    @GET("/resource/fced6df9-a360-4e08-8ca0-f283fc74ce15")
    //?api-key=579b464db66ec23bdd000001cdd3946e44ce4aad7209ff7b23ac571b&format=json&offset=0&limit=10&filters[__city]=Rewa
    Call<BloodBankPOJO> getBloodBanks(
            @Query("api-key") String apiKey,
            @Query("format") String format,
            @Query("offset") int offset,
            @Query("limit") int limit,
            @Query("filters[__city]") String CityName
    );
}
