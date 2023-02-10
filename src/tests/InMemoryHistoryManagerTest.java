package tests;

import history.HistoryManager;
import history.InMemoryHistoryManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Task;
import task.enums.Statuses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {

    HistoryManager historyManager;
    TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        historyManager = new InMemoryHistoryManager();
        taskManager = Managers.getDefault();
    }

    @Test
    public void testAddTaskToHistoryAndGetHistoryWithTasks() {
        Task task1 = new Task(taskManager.getId(), "Task 1",
                "Description Task 1", Statuses.NEW, "01.02.23 12.00", 60);
        Task task2 = new Task(taskManager.getId(), "Task 2",
                "Description Task 2", Statuses.NEW, "01.02.23 13.00", 60);
        historyManager.addTask(task1);
        historyManager.addTask(task2);
        List<Task> listTasks = new ArrayList<>();
        listTasks.add(task1);
        listTasks.add(task2);
        Collections.reverse(listTasks);
        assertEquals(listTasks, historyManager.getHistory());
    }

    @Test
    public void testRemoveTaskFromHistory() {
        Task task1 = new Task(taskManager.getId(), "Task 1",
                "Description Task 1", Statuses.NEW, "01.02.23 12.00", 60);
        Task task2 = new Task(taskManager.getId(), "Task 2",
                "Description Task 2", Statuses.NEW, "01.02.23 13.00", 60);
        historyManager.addTask(task1);
        historyManager.addTask(task2);
        historyManager.remove(1);
        List<Task> checkList = new ArrayList<>();
        checkList.add(task2);
        assertEquals(checkList, historyManager.getHistory());
    }

    @Test
    public void testGetHistoryWhenHistoryIsEmpty() {
        assertEquals(new ArrayList<>(), historyManager.getHistory());
    }

    @Test
    public void testAddEqualTaskAndRemoveDouble() {
        Task task1 = new Task(taskManager.getId(), "Task 1",
                "Description Task 1", Statuses.NEW, "01.02.23 12.00", 60);
        Task task2 = new Task(taskManager.getId(), "Task 2",
                "Description Task 2", Statuses.NEW, "01.02.23 13.00", 60);
        historyManager.addTask(task1);
        historyManager.addTask(task2);
        historyManager.addTask(task1);
        historyManager.addTask(task2);
        List<Task> listTasks = new ArrayList<>();
        listTasks.add(task1);
        listTasks.add(task2);
        Collections.reverse(listTasks);
        assertEquals(listTasks, historyManager.getHistory());
    }
}