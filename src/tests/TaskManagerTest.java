package tests;

import history.InMemoryHistoryManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;
import task.enums.Statuses;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest <T extends TaskManager> {

    T taskManager;

    abstract T createManager();

    @BeforeEach
    public void beforeEach(){
        taskManager = createManager();
    }

    @Test
    public void testSubtasksHaveEpicAndEpicHaveSubtasksId() {
        Epic epic = new Epic(taskManager.getId(), "Epic1", "Description Epic 1",
                Statuses.IN_PROGRESS, "01.02.23 03.00", 6 * 24 * 60);
        Subtask subtask = new Subtask(taskManager.getId(), "Subtask 1 for epic 1",
                "Description subtask 1 for epic 1", Statuses.NEW, "01.02.23 09.00",
                4 * 60, 1);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        assertEquals(1, taskManager.getSubtask(2).getIdEpic());
        assertEquals(2, taskManager.getEpic(1).getIdSubtasks().get(0));
    }

    @Test
    public void testEpicStatusDoneWhenSubtasksDone() {
        Epic epic = new Epic(taskManager.getId(), "Epic1", "Description Epic 1",
                Statuses.IN_PROGRESS, "01.02.23 03.00", 6 * 24 * 60);
        Subtask subtask1 = new Subtask(taskManager.getId(), "Subtask 1 for epic 1",
                "Description subtask 1 for epic 1", Statuses.DONE, "01.02.23 09.00",
                4 * 60, 1);
        Subtask subtask2 = new Subtask(taskManager.getId(), "Subtask 2 for epic 1",
                "Description subtask 2 for epic 1", Statuses.DONE, "01.02.23 14.00",
                2 * 24 * 60, 1);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(Statuses.DONE, taskManager.getEpic(epic.getId()).getStatus());
    }

    @Test
    public void testAddNewTaskAndGetTaskId1() {
        Task task = new Task(taskManager.getId(), "Task 1",
                "Description Task 1", Statuses.NEW, "01.02.23 12.00", 60);
        taskManager.addTask(task);
        assertEquals(task, taskManager.getTask(1));
    }

    @Test
    public void testAddNewSubtaskAndGetSubtaskId1() {
        Epic epic = new Epic(taskManager.getId(), "Epic1", "Description Epic 1",
                Statuses.IN_PROGRESS, "01.02.23 03.00", 6 * 24 * 60);
        Subtask subtask = new Subtask(taskManager.getId(), "Subtask 1 for epic 1",
                "Description subtask 1 for epic 1", Statuses.NEW, "01.02.23 09.00",
                4 * 60, 1);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        assertEquals(subtask, taskManager.getSubtask(2));
    }

    @Test
    public void testAddNewEpicAndGetEpicId1() {
        Epic epic = new Epic(taskManager.getId(), "Epic1", "Description Epic 1",
                Statuses.IN_PROGRESS, "01.02.23 03.00", 6 * 24 * 60);
        taskManager.addEpic(epic);
        assertEquals(epic, taskManager.getEpic(1));
    }

    @Test
    public void testGetAllTasks() {
        Task task1 = new Task(taskManager.getId(), "Task 1",
                "Description Task 1", Statuses.NEW, "01.02.23 12.00", 60);
        Task task2 = new Task(taskManager.getId(), "Task 2",
                "Description Task 2", Statuses.NEW, "01.02.23 13.00", 60);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        assertArrayEquals(tasks.toArray(), taskManager.getTasks().toArray());
    }

    @Test
    public void testGetAllSubtasks() {
        Epic epic = new Epic(taskManager.getId(), "Epic1", "Description Epic 1",
                Statuses.IN_PROGRESS, "01.02.23 03.00", 6 * 24 * 60);
        Subtask subtask1 = new Subtask(taskManager.getId(), "Subtask 1 for epic 1",
                "Description subtask 1 for epic 1", Statuses.DONE, "01.02.23 09.00",
                4 * 60, 1);
        Subtask subtask2 = new Subtask(taskManager.getId(), "Subtask 2 for epic 1",
                "Description subtask 2 for epic 1", Statuses.DONE, "01.02.23 14.00",
                2 * 24 * 60, 1);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        List<Subtask> subtasks = new ArrayList<>();
        subtasks.add(subtask1);
        subtasks.add(subtask2);
        assertArrayEquals(subtasks.toArray(), taskManager.getSubtasks().toArray());
    }

    @Test
    public void testGetAllEpics() {
        Epic epic1 = new Epic(taskManager.getId(), "Epic1",
                "Description Epic 1", Statuses.NEW, "01.02.23 03.00", 6 * 24 * 60);
        Epic epic2 = new Epic(taskManager.getId(), "Epic2",
                "Description Epic 2", Statuses.NEW, "02.02.23 03.00", 12 * 24 * 60);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        List<Epic> tasks = new ArrayList<>();
        tasks.add(epic1);
        tasks.add(epic2);
        assertArrayEquals(tasks.toArray(), taskManager.getEpics().toArray());
    }

    @Test
    public void testRemoveAllTasks() {
        Task task1 = new Task(taskManager.getId(), "Task 1",
                "Description Task 1", Statuses.NEW, "01.02.23 12.00", 60);
        Task task2 = new Task(taskManager.getId(), "Task 2",
                "Description Task 2", Statuses.NEW, "01.02.23 13.00", 60);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.deleteAllTasks();
        assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    public void testRemoveAllSubtasks() {
        Epic epic = new Epic(taskManager.getId(), "Epic1", "Description Epic 1",
                Statuses.IN_PROGRESS, "01.02.23 03.00", 6 * 24 * 60);
        Subtask subtask1 = new Subtask(taskManager.getId(), "Subtask 1 for epic 1",
                "Description subtask 1 for epic 1", Statuses.DONE, "01.02.23 09.00",
                4 * 60, 1);
        Subtask subtask2 = new Subtask(taskManager.getId(), "Subtask 2 for epic 1",
                "Description subtask 2 for epic 1", Statuses.DONE, "01.02.23 14.00",
                2 * 24 * 60, 1);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.deleteAllSubtasks();
        assertEquals(0, taskManager.getSubtasks().size());
    }

    @Test
    public void testRemoveAllEpics() {
        Epic epic1 = new Epic(taskManager.getId(), "Epic1",
                "Description Epic 1", Statuses.NEW, "01.02.23 03.00", 6 * 24 * 60);
        Epic epic2 = new Epic(taskManager.getId(), "Epic2",
                "Description Epic 2", Statuses.NEW, "02.02.23 03.00", 12 * 24 * 60);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.deleteAllEpics();
        assertEquals(0, taskManager.getEpics().size());
    }

    @Test
    public void testReturnTaskIfIdIsWrong() {
        Task task1 = new Task(taskManager.getId(), "Task 1",
                "Description Task 1", Statuses.NEW, "01.02.23 12.00", 60);
        taskManager.addTask(task1);
        assertNull(taskManager.getTask(6));
    }

    @Test
    public void testReturnTaskById() {
        Task task1 = new Task(taskManager.getId(), "Task 1",
                "Description Task 1", Statuses.NEW, "01.02.23 12.00", 60);
        taskManager.addTask(task1);
        assertEquals(task1, taskManager.getTask(1));
    }

    @Test
    public void testReturnSubtaskIfIdIsWrong() {
        Epic epic = new Epic(taskManager.getId(), "Epic1", "Description Epic 1",
                Statuses.IN_PROGRESS, "01.02.23 03.00", 6 * 24 * 60);
        Subtask subtask1 = new Subtask(taskManager.getId(), "Subtask 1 for epic 1",
                "Description subtask 1 for epic 1", Statuses.DONE, "01.02.23 09.00",
                4 * 60, 1);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1);
        assertNull(taskManager.getSubtask(3));
    }

    @Test
    public void testReturnSubtaskById() {
        Epic epic = new Epic(taskManager.getId(), "Epic1", "Description Epic 1",
                Statuses.IN_PROGRESS, "01.02.23 03.00", 6 * 24 * 60);
        Subtask subtask1 = new Subtask(taskManager.getId(), "Subtask 1 for epic 1",
                "Description subtask 1 for epic 1", Statuses.DONE, "01.02.23 09.00",
                4 * 60, 1);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1);
        assertEquals(subtask1, taskManager.getSubtask(2));
    }

    @Test
    public void testReturnEpicIfIdIsWrong() {
        Epic epic = new Epic(taskManager.getId(), "Epic1", "Description Epic 1",
                Statuses.IN_PROGRESS, "01.02.23 03.00", 6 * 24 * 60);
        taskManager.addEpic(epic);
        assertNull(taskManager.getEpic(2));
    }

    @Test
    public void testReturnEpicById() {
        Epic epic = new Epic(taskManager.getId(), "Epic1", "Description Epic 1",
                Statuses.IN_PROGRESS, "01.02.23 03.00", 6 * 24 * 60);
        taskManager.addEpic(epic);
        assertEquals(epic, taskManager.getEpic(1));
    }

    @Test
    public void testUpdateTask() {
        Task task = new Task(taskManager.getId(), "Task 1",
                "Description Task 1", Statuses.NEW, "01.02.23 12.00", 60);
        Task newTask = new Task(1, "Task 1 update",
                "Description Task 1 update", Statuses.DONE, "01.02.23 14.00", 60);
        taskManager.addTask(task);
        taskManager.updateTask(newTask);
        assertEquals(newTask, taskManager.getTask(task.getId()));
    }

    @Test
    public void testUpdateSubtask() {
        Epic epic = new Epic(taskManager.getId(), "Epic1", "Description Epic 1",
                Statuses.IN_PROGRESS, "01.02.23 03.00", 6 * 24 * 60);
        Subtask subtask1 = new Subtask(taskManager.getId(), "Subtask 1 for epic 1",
                "Description subtask 1 for epic 1", Statuses.DONE, "01.01.23 09.00",
                4 * 60, 1);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1);
        Subtask newSubtask = new Subtask(2, "Subtask 1 for epic 1 update",
                "Description subtask 1 for epic 1 update", Statuses.IN_PROGRESS, "01.02.23 09.00",
                4 * 60, 1);
        taskManager.updateSubtask(newSubtask);
        assertEquals(newSubtask, taskManager.getSubtask(subtask1.getId()));
    }

    @Test
    public void testUpdateEpic() {
        Epic epic = new Epic(taskManager.getId(), "Epic1", "Description Epic 1",
                Statuses.IN_PROGRESS, "01.02.23 03.00", 6 * 24 * 60);
        Epic newEpic = new Epic(1, "Epic1 update", "Description Epic 1 update",
                Statuses.DONE, "01.02.23 03.00", 6 * 24 * 60);
        taskManager.addEpic(epic);
        taskManager.updateEpic(newEpic);
        assertEquals(newEpic, taskManager.getEpic(epic.getId()));
    }

    @Test
    public void testDeleteTask() {
        Task task1 = new Task(taskManager.getId(), "Task 1",
                "Description Task 1l", Statuses.NEW, "01.02.23 12.00", 60);
        taskManager.addTask(task1);
        taskManager.deleteTaskById(task1.getId());
        assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    public void testDeleteSubtask() {
        Epic epic = new Epic(taskManager.getId(), "Epic1", "Description Epic 1",
                Statuses.IN_PROGRESS, "01.02.23 03.00", 6 * 24 * 60);
        Subtask subtask1 = new Subtask(taskManager.getId(), "Subtask 1 for epic 1",
                "Description subtask 1 for epic 1", Statuses.DONE, "01.02.23 09.00",
                4 * 60, 1);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1);
        taskManager.deleteSubtaskById(subtask1.getId());
        assertEquals(0, taskManager.getSubtasks().size());
    }

    @Test
    public void testDeleteEpic() {
        Epic epic = new Epic(taskManager.getId(), "Epic1", "Description Epic 1",
                Statuses.IN_PROGRESS, "01.02.23 03.00", 6 * 24 * 60);
        taskManager.addEpic(epic);
        taskManager.deleteEpicById(epic.getId());
        assertEquals(0, taskManager.getEpics().size());
    }

    @Test
    public void testReturnListOfSubtaskByEpicId() {
        Epic epic = new Epic(taskManager.getId(), "Epic1", "Description Epic 1",
                Statuses.IN_PROGRESS, "01.02.23 03.00", 6 * 24 * 60);
        Subtask subtask1 = new Subtask(taskManager.getId(), "Subtask 1 for epic 1",
                "Description subtask 1 for epic 1", Statuses.DONE, "01.02.23 09.00",
                4 * 60, 1);
        Subtask subtask2 = new Subtask(taskManager.getId(), "Subtask 2 for epic 1",
                "Description subtask 2 for epic 1", Statuses.DONE, "01.02.23 14.00",
                2 * 24 * 60, 1);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        List<Subtask> subtasks = new ArrayList<>();
        subtasks.add(subtask1);
        subtasks.add(subtask2);
        assertArrayEquals(subtasks.toArray(), taskManager.getSubtasksByEpic(epic).toArray());
    }

    @Test
    public void testReturnHistory() {
        InMemoryHistoryManager historyCheck = Managers.getDefaultHistory();
        assertEquals(historyCheck.getHistory(), taskManager.getHistory());
    }
}