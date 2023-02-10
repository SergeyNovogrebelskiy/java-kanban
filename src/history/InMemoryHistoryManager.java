package history;

import task.Task;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    CustomLinkedList customLinkedList = new CustomLinkedList<>();

    @Override
    public void addTask(Task task) {
        customLinkedList.addLast(task);
    }

    @Override
    public void remove(int id) {
        customLinkedList.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return customLinkedList.getHistoryTasks();
    }
}
