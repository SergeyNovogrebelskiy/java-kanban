package task;

import task.enums.Statuses;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList <Integer> subtasks;

    public Epic(int id, String name, String description, Statuses status) {
        super(id, name, description, status);
        subtasks = new ArrayList<>();
    }
    public void addSubtaskToEpic(int idSubtask) {
        subtasks.add(idSubtask);
    }
    public int getNumberOfSubtasks() {
        return subtasks.size();
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasks, epic.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
    }
}