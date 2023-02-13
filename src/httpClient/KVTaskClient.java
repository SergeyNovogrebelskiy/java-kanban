package httpClient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private String url;
    private String apiToken;
    private HttpClient client = HttpClient.newHttpClient();
    private HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
    private int PORT = 8078;

    public KVTaskClient(String url) {
        this.url = url;
        try {
            URI uri = URI.create(this.url + ":" + PORT + "/register");
            HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
            HttpResponse<String> response = client.send(request, handler);
            if (response.statusCode() == 200) {
                apiToken = response.body();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при регистрации клиента");
        }
    }

    public void put(String key, String json) {
        try {
            URI uri = URI.create(this.url + ":" + PORT + "/save/" + key + "?API_TOKEN=" + apiToken);
            HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest request = HttpRequest.newBuilder().POST(body).uri(uri).build();
            HttpResponse<String> response = client.send(request, handler);
            if (response.statusCode() == 200) {
                System.out.println("Успешно добавлено");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при загрузке данных");
        }
    }

    public String load(String key) {
        try {
            String cammonPartUrl = this.url + ":" + PORT;
            URI uri = URI.create(cammonPartUrl + "/load/" + key + "?API_TOKEN=" + apiToken);
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
            HttpRequest request = requestBuilder.GET().uri(uri).version(HttpClient.Version.HTTP_1_1).header("Accept", "application/json").build();
            HttpResponse<String> response = client.send(request, handler);
            if (response.statusCode() == 200) {
                System.out.println("Успешно получено");
                return response.body();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при получении данных");
        }
        return null;
    }
}