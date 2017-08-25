package sahnimohit;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import java.util.logging.Logger;

/**
 * Created by mohitsahni on 17/7/17.
 */
public class MongoConnection {
    private String name = "MongoConnection";
    private static final Logger log = Logger.getLogger(MongoConnection.class.getName());
    //MongoClientURI uri  = new MongoClientURI("mongodb://user:pass@host:port/db");
    //protected MongoClientURI uri  = new MongoClientURI("mongodb://project:Qwerty321@localhost:27017/project");
    protected MongoClientURI uri = new MongoClientURI("mongodb://192.168.100.215:27017/pokemonmeta");

    public MongoConnection() {
        //System.out.println("default constructor doesn't do anything ");;
    }
    public MongoConnection(MongoClientURI uri) {
        this.uri = uri;
    }

    public MongoClientURI getUri() {
        return uri;
    }

    public void setUri(MongoClientURI uri) {
        this.uri = uri;
    }

    protected MongoClient client = new MongoClient(uri);
    protected MongoDatabase db = client.getDatabase(uri.getDatabase());

    public void printDB ()
    {

        //System.out.println("Current Database is: "+db.getName());
        log.info("Current Database is:"+db.getName());
    }
}
