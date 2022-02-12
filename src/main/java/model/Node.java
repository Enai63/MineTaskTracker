package model;

public class Node {
    private Task task;
    private Node next;
    private Node last;

    public Node(Task task) {
        this.task = task;
        this.next = null;
        this.last = null;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getLast() {
        return last;
    }

    public void setLast(Node last) {
        this.last = last;
    }
}
