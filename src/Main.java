import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.Statuses;
import task.Subtask;
import task.Task;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        taskManager.addObjectTask(new Task(taskManager.getId(), "Name Task 1",
                "Description Task 1", Statuses.NEW));
        taskManager.addObjectTask(new Task(taskManager.getId(), "Name Task 2",
                "Description Task 2", Statuses.NEW));

        taskManager.addObjectEpic(new Epic(taskManager.getId(), "Epic1",
                "Description Epic 1", Statuses.NEW));
        taskManager.addObjectEpic(new Epic(taskManager.getId(), "Epic2",
                "Description Epic 2", Statuses.NEW));

        taskManager.addObjectSubtask(new Subtask(taskManager.getId(), "Subtask 1 for epic 1",
                "Description subtask 1 for epic 1", Statuses.NEW, 3, taskManager));
        taskManager.addObjectSubtask(new Subtask(taskManager.getId(), "Subtask 2 for epic 1",
                "Description subtask 2 for epic 1", Statuses.NEW, 3, taskManager));
        taskManager.addObjectSubtask(new Subtask(taskManager.getId(), "Subtask 1 for epic 2",
                "Description subtask 1 for epic 2", Statuses.NEW, 4, taskManager));

        printNameEpics(taskManager);
        printNameTasks(taskManager);
        printNameSubtask(taskManager);

        taskManager.updateTasks(new Task(1, "Name Task 1 update",
                "Description Task 1 update", Statuses.IN_PROGRESS));
        taskManager.updateTasks(new Task(2, "Name Task 2 update",
                "Description Task 2 update", Statuses.DONE));

        taskManager.updateSubtask(new Subtask(5, "Subtask 1 for epic 1 update",
                "Description subtask 1 for epic 1 update", Statuses.DONE, 3, taskManager));
        taskManager.updateSubtask(new Subtask(6, "Subtask 2 for epic 1 update",
                "Description subtask 2 for epic 1 update", Statuses.DONE, 3, taskManager));
        taskManager.updateSubtask(new Subtask(7, "Subtask 1 for epic 2 update",
                "Description subtask 1 for epic 2 update", Statuses.IN_PROGRESS, 4, taskManager));

        printTask(taskManager, 1, historyManager);
        printTask(taskManager, 2, historyManager);
        printSubtask(taskManager, 5, historyManager);
        printSubtask(taskManager, 6, historyManager);
        printEpic(taskManager, 3, historyManager);
        printSubtask(taskManager, 7, historyManager);
        printEpic(taskManager, 4, historyManager);

        taskManager.deleteTaskById(2);
        taskManager.deleteEpicById(3);
        taskManager.deleteSubtaskById(7);

        printNameTasks(taskManager);
        printNameEpics(taskManager);
        printNameSubtask(taskManager);

        taskManager.getTask(1, historyManager); //1
        taskManager.getEpic(4, historyManager); //4
        taskManager.getTask(1, historyManager); //5
        taskManager.getSubtask(5, historyManager); //6
        taskManager.getEpic(4, historyManager); //10

        System.out.println(historyManager.getHistory().size());
        printHistory(historyManager.getHistory());


    }
    public static void printTask(TaskManager taskManager, int idTask, HistoryManager historyManager){
        Task task = taskManager.getTask(idTask, historyManager);
        System.out.println("Task ID: " + task.getId() + "\t" + task.getName() + "\t" + task.getDescription() +
                "\tStatus: " + task.getStatus());
    }
    public static void printEpic(TaskManager taskManager, int idEpic, HistoryManager historyManager){
        Epic epic = taskManager.getEpic(idEpic, historyManager);
        System.out.println("Epic ID: " + epic.getId() + "\t" + epic.getName() + "\t" + epic.getDescription() +
                "\tStatus: " + epic.getStatus() + "\tNumber of subtasks: " + epic.getNumberOfSubtasks());
    }
    public static void printSubtask(TaskManager taskManager, int idSubtask, HistoryManager historyManager){
        Subtask subtask = taskManager.getSubtask(idSubtask, historyManager);
        System.out.println("Subtask ID: " + subtask.getId() + "\t" + subtask.getName() + "\t" +
                subtask.getDescription() + "\tStatus: " + subtask.getStatus() + "\tEpic ID: " + subtask.getIdEpic());
    }

    public static void printHistory(List<Task> history){
        for (int i = 0; i < history.size(); i++) {
            System.out.print(history.get(i).getName() + "  ");
        }
        System.out.println();
    }

    public static void printNameTasks(TaskManager taskManager){
        List<Task> listTasks = taskManager.getTasks();
        for (int i = 0; i < listTasks.size(); i++) {
            System.out.print(listTasks.get(i).getName() + "  ");
        }
        System.out.println();
    }
    public static void printNameEpics(TaskManager taskManager){
        List<Epic> listEpics = taskManager.getEpics();
        for (int i = 0; i < listEpics.size(); i++) {
            System.out.print(listEpics.get(i).getName() + "  ");
        }
        System.out.println();
    }
    public static void printNameSubtask(TaskManager taskManager){
        List<Subtask> listSubtask = taskManager.getSubtasks();
        for (int i = 0; i < listSubtask.size(); i++) {
            System.out.print(listSubtask.get(i).getName() + "  ");
        }
        System.out.println();
    }

}
