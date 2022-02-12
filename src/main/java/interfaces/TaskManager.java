package interfaces;

import model.EpicTask;
import model.SubTask;
import model.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getAllTasks();

    List<EpicTask> getEpicTaskList();

    List<SubTask> getAllSubTask();

    List<SubTask> getSubTaskListOnEpic(EpicTask epicTask);

    Task getTaskOnId(Integer id);

    void addTask(Task task);

    void addEpicTask(EpicTask epicTask);

    void addSubTask(SubTask subTask);

    void updateTask(Task task);

    void remove(Integer taskId);

    void removeAllTask();

    HistoryManager getHistoryManager();
}
