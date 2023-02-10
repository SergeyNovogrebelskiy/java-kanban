package task;

import task.enums.Statuses;
import task.enums.TasksType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    protected ArrayList <Integer> subtasks;
    private LocalDateTime endTime;

    public Epic(int id, String name, String description, Statuses status, String startTime, int duration) {
        super(id, name, description, status, startTime, duration);
        subtasks = new ArrayList<>();
    }
    public void addSubtaskToEpic(int idSubtask) {
        subtasks.add(idSubtask);
    }

    public ArrayList<Integer> getIdSubtasks() {
        return subtasks;
    }

//    @Override
//    public LocalDateTime getEndTime() {
//        return endTime;
//    }

    public int getNumberOfSubtasks() {
        return subtasks.size();
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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

    public String toString() {
        return id + "," + TasksType.EPIC.toString() + "," + name + "," +
                status + "," + description + "," + startTime.format(formatter()) + "," +
                duration + "," + getEndTime().format(formatter()) + ",";
    }
}