import manager.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();
        taskManager.addOjectTask(new Task(taskManager.getId(), "Name Task 1",
                "Description Task 1", "NEW"));
        taskManager.addOjectTask(new Task(taskManager.getId(), "Name Task 2",
                "Description Task 2", "NEW"));

        taskManager.addOjectEpic(new Epic(taskManager.getId(), "Epic1",
                "Description Epic 1", "NEW"));
        taskManager.addOjectEpic(new Epic(taskManager.getId(), "Epic2",
                "Description Epic 2", "NEW"));

        taskManager.addOjectSubtask(new Subtask(taskManager.getId(), "Subtask 1 for epic 1",
                "Description subtask 1 for epic 1", "NEW", 3, taskManager));
        taskManager.addOjectSubtask(new Subtask(taskManager.getId(), "Subtask 2 for epic 1",
                "Description subtask 2 for epic 1", "NEW", 3, taskManager));
        taskManager.addOjectSubtask(new Subtask(taskManager.getId(), "Subtask 1 for epic 2",
                "Description subtask 1 for epic 2", "NEW", 4, taskManager));

        printNameEpics(taskManager);
        printNameTasks(taskManager);
        printNameSubtask(taskManager);

        taskManager.updateTasks(new Task(1, "Name Task 1 update",
                "Description Task 1 update", "IN_PROGRESS"));
        taskManager.updateTasks(new Task(2, "Name Task 2 update",
                "Description Task 2 update", "DONE"));

        taskManager.updateSubtask(new Subtask(5, "Subtask 1 for epic 1 update",
                "Description subtask 1 for epic 1 update", "DONE", 3, taskManager));
        taskManager.updateSubtask(new Subtask(6, "Subtask 2 for epic 1 update",
                "Description subtask 2 for epic 1 update", "DONE", 3, taskManager));
        taskManager.updateSubtask(new Subtask(7, "Subtask 1 for epic 2 update",
                "Description subtask 1 for epic 2 update", "IN_PROGRESS", 4, taskManager));

        printTask(taskManager, 1);
        printTask(taskManager, 2);
        printSubtask(taskManager, 5);
        printSubtask(taskManager, 6);
        printEpic(taskManager, 3);
        printSubtask(taskManager, 7);
        printEpic(taskManager, 4);

        taskManager.deleteTaskById(2);
        taskManager.deleteEpicById(3);
        taskManager.deleteSubtaskById(7);

        printNameTasks(taskManager);
        printNameEpics(taskManager);
        printNameSubtask(taskManager);
    }
    public static void printTask(TaskManager taskManager, int idTask){
        Task task = taskManager.getTask(idTask);
        System.out.println("Task ID: " + task.getId() + "\t" + task.getName() + "\t" + task.getDescription() +
                "\tStatus: " + task.getStatus());
    }
    public static void printEpic(TaskManager taskManager, int idEpic){
        Epic epic = taskManager.getEpic(idEpic);
        System.out.println("Epic ID: " + epic.getId() + "\t" + epic.getName() + "\t" + epic.getDescription() +
                "\tStatus: " + epic.getStatus() + "\tNumber of subtasks: " + epic.getNumberOfSubtasks());
    }
    public static void printSubtask(TaskManager taskManager, int idSubtask){
        Subtask subtask = taskManager.getSubtask(idSubtask);
        System.out.println("Subtask ID: " + subtask.getId() + "\t" + subtask.getName() + "\t" +
                subtask.getDescription() + "\tStatus: " + subtask.getStatus() + "\tEpic ID: " + subtask.getIdEpic());
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
