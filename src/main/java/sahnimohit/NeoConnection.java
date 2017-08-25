package sahnimohit;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;

/**
 * Created by mohitsahni on 21/7/17.
 */
public class NeoConnection {
    private String uri="bolt://192.168.100.215:7687";
    private String user="neo4j";
    private String passwd="Qwerty@321";

    protected Driver driver;

    public void connectToNeo(){
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, passwd));

        System.out.println("Driver is : "+driver);
    }

    public void closeTheNeo(){
        driver.close();
    }
}
