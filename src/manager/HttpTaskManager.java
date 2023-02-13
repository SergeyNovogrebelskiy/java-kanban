package manager;

import com.google.gson.*;
import exceptions.ManagerSaveException;
import httpClient.KVTaskClient;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager{
    KVTaskClient client;
    Gson gson = new Gson();

    public HttpTaskManager(String url){
        client = new KVTaskClient(url);
    }

    @Override
    public void save() throws ManagerSaveException {
        try {
            client.put("TASK", gson.toJson(tasks.values()));
            client.put("SUBTASK", gson.toJson(subtasks.values()));
            client.put("EPIC", gson.toJson(epics.values()));
            client.put("HISTORY", gson.toJson(getHistory()));
            client.put("ID", gson.toJson(id));
        } catch (ManagerSaveException e) {
            System.out.println("Ошибка записи");
        }
    }

    public static HttpTaskManager load() {
        HttpTaskManager manager = new HttpTaskManager("http://localhost");
        KVTaskClient client = manager.client;
        Gson gson = new Gson();
        try {
            String tasksJson = client.load("TASK");
            if (tasksJson != null) {
                JsonElement taskElement = JsonParser.parseString(tasksJson);
                if (taskElement.isJsonArray()) {
                    JsonArray array = taskElement.getAsJsonArray();
                    for (JsonElement taskJson : array) {
                        if (taskJson.isJsonObject()) {
                            Task task = gson.fromJson(taskJson, Task.class);
                            manager.addTaskToMap(task);
                        }
                    }
                }
            }


            String subtasksJson = client.load("SUBTASK");
            if (subtasksJson != null) {
                JsonElement subtaskElement = JsonParser.parseString(subtasksJson);
                if (subtaskElement.isJsonArray()) {
                    JsonArray array = subtaskElement.getAsJsonArray();
                    for (JsonElement taskJson : array) {
                        if (taskJson.isJsonObject()) {
                            Subtask task = gson.fromJson(taskJson, Subtask.class);
                            manager.addSubtaskToMap(task);
                        }
                    }
                }
            }


            String epicsJson = client.load("EPIC");
            if (epicsJson != null) {
                JsonElement epicElement = JsonParser.parseString(epicsJson);
                if (epicElement.isJsonArray()) {
                    JsonArray array = epicElement.getAsJsonArray();
                    for (JsonElement taskJson : array) {
                        if (taskJson.isJsonObject()) {
                            Epic task = gson.fromJson(taskJson, Epic.class);
                            manager.addEpicToMap(task);
                        }
                    }
                }
            }


            String historyJson = client.load("HISTORY");
            if (historyJson != null) {
                JsonElement historyElement = JsonParser.parseString(historyJson);
                if (historyElement.isJsonArray()) {
                    JsonArray array = historyElement.getAsJsonArray();
                    for (JsonElement taskJson : array) {
                        if (taskJson.isJsonObject()) {
                            JsonObject taskObject = taskJson.getAsJsonObject();
                            int id = taskObject.get("id").getAsInt();
                            Task task = null;
                            if (manager.tasks.containsKey(id)) {
                                task = gson.fromJson(taskObject, Task.class);
                            } else if (manager.subtasks.containsKey(id)) {
                                task = gson.fromJson(taskObject, Subtask.class);
                            } else if (manager.epics.containsKey(id)) {
                                task = gson.fromJson(taskObject, Epic.class);
                            }
                            if (task != null) {
                                manager.historyManager.addTask(task);
                            }
                        }
                    }
                }
            }

            String managerId = client.load("ID");
            if (managerId != null) {
                Integer id = gson.fromJson(managerId, Integer.class);
                manager.id = id;
            }

        } catch (ManagerSaveException e) {
            System.out.println("Ошибка загрузки состояния");
        }
        return manager;
    }

    public void addTaskToMap(Task task) {
        this.tasks.put(task.getId(), task);
        this.sortedTasks.add(task);
    }

    public void addSubtaskToMap(Subtask task) {
        this.subtasks.put(task.getId(), task);
        this.sortedTasks.add(task);
    }

    public void addEpicToMap(Epic task) {
        this.epics.put(task.getId(), task);
        this.sortedTasks.add(task);
    }
}