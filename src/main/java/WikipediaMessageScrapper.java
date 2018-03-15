import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;


public class WikipediaMessageScrapper {
    public String wikiMessage(String wikiWord) throws IOException, ParserConfigurationException, SAXException {
        URL url = new URL("https://en.wikipedia.org/w/api.php?action=opensearch&search="+wikiWord+"&limit=1&format=xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(url.openStream());
        try {
            System.out.println(document.getElementsByTagName("Description").item(0).getTextContent());
        }
        catch (Exception exception){
            System.out.println("invalid query");
        }
        try {
            return document.getElementsByTagName("Description").item(0).getTextContent();
        }
        catch (Exception exception){
            return "invalid query";
        }
    }
}

