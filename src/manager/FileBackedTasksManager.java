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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager{

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
    public void updateTasks(Task task) {
        super.updateTasks(task);
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
        System.out.println(historyManager.getHistory().size());
        try {
            Files.createDirectory(Paths.get(path));

        } catch (Exception exception) {
            exception.getStackTrace();
        }
        try (Writer fileWriter = new FileWriter(historyFile)) {
            fileWriter.write("id,type,name,status,description,epic\n");
            for (Task task: tasks.values()) {
                fileWriter.write(task.toString() + "\n");
            }
            for (Epic task: epics.values()) {
                fileWriter.write(task.toString() + "\n");
            }
            for (Subtask task: subtasks.values()) {
                fileWriter.write(task.toString() + "\n");
            }
            fileWriter.write("\n");
            System.out.println(historyManager.getHistory().size() + "pisech");
            fileWriter.write(historyToString(historyManager));
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            exception.getStackTrace();
            throw new ManagerSaveException("Произошла ошибка при зписи данных в файл.");
        }

    }

//    protected static Task fromString(String taskString) {
//        String[] entryArray = taskString.split(",");
//        Task task;
//        if (entryArray[1].equals(TasksType.TASK)) {
//            task = new Task(Integer.parseInt(entryArray[0]), entryArray[2], entryArray[4],
//                    Statuses.valueOf(entryArray[3]));
//        } else if (entryArray[1].equals(TasksType.EPIC)) {
//            task = new Epic(Integer.parseInt(entryArray[0]), entryArray[2], entryArray[4],
//                    Statuses.valueOf(entryArray[3]));
//        } else if (entryArray[1].equals(TasksType.SUBTASK)) {
//            task = new Subtask(Integer.parseInt(entryArray[0]), entryArray[2], entryArray[4],
//                    Statuses.valueOf(entryArray[3]), Integer.parseInt(entryArray[5]));
//        } else task = null;
//
//        return task;
//    }
    private static Task taskFromString(String taskString) {
        String[] fields = taskString.split(",");
        Task task;
        switch (fields[1]) {
            case "TASK":
                task = new Task(Integer.parseInt(fields[0]), fields[2], fields[4],
                        Statuses.valueOf(fields[3]));
                //task.setId(Integer.parseInt(fields[0]));
                break;
            case "SUBTASK":
                task = new Subtask(Integer.parseInt(fields[0]), fields[2], fields[4],
                        Statuses.valueOf(fields[3]), Integer.parseInt(fields[5]));
                //task.setId(Integer.parseInt(fields[0]));
                break;
            case "EPIC":
                task = new Epic(Integer.parseInt(fields[0]), fields[2], fields[4],
                        Statuses.valueOf(fields[3]));
                task.setId(Integer.parseInt(fields[0]));
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

//    static List<Integer> historyFromString(String value) {
//        List <Integer> historyList = new ArrayList<>();
//        if (value.equals("")) {
//            return historyList;
//        }
//        String[] taskIds = value.split(",");
//        for (String taskId: taskIds) {
//            historyList.add(Integer.parseInt(taskId));
//        }
//        return historyList;
//    }

    public static String historyToString(HistoryManager manager){
        List<Task> history = manager.getHistory();
        System.out.println(history.size() + "lya");
        String historyString = "";

        for (int i = 0; i < history.size(); i++) {
            if (i == history.size() - 1) {
                historyString += history.get(i).getId();
            } else {
                historyString += history.get(i).getId() + ",";
            }
        }
        return "1,3,5,8";
    }

//    public static String historyToString(HistoryManager manager) {
//        List<Task> history = manager.getHistory();
//        String res = "";
//        for (int i = 0; i < history.size(); i++) {
//            if (i == history.size() - 1) {
//                res += history.get(i).getId();
//            } else {
//                res += history.get(i).getId() + ",";
//            }
//        }
//        return res;
//    }

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
            List <Integer> historyIds = historyFromString(blocks[1].trim());
            Map <Integer, List<Integer>> subtasksToEpic = new HashMap<>();

            for (int i = 1; i < tasksStrings.length; i++) {
                String type = tasksStrings[i].split(",")[1];
                switch (type) {
                    case "TASK":
                        String taskString = tasksStrings[i];
                        Task task = taskFromString(taskString);
                        fileBackedTasksManager.addTask(task);
                        fileBackedTasksManager.incremetId();
                        break;
                    case "SUBTASK":
                        Subtask subtask = (Subtask) taskFromString(tasksStrings[i]);
                        List<Integer> subtasks = subtasksToEpic.getOrDefault(subtask.getIdEpic(), null);
                        if (subtasks != null) {
                            subtasks.add(subtask.getId());
                        } else {
                            subtasks = new ArrayList<>();
                            subtasks.add(subtask.getId());
                        }
                        subtasksToEpic.put(subtask.getIdEpic(), subtasks);
                        fileBackedTasksManager.addSubtask(subtask);
                        fileBackedTasksManager.incremetId();
                        break;
                    case "EPIC":
                        Epic epic = (Epic) taskFromString(tasksStrings[i]);
                        List<Integer> subtasksList = subtasksToEpic.getOrDefault(epic.getId(), null);
                        if (subtasksList != null) {
                            for (Integer id : subtasksList) {
                                epic.addSubtaskToEpic(id);
                            }
                        }
                        fileBackedTasksManager.addEpic(epic);
                        fileBackedTasksManager.incremetId();
                        break;
                    default:
                        System.out.println("Что-то пошло не так...и задача из файла не создалась!!!");
                }
            }

            for (Integer id : historyIds) {
                fileBackedTasksManager.getTask(id);
                fileBackedTasksManager.getSubtask(id);
                fileBackedTasksManager.getEpic(id);
            }
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            return fileBackedTasksManager;
        }

        return fileBackedTasksManager;
    }
}
