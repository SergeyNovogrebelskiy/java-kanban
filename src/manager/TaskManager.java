package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private int id = 1;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void deleteAllTasks() {
        tasks.clear();
    }
    public void deleteAllEpics() {
        epics.clear();
        deleteAllSubtasks();
    }
    public void deleteAllSubtasks() {
        subtasks.clear();
        for (int key: epics.keySet()) {
            changeStatusEpicToNew(key);
        }
    }
    private void changeStatusEpicToNew(int key) {
        Epic temp = epics.get(key);
        temp.setStatus("NEW");
    }

    public Task getTask(int key) {
        return tasks.get(key);
    }
    public Epic getEpic(int key) {
        return epics.get(key);
    }
    public Subtask getSubtask(int key) {
        return subtasks.get(key);
    }

    public void addOjectTask(Task task){
        tasks.put(task.getId(), task);
    }
    public void addOjectEpic(Epic epic){
        epics.put(epic.getId(), epic);
        calculateStatusEpic(epic.getId());
    }
    public void addOjectSubtask(Subtask subtask){
        subtasks.put(subtask.getId(), subtask);
        calculateStatusEpic(subtask.getIdEpic());
    }
    public void updateTasks(Task task) {
        int id = task.getId();
        if(tasks.containsKey(id)) {
            addOjectTask(task);
        }
    }
    public void updateEpic(Epic epic) {
        int id = epic.getId();
        if(epics.containsKey(id)) {
            addOjectEpic(epic);
        }
        calculateStatusEpic(id);
    }
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

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        ArrayList<Integer> idSubtasks = epic.getSubtasks();
        for (int idSubtask: idSubtasks) {
            subtasks.remove(idSubtask);
        }
        epics.remove(id);
    }
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
        String status = "NEW";
        String statusSubtask;
        Epic epic = epics.get(idEpic);
        int count = 0;
        ArrayList<Integer> idSubtasks = epic.getSubtasks();
        if (idSubtasks.size() > 0) {
            for(int idSubtask: idSubtasks){
                statusSubtask = (subtasks.get(idSubtask)).getStatus();
                if(!statusSubtask.equals("NEW")){
                    status = "IN_PROGRESS";
                    if(statusSubtask.equals("DONE")){
                        count++;
                    }
                }
            }
            if(count == idSubtasks.size()) {
                status = "DONE";
            }
        }
        epic.setStatus(status);
    }

    public int getId() {
        int res = id;
        incremetId();
        return res;
    }

    private void incremetId(){
        id++;
    }
}