package com.chewuwuyou.app.transition_model;

import com.chewuwuyou.app.AppContext;
import com.chewuwuyou.app.BuildConfig;
import com.chewuwuyou.app.transition_entity.UserBean;
import com.chewuwuyou.app.transition_utils.HttpBase;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Yogi on 16/9/24.
 */

public class BaseModel {

    public static String BASE_URL = BuildConfig.BASE_URL;//URL地址
    public static String XMPP_BASE_URL = BuildConfig.XMPP_HOST_URL;
    private static final int DEFAULT_TIMEOUT = 5;//超时s

    public Retrofit getDefaultRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(XMPP_BASE_URL)
                .client(getHttpClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * 老系统接口进行拼接
     *
     * @return
     */
    public Retrofit getOldRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getHttpClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * 添加请求头
     *
     * @return
     */
    public static OkHttpClient getHttpClient() {
        OkHttpClient httpClient = new OkHttpClient().newBuilder().connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder request = chain.request().newBuilder();
                UserBean userBean = UserBean.getInstall(AppContext.getInstance());
                if (userBean != null) {
                    request.addHeader("token", userBean.getRequestToken());
                }
                request.addHeader("version", BuildConfig.VERSION_NAME);
                return chain.proceed(request.build());
            }
        }).addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build();//日志打印
        return httpClient;
    }


}
