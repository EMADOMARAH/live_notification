package com.emad.live_notification.auth;

import com.emad.live_notification.auth.Models.Request.Post;
import com.emad.live_notification.auth.Models.Responce.Response;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {

    String SERVER_KEY = "AAAAVN8r7BQ:APA91bH7-7EN8MwsEql-_qghHhGvzURUxA1sRc1aRTb8Vha4a6jgzQ32ampMUGQCfl5OWU2uJI2294NoefTtglVPVl4VCFtt7NYEdPIaVRgxVQcOwMKagHlHap3yO8Xs6VPOqoUpatuK";

    @Headers({"Content-Type: application/json",
            "Authorization:key=AAAAVN8r7BQ:APA91bH7-7EN8MwsEql-_qghHhGvzURUxA1sRc1aRTb8Vha4a6jgzQ32ampMUGQCfl5OWU2uJI2294NoefTtglVPVl4VCFtt7NYEdPIaVRgxVQcOwMKagHlHap3yO8Xs6VPOqoUpatuK"})
    @POST("send")
    public Call<Response> sendNotification(@Body Post notificationModel);
}
