package task;

import manager.TaskManager;
import task.enums.Statuses;
import task.enums.TasksType;

import java.util.Objects;

public class Subtask extends Task {

    protected int idEpic;
    public Subtask(int id, String name, String description, Statuses status,
                   String startTime, int duration, int idEpic) {
        super(id, name, description, status, startTime, duration);
        this.idEpic = idEpic;
    }

    public int getIdEpic() {
        return idEpic;
    }

    public void setIdEpic(int idEpic) {
        this.idEpic = idEpic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return idEpic == subtask.idEpic;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idEpic);
    }

    public String toString() {
        return id + "," + TasksType.SUBTASK.toString() + "," + name + "," +
                status + "," + description + "," + startTime.format(formatter()) + "," +
                duration + "," + getEndTime().format(formatter()) + "," + idEpic;
    }
}