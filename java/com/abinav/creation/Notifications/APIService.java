package com.abinav.creation.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAHS4rEzY:APA91bF0RkOR2_bUylNuukS-6jfxBQcNX2Cy4efLCBaS9vvKSLrZDVTdBHHzGa_kmf3SROgiNbgD64YiPMuMKgXbSTbFbOsKL_Hp1QRGN3a5RHcB1khREHMGjDjwUCKJg-OpD9E_rDyJ"
            })

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
