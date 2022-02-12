package informer;

public class Informant {
    private static final String TASK = "Задача";
    public static final String ID = " с id: ";
    private static final String ADD = " добавлена.";
    private static final String NO_ADD = " не добавлена";
    private static final String ACTION_SUCCESS = "Действие успешно";
    private static final String ACTION_UNSUCCESSFUL = "Действие не успешно";

    public void whenAction(boolean flag) {
        if (flag) {
            System.out.println(ACTION_SUCCESS);
        } else {
            System.out.println(ACTION_UNSUCCESSFUL);
        }
    }

    public void addIsSuccess(int id) {
        if (id > 0) {
            System.out.println(TASK + ID + id + ADD);
        } else {
            System.out.println(TASK + NO_ADD);
        }
    }
}
