package com.chewuwuyou.app.transition_utils;

import com.chewuwuyou.app.AppContext;
import com.chewuwuyou.app.BuildConfig;
import com.chewuwuyou.app.transition_entity.UserBean;

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
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Box Administrator on 2016/7/18 0018.
 */
public class HttpBase {

    /**
     * 请求显示下载进度
     */
    public static final String PROGRESS_DOWN = "PROGRESS_DOWN";
    /**
     * 请求显示上传进度
     */
    public static final String PROGRESS_UP = "PROGRESS_UP";

    public static String BASE_URL = BuildConfig.BASE_URL;//URL地址
    public static String XMPP_BASE_URL=BuildConfig.XMPP_HOST_URL;
    private static final int DEFAULT_TIMEOUT = 5;//超时s
    private Retrofit retrofit;

    private HttpBase(String url) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        okHttpClientBuilder.addInterceptor(new Interceptor() {
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
        }).addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        if (retrofit == null)
            retrofit = new Retrofit.Builder()
                    .client(okHttpClientBuilder.build())
                    .baseUrl(url)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
    }

    public <T> T createApi(Class<T> service) {
        if (retrofit == null)
            throw new RuntimeException("Retrofit is null.");
        return retrofit.create(service);
    }

    /**
     * 老API单例
     */
    private static class SingletonOldHolder {
        private static final HttpBase INSTANCE = new HttpBase(BASE_URL);
    }

    /**
     * 取得老API的请求实体
     *
     * @return
     */
    public static HttpBase getOldInstance() {
        return SingletonOldHolder.INSTANCE;
    }

    /**
     * 新API单例
     */
    private static class SingletonHolder {
        private static final HttpBase INSTANCE = new HttpBase(XMPP_BASE_URL);
    }

    /**
     * 取得新API的请求实体
     *
     * @return
     */
    public static HttpBase getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
