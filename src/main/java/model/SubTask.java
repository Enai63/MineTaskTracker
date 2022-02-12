package model;

public class SubTask extends Task {
    private EpicTask epicTask;

    public SubTask(String name, String descriptions, EpicTask epicTask) {
        super(name, descriptions);
        this.epicTask = epicTask;
    }

    public SubTask(Integer id, String name, String descriptions, Status status) {
        super(id, name, descriptions, status);
    }

    public SubTask(Integer id, String name, String descriptions, Status status, EpicTask epicTask) {
        super(id, name, descriptions, status);
        this.epicTask = epicTask;
    }

    public EpicTask getEpicTask() {
        return epicTask;
    }

    public void setEpicTask(EpicTask epicTask) {
        this.epicTask = epicTask;
    }

    @Override
    public String toString() {
        return "\n" + "Sub task - id: " + super.getId() + "\n"
                + "name: " + super.getName() + "\n"
                + "description: " + super.getDescriptions() + "\n"
                + "status " + super.getStatus() + "\n"
                + "epic: " + epicTask.getName() + "\n";
    }
}
