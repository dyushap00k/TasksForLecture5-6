import com.task2.BuildException;
import com.task2.ExampleClass1;
import com.task2.UtilClass;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws JAXBException, IOException, BuildException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
//        Task1.doCalculateJsonToXml(new File("src/main/resources/json_fines"));
//        Task1.doCalculateXmlToJson(new File("src/main/resources/xml_fines"));
        ExampleClass1 exampleClass1 = UtilClass.toBuild(ExampleClass1.class, Path.of("src/main/resources/properties1.properties"));
        System.out.println(exampleClass1);
    }
}
