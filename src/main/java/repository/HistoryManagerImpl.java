package repository;

import informer.Informant;
import interfaces.HistoryManager;
import model.Node;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryManagerImpl implements HistoryManager {
    private Node tail;
    private Node head;
    private final Map<Integer, Node> historyMap;
    private final Informant informant;

    public HistoryManagerImpl() {
        historyMap = new HashMap<>();
        informant = new Informant();
    }

    /*
    Add task in history
     */
    @Override
    public void addTaskInHistory(Task task) {
        if (task != null) {
            removeNode(task.getId());
            linkLast(task);
        }
    }

    /*
    Get all history from custom linked list
     */
    @Override
    public List<Task> getHistoryTasks() {
        List<Task> taskList = new ArrayList<>();
        Node current = head;
        while (current != null) {
            taskList.add(current.getTask());
            current = current.getNext();
        }

        informant.whenAction(!taskList.isEmpty());
        return taskList;
    }

    /*
    Delete task from history on id
     */
    @Override
    public void remove(int id) {
        Node removeNode = historyMap.remove(id);
        if (removeNode != null) {
            Node last = removeNode.getLast();
            Node next = removeNode.getNext();
            if (last == null) {
                next.setLast(null);
            } else if (next == null) {
                last.setNext(null);
            } else {
                last.setNext(next);
                next.setLast(last);
            }
        }
    }

    private void linkLast(Task task) {
        Node newNode = new Node(task);
        if (head == null) {
            head = newNode;
        } else {
            tail.setNext(newNode);
            newNode.setLast(tail);
        }
        tail = newNode;
        historyMap.put(task.getId(), newNode);
    }

    private void removeNode(int id) {
        Node removeNode = historyMap.remove(id);
        if (removeNode != null) {
            Node last = removeNode.getLast();
            Node next = removeNode.getNext();
            last.setNext(next);
            tail = last;
        }
    }
}
