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
}