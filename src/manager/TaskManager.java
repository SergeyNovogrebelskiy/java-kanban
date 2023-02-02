package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.util.List;

public interface TaskManager {
    List<Task> getTasks();
    List<Epic> getEpics();
    List<Subtask> getSubtasks();

    Task getTask(int id);
    Subtask getSubtask(int id);
    Epic getEpic(int id);
    Epic getEpicForConstructorSubtask(int id);

    void deleteAllTasks();
    void deleteAllEpics();
    void deleteAllSubtasks();

    void addTask(Task task);
    void addEpic(Epic epic);
    void addSubtask(Subtask subtask);

    void updateTasks(Task task);
    void updateEpic(Epic epic);
    void updateSubtask(Subtask subtask);

    void deleteTaskById(int id);
    void deleteEpicById(int id);
    void deleteSubtaskById(int id);

    int getId();


    List<Task> getHistory();
}
