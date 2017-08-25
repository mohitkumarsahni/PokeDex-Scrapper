package sahnimohit;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import java.util.logging.Logger;

/**
 * Created by mohitsahni on 17/7/17.
 */
public class CassandraConnection extends MongoConnection{
    private String name = "CassandraConnection";
    private static final Logger log = Logger.getLogger(CassandraConnection.class.getName());

    protected Cluster cluster;
    protected Session session;

    public void connect(){
        // Connect to the cluster and keyspace "demo"
        cluster = Cluster.builder().addContactPoint("192.168.100.215").build();
        session = cluster.connect("pokemonmeta");
//        System.out.println("Connected to cluster : "+cluster.getClusterName());
//        System.out.println("Connected to session 'pokemonmeta' keyspace : "+session.getState());
        log.info("Connected to "+ cluster.getClusterName()+" with Keyspace 'pokemonmeta'");
    }

    public void close(){
        // Clean up the connection by closing it
        cluster.close();
        log.info("Now Cassandra Cluster is closing.");
    }
}
