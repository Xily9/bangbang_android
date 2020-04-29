package com.autowheel.bangbang.model.network;

import com.autowheel.bangbang.BuildConfig;
import com.autowheel.bangbang.ConstantsKt;
import com.autowheel.bangbang.model.network.service.ApiService;
import com.autowheel.bangbang.utils.UserUtil;
import com.didichuxing.doraemonkit.kit.network.okhttp.DoraemonInterceptor;
import com.didichuxing.doraemonkit.kit.network.okhttp.DoraemonWeakNetworkInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Xily on 2020/3/25.
 */
public class RetrofitHelper {
    private volatile static ApiService apiService;

    public static ApiService getApiService() {
        if (apiService == null) {
            synchronized (RetrofitHelper.class) {
                if (apiService == null) {
                    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                    httpLoggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
                    /*Interceptor tokenInterceptor = chain -> {
                        String token = UserUtil.INSTANCE.getToken();
                        if (token.isEmpty()) {
                            return chain.proceed(chain.request());
                        }
                        Request.Builder builder = chain.request().newBuilder();
                        builder.addHeader("token", token);
                        return chain.proceed(builder.build());
                    };*/
                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .addInterceptor(httpLoggingInterceptor)
                            //.addInterceptor(tokenInterceptor)
                            .cookieJar(new CookieJar() {
                                @Override
                                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                                    UserUtil.INSTANCE.setToken(cookies.get(0).toString());
                                }

                                @Override
                                public List<Cookie> loadForRequest(HttpUrl url) {
                                    List<Cookie> cookies = new ArrayList<>();
                                    String token = UserUtil.INSTANCE.getToken();
                                    if (!token.isEmpty()) {
                                        cookies.add(Cookie.parse(url, token));
                                    }
                                    return cookies;
                                }
                            })
                            .addNetworkInterceptor(new DoraemonWeakNetworkInterceptor())
                            .addInterceptor(new DoraemonInterceptor())
                            .connectTimeout(0, TimeUnit.SECONDS)
                            .callTimeout(0, TimeUnit.SECONDS)
                            .readTimeout(0, TimeUnit.SECONDS)
                            .writeTimeout(0, TimeUnit.SECONDS)
                            .build();

                    apiService = new Retrofit.Builder()
                            .client(okHttpClient)
                            .baseUrl(ConstantsKt.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                            .create(ApiService.class);
                }
            }
        }
        return apiService;
    }
}
