package com.bignerdranch.android.suicidepreventionapp;
import com.bignerdranch.android.suicidepreventionapp.Models.AnswerResponse;
import com.bignerdranch.android.suicidepreventionapp.Models.ChatResponse;
import com.bignerdranch.android.suicidepreventionapp.Models.Form;
import com.bignerdranch.android.suicidepreventionapp.Models.KnockResponse;
import com.bignerdranch.android.suicidepreventionapp.Models.Message;
import com.bignerdranch.android.suicidepreventionapp.Models.Student;
import com.bignerdranch.android.suicidepreventionapp.Models.SubmissionStatus;
import com.bignerdranch.android.suicidepreventionapp.Models.SubmittedForm;
import com.bignerdranch.android.suicidepreventionapp.Models.User;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Api {

    @GET("profile/{id}")
    Call<User> getProfile(@Path("id") String id);

    @FormUrlEncoded
    @POST("register")
    Call<User> register(@Field("name") String name,
                        @Field("email") String email,
                        @Field("birth_date") String birthday,
                        @Field("password") String password,
                        @Field("school") String school,
                        @Field("school_id") String school_id,
                        @Field("grad_year") String graduation_year,
                        @Field("gender") String gender,
                        @Field("phone_number") String phone_number,
                        @Field("counselor_id") String counselor_id,
                        @Field("role") int role);

    @FormUrlEncoded
    @POST("login")
    Call<User> login(@Field("email") String email,
                     @Field("password") String password
                     );

    @FormUrlEncoded
    @POST("profile/update/{id}")
    Call<User> update(@Path("id") String id,@Field("name") String name,
                      @Field("email") String email,
                      @Field("birth_date") String birth_date,
                      @Field("phone_number") String phone_number,
                      @Field("gender") String gender,
                      @Field("school") String school,
                      @Field("school_id") String school_id,
                      @Field("counselor_id") String counselor_id,
                      @Field("grad_year") String grad_year);

    @GET("get/form/index/{id}")
    Call<List<Form>> getFormByIndex(@Path("id") int id);

    @GET("get/form/available/{user_id}")
    Call<List<SubmissionStatus>> getAvailableForms(@Path("user_id") String user_id);

    @GET("get/form/submit/{id}")
    Call<List<Form>> getForm(@Path("id") int id);

    @FormUrlEncoded
    @POST("submit/question/{question_id}/{submitted_id}")
    Call<AnswerResponse> answer(@Path("question_id") int question_id,
                                @Path("submitted_id") int submitted_id,
                                @Field("answer") int answer);

    @GET("get/form/counselor/{id}")
    Call<List<SubmittedForm>> getSubmittedForm(@Path("id") int id);

    @GET("submit/{id}")
    Call<Form> submitForm(@Path("id") int id);

    @GET("profile/counselor/{id}")
    Call<List<Student>> getStudent(@Path("id") int id);

    @GET("profile/counselor/{id}/*")
    Call<List<User>> getCounselorById(@Path("id") int id);

    @GET("register/submission/{user_id}/{form_id}")
    Call<Form> assignUser(@Path("user_id") String user_id,
                          @Path("form_id") int form_id );

    @GET("chat/get/{id}")
    Call<Message> getMessage(@Path("id") int id);

    @GET("chat/recall/{sender}/{receiver}")
    Call<List<ChatResponse>> getSavedMessages(@Path("sender") String sender, @Path("receiver") String receiver);


    @FormUrlEncoded
    @POST("chat/send")
    Call<ChatResponse> sendMessage(@Field("message") String message, @Field("sender") String sender, @Field("receiver") String receiver);


}

















