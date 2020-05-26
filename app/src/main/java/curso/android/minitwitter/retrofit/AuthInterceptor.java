package curso.android.minitwitter.retrofit;

import java.io.IOException;

import curso.android.minitwitter.common.Constantes;
import curso.android.minitwitter.common.SharedPreferencesManager;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_TOKEN);
        Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer " + token).build();

        return chain.proceed(request);
    }
}
