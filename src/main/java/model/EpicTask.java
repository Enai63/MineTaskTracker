package model;

import java.util.ArrayList;
import java.util.List;

public class EpicTask extends Task {
    private List<SubTask> subTaskList;

    public EpicTask(String name, String descriptions) {
        super(name, descriptions);
        this.subTaskList = new ArrayList<>();
    }

    public EpicTask(String name, String descriptions, Status status) {
        super(name, descriptions, status);
        this.subTaskList = new ArrayList<>();
    }

    public EpicTask(Integer id, String name, String descriptions) {
        super(id, name, descriptions);
        this.subTaskList = new ArrayList<>();
    }


    public List<SubTask> getSubTaskList() {
        return subTaskList;
    }

    public void setSubTaskList(List<SubTask> subTaskList) {
        this.subTaskList = subTaskList;
    }


    @Override
    public String toString() {
        return "Epic task - id: " + super.getId() + "\n"
                + "name: " + super.getName() + "\n"
                + "description: " + super.getDescriptions() + "\n"
                + "status: " + super.getStatus() + "\n" +
                "\n"
                + "list sub task: " + subTaskList + "\n";
    }
}
