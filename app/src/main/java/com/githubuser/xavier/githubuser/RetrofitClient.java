package com.githubuser.xavier.githubuser;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit instance = null;

    public static Retrofit getClient() {
        synchronized (RetrofitClient.class) {
            if (instance == null) {

                Interceptor interceptor = new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                        okhttp3.Request original = chain.request();

                        /** Request customization: add request headers */
                        okhttp3.Request.Builder requestBuilder = original.newBuilder();
//                                .header("Authorization", "auth-value"); // <-- this is the important line

                        okhttp3.Request request = requestBuilder.build();

                        return chain.proceed(request);
                    }
                };

                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(0, TimeUnit.SECONDS)    // connect timeout
                        .writeTimeout(0, TimeUnit.SECONDS)
                        .readTimeout(0, TimeUnit.SECONDS)       // socket timeout
                        .addInterceptor(interceptor)
                        .build();

                instance = new Retrofit.Builder()
                        .baseUrl("https://api.github.com/")
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return instance;
        }
    }
}
