package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getTasks();
    List<Epic> getEpics();
    List<Subtask> getSubtasks();

    Task getTask(int id, HistoryManager historyManager);
    Subtask getSubtask(int id, HistoryManager historyManager);
    Epic getEpic(int id, HistoryManager historyManager);
    Epic getEpicForConstructorSubtask(int id);

    void deleteAllTasks(HistoryManager historyManager);
    void deleteAllEpics(HistoryManager historyManager);
    void deleteAllSubtasks(HistoryManager historyManager);

    void addObjectTask(Task task);
    void addObjectEpic(Epic epic);
    void addObjectSubtask(Subtask subtask);

    void updateTasks(Task task);
    void updateEpic(Epic epic);
    void updateSubtask(Subtask subtask);

    void deleteTaskById(int id, HistoryManager historyManager);
    void deleteEpicById(int id, HistoryManager historyManager);
    void deleteSubtaskById(int id, HistoryManager historyManager);

    int getId();
}
