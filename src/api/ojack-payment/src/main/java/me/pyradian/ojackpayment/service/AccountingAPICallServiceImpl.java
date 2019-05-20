package me.pyradian.ojackpayment.service;

import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Map;

@Service
public class AccountingAPICallServiceImpl implements AccountingAPICallService {
    private final String base_url = "https://ojoakua.site/accounting";
    private Retrofit retro;
    private AccountingAPICall accountingAPICall;

    public AccountingAPICallServiceImpl() {
        this.retro = new Retrofit.Builder()
                            .baseUrl(base_url)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
        this.accountingAPICall = retro.create(AccountingAPICall.class);
    }

    @Override
    public boolean createPendapatan(String tanggal, int jumlah) {
        // mockup token
        String token = "sdfsdfsdfsdfsf";
        JsonObject toSend = new JsonObject();
        toSend.addProperty("tanggal", tanggal);
        toSend.addProperty("jumlah", jumlah);

        Call<Map<String, Object>> pendapatan = accountingAPICall.createPendapatan(token, toSend);
        Response<Map<String, Object>> respPendapatan;
        try {
            respPendapatan = pendapatan.execute();
            if (respPendapatan.code() != 201)
                return false;
            return true;
        } catch(Exception e) {
            System.out.println("error " + e);
        }
        return false;
    }

    @Override
    public boolean createPengeluaran(String tanggal, int jumlah, String keterangan) {
        // mockup token
        String token = "sdfsdfsdfsdfsf";
        JsonObject toSend = new JsonObject();
        toSend.addProperty("tanggal", tanggal);
        toSend.addProperty("jumlah", jumlah);
        toSend.addProperty("keterangan", keterangan);

        Call<Map<String, Object>> pengeluaran = accountingAPICall.createPengeluaran(token, toSend);
        Response<Map<String, Object>> respPengeluaran;
        try {
            respPengeluaran = pengeluaran.execute();
            if (respPengeluaran.code() != 201)
                return false;
            return true;
        } catch(Exception e) {
            System.out.println("error " + e);
        }
        return false;
    }
}
