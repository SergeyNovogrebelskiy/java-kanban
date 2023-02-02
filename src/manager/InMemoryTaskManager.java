package manager;

import history.HistoryManager;
import task.Epic;
import task.enums.Statuses;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    protected int id = 1;
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    public HistoryManager historyManager = Managers.getDefaultHistory();

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
    }

    private void changeStatusEpicToNew(int key) {
        Epic temp = epics.get(key);
        temp.setStatus(Statuses.NEW);
    }

    @Override
    public Task getTask(int id) {
        historyManager.addTask(tasks.get(id));
        return tasks.get(id);
    }
    @Override
    public Epic getEpic(int id) {
        historyManager.addTask(epics.get(id));
        return epics.get(id);
    }
    @Override
    public Epic getEpicForConstructorSubtask(int id) {
        return epics.get(id);
    }
    @Override
    public Subtask getSubtask(int id) {
        historyManager.addTask(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }
    @Override
    public void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        calculateStatusEpic(epic.getId());
    }
    @Override
    public void addSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        Epic epic = getEpicForConstructorSubtask(subtask.getIdEpic());
        epic.addSubtaskToEpic(subtask.getId());
                //getEpicForConstructorSubtask(subtask.getIdEpic()).addSubtaskToEpic(subtask.getId());
        calculateStatusEpic(subtask.getIdEpic());
    }
    @Override
    public void updateTasks(Task task) {
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
            ArrayList<Integer> idSubtasks = (epics.get(subtask.getIdEpic())).getSubtasks();
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
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        historyManager.remove(id);
        Epic epic = epics.get(id);
        ArrayList<Integer> idSubtasks = epic.getSubtasks();
        for (int idSubtask: idSubtasks) {
            historyManager.remove(idSubtask);
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
        historyManager.remove(id);
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

    public void setId(int id) {
        this.id = id;
    }

    protected void incremetId(){
        id++;
    }

}