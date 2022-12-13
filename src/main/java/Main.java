import com.task1.Task1;
import com.task2.BuildException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws JAXBException, IOException, BuildException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        Task1.doCalculateJsonToXml(new File("src/main/resources/json_fines"));
        Task1.doCalculateXmlToJson(new File("src/main/resources/xml_fines"));
    }
}
