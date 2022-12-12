import com.task2.BuildException;
import com.task2.ExampleClass1;
import com.task2.ExampleClass2;
import com.task2.UtilClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import task2.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Task2Tests {
    @Test
    void testWhenExampleClassIncorrectFieldsNameAndCorrectAnnotationThenCreateNewObj() throws IOException, BuildException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        ExampleClass1 example = UtilClass.toBuild(ExampleClass1.class, Path.of("src/main/resources/properties1.properties"));

        Assertions.assertEquals("value1", example.getMyString());
        Assertions.assertEquals(10, example.getMyNumber());
        Assertions.assertEquals(LocalDateTime.parse("29.11.2022 18:30", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")), example.getMyTime());
    }
    @Test
    void testWhenExampleClassCorrectFieldNameAndHasNotNameAnnotationThenCreateNewObj() throws IOException, BuildException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        ExampleClass2 example = UtilClass.toBuild(ExampleClass2.class, Path.of("src/main/resources/properties2.properties"));

        Assertions.assertEquals("value1", example.getStringProperty());
        Assertions.assertEquals(10, example.getNumberProperty());
        Assertions.assertEquals(LocalDateTime.parse("29.11.2022 18:30:12", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")), example.getTimeProperty());
    }
}
