package sahnimohit;



import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

/**
 * Created by mohitsahni on 17/7/17.
 */
public class Main {
    public static void main(String[] args) {
        String name = "Main";
        final Logger log = Logger.getLogger(Main.class.getName());
        ScrapeInit sc = new ScrapeInit();
        sc.initiateScraper();
    }
}
