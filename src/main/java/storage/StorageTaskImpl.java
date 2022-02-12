package storage;

import interfaces.TaskStorage;
import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.*;
import java.util.stream.Collectors;

public class StorageTaskImpl implements TaskStorage {
    private Integer idTask;
    private final Map<Integer, Task> storage;
    private final List<Integer> noUseId;

    public StorageTaskImpl() {
        idTask = 0;
        storage = new HashMap<>();
        noUseId = new ArrayList<>();
    }

    @Override
    public int createTask(Task task) {
        if (task != null) {
            Integer id = simpleGeneratorId();
            task.setStatus(Status.NEW);
            task.setId(id);
            this.storage.put(id, task);
            boolean isContains = this.storage.containsKey(id) && this.storage.containsValue(task);
            if (isContains) {
                return id;
            }
        }
        return 0;
    }

    @Override
    public int createTask(SubTask subTask) {
        if (subTask != null && subTask.getEpicTask() != null) {
            EpicTask epicTask = subTask.getEpicTask();
            List<EpicTask> listEpicTask = getListEpicTask();
            if (listEpicTask.contains(epicTask)) {
                int id = simpleGeneratorId();
                subTask.setId(id);
                subTask.setStatus(Status.NEW);
                epicTask.getSubTaskList().add(subTask);
                return id;
            } else {
                return -1;
            }
        } else {
            return 0;
        }
    }

    @Override
    public Task getTaskOnId(Integer id) {
        if (id != null) {
            return this.storage.values().stream()
                    .filter(task -> task.getId().equals(id))
                    .findFirst().orElseGet(() -> storage.values().stream()
                            .filter(task -> task instanceof EpicTask)
                            .map(task -> ((EpicTask) task))
                            .map(EpicTask::getSubTaskList)
                            .flatMap(subTasksList -> subTasksList.stream()
                                    .filter(subTask -> subTask.getId().equals(id)))
                            .findFirst().orElse(null));
        }
        return null;
    }

    @Override
    public boolean updateTask(Task task) {
        if (task != null) {
            Task taskFromStorage = this.storage.entrySet()
                    .stream()
                    .filter(i -> i.getKey().equals(task.getId()))
                    .map(Map.Entry::getValue)
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("Element don't find out"));
            taskFromStorage.setName(task.getName());
            taskFromStorage.setDescriptions(task.getDescriptions());
            taskFromStorage.setStatus(task.getStatus());
            return taskFromStorage.equals(task);
        }
        return false;
    }

    @Override
    public boolean updateTask(EpicTask epicTask) {
        if (epicTask != null) {
            EpicTask taskFromStorage = (EpicTask) this.storage.entrySet()
                    .stream()
                    .filter(i -> i.getKey().equals(epicTask.getId()))
                    .map(Map.Entry::getValue)
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("Element don't find out"));
            taskFromStorage.setName(epicTask.getName());
            taskFromStorage.setDescriptions(epicTask.getDescriptions());
            epicTask.setStatus(taskFromStorage.getStatus());
            return taskFromStorage.equals(epicTask);
        }
        return false;
    }

    @Override
    public boolean updateTask(SubTask subTask) {
        if (subTask != null) {
            SubTask subTaskFromList = (SubTask) getListTask().stream()
                    .filter(i -> i.getId().equals(subTask.getId()))
                    .findFirst().orElseThrow(NoSuchElementException::new);
            subTaskFromList.setName(subTask.getName());
            subTaskFromList.setDescriptions(subTask.getDescriptions());
            subTaskFromList.setStatus(subTask.getStatus());
            SubTask subTaskUpd = (SubTask) getListTask().stream()
                    .filter(i -> i.getId().equals(subTask.getId()))
                    .findFirst().orElseThrow(NoSuchElementException::new);
            return subTaskUpd.equals(subTask);
        }
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        if (id != null) {
            boolean containsKey = this.storage.containsKey(id);
            if (containsKey) {
                Task task = this.storage.get(id);
                if (task instanceof EpicTask) {
                    EpicTask epicTask = (EpicTask) task;
                    List<SubTask> subTaskList = epicTask.getSubTaskList();
                    subTaskList.forEach(i -> this.noUseId.add(i.getId()));
                    subTaskList.clear();
                    storage.remove(epicTask.getId());
                    noUseId.add(id);
                    return true;
                } else {
                    storage.remove(id);
                    this.noUseId.add(id);
                    return true;
                }
            } else {
                List<EpicTask> listEpicTask = getListEpicTask();
                boolean removeIf = false;
                for (EpicTask epicTask : listEpicTask) {
                    List<SubTask> subTaskList = epicTask.getSubTaskList();
                    for (SubTask subTask : subTaskList) {
                        if (subTask.getId().equals(id)) {
                            subTaskList.remove(subTask);
                            removeIf = true;
                            break;
                        }
                    }
                }
                if (removeIf) {
                    this.noUseId.add(id);
                }
                return removeIf;
            }
        }
        return false;
    }

    @Override
    public boolean deleteAll() {
        this.idTask = 0;
        this.noUseId.clear();
        this.storage.clear();
        return true;
    }

    @Override
    public List<Task> getListTask() {
        List<Task> collect = new ArrayList<>(this.storage.values());
        this.storage.values()
                .stream()
                .filter(task -> task instanceof EpicTask)
                .map(task -> (EpicTask) task)
                .forEach(task -> collect.addAll(task.getSubTaskList()));
        return collect;
    }

    @Override
    public List<EpicTask> getListEpicTask() {
        return this.storage.values()
                .stream()
                .filter(task -> task instanceof EpicTask)
                .map(task -> (EpicTask) task)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubTask> getSubTaskListDefiniteEpicTask(EpicTask task) {
        return this.storage.values()
                .stream().filter(ts -> ts instanceof EpicTask)
                .map(ts -> (EpicTask) ts)
                .filter(ts -> ts.getName().equals(task.getName()))
                .map(EpicTask::getSubTaskList)
                .findFirst().orElseThrow(() -> new NoSuchElementException("Epic don't find out"));
    }

    @Override
    public List<SubTask> getAllSubTask() {
        List<SubTask> subTaskList = new ArrayList<>();
        this.storage.values()
                .stream()
                .filter(task -> task instanceof EpicTask)
                .map(task -> (EpicTask) task)
                .forEach(task -> subTaskList.addAll(task.getSubTaskList()));
        return subTaskList;
    }

    private Integer simpleGeneratorId() {
        if (!this.noUseId.isEmpty()) {
            int i = noUseId.size() - 1;
            noUseId.remove(noUseId.size() - 1);
            return noUseId.get(i);
        } else {
            return ++this.idTask;
        }
    }

    public Integer getIdTask() {
        return idTask;
    }

    public Map<Integer, Task> getStorage() {
        return storage;
    }

    public List<Integer> getNoUseId() {
        return noUseId;
    }
}
