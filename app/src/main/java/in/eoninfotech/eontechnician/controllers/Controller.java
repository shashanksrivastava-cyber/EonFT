package in.eoninfotech.eontechnician.controllers;

import retrofit2.Call;

import static java.net.HttpURLConnection.HTTP_ACCEPTED;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Created by root on 11/1/19.
 */

public class Controller {

    static void cancelCall(Call<?> call) {
        if (call != null) {
            call.cancel();
        }
    }

}
