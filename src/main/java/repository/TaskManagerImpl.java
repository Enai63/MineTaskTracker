package repository;

import informer.Informant;
import interfaces.HistoryManager;
import interfaces.TaskManager;
import interfaces.TaskStorage;
import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;
import storage.StorageTaskImpl;

import java.util.List;

public class TaskManagerImpl implements TaskManager {
    private TaskStorage taskStorage;
    private final Informant informant;
    private final HistoryManager historyManager;

    public TaskManagerImpl() {
        taskStorage = new StorageTaskImpl();
        informant = new Informant();
        historyManager = new HistoryManagerImpl();
    }

    /*
       Get all tasks
    */
    @Override
    public List<Task> getAllTasks() {
        List<Task> listTask = taskStorage.getListTask();
        informant.whenAction(!listTask.isEmpty());
        return listTask;
    }

    /*
       Get all epic tasks
    */
    @Override
    public List<EpicTask> getEpicTaskList() {
        List<EpicTask> listEpicTask = taskStorage.getListEpicTask();
        informant.whenAction(!listEpicTask.isEmpty());
        return listEpicTask;
    }
    @Override
    public List<SubTask> getAllSubTask() {
        List<SubTask> allSubTask = taskStorage.getAllSubTask();
        informant.whenAction(!allSubTask.isEmpty());
        return allSubTask;
    }

    /*
    Get all sub-task definite on epic
     */
    @Override
    public List<SubTask> getSubTaskListOnEpic(EpicTask epicTask) {
        List<SubTask> subTaskListDefiniteEpicTask = taskStorage.getSubTaskListDefiniteEpicTask(epicTask);
        informant.whenAction(!subTaskListDefiniteEpicTask.isEmpty());
        return subTaskListDefiniteEpicTask;
    }

    /*
    Get task on id
     */
    @Override
    public Task getTaskOnId(Integer id) {
        if (id != null) {
            Task task = null;
            try {
                task = taskStorage.getTaskOnId(id);
            } catch (NullPointerException e) {
                informant.whenAction(false);
            }
            informant.whenAction(task != null);
            historyManager.addTaskInHistory(task);
            return task;
        }
        return null;
    }

    /*
    Add task
    */
    @Override
    public void addTask(Task task) {
        int taskId = taskStorage.createTask(task);
        informant.addIsSuccess(taskId);
    }

    /*
    Add epic-task
     */
    @Override
    public void addEpicTask(EpicTask epicTask) {
        int taskId = taskStorage.createTask(epicTask);
        informant.addIsSuccess(taskId);
    }

    /*
    Add sub-task
     */
    @Override
    public void addSubTask(SubTask subTask) {
        int taskId = taskStorage.createTask(subTask);
        informant.addIsSuccess(taskId);
    }

    /*
    Update task
     */
    public void updateTask(Task task) {
        boolean flagUpd;
        if (task instanceof EpicTask) {
            flagUpd = taskStorage.updateTask((EpicTask) task);
        } else if (task instanceof SubTask) {
            flagUpd = taskStorage.updateTask((SubTask) task);
        } else {
            flagUpd = taskStorage.updateTask(task);
        }
        informant.whenAction(flagUpd);
        taskStorage.getListEpicTask().forEach(this::updateStatusInEpicTasks);
    }

    /*
    Remove task on id
     */
    @Override
    public void remove(Integer taskId) {
        boolean taskIsDelete = taskStorage.delete(taskId);
        informant.whenAction(taskIsDelete);
        taskStorage.getListEpicTask().forEach(this::updateStatusInEpicTasks);
    }

    /*
    Remove all tasks
     */
    @Override
    public void removeAllTask() {
        boolean tasksIsClean = taskStorage.deleteAll();
        informant.whenAction(tasksIsClean);
    }

    /*
    Get history manager
     */
    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    private void updateStatusInEpicTasks(EpicTask epicTask) {
        int countStatusDone = 0;
        int countStatusNew = 0;
        List<SubTask> subTaskList = epicTask.getSubTaskList();
        if (subTaskList.isEmpty()) {
            epicTask.setStatus(Status.NEW);
        } else {
            for (SubTask subTask : subTaskList) {
                if (subTask.getStatus().equals(Status.NEW)) {
                    countStatusNew++;
                } else if (subTask.getStatus().equals(Status.DONE)) {
                    countStatusDone++;
                }
            }
            if (countStatusDone == subTaskList.size()) {
                epicTask.setStatus(Status.DONE);
            } else if (countStatusNew == subTaskList.size()) {
                epicTask.setStatus(Status.NEW);
            } else {
                epicTask.setStatus(Status.IN_PROGRESS);
            }
        }
    }

    public TaskStorage getTaskStorage() {
        return taskStorage;
    }

    public void setTaskStorage(TaskStorage taskStorage) {
        this.taskStorage = taskStorage;
    }
}
