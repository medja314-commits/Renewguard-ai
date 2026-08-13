package fr.renewguard.service;
 
import fr.renewguard.util.TokenStore;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
 
public final class AuthInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = TokenStore.getInstance().get();
        Request original = chain.request();
        if (token == null || token.isBlank()) return chain.proceed(original);
        Request authenticated = original.newBuilder()
            .header("Authorization", "Bearer " + token)
            .header("Accept", "application/json")
            .build();
        return chain.proceed(authenticated);
    }
}