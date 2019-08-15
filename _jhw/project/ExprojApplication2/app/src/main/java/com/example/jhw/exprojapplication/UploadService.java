package com.example.jhw.exprojapplication;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadService {

    @Multipart
    @POST("imageUpload.jsp")
    Call<ResponseBody> uploadFile(
            @Part("userId")RequestBody userId,
            @Part("grade")RequestBody grade,
            @Part("opinion")RequestBody opinion,
            @Part MultipartBody.Part file);

}
