package manager;

import task.Epic;
import task.Statuses;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int id = 1;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private List<Task> history = new ArrayList<>();
    //private HistoryManager historyManager;

//    public InMemoryTaskManager(HistoryManager historyManager) {
//        this.historyManager = Managers.getDefaultHistory();
//    }


    public InMemoryTaskManager() {
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
        tasks.clear();
    }
    @Override
    public void deleteAllEpics() {
        epics.clear();
        deleteAllSubtasks();
    }
    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        for (int key: epics.keySet()) {
            changeStatusEpicToNew(key);
        }
    }
    private void changeStatusEpicToNew(int key) {
        Epic temp = epics.get(key);
        temp.setStatus(Statuses.NEW);
    }

    @Override
    public Task getTask(int id, HistoryManager historyManager) {

        //Task task;
        historyManager.addTask(tasks.get(id));
        //return task;
        return tasks.get(id);
    }
    @Override
    public Epic getEpic(int id, HistoryManager historyManager) {
        historyManager.addTask(epics.get(id));
        return epics.get(id);
    }
    @Override
    public Epic getEpicForConstructorSubtask(int id) {
        return epics.get(id);
    }
    @Override
    public Subtask getSubtask(int id, HistoryManager historyManager) {
        historyManager.addTask(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public void addObjectTask(Task task){
        tasks.put(task.getId(), task);
    }
    @Override
    public void addObjectEpic(Epic epic){
        epics.put(epic.getId(), epic);
        calculateStatusEpic(epic.getId());
    }
    @Override
    public void addObjectSubtask(Subtask subtask){
        subtasks.put(subtask.getId(), subtask);
        calculateStatusEpic(subtask.getIdEpic());
    }
    @Override
    public void updateTasks(Task task) {
        int id = task.getId();
        if(tasks.containsKey(id)) {
            addObjectTask(task);
        }
    }
    @Override
    public void updateEpic(Epic epic) {
        int id = epic.getId();
        if(epics.containsKey(id)) {
            addObjectEpic(epic);
        }
        calculateStatusEpic(id);
    }
    @Override
    public void updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        if(subtasks.containsKey(id)) {
            subtasks.remove(id);
            subtasks.put(subtask.getId(), subtask);
            ArrayList<Integer> idSubtasks = (epics.get(subtask.getIdEpic())).getSubtasks();
            for (int i = 0; i < idSubtasks.size(); i++){
                if(idSubtasks.get(i) == id){
                    idSubtasks.remove(i);
                }
            }
        }
        calculateStatusEpic(subtask.getIdEpic());
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        ArrayList<Integer> idSubtasks = epic.getSubtasks();
        for (int idSubtask: idSubtasks) {
            subtasks.remove(idSubtask);
        }
        epics.remove(id);
    }
    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        int idEpic = subtask.getIdEpic();
        ArrayList<Integer> idSubtasks = (epics.get(idEpic)).getSubtasks();
        for (int i = 0; i < idSubtasks.size(); i++){
            if(idSubtasks.get(i) == id){
                idSubtasks.remove(i);
            }
        }
        subtasks.remove(id);
        calculateStatusEpic(idEpic);
    }
    public ArrayList<Integer> getIdSubtasksOfEpic(int idEpic) {
        Epic epic = epics.get(id);
        return epic.getSubtasks();
    }

    public void calculateStatusEpic(int idEpic) {
        Statuses status = Statuses.NEW;
        Statuses statusSubtask;
        Epic epic = epics.get(idEpic);
        int count = 0;
        ArrayList<Integer> idSubtasks = epic.getSubtasks();
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

    private void incremetId(){
        id++;
    }

//    @Override
//    public List<Task> getHistory() {
//        return null; //historyManager.getHistory();
//    }


    public List<Task> getHistory() {
        return null;
    }
}