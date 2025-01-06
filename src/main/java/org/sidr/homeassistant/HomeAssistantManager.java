package org.sidr.homeassistant;

import okhttp3.*;
import org.jetbrains.annotations.Nullable;
import org.sidr.Sidr;

import java.io.IOException;

public class HomeAssistantManager {
    private final Sidr sidr;
    private final String ip;
    private final String port;
    private final String token;
    public HomeAssistantManager(Sidr sidr){
        this.sidr = sidr;
        ip = sidr.getPropertiesManager().getIpHA();
        port = sidr.getPropertiesManager().getPortHA();
        token = sidr.getPropertiesManager().getTokenHA();
    }

    @Nullable
    public String getDeviceInfo(String entityID) throws IOException {
        OkHttpClient client = new OkHttpClient();
        // Создаем запрос
        Request request = new Request.Builder()
                .url("http://"+ip+":"+port+"/api/states/" + entityID)
                .addHeader("Authorization", "Bearer " + token)
                .build();

        // Выполняем запрос
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                // Выводим состояние устройства
                return response.body().string();
            } else {
                System.err.println("Ошибка: " + response.code());
                return null;
            }
        }
    }

    public boolean sendRequest(String entityID,String requestID) throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create("{ \"entity_id\": \"" + entityID + "\" }", MediaType.get("application/json"));

        // Создаем запрос
        Request request = new Request.Builder()
                .url("http://"+ip+":"+port+"/api/services/"+requestID)
                .addHeader("Authorization", "Bearer " + token)
                .post(body)
                .build();

        // Выполняем запрос
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                // Выводим состояние устройства
                return true;
            } else {
                System.err.println("Ошибка: " + response.code());
                return false;
            }
        }
    }
}
