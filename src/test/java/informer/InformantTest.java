package informer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class InformantTest {
    private Informant informer;
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        this.informer = new Informant();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        this.informer = null;
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    void testWhenActionTrue() {
        informer.whenAction(true);

        assertThat(outputStreamCaptor.toString().trim()).isEqualTo("Действие успешно");
    }

    @Test
    void testWhenActionFalse() {
        informer.whenAction(false);
        assertThat(outputStreamCaptor.toString().trim()).isEqualTo("Действие не успешно");
    }

    @Test
    void testSuccess() {
        informer.addIsSuccess(1);
        assertThat(outputStreamCaptor.toString().trim()).isEqualTo("Задача с id: 1 добавлена.");
    }

    @Test
    void testUnSuccess() {
        informer.addIsSuccess(-2);
        assertThat(outputStreamCaptor.toString().trim()).isEqualTo("Задача не добавлена");
    }
}