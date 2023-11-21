package validator;

import java.io.File;
import java.io.IOException;
import org.xml.sax.SAXException;
import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.transform.stream.StreamSource;

public class DepartmentValidator {
    private static final String SCHEMA = "resources/department.xsd";

    public static boolean validate(String path) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(SCHEMA));

            javax.xml.validation.Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(path)));

            return true;
        } catch (SAXException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}