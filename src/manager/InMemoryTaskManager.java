package manager;

import history.HistoryManager;
import task.Epic;
import task.enums.Statuses;
import task.Subtask;
import task.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected int id = 1;
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    public HistoryManager historyManager = Managers.getDefaultHistory();
    protected Set<Task> sortedTasks = new TreeSet<>();

    public InMemoryTaskManager() {
    }

    @Override
    public List <Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }
    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteAllTasks() {
        sortedTasks.removeAll(tasks.values());
        for (int key: tasks.keySet()) {
            historyManager.remove(key);
        }
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        for (int key: epics.keySet()) {
            historyManager.remove(key);
        }
        epics.clear();
        deleteAllSubtasks();
    }

    @Override
    public void deleteAllSubtasks() {
        for (int key: subtasks.keySet()) {
            historyManager.remove(key);
        }
        subtasks.clear();
        for (int key: epics.keySet()) {
            changeStatusEpicToNew(key);
        }
        sortedTasks.removeAll(subtasks.values());
    }

    private void changeStatusEpicToNew(int key) {
        Epic temp = epics.get(key);
        temp.setStatus(Statuses.NEW);
    }

    @Override
    public Task getTask(int id) {
        if (tasks.get(id) != null) {
            historyManager.addTask(tasks.get(id));
        }
        return tasks.getOrDefault(id, null);
    }
    @Override
    public Epic getEpic(int id) {
        if (epics.get(id) != null) {
            historyManager.addTask(epics.get(id));
        }
        return epics.getOrDefault(id, null);
    }
    @Override
    public Epic getEpicForConstructorSubtask(int id) {
        return epics.get(id);
    }
    @Override
    public Subtask getSubtask(int id) {
        if (subtasks.get(id) != null) {
            historyManager.addTask(subtasks.get(id));
        }
        return subtasks.getOrDefault(id, null);
    }

    @Override
    public void addTask(Task task) {
        tasks.put(task.getId(), task);
        validateTask(task);
        sortedTasks.add(task);
    }
    @Override
    public void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        calculateStatusEpic(epic.getId());
        calculateStartAndEndTimeEpic(epic);
    }

    private void calculateStartAndEndTimeEpic(Epic epic) {
        setEpicStartTime(epic);
        setEpicDuration(epic);
        setEpicEndTime(epic);
    }
    @Override
    public void addSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        Epic epic = getEpicForConstructorSubtask(subtask.getIdEpic());
        epic.addSubtaskToEpic(subtask.getId());
                //getEpicForConstructorSubtask(subtask.getIdEpic()).addSubtaskToEpic(subtask.getId());
        calculateStatusEpic(subtask.getIdEpic());
        validateTask(subtask);
        calculateStartAndEndTimeEpic(epic);
        sortedTasks.add(subtask);
    }

    protected void setEpicStartTime(Epic epic) {
        List<Subtask> subtasksByEpic = getSubtasksByEpic(epic);
        if (subtasksByEpic.size() > 0) {
            LocalDateTime startTime = null;
            for (Subtask subtask : subtasksByEpic) {
                if (startTime == null) {
                    startTime = subtask.getStartTime();
                }else if (subtask.getStartTime().isBefore(startTime)){
                    startTime = subtask.getStartTime();
                }
            }
            epic.setStartTime(startTime);
        }
    }

    protected void setEpicDuration(Epic epic) {
        List<Subtask> subtasksByEpic = getSubtasksByEpic(epic);
        if (subtasksByEpic.size() > 0) {
            int duration = 0;
            for (Subtask subtask : subtasksByEpic) {
                duration += subtask.getDuration();
            }
            epic.setDuration(duration);
        }
    }

    protected void setEpicEndTime(Epic epic) {
        List<Subtask> subtasksByEpic = getSubtasksByEpic(epic);
        if (subtasksByEpic.size() > 0) {
            LocalDateTime endTime = null;
            for (Subtask subtask : subtasksByEpic) {
                if (endTime == null) {
                    endTime = subtask.getEndTime();
                } else if (subtask.getEndTime().isAfter(endTime)) {
                    endTime = subtask.getEndTime();
                }
            }
            epic.setEndTime(endTime);
        }
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return this.sortedTasks;
    }

    public void validateTask(Task newTask) {
        for (Task task : sortedTasks) {
            if (newTask.getStartTime().isAfter(task.getStartTime()) &&
                    newTask.getStartTime().isBefore(task.getEndTime())) {
                System.out.println("Ваши задача пересекается по времени выполнения!");
                break;
            }
        }
    }

    @Override
    public List<Subtask> getSubtasksByEpic(Epic epic) {
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        for (Integer subtaskId : epic.getIdSubtasks()) {
            subtaskList.add(subtasks.get(subtaskId));
        }
        return subtaskList;
    }
    @Override
    public void updateTask(Task task) {
        int id = task.getId();
        if(tasks.containsKey(id)) {
            addTask(task);
        }
    }
    @Override
    public void updateEpic(Epic epic) {
        int id = epic.getId();
        if(epics.containsKey(id)) {
            addEpic(epic);
        }
        calculateStatusEpic(id);
    }
    @Override
    public void updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        if(subtasks.containsKey(id)) {
            subtasks.remove(id);
            subtasks.put(subtask.getId(), subtask);
            ArrayList<Integer> idSubtasks = (epics.get(subtask.getIdEpic())).getIdSubtasks();
            for (int i = 0; i < idSubtasks.size(); i++){
                if(idSubtasks.get(i) == id){
                    idSubtasks.remove(i);
                }
            }
        }
        addSubtask(subtask);
        calculateStatusEpic(subtask.getIdEpic());
    }

    @Override
    public void deleteTaskById(int id) {
        Task task = getTask(id);
        historyManager.remove(id);
        tasks.remove(id);
        sortedTasks.remove(task);
    }

    @Override
    public void deleteEpicById(int id) {
        historyManager.remove(id);
        Epic epic = epics.get(id);
        ArrayList<Integer> idSubtasks = epic.getIdSubtasks();
        for (int idSubtask: idSubtasks) {
            historyManager.remove(idSubtask);
            subtasks.remove(idSubtask);
            sortedTasks.remove(getSubtask(idSubtask));
        }
        epics.remove(id);
    }
    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        int idEpic = subtask.getIdEpic();
        ArrayList<Integer> idSubtasks = (epics.get(idEpic)).getIdSubtasks();
        for (int i = 0; i < idSubtasks.size(); i++){
            if(idSubtasks.get(i) == id){
                idSubtasks.remove(i);
            }
        }
        sortedTasks.remove(subtask);
        historyManager.remove(id);
        subtasks.remove(id);
        calculateStatusEpic(idEpic);
        calculateStartAndEndTimeEpic(epics.get(idEpic));
    }
    public ArrayList<Integer> getIdSubtasksOfEpic(int idEpic) {
        Epic epic = epics.get(id);
        return epic.getIdSubtasks();
    }

    public void calculateStatusEpic(int idEpic) {
        Statuses status = Statuses.NEW;
        Statuses statusSubtask;
        Epic epic = epics.get(idEpic);
        int count = 0;
        ArrayList<Integer> idSubtasks = epic.getIdSubtasks();
        if (idSubtasks.size() > 0) {
            for(int idSubtask: idSubtasks){
                statusSubtask = (subtasks.get(idSubtask)).getStatus();
                if(!statusSubtask.equals(Statuses.NEW)){
                    status = Statuses.IN_PROGRESS;
                    if(statusSubtask.equals(Statuses.DONE)){
                        count++;
                    }
                }
            }
            if(count == idSubtasks.size()) {
                status = Statuses.DONE;
            }
        }
        epic.setStatus(status);
    }

    @Override
    public int getId() {
        int res = id;
        incremetId();
        return res;
    }

    public void setId(int id) {
        this.id = id;
    }

    protected void incremetId(){
        id++;
    }

}