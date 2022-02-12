package storage;

import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class StorageTaskImplTest {
    private StorageTaskImpl storageTask;
    private final Task taskOne = new Task("task", "Task for test");
    private final Task taskOneUpd = new Task(1, "task upd", "Task for test upd", Status.IN_PROGRESS);
    private final EpicTask firstEpic = new EpicTask("Epic", "new epic task");
    private final SubTask firstSubTask = new SubTask("sub task one", "First task epic first", firstEpic);
    private final SubTask twoSubTask = new SubTask("sub task two", "Two task epic first", firstEpic);
    private final SubTask treeSubTask = new SubTask("sub task three", "Three task epic first", firstEpic);

    private final EpicTask updateEpicTaskFirst = new EpicTask(2, "Updated epic first", "Updated epic first");
    private final SubTask subTaskUpdate = new SubTask(5, "Update task on id 5", "updateswr", Status.IN_PROGRESS, firstEpic);

    private final EpicTask twoEpic = new EpicTask("Epic 2", "new epic task 2");
    private final SubTask firstSubTasTwoEpic = new SubTask("sub task one ep2", "First task epic two", twoEpic);
    private final SubTask twoSubTaskTwoEpic = new SubTask("sub task two ep2", "Two task epic two", twoEpic);
    private final SubTask treeSubTaskTwoEpic = new SubTask("sub task three ep2", "Three task epic two", twoEpic);

    List<Task> testList = List.of(
            taskOne, firstEpic, firstSubTask, twoSubTask, treeSubTask,
            twoEpic, firstSubTasTwoEpic, twoSubTaskTwoEpic, treeSubTaskTwoEpic
    );

    void fillStorage(List<Task> testList) {
        for (Task task : testList) {
            if (task instanceof SubTask) {
                storageTask.createTask((SubTask) task);
            } else {
                storageTask.createTask(task);
            }
        }
    }

    @BeforeEach
    void initStorage() {
        this.storageTask = new StorageTaskImpl();
    }

    @AfterEach
    void clearStorage() {
        this.storageTask = null;
    }

    @Test
    void testAddTask() {
        int task = storageTask.createTask(taskOne);
        Map<Integer, Task> storage = storageTask.getStorage();

        assertAll(
                () -> assertThat(storage).containsValues(taskOne),
                () -> assertThat(taskOne.getId()).isEqualTo(task)
        );
    }

    @Test
    void testDeleteOnId() {
        storageTask.createTask(taskOne);

        boolean delete = storageTask.delete(1);
        Map<Integer, Task> storage = storageTask.getStorage();
        List<Integer> noUseId = storageTask.getNoUseId();

        assertAll(
                () -> assertThat(storage).doesNotContainValue(taskOne),
                () -> assertThat(delete).isTrue(),
                () -> assertThat(noUseId).contains(1)
        );
    }

    @Test
    void testUpdateTask() {
        storageTask.createTask(taskOne);
        boolean isUpdate = storageTask.updateTask(taskOneUpd);
        Task taskOnID = storageTask.getTaskOnId(taskOneUpd.getId());

        assertAll(
                () -> assertThat(isUpdate).isTrue(),
                () -> assertThat(taskOnID).isEqualTo(taskOne)
        );
    }

    @Test
    void testReadTask() {
        Map<Integer, Task> storage = storageTask.getStorage();
        storage.put(1, new Task(1, "written task", "written task hands", Status.IN_PROGRESS));
        Task taskOnID = storageTask.getTaskOnId(1);

        assertThat(taskOnID).isEqualTo(
                new Task(1, "written task", "written task hands", Status.IN_PROGRESS)
        );
    }

    @Test
    void testAddEpic() {
        int epicTaskId = storageTask.createTask(firstEpic);
        Map<Integer, Task> storage = storageTask.getStorage();
        assertAll(
                () -> assertThat(epicTaskId).isEqualTo(firstEpic.getId()),
                () -> assertThat(firstEpic.getId()).isEqualTo(1),
                () -> assertThat(storage).containsValues(firstEpic)
        );
    }

    @Test
    void testAddEpicTaskAndHisSubTask() {
        int idFirstEpicTask = storageTask.createTask(firstEpic);
        int idFirstSubTask = storageTask.createTask(firstSubTask);
        int idTwoSubTask = storageTask.createTask(twoSubTask);
        int idTreeSubTask = storageTask.createTask(treeSubTask);

        EpicTask epFromBase = (EpicTask) storageTask.getTaskOnId(1);
        List<SubTask> subTaskList = epFromBase.getSubTaskList();

        assertAll(
                () -> assertThat(idFirstEpicTask).isEqualTo(1),
                () -> assertThat(idFirstSubTask).isEqualTo(2),
                () -> assertThat(idTwoSubTask).isEqualTo(3),
                () -> assertThat(idTreeSubTask).isEqualTo(4),
                () -> assertThat(subTaskList).hasSize(3),
                () -> assertThat(subTaskList).contains(firstSubTask),
                () -> assertThat(subTaskList).contains(treeSubTask),
                () -> assertThat(subTaskList).contains(treeSubTask)
        );
    }

    @Test
    void testAddSubTaskForFirstEpicTaskWithoutFirstEpicTask() {
        int firstSubTaskId = storageTask.createTask(firstSubTask);
        assertThat(firstSubTaskId).isEqualTo(-1);
    }

    @Test
    void testOnDeleteOnId() {
        fillStorage(testList);
        boolean deleteFistTask = storageTask.delete(1);
        Map<Integer, Task> storage = storageTask.getStorage();

        assertAll(
                () -> assertThat(storage).doesNotContainKey(1),
                () -> assertThat(storage).doesNotContainValue(taskOne),
                () -> assertThat(deleteFistTask).isTrue()
        );
    }

    @Test
    void testRemoveAll() {
        fillStorage(testList);
        boolean isAllDelete = storageTask.deleteAll();
        Map<Integer, Task> storage = storageTask.getStorage();

        assertAll(
                () -> assertThat(isAllDelete).isTrue(),
                () -> assertThat(storage).isEmpty()
        );
    }

    @Test
    void testGetTaskOnIdExpectNull() {
        Task taskOnID = storageTask.getTaskOnId(1);
        assertThat(taskOnID).isNull();
    }

    @Test
    void testUpdateEpicTask() {
        fillStorage(testList);
        boolean isUpdate = storageTask.updateTask(updateEpicTaskFirst);
        EpicTask updatedEpicTask = (EpicTask) storageTask.getTaskOnId(2);
        //   System.out.println(updatedEpicTask);

        assertAll(
                () -> assertThat(isUpdate).isTrue(),
                () -> assertThat(updatedEpicTask).isEqualTo(updateEpicTaskFirst)
        );
    }

    @Test
    void testUpdateSubTask() {
        fillStorage(testList);
        boolean isUpdate = storageTask.updateTask(subTaskUpdate);
        SubTask subTaskOnId = (SubTask) storageTask.getTaskOnId(5);

        assertAll(
                () -> assertThat(subTaskOnId).isEqualTo(subTaskOnId),
                () -> assertThat(isUpdate).isTrue()
        );
    }

    @Test
    void testGetAllTask() {
        fillStorage(testList);
        List<Task> listTask = storageTask.getListTask();

        assertAll(
                () -> assertThat(listTask).hasSize(9),
                () -> assertThat(listTask).contains(firstEpic),
                () -> assertThat(listTask).contains(twoSubTaskTwoEpic)
        );
    }

    @Test
    void testGetEpicTaskList() {
        fillStorage(testList);
        List<EpicTask> listEpicTask = storageTask.getListEpicTask();
        assertThat(listEpicTask).hasSize(2);
    }

    @Test
    void testCheckNoUseId() {
        fillStorage(testList);
        boolean deleteOnId1 = storageTask.delete(1);
        boolean deleteOnId2 = storageTask.delete(6);
        List<Integer> noUseId = storageTask.getNoUseId();
        assertAll(
                () -> assertThat(deleteOnId1).isTrue(),
                () -> assertThat(deleteOnId2).isTrue(),
                () -> assertThat(noUseId).isNotEmpty(),
                () -> assertThat(noUseId).hasSize(5),
                () -> assertThat(noUseId).contains(1),
                () -> assertThat(noUseId).contains(6),
                () -> assertThat(noUseId).contains(7),
                () -> assertThat(noUseId).contains(8),
                () -> assertThat(noUseId).contains(9)
        );
    }

    @Test
    void testDeleteOnIdSubTask() {
        fillStorage(testList);
        boolean delete = storageTask.delete(7);
        Task taskOnID = storageTask.getTaskOnId(7);
        List<Task> listTask = storageTask.getListTask();
        List<SubTask> subTaskListDefiniteEpicTask = storageTask.getSubTaskListDefiniteEpicTask(twoEpic);

        assertAll(
                () -> assertThat(delete).isTrue(),
                () -> assertThat(taskOnID).isNull(),
                () -> assertThat(listTask).doesNotContain(firstSubTasTwoEpic),
                () -> assertThat(listTask).hasSize(8),
                () -> assertThat(subTaskListDefiniteEpicTask).hasSize(2),
                () -> assertThat(subTaskListDefiniteEpicTask).doesNotContain(firstSubTasTwoEpic)
        );
    }

    @Test
    void testGetListAllSubTasks() {
        fillStorage(testList);
        List<SubTask> subTaskList = storageTask.getAllSubTask();

        assertAll(
                () -> assertThat(subTaskList).hasSize(6),
                () -> assertThat(subTaskList).contains(firstSubTask),
                () -> assertThat(subTaskList).contains(twoSubTask),
                () -> assertThat(subTaskList).contains(treeSubTask),
                () -> assertThat(subTaskList).contains(firstSubTasTwoEpic),
                () -> assertThat(subTaskList).contains(twoSubTaskTwoEpic),
                () -> assertThat(subTaskList).contains(treeSubTaskTwoEpic)
        );
    }
}