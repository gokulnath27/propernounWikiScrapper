import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.ArrayCoreMap;
import edu.stanford.nlp.util.CoreMap;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ImportantWordScrapper<C extends ArrayCoreMap> {

    public Map<String,String> scrapper(List<String> derivedWords) throws IOException, ParserConfigurationException, SAXException {
        String properNounWord = "";
        int properNounMatcherFlag = 0;
        Map<String,String> finalScrappedData = new HashMap<String, String>();
        Properties stanfordCoreNLPProperties = new Properties();
        stanfordCoreNLPProperties.setProperty("annotators", "tokenize, ssplit, pos");
        stanfordCoreNLPProperties.setProperty("tokenize.language", "en");
        WikipediaMessageScrapper message = new WikipediaMessageScrapper();
        StanfordCoreNLP pipeline = new StanfordCoreNLP(stanfordCoreNLPProperties);
        for (String eachWord : derivedWords) {
            Annotation document = new Annotation(eachWord);
            pipeline.annotate(document);
            CoreLabel coreLabel = CoreLabel.wordFromString(eachWord);
            coreLabel.get(CoreAnnotations.PartOfSpeechAnnotation.class);
            List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
            for (CoreMap sentence : sentences) {
                for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                    String word = token.get(CoreAnnotations.TextAnnotation.class);
                    String posTag = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                    if(posTag.trim().equals("NNP")){
                        if(properNounMatcherFlag == 1)
                        properNounWord = properNounWord + "_" + word;
                        else
                        properNounWord = properNounWord + word;
                        properNounMatcherFlag = 1;
                    }
                    else{
                        properNounMatcherFlag = 0;
                        if(!properNounWord.equals("")){
                           String wikiScrappedMessage = message.wikiMessage(properNounWord);
                            finalScrappedData.put(properNounWord,wikiScrappedMessage);
                            properNounWord = "";
                        }
                    }
                }
                if (!properNounWord.equals("")) {
                        String wikiScrappedMessage = message.wikiMessage(properNounWord);
                        finalScrappedData.put(properNounWord, wikiScrappedMessage);
                        properNounWord = "";
                }
            }
        }
        return  finalScrappedData;
    }
}

