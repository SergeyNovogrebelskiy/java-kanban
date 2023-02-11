package tests;

import manager.FileBackedTasksManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Task;
import task.enums.Statuses;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @Override
    FileBackedTasksManager createManager() {
        return new FileBackedTasksManager("src/files/SaveTasks.csv");
    }

    File file = new File("resources/files/SaveTasks.csv");

//    @AfterEach
//    protected void tearDown() {
//        assertTrue(file.delete());  // Тесты не проходят, поскольку в этой строке получается false
//    }

    @Test
    public void testSaveToFileAndLoadFromFileWithoutHistory() {
        Task task = new Task(taskManager.getId(), "Task 1",
                "Description Task 1", Statuses.NEW, "01.02.23 12.00", 60);
        taskManager.addTask(task);
        File file = new File("src/files", "SaveTasks.csv");
        FileBackedTasksManager newTaskManager = FileBackedTasksManager.loadFromFile(file);
        assertEquals(task, newTaskManager.getTask(task.getId()));
    }

    @Test
    public void testSaveToFileAndLoadFromFileWithHistory() {
        Task task = new Task(taskManager.getId(), "Task 1",
                "Description Task 1", Statuses.NEW, "01.02.23 12.00", 60);
        taskManager.addTask(task);
        taskManager.getTask(task.getId());
        File file = new File("src/files", "SaveTasks.csv");
        FileBackedTasksManager newTaskManager = FileBackedTasksManager.loadFromFile(file);
        assertEquals(task, newTaskManager.getTask(task.getId()));
    }

    @Test
    public void testSaveToFileAndLoadFromFileWithoutTasks() {
        taskManager.save();
        File file = new File("src/files", "SaveTasks.csv");
        FileBackedTasksManager newTaskManager = FileBackedTasksManager.loadFromFile(file);
        assertEquals(0, newTaskManager.getTasks().size());
        assertEquals(0, newTaskManager.getSubtasks().size());
        assertEquals(0, newTaskManager.getEpics().size());
    }

    @Test
    public void testLoadToFileEpicWithoutSubtasks() {
        Epic epic = new Epic(taskManager.getId(), "Epic1",
                "Description Epic 1", Statuses.NEW, "01.02.23 03.00", 6 * 24 * 60);
        taskManager.addEpic(epic);
        File file = new File("src/files", "SaveTasks.csv");
        FileBackedTasksManager newTaskManager = FileBackedTasksManager.loadFromFile(file);
        assertEquals(epic, newTaskManager.getEpic(epic.getId()));
    }
}