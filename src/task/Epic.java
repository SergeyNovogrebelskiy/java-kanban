package task;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList <Integer> subtasks;

    public Epic(int id, String name, String description, String status) {
        super(id, name, description, status);
        subtasks = new ArrayList<>();
    }
    public void addSubtaskToEpic(int idSubtask){
        subtasks.add(idSubtask);
    }
    public int getNumberOfSubtasks(){
        return subtasks.size();
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }
}