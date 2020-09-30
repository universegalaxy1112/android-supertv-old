//package com.livetv.normal.utils.networking;
//
//
//import android.util.Log;
//
//import com.livetv.normal.BuildConfig;
//import com.livetv.normal.utils.Connectivity;
//import com.livetv.normal.utils.Files;
//
//import java.io.IOException;
//import java.util.concurrent.TimeUnit;
//
//import okhttp3.Cache;
//import okhttp3.CacheControl;
//import okhttp3.Interceptor;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//import okhttp3.logging.HttpLoggingInterceptor;
//
//import static okhttp3.logging.HttpLoggingInterceptor.Level.HEADERS;
//import static okhttp3.logging.HttpLoggingInterceptor.Level.NONE;
//
//public class OKHttpClientProvider {
//
//    private OkHttpClient httpClient;
//    private static final String CACHE_CONTROL = "Cache-Control";
//
//    public OKHttpClientProvider() {
//        httpClient = new OkHttpClient.Builder()
////                .addInterceptor( provideHttpLoggingInterceptor() )
////                .addInterceptor( provideOfflineCacheInterceptor() )
//                .addNetworkInterceptor( provideCacheInterceptor() )
//                .cache(provideCache())
//                .build();
////        httpClient.networkInterceptors().add(provideCacheInterceptor());
//    }
//
//    public OkHttpClient getHttpClient() {
//        return httpClient;
//    }
//
//    private static Cache provideCache ()
//    {
//        Cache cache = null;
//        try
//        {
//            cache = new Cache( Files.GetFile(Files.GetCacheDir(), "http-cache"), 10 * 1024 * 1024 ); // 10 MB
//        }
//        catch (Exception e)
//        {
////            Timber.e( e, "Could not create Cache!" );
//            ;//Log.d("OKHttpClientProvider", "Could not create Cache! " + e);
//        }
//        return cache;
//    }
//
//    private static HttpLoggingInterceptor provideHttpLoggingInterceptor ()
//    {
//        HttpLoggingInterceptor httpLoggingInterceptor =
//                new HttpLoggingInterceptor( new HttpLoggingInterceptor.Logger()
//                {
//                    @Override
//                    public void log (String message)
//                    {
//                        ;//Log.d("OKHttpClientProvider", "Loggin interceptor message : "+message);
////                        Timber.d( message );
//                    }
//                } );
//        httpLoggingInterceptor.setLevel( BuildConfig.DEBUG ? HEADERS : NONE );
//        return httpLoggingInterceptor;
//    }
//
//    public static Interceptor provideCacheInterceptor ()
//    {
//        return new Interceptor()
//        {
//            @Override
//            public Response intercept (Chain chain) throws IOException
//            {
//                Request request = chain.request();
//
//                if(Connectivity.isConnected()) {
//                    request = request.newBuilder()
//                            .header("Cache-Control", "only-if-cached")
//                            .build();
//                }
//                else {
//                    request = request.newBuilder()
//                            .header("Cache-Control", "public, max-stale=54000")
//                            .build();
//                }
//
////                // re-write response header to force use of cache
////                CacheControl cacheControl = new CacheControl.Builder()
////                        .maxAge( 15, TimeUnit.MINUTES )//cache will last for 15 minutes
//////                        .onlyIfCached()
////                        .build();
//
//                Response response = chain.proceed( chain.request() );
//
//                return response.newBuilder()
//                        .header("Cache-Control", "max-age=900")
//                        .build();
////                return response.newBuilder()
////                        .header( CACHE_CONTROL, cacheControl.toString() )
////                        .build();
//            }
//        };
//    }
//
//    public static Interceptor provideOfflineCacheInterceptor ()
//    {
//        return new Interceptor()
//        {
//            @Override
//            public Response intercept (Chain chain) throws IOException
//            {
//                Request request = chain.request();
//
//                if ( !Connectivity.isConnected() )
//                {
//                    CacheControl cacheControl = new CacheControl.Builder()
//                            .maxStale( 7, TimeUnit.DAYS )
////                            .onlyIfCached()
//                            .build();
//
//                    request = request.newBuilder()
//                            .cacheControl( cacheControl )
//                            .build();
//                }
//
//                return chain.proceed( request );
//            }
//        };
//    }
//}