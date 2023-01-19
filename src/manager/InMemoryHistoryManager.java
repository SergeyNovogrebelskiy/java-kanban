package manager;

import task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private int STORY_SIZE = 10;
    private List<Task> history = new ArrayList<>();

    @Override
    public void addTask(Task task) {
        while (history.size() >= STORY_SIZE){
            history.remove(0);
        }
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        List<Task> res = new ArrayList<>();
        for (int i = history.size() - 1; i > -1; i--) {
            res.add(history.get(i));
        }
        return res;
    }
}
