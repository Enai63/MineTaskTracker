package repository;

import interfaces.HistoryManager;
import interfaces.TaskStorage;
import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TaskManagerImplTest {

    private TaskManagerImpl manager;
    private HistoryManager historyManager;

    Task taskOne = new Task("first", "first simple task");
    Task taskTwo = new Task("two", "two simple task");
    Task taskThree = new Task("three", "three simple task");
    Task taskFour = new Task("five", "five simple task");

    Task taskUpdThree = new Task(3, "three upd", "three upd task", Status.IN_PROGRESS);

    EpicTask epicTaskOne = new EpicTask("EPIC 1", "First epic");
    EpicTask epicTaskTwo = new EpicTask("EPIC 2", "Two epic");
    EpicTask epicTaskThree = new EpicTask("EPIC 3", "Three epic");

    EpicTask epicTaskFour = new EpicTask(6, "EPIC UPD", "Epic for upd");

    EpicTask epicTaskTestFetList = new EpicTask("Epic 4", "Four epic add");

    SubTask subTaskOne = new SubTask("first sub", "sub for epic 1", epicTaskOne);
    SubTask subTaskTwo = new SubTask("two sub", "sub for epic 1", epicTaskOne);
    SubTask subTaskThree = new SubTask("three sub", "sub for epic 1", epicTaskOne);
    SubTask subTaskFour = new SubTask("four sub", "sub for epic 1", epicTaskOne);

    SubTask subTaskOneUpdInProgress = new SubTask(8, "four sub", "sub for epic 1", Status.IN_PROGRESS,
            epicTaskOne);
    SubTask subTaskOneUpd = new SubTask(8, "four sub", "sub for epic 1", Status.DONE, epicTaskOne);
    SubTask subTaskTwoUpd = new SubTask(9, "four sub", "sub for epic 1", Status.DONE, epicTaskOne);
    SubTask subTaskThreeUpd = new SubTask(10, "four sub", "sub for epic 1", Status.DONE, epicTaskOne);
    SubTask subTaskFourUpd = new SubTask(11, "four sub", "sub for epic 1", Status.DONE, epicTaskOne);


    SubTask subTaskFive = new SubTask("five sub", "sub for epic 2", epicTaskTwo);
    SubTask subTaskSix = new SubTask("six sub", "sub for epic 2", epicTaskTwo);
    SubTask subTaskSeven = new SubTask("seven sub", "sub for epic 2", epicTaskTwo);
    SubTask subTaskEight = new SubTask("eight sub", "sub for epic 2", epicTaskTwo);

    SubTask subTaskNine = new SubTask("nine sub", "sub for epic 3", epicTaskThree);
    SubTask subTaskTen = new SubTask("ten sub", "sub for epic 3", epicTaskThree);
    SubTask subTaskEleven = new SubTask("eleven sub", "sub for epic 3", epicTaskThree);
    SubTask subTaskTwelve = new SubTask("twelve sub", "sub for epic 3", epicTaskThree);

    SubTask subTaskThirteen = new SubTask(8, "thirteen sub upd", "sub task upd",
            Status.IN_PROGRESS, epicTaskOne);

    List<Task> testList = List.of(
            taskOne, taskTwo, taskThree, taskFour, epicTaskOne, epicTaskTwo, epicTaskThree, subTaskOne, subTaskTwo,
            subTaskThree, subTaskFour, subTaskFive, subTaskSix, subTaskSeven, subTaskEight, subTaskNine, subTaskTen,
            subTaskEleven, subTaskTwelve
    );

    Task simpleTaskAdd = new Task("new task", "test add task");
    EpicTask epicTaskNewAdd = new EpicTask("New epic", "Test add epic task");
    SubTask subTaskNewAddOne = new SubTask("new sub task 1", "add task 1 for new epic", epicTaskNewAdd);
    SubTask subTaskNewAddTwo = new SubTask("new sub task 2", "add task 2 for new epic", epicTaskNewAdd);
    SubTask subTaskNewAddThree = new SubTask("new sub task 3", "add task 3 for new epic", epicTaskNewAdd);

    @BeforeEach
    void initTaskService() {
        manager = new TaskManagerImpl();
        historyManager = manager.getHistoryManager();
        TaskStorage taskService = manager.getTaskStorage();
        for (Task task : testList) {
            if (task instanceof SubTask) {
                taskService.createTask((SubTask) task);
            } else {
                taskService.createTask(task);
            }
        }
        manager.setTaskStorage(taskService);
    }

    @AfterEach
    void tearDown() {
        this.manager = null;
    }

    @Test
    void testGetTaskOnID() {
        Task simpleTaskOneFindID = manager.getTaskOnId(1);
        Task simpleTaskThreeFinID = manager.getTaskOnId(3);
        EpicTask epicTaskTwoOnId = (EpicTask) manager.getTaskOnId(6);
        SubTask subTaskOneOnId = (SubTask) manager.getTaskOnId(8);
        SubTask subTaskTwoOnId = (SubTask) manager.getTaskOnId(14);
        SubTask subTaskThreeOnId = (SubTask) manager.getTaskOnId(17);


        assertAll(
                () -> assertThat(simpleTaskOneFindID).isNotNull(),
                () -> assertThat(simpleTaskOneFindID.getName()).isEqualTo(taskOne.getName()),
                () -> assertThat(simpleTaskOneFindID.getStatus()).isEqualTo(Status.NEW),

                () -> assertThat(simpleTaskThreeFinID).isNotNull(),
                () -> assertThat(simpleTaskThreeFinID.getStatus()).isEqualTo(Status.NEW),
                () -> assertThat(simpleTaskThreeFinID.getName()).isEqualTo("three"),

                () -> assertThat(epicTaskTwoOnId).isNotNull(),
                () -> assertThat(epicTaskTwoOnId.getName()).isEqualTo(epicTaskTwo.getName()),

                () -> assertThat(subTaskOneOnId).isNotNull(),
                () -> assertThat(subTaskOneOnId.getDescriptions()).isEqualTo("sub for epic 1"),

                () -> assertThat(subTaskTwoOnId).isNotNull(),
                () -> assertThat(subTaskTwoOnId.getEpicTask()).isEqualTo(epicTaskTwo),

                () -> assertThat(subTaskThreeOnId).isNotNull(),
                () -> assertThat(subTaskThreeOnId.getId()).isEqualTo(17)
        );
    }

    @Test
    void testAddNewTask() {
        manager.addTask(simpleTaskAdd);
        manager.addEpicTask(epicTaskNewAdd);
        manager.addSubTask(subTaskNewAddOne);
        manager.addSubTask(subTaskNewAddTwo);
        manager.addSubTask(subTaskNewAddThree);

        Task taskOnId20 = manager.getTaskOnId(20);
        EpicTask epicTaskWithId21 = (EpicTask) manager.getTaskOnId(21);

        SubTask subTask22 = (SubTask) manager.getTaskOnId(22);
        SubTask subTask23 = (SubTask) manager.getTaskOnId(23);
        SubTask subTask24 = (SubTask) manager.getTaskOnId(24);

        List<SubTask> subTaskList = epicTaskWithId21.getSubTaskList();


        assertAll(
                () -> assertThat(taskOnId20).isNotNull(),
                () -> assertThat(taskOnId20.getName()).isEqualTo(simpleTaskAdd.getName()),
                () -> assertThat(taskOnId20.getStatus()).isEqualTo(Status.NEW),

                () -> assertThat(epicTaskWithId21).isNotNull(),
                () -> assertThat(epicTaskWithId21.getName()).isEqualTo(epicTaskNewAdd.getName()),
                () -> assertThat(epicTaskWithId21.getStatus()).isEqualTo(Status.NEW),

                () -> assertThat(subTaskList).isNotNull(),
                () -> assertThat(subTaskList.size()).isEqualTo(3),
                () -> assertThat(subTask22).isNotNull(),
                () -> assertThat(subTask23.getEpicTask()).isEqualTo(subTaskNewAddTwo.getEpicTask()),
                () -> assertThat(subTask24.getStatus()).isEqualTo(Status.NEW)
        );
    }

    @Test
    void testUpdateTask() {
        manager.updateTask(taskUpdThree);
        manager.updateTask(epicTaskFour);
        manager.updateTask(subTaskThirteen);


        Task taskThreeOnId = manager.getTaskOnId(3);
        EpicTask epicTaskOnId6 = (EpicTask) manager.getTaskOnId(6);
        SubTask subTaskOnId8 = (SubTask) manager.getTaskOnId(8);


        assertAll(
                () -> assertThat(taskThreeOnId).isEqualTo(taskThree),
                () -> assertThat(epicTaskOnId6).isEqualTo(epicTaskFour),
                () -> assertThat(subTaskOnId8).isEqualTo(subTaskOne)
        );
    }

    @Test
    void testRemoveTaskOnId() {
        manager.remove(2);
        List<Task> allTasks = manager.getAllTasks();
        manager.remove(6);
        List<Task> taskList = manager.getAllTasks();
        manager.remove(16);
        List<Task> taskListAfterDeleteSubTask = manager.getAllTasks();

        assertAll(
                () -> assertThat(allTasks.size()).isEqualTo(18),
                () -> assertThat(taskList.size()).isEqualTo(13),
                () -> assertThat(taskListAfterDeleteSubTask.size()).isEqualTo(12)
        );
    }

    @Test
    void testRemoveAllTask() {
        manager.removeAllTask();
        List<EpicTask> epicTaskList = manager.getEpicTaskList();

        assertThat(epicTaskList.isEmpty()).isTrue();
    }

    @Test
    void testCheckOnGetEpicTaskList() {
        manager.addTask(epicTaskTestFetList);
        List<EpicTask> epicTaskList = manager.getEpicTaskList();
        assertThat(epicTaskList.size()).isEqualTo(4);
    }

    @Test
    void testCheckSizeSubTaskOnEpic() {
        List<SubTask> subTaskListOnEpic = manager.getSubTaskListOnEpic(epicTaskTwo);
        assertThat(subTaskListOnEpic.size()).isEqualTo(4);
    }

    @Test
    void testHowUpdatesStatusEpicTaskExpectInProgress() {
        manager.updateTask(subTaskOneUpdInProgress);
        EpicTask epicTask = (EpicTask) manager.getTaskOnId(5);
        assertThat(epicTask.getStatus()).isEqualTo(Status.IN_PROGRESS);
    }

    @Test
    void testHowUpdateStatusEpicTaskInDone() {
        manager.updateTask(subTaskOneUpd);
        manager.updateTask(subTaskTwoUpd);
        manager.updateTask(subTaskThreeUpd);
        manager.updateTask(subTaskFourUpd);

        EpicTask epicTask = (EpicTask) manager.getTaskOnId(5);

        assertThat(epicTask.getStatus()).isEqualTo(Status.DONE);
    }

    @Test
    void testGetAllSubTaskList() {
        List<SubTask> subTaskList = manager.getAllSubTask();

        assertAll(
                () -> assertThat(subTaskList.isEmpty()).isFalse(),
                () -> assertThat(subTaskList.size()).isEqualTo(12),
                () -> assertThat(subTaskList.contains(subTaskThree)).isTrue(),
                () -> assertThat(subTaskList.contains(subTaskFour)).isTrue(),
                () -> assertThat(subTaskList.contains(subTaskSix)).isTrue(),
                () -> assertThat(subTaskList.contains(subTaskNine)).isTrue(),
                () -> assertThat(subTaskList.contains(subTaskTen)).isTrue()
        );
    }

    //Test history
    @Test
    void testHistoryManager() {
        Task taskOnId = manager.getTaskOnId(1);
        manager.getTaskOnId(2);
        manager.getTaskOnId(3);
        manager.getTaskOnId(4);
        manager.getTaskOnId(5);
        manager.getTaskOnId(6);
        Task taskOnId7 = manager.getTaskOnId(7);
        manager.getTaskOnId(8);
        manager.getTaskOnId(9);
        manager.getTaskOnId(10);
        manager.getTaskOnId(11);
        Task taskOnId12 = manager.getTaskOnId(12);
        manager.getTaskOnId(13);

        List<Task> historyTasks = manager.getHistoryManager().getHistoryTasks();

        assertAll(
                () -> assertThat(historyTasks.size()).isEqualTo(13),
                () -> assertThat(historyTasks.get(0)).isEqualTo(taskOnId),
                () -> assertThat(historyTasks.get(6)).isEqualTo(taskOnId7),
                () -> assertThat(historyTasks.get(11)).isEqualTo(taskOnId12)
        );
    }

    @Test
    void testOnRepeatTask() {
        manager.getTaskOnId(1);
        manager.getTaskOnId(2);
        manager.getTaskOnId(3);
        Task taskOnId3 = manager.getTaskOnId(3);
        manager.getTaskOnId(4);

        List<Task> historyTasks = manager.getHistoryManager().getHistoryTasks();

        assertAll(
                () -> assertThat(historyTasks.size()).isEqualTo(4),
                () -> assertThat(historyTasks.get(2)).isEqualTo(taskOnId3)
        );

    }

    @Test
    void testDeleteTaskFromHistory() {
        manager.getTaskOnId(1);
        manager.getTaskOnId(2);
        manager.getTaskOnId(3);
        Task taskOnId = manager.getTaskOnId(4);

        HistoryManager historyManager = manager.getHistoryManager();
        historyManager.remove(4);
        List<Task> historyTasks = historyManager.getHistoryTasks();

        assertAll(
                () -> assertThat(historyTasks.size()).isEqualTo(3),
                () -> assertThat(historyTasks.contains(taskOnId)).isFalse()
        );
    }
}