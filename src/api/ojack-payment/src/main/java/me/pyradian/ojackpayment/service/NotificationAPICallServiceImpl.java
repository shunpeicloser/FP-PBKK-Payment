package me.pyradian.ojackpayment.service;

import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Map;

@Service
public class NotificationAPICallServiceImpl implements NotificationAPICallService {
    private final String base_url = "http://punyajoel:port/api";
    private Retrofit retro;
    private NotificationAPICall notificationAPICall;

    public NotificationAPICallServiceImpl() {
        this.retro = new Retrofit.Builder()
                            .baseUrl(base_url)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
        this.notificationAPICall = retro.create(NotificationAPICall.class);
    }

    @Override
    public int sendNotification(String message) {
        // mockup token
        String token = "dfiud8fhgdifhudifhg";

        JsonObject toSend = new JsonObject();
        toSend.addProperty("sender", "Payment-Service");
        toSend.addProperty("type", "payment_notif");
        toSend.addProperty("message", message);
//        toSend.addProperty("user_id", user_id);
        toSend.addProperty("callback_url", "http://pyradian.me:9443");

        Call<Map<String, Object>> notif = notificationAPICall.sendNotification(token, toSend);
        Response<Map<String, Object>> respNotif;
        try {
            respNotif = notif.execute();
            System.out.println(respNotif.body());
            return 0;
        } catch (Exception e) {
            System.out.println("error " + e);
        }
        return -1;
    }
}
