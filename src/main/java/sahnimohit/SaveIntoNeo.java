package sahnimohit;

import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import java.util.logging.Logger;

import static org.neo4j.driver.v1.Values.parameters;

/**
 * Created by mohitsahni on 21/7/17.
 */
public class SaveIntoNeo extends NeoConnection {
    private String name = "SaveIntoNeo";
    private static final Logger log = Logger.getLogger(SaveIntoNeo.class.getName());

    public boolean checkPokemonNode(String name){
        Session session = driver.session();
       // boolean isPresent = false;
        String res = null;
       StatementResult result =  session.run("match (a:Pokemon) where a.name = {name} return a.name as name", parameters("name", name));
        while(result.hasNext()){
            Record rc = result.next();
            res = rc.get("name").toString();
        }

        session.close();

        if(res == null){
            return false;
        }else{
            return true;
        }
    }

    public boolean checkTypeNode(String type){
        Session session = driver.session();
        // boolean isPresent = false;
        String res = null;
        StatementResult result =  session.run("match (a:Type) where a.type = {type} return a.type as type", parameters("type", type));
        while(result.hasNext()){
            Record rc = result.next();
            res = rc.get("type").toString();
        }

        session.close();
        if(res == null){
            return false;
        }else{
            return true;
        }
    }

    public boolean checkSpeciesNode(String name){
        Session session = driver.session();

        String res = null;
        StatementResult result = session.run("match (n:Species) where n.name = {name} return n.name as name", parameters("name", name));

        while(result.hasNext()){
            Record rc = result.next();
            res = rc.get("name").asString();
        }

        session.close();

        if(res == null){ return false;}else{ return true;}
    }

    public void makePokemonNode(String name, int id, String type){
        Session session = driver.session();

        StatementResult result = session.run("create (n:Pokemon {name:{name}, id:{id} ,type:{type}}) return n.name as name ",parameters("name", name, "id", id, "type", type));
        Record rc = result.next();

        session.close();

        log.info("Node for "+name+" Pokemon is created as : "+rc);
    }

    public void makeTypeNode(String type){
        Session session = driver.session();
        StatementResult result = session.run("create (n:Type {type:{type}}) return n.type as type", parameters("type",type));
        Record rc = result.next();

        session.close();

        log.info("Node for "+type+" type is created as : "+rc);
    }

    public void makeSpeciesNode(String name){
        Session session = driver.session();
        StatementResult result = session.run("create (n:Species {name:{name}}) return n.name as name", parameters("name",name));

        session.close();

        log.info("Node for "+name+" species is created.");
    }

    public void makeRelationPokemonAndType(String name, String type){
        Session session = driver.session();
        StatementResult result = session.run("match (a:Pokemon), (b:Type) where a.name = {name} and b.type = {type} create (a)-[:Is_Of_Type_Of]->(b)", parameters("name",name,"type",type));

        session.close();
        log.info("Relationship between "+name+" and "+type+" is created.");
    }

    public void makeRelationPokemonAndSpeices(String pokeName, String species){
        Session session = driver.session();
        session.run("match (a:Pokemon), (b:Species) where a.name = {name} and b.name = {species} create (a)-[:Is_a_Species_Of]->(b)",parameters("name",pokeName,"species",species));
        session.close();
        log.info("Relationship between "+pokeName+" and "+species+" is created.");
    }




}
