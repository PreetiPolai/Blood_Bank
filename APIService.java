package com.android.BloodBank;

import com.android.BloodBank.Notifications.MyResponse;
import com.android.BloodBank.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA7Jm-7H8:APA91bGUxc-ErVxw9iwGJZWBNOAeiQzJp22MCAIz32oJqJYVIqPRdFLDajWHByw7-yXI-UYbxUnfLyEoFIIvesz7fs_5DXY5zR7ru_YvVnap0WjZxibBQQFbmwnLOvNJF2nATRR_1BhM"
            }
    )


    @POST("fcm/send")
    //basically will have the body object
    Call<MyResponse> sendNotification(@Body Sender body);


}
