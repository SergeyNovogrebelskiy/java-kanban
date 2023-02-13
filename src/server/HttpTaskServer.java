package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

public class HttpTaskServer {
    private final int PORT = 8080;

    TaskManager manager = Managers.getDefault();
    HttpServer httpServer;
    Gson gson = new Gson();

    public HttpTaskServer() throws IOException, InterruptedException {
    }

    public void start() throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/task", new TaskHandler());
        httpServer.createContext("/tasks/subtask", new SubtaskHandler());
        httpServer.createContext("/tasks/epic", new EpicHandler());
        httpServer.createContext("/tasks/subtask/epic", new SubtaskByEpicHandler());
        httpServer.createContext("/tasks/history", new HistoryHandler());
        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.start();

        System.out.println("Сервер запущен на порт: " + PORT);
    }

    private void writeResponse(HttpExchange exchange,
                               String responseString,
                               int responseCode) throws IOException {
        if(responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = responseString.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }

    public void stop() {
        httpServer.stop(0);
    }

    class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();

            switch (method) {
                case "GET":
                    String getQuery = exchange.getRequestURI().getQuery();
                    if (getQuery == null) {
                        List<Task> tasks = manager.getTasks();
                        String tasksJson = gson.toJson(tasks);
                        writeResponse(exchange, tasksJson, 200);
                    } else {
                        int id = Integer.parseInt(getQuery.split("=")[1]);
                        Task task = manager.getTask(id);
                        if(task == null) {
                            writeResponse(exchange, "Задача с запрошенным id не найдена", 404);
                            break;
                        }
                        String taskJson = gson.toJson(task);
                        writeResponse(exchange, taskJson, 200);
                    }
                    break;
                case "POST":
                    InputStream is = exchange.getRequestBody();
                    String requestBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                    if (requestBody.isBlank()) {
                        writeResponse(exchange, "Данные не переданы", 404);
                        break;
                    }
                    Task task = gson.fromJson(requestBody, Task.class);
                    if (manager.getTask(task.getId()) == null) {
                        manager.addTask(task);
                        writeResponse(exchange, "Задача успешно добавлена", 200);
                    } else {
                        manager.updateTask(task);
                        writeResponse(exchange, "Задача успешно обновлена", 200);
                    }
                    break;
                case "DELETE":
                    String deleteQuery = exchange.getRequestURI().getQuery();
                    if (deleteQuery == null) {
                        manager.deleteAllTasks();
                        writeResponse(exchange, "Все задачи успешно удалены", 200);
                    } else {
                        int id = Integer.parseInt(deleteQuery.split("=")[1]);
                        if (manager.getTask(id) == null) {
                            writeResponse(exchange, String.format("Задача с id: %d не найдена", id), 404);
                            break;
                        }
                        manager.deleteTaskById(id);
                        writeResponse(exchange, String.format("Задача с id: %d успешно удалена", id), 200);
                    }
                    break;
                default:
                    writeResponse(exchange, "Неизвестный метод", 404);
            }
        }
    }
    class SubtaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            switch (method) {
                case "GET":
                    String getQuery = exchange.getRequestURI().getQuery();
                    if (getQuery == null) {
                        List<Subtask> subtasks = manager.getSubtasks();
                        String subtasksJson = gson.toJson(subtasks);
                        writeResponse(exchange, subtasksJson, 200);
                    } else {
                        int id = Integer.parseInt(getQuery.split("=")[1]);
                        Subtask subtask = manager.getSubtask(id);
                        if (subtask == null) {
                            writeResponse(exchange, "Подзадача с запрошенным id не найдена", 404);
                            break;
                        }
                        String subtaskJson = gson.toJson(subtask);
                        writeResponse(exchange, subtaskJson, 200);
                    }
                    break;
                case "POST":
                    InputStream is = exchange.getRequestBody();
                    String requestBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                    if (requestBody.isBlank()) {
                        writeResponse(exchange, "Данные не переданы", 404);
                        break;
                    }
                    Subtask subtask = gson.fromJson(requestBody, Subtask.class);
                    if (manager.getSubtask(subtask.getId()) == null) {
                        manager.addSubtask(subtask);
                        writeResponse(exchange, "Подзадача успешно добавлена", 200);
                    } else {
                        manager.updateSubtask(subtask);
                        writeResponse(exchange, "Подзадача успешно обновлена", 200);
                    }
                    break;
                case "DELETE":
                    String deleteQuery = exchange.getRequestURI().getQuery();
                    if (deleteQuery == null) {
                        manager.deleteAllSubtasks();
                        writeResponse(exchange, "Все подзадачи успешно удалены", 200);
                    } else {
                        int id = Integer.parseInt(deleteQuery.split("=")[1]);
                        if (manager.getSubtask(id) == null) {
                            writeResponse(exchange, "Подзадача с id:" + id + " не найдена", 404);
                            break;
                        }
                        manager.deleteSubtaskById(id);
                        writeResponse(exchange, "Подзадача id:" + id + " успешно удалена", 200);
                    }
                    break;
                default:
                    writeResponse(exchange, "Неизвестный метод", 404);
            }
        }
    }
    class EpicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            switch (method) {
                case "GET":
                    String getQuery = exchange.getRequestURI().getQuery();
                    if (getQuery == null) {
                        List<Epic> epics = manager.getEpics();
                        String epicsJson = gson.toJson(epics);
                        writeResponse(exchange, epicsJson, 200);
                    } else {
                        int id = Integer.parseInt(getQuery.split("=")[1]);
                        Epic epic = manager.getEpic(id);
                        if (epic == null) {
                            writeResponse(exchange, "Эпик с запрошенным id не найдена", 404);
                            break;
                        }
                        String epicJson = gson.toJson(epic);
                        writeResponse(exchange, epicJson, 200);
                    }
                    break;
                case "POST":
                    InputStream is = exchange.getRequestBody();
                    String requestBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                    if (requestBody.isBlank()) {
                        writeResponse(exchange, "Данные не переданы", 404);
                        break;
                    }
                    Epic epic = gson.fromJson(requestBody, Epic.class);
                    if (manager.getEpic(epic.getId()) == null) {
                        manager.addEpic(epic);
                        writeResponse(exchange, "Эпик успешно добавлен", 200);
                    } else {
                        manager.updateEpic(epic);
                        writeResponse(exchange, "Эпик успешно обновлен", 200);
                    }
                    break;
                case "DELETE":
                    String deleteQuery = exchange.getRequestURI().getQuery();
                    if (deleteQuery == null) {
                        manager.deleteAllEpics();
                        writeResponse(exchange, "Все эпики успешно удалены", 200);
                    } else {
                        int id = Integer.parseInt(deleteQuery.split("=")[1]);
                        if (manager.getEpic(id) == null) {
                            writeResponse(exchange, "Эпик с id:" + id + " не найден", 404);
                            break;
                        }
                        manager.deleteEpicById(id);
                        writeResponse(exchange, "Эпик id:" + id + " успешно удален", 200);
                    }
                    break;
            }
        }
    }
    class SubtaskByEpicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            int id = Integer.parseInt(exchange.getRequestURI().getQuery().split("=")[1]);
            Epic epic = manager.getEpic(id);
            if (epic == null) {
                writeResponse(exchange, "Эпик с id: " + id + " не найден", 404);
            }
            List<Subtask> subtasks = manager.getSubtasksByEpic(epic);
            String subtasksJson = gson.toJson(subtasks);
            writeResponse(exchange, subtasksJson, 200);
        }
    }
    class HistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            List<Task> history = manager.getHistory();
            String historyJson = gson.toJson(history);
            writeResponse(exchange, historyJson, 200);
        }
    }
    class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            List<Task> prioritizedTasks = manager.getPrioritizedTasks();
            String tasksJson = gson.toJson(prioritizedTasks);
            writeResponse(exchange, tasksJson, 200);
        }
    }
}