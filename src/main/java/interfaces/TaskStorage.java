package interfaces;

import model.EpicTask;
import model.SubTask;
import model.Task;

import java.util.List;

public interface TaskStorage {
    int createTask(Task task);

    int createTask(SubTask subTask);

    Task getTaskOnId(Integer id);

    boolean updateTask(Task task);

    boolean updateTask(EpicTask epicTask);

    boolean updateTask(SubTask subTask);

    boolean delete(Integer id);

    boolean deleteAll();

    List<Task> getListTask();

    List<EpicTask> getListEpicTask();

    List<SubTask> getSubTaskListDefiniteEpicTask(EpicTask task);

    List<SubTask> getAllSubTask();
}
