package interfaces;

import model.Task;

import java.util.List;

public interface HistoryManager {
    void addTaskInHistory(Task task);

    List<Task> getHistoryTasks();

    void remove(int id);
}
