package in.eoninfotech.eontechnician.service;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    public interface LogoutListener {
        void onLogout(String msg);
    }

    private static LogoutListener logoutListener;

    public static void setLogoutListener(LogoutListener listener) {
        logoutListener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());

        if (response.code() == 401 || response.code() == 403) {
            if (logoutListener != null) {
                logoutListener.onLogout("Session expired. Please login again.");
            }
        }
        return response;
    }
}
