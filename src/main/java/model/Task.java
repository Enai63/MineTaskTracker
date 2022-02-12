package model;

import java.util.Objects;

public class Task {
    private Integer id;
    private String name;
    private String descriptions;
    private Status status;

    public Task() {
    }

    public Task(String name, String descriptions) {
        this.name = name;
        this.descriptions = descriptions;
    }

    public Task(String name, String descriptions, Status status) {
        this.name = name;
        this.descriptions = descriptions;
        this.status = status;
    }

    public Task(Integer id, String name, String descriptions, Status status) {
        this.id = id;
        this.name = name;
        this.descriptions = descriptions;
        this.status = status;
    }

    public Task(Integer id, String name, String descriptions) {
        this.id = id;
        this.name = name;
        this.descriptions = descriptions;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(name, task.name)
                && Objects.equals(descriptions, task.descriptions) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, descriptions, status);
    }

    @Override
    public String toString() {
        return "Simple task " + "\n"
                + "id: " + id + "\n"
                + "name: " + name + "\n"
                + "descriptions: " + descriptions + "\n"
                + "status: " + status;
    }
}
