package manager;

import exceptions.ManagerSaveException;
import history.HistoryManager;
import task.Epic;
import task.Subtask;
import task.Task;
import task.enums.Statuses;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {

    final private String historyFile;
    final String path = "src/files/";

    public FileBackedTasksManager(String file) {
        historyFile = file;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }
    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }
    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }
    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }
    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    public void save() throws ManagerSaveException {
        try {
            Files.createDirectory(Paths.get(path));
        } catch (Exception exception) {
            exception.getStackTrace();
        }
        try (Writer fileWriter = new FileWriter(historyFile); BufferedWriter bw = new BufferedWriter(fileWriter)) {
            bw.write("id,type,name,status,description,start time, duration, end time, epic\n");
            for (Task task: tasks.values()) {
                bw.write(task.toString() + "\n");
            }
            for (Epic task: epics.values()) {
                bw.write(task.toString() + "\n");
            }
            for (Subtask task: subtasks.values()) {
                bw.write(task.toString() + "\n");
            }
            bw.newLine();
            bw.write(historyToString(historyManager));
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            exception.getStackTrace();
            throw new ManagerSaveException("Произошла ошибка при записи данных в файл.");
        }
    }

    private static Task fromString(String value) {
        String[] entryArray = value.split(",");
        Task task;
        switch (entryArray[1]) {
            case "TASK":
                task = new Task(Integer.parseInt(entryArray[0]), entryArray[2], entryArray[4],
                    Statuses.valueOf(entryArray[3]), entryArray[5], Integer.parseInt(entryArray[6]));
                break;
            case "SUBTASK":
                task = new Subtask(Integer.parseInt(entryArray[0]), entryArray[2], entryArray[4],
                        Statuses.valueOf(entryArray[3]), entryArray[5], Integer.parseInt(entryArray[6]),
                        Integer.parseInt(entryArray[8]));

                break;
            case "EPIC":
                task = new Epic(Integer.parseInt(entryArray[0]), entryArray[2], entryArray[4],
                    Statuses.valueOf(entryArray[3]), entryArray[5], Integer.parseInt(entryArray[6]));
                break;
            default:
                task = null;
        }
        return task;
    }

    static List<Integer> historyFromString(String value) {
        List <Integer> historyList = new ArrayList<>();
        String[] taskIds = value.split(",");
        for (String taskId: taskIds) {
            historyList.add(Integer.parseInt(taskId));
        }
        return historyList;
    }

    public static String historyToString(HistoryManager historyManager){
        List<Task> tasksHistory = historyManager.getHistory();
        String res = "";
        if (!tasksHistory.isEmpty()) {
            res = String.valueOf(tasksHistory.get(tasksHistory.size() - 1).getId());
            for (int i = tasksHistory.size() - 2; i > -1; i--) {
                res = tasksHistory.get(i).getId() + "," + res;
            }
        }
        return res;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file.getAbsolutePath());
        try(Reader reader = new FileReader(file.getAbsolutePath())) {
            BufferedReader br = new BufferedReader(reader);
            StringBuilder builder = new StringBuilder();
            while (br.ready()) {
                builder.append(br.readLine());
                builder.append("\n");
            }
            String fileString = builder.toString();
            String[] blocks = fileString.split("\n\n");

            String[] tasksStrings = blocks[0].split("\n");

            int maxId = 0;
            for (int i = 1; i < tasksStrings.length; i++) {
                String type = tasksStrings[i].split(",")[1];
                switch (type) {
                    case "TASK":
                        Task task = fromString(tasksStrings[i]);
                        fileBackedTasksManager.addTask(task);
                        if (task.getId() > maxId) {
                            maxId = task.getId();
                        }
                        break;
                    case "SUBTASK":
                        Subtask subtask = (Subtask) fromString(tasksStrings[i]);
                        fileBackedTasksManager.addSubtask(subtask);
                        if (subtask.getId() > maxId) {
                            maxId = subtask.getId();
                        }
                        break;
                    case "EPIC":
                        Epic epic = (Epic) fromString(tasksStrings[i]);
                        fileBackedTasksManager.addEpic(epic);
                        if (epic.getId() > maxId) {
                            maxId = epic.getId();
                        }
                        break;
                    default:
                        System.out.println("Произошла ошибка при чтении файла.");
                }
            }
            fileBackedTasksManager.setId(maxId);
            if (blocks.length > 1){
                List <Integer> historyIds = historyFromString(blocks[1].trim());
                for (int i = historyIds.size() - 1; i > -1; i--) {
                    if (fileBackedTasksManager.tasks.containsKey(historyIds.get(i))) {
                        fileBackedTasksManager.getTask(historyIds.get(i));
                    } else if (fileBackedTasksManager.subtasks.containsKey(historyIds.get(i))) {
                        fileBackedTasksManager.getSubtask(historyIds.get(i));
                    } else if (fileBackedTasksManager.epics.containsKey(historyIds.get(i))) {
                        fileBackedTasksManager.getEpic(historyIds.get(i));
                    }
                }
            }
        } catch (ManagerSaveException exception) {
            System.out.println(exception.getMessage());
        } catch (IOException exception) {
            return fileBackedTasksManager;
        }
        return fileBackedTasksManager;
    }

    @Override
    public Task getTask(int id) {
        if (tasks.get(id) != null) {
            historyManager.addTask(tasks.get(id));
        }
        save();
        return tasks.getOrDefault(id, null);
    }

    @Override
    public Epic getEpic(int id) {
        if (epics.get(id) != null) {
            historyManager.addTask(epics.get(id));
        }
        save();
        return epics.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        if (subtasks.get(id) != null) {
            historyManager.addTask(subtasks.get(id));
        }
        save();
        return subtasks.get(id);
    }

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        taskManager.addTask(new Task(taskManager.getId(), "Task 1",
                "Description Task 1", Statuses.NEW, "01.02.23 12.00", 60));
        taskManager.addTask(new Task(taskManager.getId(), "Task 2",
                "Description Task 2", Statuses.NEW, "01.02.23 13.00", 60));

        taskManager.addEpic(new Epic(taskManager.getId(), "Epic1",
                "Description Epic 1", Statuses.NEW, "01.02.23 03.00", 6 * 24 * 60));
        taskManager.addEpic(new Epic(taskManager.getId(), "Epic2",
                "Description Epic 2", Statuses.NEW, "02.02.23 03.00", 12 * 24 * 60));

        taskManager.addSubtask(new Subtask(taskManager.getId(), "Subtask 1 for epic 1",
                "Description subtask 1 for epic 1", Statuses.NEW, "01.02.23 09.00",
                4 * 60, 3));
        taskManager.addSubtask(new Subtask(taskManager.getId(), "Subtask 2 for epic 1",
                "Description subtask 2 for epic 1", Statuses.NEW, "01.02.23 14.00",
                2 * 24 * 60, 3));
        taskManager.addSubtask(new Subtask(taskManager.getId(), "Subtask 3 for epic 1",
                "Description subtask 3 for epic 1", Statuses.NEW, "03.02.23 14.00",
                21 * 60, 3));
        taskManager.addSubtask(new Subtask(taskManager.getId(), "Subtask 1 for epic 2",
                "Description subtask 1 for epic 2", Statuses.NEW, "04.02.23 15.00",
                5 * 24 * 60, 4));

        taskManager.getTask(1);
        taskManager.getEpic(4);
        taskManager.getSubtask(5);
        taskManager.getSubtask(7);
        taskManager.getTask(1);
        taskManager.getSubtask(7);
        taskManager.getTask(2);
        taskManager.getSubtask(5);
        taskManager.getEpic(3);
        taskManager.getSubtask(6);
        taskManager.getSubtask(6);
        taskManager.getSubtask(8);

        printHistory(taskManager.getHistory());
        System.out.println("Отсортированый список: " + taskManager.getPrioritizedTasks());
        printNameTasks(taskManager.getPrioritizedTasks());

        TaskManager newTaskManager = Managers.getDefault(new File("src/files", "SaveTasks.csv"));
        printHistory(newTaskManager.getHistory());
        newTaskManager.addTask(new Task(taskManager.getId(), "Task 9",
                "Description Task 9", Statuses.NEW, "20.02.23 11.00", 30));
        System.out.println(newTaskManager.getTask(9));
    }
    public static void printTask(TaskManager taskManager, int idTask, HistoryManager historyManager){
        Task task = taskManager.getTask(idTask);
        System.out.println("Task ID: " + task.getId() + "\t" + task.getName() + "\t" + task.getDescription() +
                "\tStatus: " + task.getStatus());
    }
    public static void printEpic(TaskManager taskManager, int idEpic, HistoryManager historyManager){
        Epic epic = taskManager.getEpic(idEpic);
        System.out.println("Epic ID: " + epic.getId() + "\t" + epic.getName() + "\t" + epic.getDescription() +
                "\tStatus: " + epic.getStatus() + "\tNumber of subtasks: " + epic.getNumberOfSubtasks());
    }
    public static void printSubtask(TaskManager taskManager, int idSubtask, HistoryManager historyManager){
        Subtask subtask = taskManager.getSubtask(idSubtask);
        System.out.println("Subtask ID: " + subtask.getId() + "\t" + subtask.getName() + "\t" +
                subtask.getDescription() + "\tStatus: " + subtask.getStatus() + "\tEpic ID: " + subtask.getIdEpic());
    }

    public static void printHistory(List<Task> history){
        System.out.print("History: ");
        for (int i = 0; i < history.size(); i++) {
            System.out.print(history.get(i).getName() + "  ");
        }
        System.out.println();
    }

    public static void printNameTasks(Set<Task> taskSet){
        System.out.print("Отсортированные имена: ");
        for (Task task: taskSet) {
            System.out.print("[ID " + task.getId() +  ", NAME " + task.getName() + "] ");
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
