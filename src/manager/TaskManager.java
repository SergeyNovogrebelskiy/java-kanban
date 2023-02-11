package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;
import java.util.Set;

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

    void updateTask(Task task);
    void updateEpic(Epic epic);
    void updateSubtask(Subtask subtask);

    void deleteTaskById(int id);
    void deleteEpicById(int id);
    void deleteSubtaskById(int id);

    int getId();

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();

    List<Subtask> getSubtasksByEpic(Epic epic);
}
