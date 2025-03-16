import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class JAXPTransformer {
    public static void main(String[] args) {
        try {
            File xmlFile = new File("kirill_example.xml");
            File xsltFile = new File("description.xslt");
            File outputFile = new File("kirill_xslt_example.html");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true); 
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            Document document = builder.parse(xmlFile);
            DOMSource domSource = new DOMSource(document);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer(new StreamSource(xsltFile));

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            OutputStream outputStream = new FileOutputStream(outputFile);
            StreamResult result = new StreamResult(outputStream);
            transformer.transform(domSource, result);

            outputStream.close();

            System.out.println("Transformation completed successfully");

        } catch (Exception e) {
            System.err.println("Error during transformation: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
