package tests;
import manager.HttpTaskManager;
import org.junit.jupiter.api.*;
import server.KVServer;
import task.Epic;
import task.Subtask;
import task.Task;
import task.enums.Statuses;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    KVServer kvServer;

    @Override
    HttpTaskManager createManager() throws IOException {
        return new HttpTaskManager("http://localhost");
    }

    @BeforeEach
    public void beforeEach() {
        try{
            kvServer = new KVServer();
            kvServer.start();
            taskManager = createManager();
        } catch (IOException e) {
            System.out.println("Error at starting kvserver");
        }
    }

    @AfterEach
    public void stopKvServer() {
        kvServer.stop();
    }

    @Test
    public void testLoadingStateFromKVServerToOtherManager() {
        Task task = new Task(taskManager.getId(), "Task 1",
                "Description Task 1", Statuses.NEW, "01.02.23 12.00", 60);
        Epic epic = new Epic(taskManager.getId(), "Epic1", "Description Epic 1",
                Statuses.IN_PROGRESS, "01.02.23 03.00", 6 * 24 * 60);
        Subtask subtask = new Subtask(taskManager.getId(), "Subtask 1 for epic 1",
                "Description subtask 1 for epic 1", Statuses.NEW, "01.02.23 09.00",
                4 * 60, 2);

        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);

        taskManager.getSubtask(subtask.getId());
        taskManager.getTask(task.getId());

        HttpTaskManager taskManager2 = HttpTaskManager.load();

        assertEquals(task, taskManager2.getTask(task.getId()));
        assertEquals(subtask, taskManager2.getSubtask(subtask.getId()));
        assertEquals(epic, taskManager2.getEpic(epic.getId()));
    }

    @Test
    public void testLoadingHistoryFromKVServerToOtherManager() {
        Task task = new Task(taskManager.getId(), "Task 1",
                "Description Task 1", Statuses.NEW, "01.02.23 12.00", 60);
        Epic epic = new Epic(taskManager.getId(), "Epic1", "Description Epic 1",
                Statuses.IN_PROGRESS, "01.02.23 03.00", 6 * 24 * 60);
        Subtask subtask = new Subtask(taskManager.getId(), "Subtask 1 for epic 1",
                "Description subtask 1 for epic 1", Statuses.NEW, "01.02.23 09.00",
                4 * 60, 2);

        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);

        taskManager.getSubtask(subtask.getId());
        taskManager.getTask(task.getId());

        HttpTaskManager taskManager2 = HttpTaskManager.load();
        taskManager2.addTask(task);
        taskManager2.addEpic(epic);
        taskManager2.addSubtask(subtask);

        taskManager2.getSubtask(subtask.getId());
        taskManager2.getTask(task.getId());

        assertArrayEquals(taskManager.getHistory().toArray(), taskManager2.getHistory().toArray());
    }

    @Test
    public void getPrioritizedTaskTest() {
        Task task = new Task(taskManager.getId(), "Task 1",
                "Description Task 1", Statuses.NEW, "01.02.23 12.00", 60);
        Epic epic = new Epic(taskManager.getId(), "Epic1", "Description Epic 1",
                Statuses.IN_PROGRESS, "01.02.23 03.00", 6 * 24 * 60);
        Subtask subtask = new Subtask(taskManager.getId(), "Subtask 1 for epic 1",
                "Description subtask 1 for epic 1", Statuses.NEW, "01.02.23 09.00",
                4 * 60, 2);

        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);

        taskManager.getSubtask(subtask.getId());
        taskManager.getTask(task.getId());

        HttpTaskManager taskManager2 = HttpTaskManager.load();

        assertArrayEquals(taskManager.getPrioritizedTasks().toArray(), taskManager2.getPrioritizedTasks().toArray());
    }

    @Test
    public void isIdFromOtherManagerIsValid() {
        Task task = new Task(taskManager.getId(), "Task 1",
                "Description Task 1", Statuses.NEW, "01.02.23 12.00", 60);
        Epic epic = new Epic(taskManager.getId(), "Epic1", "Description Epic 1",
                Statuses.IN_PROGRESS, "01.02.23 03.00", 6 * 24 * 60);
        Subtask subtask = new Subtask(taskManager.getId(), "Subtask 1 for epic 1",
                "Description subtask 1 for epic 1", Statuses.NEW, "01.02.23 09.00",
                4 * 60, 2);

        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        taskManager.deleteTaskById(task.getId());

        HttpTaskManager taskManager2 = HttpTaskManager.load();

        assertEquals(taskManager.getId(), taskManager2.getId());
    }
}