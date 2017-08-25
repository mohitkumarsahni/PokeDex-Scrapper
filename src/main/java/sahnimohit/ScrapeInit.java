package sahnimohit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

/**
 * Created by mohitsahni on 18/7/17.
 */
public class ScrapeInit {
    private String name = "ScrapeInit";
    private static final Logger log = Logger.getLogger(ScrapeInit.class.getName());

    public void initiateScraper(){
        IntoLink inlink = new IntoLink();
        try {
            inlink.minioconnect();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        inlink.connect();

        String baseURL = "https://pokemondb.net/";
        log.info( "Initiating the Scraper..." );
        try {
            String url = "https://pokemondb.net";
            Document doc = Jsoup.connect("https://pokemondb.net/pokedex/national").get();
            log.info("Connected! Ready to Scrape...");
            //System.out.println("Base Url : "+url);
            String title = doc.title();
            //System.out.println("Title : "+ title);
            Elements ele = doc.getElementsByClass("infocard-tall");
            for(Element el : ele){
//                System.out.println("ID : "+el.getElementsByTag("small").first().ownText());
                String id = el.getElementsByTag("small").first().ownText().replace("#","") ;

//                System.out.println("Name : "+el.getElementsByClass("ent-name").text());
//                System.out.println("Type : "+el.getElementsByClass("itype").text());
                String link = el.getElementsByClass("pkg").attr("href");
//                System.out.println("Link : "+link);
                inlink.setLink(baseURL+link);
                inlink.setPoke_type(el.getElementsByClass("itype").text());
                inlink.setPoke_id(id);
                inlink.connectToLink();
                inlink.extractDataSaveData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
