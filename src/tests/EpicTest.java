package tests;

//import constants.TaskStatus;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.enums.Statuses;
//import taskmanagers.Managers;
//import taskmanagers.TaskManager;
//import tasks.Epic;
//import tasks.Subtask;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    Epic epic;
    TaskManager taskManager = Managers.getDefault();

    @BeforeEach
    public void beforeEach() {
        epic = new Epic(taskManager.getId(), "Epic1", "Description Epic 1",
                Statuses.IN_PROGRESS, "01.02.23 03.00", 6 * 24 * 60);
        taskManager.addEpic(epic);
    }

    @Test
    public void shouldEpicStatusNewWhenSubtasksMissed() {
                Statuses status = epic.getStatus();
        assertEquals(Statuses.NEW, status);
    }

    @Test
    public void shouldEpicStatusNewWhenSubtasksAllNew() {
        Subtask subtask1 = new Subtask(taskManager.getId(), "Subtask 1 for epic 1",
                "Description subtask 1 for epic 1", Statuses.NEW, "01.02.23 09.00",
                4 * 60, 1);
        Subtask subtask2 = new Subtask(taskManager.getId(), "Subtask 2 for epic 1",
                "Description subtask 2 for epic 1", Statuses.NEW, "01.02.23 14.00",
                2 * 24 * 60, 1);
        Subtask subtask3 = new Subtask(taskManager.getId(), "Subtask 3 for epic 1",
                "Description subtask 3 for epic 1", Statuses.NEW, "03.02.23 14.00",
                21 * 60, 1);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        Statuses status = epic.getStatus();
        assertEquals(Statuses.NEW, status);
    }

    @Test
    public void shouldEpicStatusDoneWhenSubtasksAllDone() {
        Subtask subtask1 = new Subtask(taskManager.getId(), "Subtask 1 for epic 1",
                "Description subtask 1 for epic 1", Statuses.DONE, "01.02.23 09.00",
                4 * 60, 1);
        Subtask subtask2 = new Subtask(taskManager.getId(), "Subtask 2 for epic 1",
                "Description subtask 2 for epic 1", Statuses.DONE, "01.02.23 14.00",
                2 * 24 * 60, 1);
        Subtask subtask3 = new Subtask(taskManager.getId(), "Subtask 3 for epic 1",
                "Description subtask 3 for epic 1", Statuses.DONE, "03.02.23 14.00",
                21 * 60, 1);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        Statuses status = epic.getStatus();
        assertEquals(Statuses.DONE, status);
    }

    @Test
    public void shouldEpicStatusInProgressWhenSubtasksNewAndDone() {
        Subtask subtask1 = new Subtask(taskManager.getId(), "Subtask 1 for epic 1",
                "Description subtask 1 for epic 1", Statuses.DONE, "01.02.23 09.00",
                4 * 60, 1);
        Subtask subtask2 = new Subtask(taskManager.getId(), "Subtask 2 for epic 1",
                "Description subtask 2 for epic 1", Statuses.DONE, "01.02.23 14.00",
                2 * 24 * 60, 1);
        Subtask subtask3 = new Subtask(taskManager.getId(), "Subtask 3 for epic 1",
                "Description subtask 3 for epic 1", Statuses.NEW, "03.02.23 14.00",
                21 * 60, 1);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        Statuses status = epic.getStatus();
        assertEquals(Statuses.IN_PROGRESS, status);
    }

    @Test
    public void shouldEpicStatusInProgressWhenSubtasksAllInProgress() {
        Subtask subtask1 = new Subtask(taskManager.getId(), "Subtask 1 for epic 1",
                "Description subtask 1 for epic 1", Statuses.DONE, "01.02.23 09.00",
                4 * 60, 1);
        Subtask subtask2 = new Subtask(taskManager.getId(), "Subtask 2 for epic 1",
                "Description subtask 2 for epic 1", Statuses.DONE, "01.02.23 14.00",
                2 * 24 * 60, 1);
        Subtask subtask3 = new Subtask(taskManager.getId(), "Subtask 3 for epic 1",
                "Description subtask 3 for epic 1", Statuses.NEW, "03.02.23 14.00",
                21 * 60, 1);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        Statuses status = epic.getStatus();
        assertEquals(Statuses.IN_PROGRESS, status);
    }
}