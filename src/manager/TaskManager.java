package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getHistory();

    List<Task> getTasks();
    List<Epic> getEpics();
    List<Subtask> getSubtasks();

    Task getTask(int id, HistoryManager historyManager);
    Subtask getSubtask(int id, HistoryManager historyManager);
    Epic getEpic(int id, HistoryManager historyManager);
    Epic getEpicForConstructorSubtask(int id);

    void deleteAllTasks();
    void deleteAllEpics();
    void deleteAllSubtasks();

    void addObjectTask(Task task);
    void addObjectEpic(Epic epic);
    void addObjectSubtask(Subtask subtask);

    void updateTasks(Task task);
    void updateEpic(Epic epic);
    void updateSubtask(Subtask subtask);

    void deleteTaskById(int id);
    void deleteEpicById(int id);
    void deleteSubtaskById(int id);

    int getId();
}
