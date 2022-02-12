package utilities;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import repository.HistoryManagerImpl;
import repository.TaskManagerImpl;

public class Managers {
    public static TaskManager getDefault() {
        return new TaskManagerImpl();
    }

    public static HistoryManager getDefaultHistory() {
        return new HistoryManagerImpl();
    }
}
