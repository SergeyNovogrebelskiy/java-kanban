package tests;

import com.google.gson.Gson;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import server.KVServer;
import task.Epic;
import task.Subtask;
import task.Task;
import task.enums.Statuses;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {
    TaskManager taskManager = Managers.getDefault();
    HttpClient client = HttpClient.newHttpClient();
    HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
    String url = "http://localhost:8080";
    Gson gson = new Gson();
    static KVServer kvServer;

    @BeforeAll
    public static void startServers() {
        try {
            kvServer = new KVServer();
            kvServer.start();
            new HttpTaskServer().start();
        } catch (IOException | InterruptedException e) {
            System.out.println("Ошибка при запуске серверов");
        }
    }

    @AfterAll
    public static void stopServers() {
        kvServer.stop();
    }

    private HttpResponse<String> add(String path, String taskJson) {
        URI uri = URI.create(url + path);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(taskJson);
        HttpRequest request = HttpRequest.newBuilder().POST(body).uri(uri).build();
        try {
            HttpResponse<String> response = client.send(request, handler);
            return response;
        } catch (IOException | InterruptedException e) {
            System.out.println("Post error");
            return null;
        }
    }

    @Test
    public void testAddTask() {
        Task task = new Task(taskManager.getId(), "Task 1",
                "Description Task 1", Statuses.NEW, "01.02.23 12.00", 60);
        String taskJson = gson.toJson(task);
        HttpResponse<String> response = add("/tasks/task", taskJson);
        assertEquals(200, response.statusCode());
    }

    @Test
    public void testAddSubtask() {
        Epic epic = new Epic(taskManager.getId(), "Epic1", "Description Epic 1",
                Statuses.IN_PROGRESS, "01.02.23 03.00", 6 * 24 * 60);
        String taskJson = gson.toJson(epic);
        HttpResponse<String> response = add("/tasks/epic", taskJson);
        assertEquals(200, response.statusCode());
    }

    @Test
    public void testAddEpic() {
        Epic epic = new Epic(taskManager.getId(), "Epic1", "Description Epic 1",
                Statuses.IN_PROGRESS, "01.02.23 03.00", 6 * 24 * 60);
        String epicJson = gson.toJson(epic);

        HttpResponse<String> response = add("/tasks/epic", epicJson);
    }

    @Test
    public void testGetAllTasks() {
        URI uri = URI.create(url + "/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        try {
            HttpResponse<String> response = client.send(request, handler);
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println("Get error");
        }
    }

    @Test
    public void testGetAllSubtasks() {
        URI uri = URI.create(url + "/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        try {
            HttpResponse<String> response = client.send(request, handler);
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println("Get error");
        }
    }

    @Test
    public void testGetAllEpics() {
        URI uri = URI.create(url + "/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        try {
            HttpResponse<String> response = client.send(request, handler);
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println("Get error");
        }
    }
}