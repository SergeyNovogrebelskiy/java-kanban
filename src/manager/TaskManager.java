package manager;

import task.Task;

import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    int id = 0;
    public void addTask(String name, String  description){
        Task task = new Task(generateId(), name, description);
        tasks.put(id, task);
    }
    private int generateId(){
        id++;
        return id;
    }
}