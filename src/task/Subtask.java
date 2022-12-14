package task;

import manager.TaskManager;

import java.util.Objects;

public class Subtask extends Task {

    int idEpic;
    public Subtask(int id, String name, String description, String status, int idEpic, TaskManager taskManager) {
        super(id, name, description, status);
        this.idEpic = idEpic;
        (taskManager.getEpic(idEpic)).addSubtaskToEpic(id);
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
}