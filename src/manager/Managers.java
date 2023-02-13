package manager;

import history.HistoryManager;
import history.InMemoryHistoryManager;

import java.io.*;

public class Managers {
    public static TaskManager getDefault(){
        return HttpTaskManager.load();
    }

    public static TaskManager getDefault(File file) {
        return FileBackedTasksManager.loadFromFile(file);
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }


}
