package manager;

import exceptions.ManagerSaveException;
import history.HistoryManager;
import history.InMemoryHistoryManager;
import task.Epic;
import task.Subtask;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Managers {
    public static TaskManager getDefault() {
        return new FileBackedTasksManager("src/files/history.csv");
    }

    public static TaskManager getDefault(File file) {
        return FileBackedTasksManager.loadFromFile(file);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }


}
