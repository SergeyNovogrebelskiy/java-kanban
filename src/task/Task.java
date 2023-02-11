package task;

import task.enums.Statuses;
import task.enums.TasksType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task implements Comparable<Task> {
    protected int id;
    protected String name;
    protected String description;
    protected Statuses status;
    protected int duration;
    protected LocalDateTime startTime;

    public Task(int id, String name, String description, Statuses status, String startTime, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = setStartTime(startTime);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Statuses getStatus() {
        return status;
    }

    public void setStatus(Statuses status) {
        this.status = status;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    protected LocalDateTime setStartTime(String start) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH.mm");
        return LocalDateTime.parse(start, formatter);
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration);
    }

    protected DateTimeFormatter formatter() {
        return DateTimeFormatter.ofPattern("dd.MM.yy HH.mm");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && duration == task.duration && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status, duration, startTime);
    }

    @Override
    public String toString() {
        return id + "," + TasksType.TASK.toString() + "," + name + "," +
                status + "," + description + "," + startTime.format(formatter()) + "," + duration + "," +
                getEndTime().format(formatter()) + ",";
    }

    public int compareTo(Task o) {
        if (this.startTime.isBefore(o.getStartTime())) {
            return -1;
        } else if (this.startTime.equals(o.getStartTime())) {
            return 0;
        } else {
            return 1;
        }
    }
}