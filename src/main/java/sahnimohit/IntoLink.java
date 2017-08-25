package sahnimohit;

import com.mongodb.client.MongoCollection;
import io.minio.errors.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParserException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by mohitsahni on 17/7/17.
 */
public class IntoLink extends MinioConnection{
    private String name = "IntoLink";
    private static final Logger log = Logger.getLogger(IntoLink.class.getName());

    SaveIntoNeo neo = new SaveIntoNeo();


    private String link = null;
    private String title = null;
    private Document scrapDoc;

    private String poke_name = null;
    private String poke_desc = null;
    private int poke_id = 000;
    private String poke_ability = null;
    private String poke_species = null;
    private String poke_japanese = null;
    private String poke_grow_rate = null;
    private String poke_img_url = null;
    private String poke_type = null;

    String[] type_poke  = null;

    public void setPoke_type(String poke_type) {
        this.poke_type = poke_type;
        type_poke = poke_type.split("\\s");
//        System.out.println(poke_type);
    }

    public void setPoke_id(String poke_id) {

        this.poke_id = Integer.parseInt(poke_id);
    }



    public void setLink(String link){
        this.link = link;
    }

    public void connectToLink(){
        try {
            scrapDoc = Jsoup.connect(link).get();
            log.info("Connected to link : "+link);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void extractDataSaveData(){
        neo.connectToNeo();
        poke_name = scrapDoc.getElementsByClass("svtabs-tab-list").first().text();
        Elements ele = scrapDoc.getElementsByClass("col desk-span-8 lap-span-6");
        poke_desc = ele.select("p").text();
        //System.out.println(scrapDoc.getElementsByClass("aside").first().parent().parent().parent().child(0).getElementsByTag("strong").text());
       // poke_id = Integer.parseInt(scrapDoc.getElementsByClass("aside").first().parent().parent().parent().child(0).getElementsByTag("strong").text());
        poke_ability = scrapDoc.getElementsByClass("aside").first().parent().text();
        poke_species = scrapDoc.getElementsByClass("aside").first().parent().parent().parent().child(2).getElementsByTag("td").text();
        try {
            poke_japanese = scrapDoc.getElementsByClass("aside").first().parent().parent().parent().child(7).getElementsByTag("td").text();
        } catch (Exception e) {

            poke_japanese = "N/A";
        }
        poke_grow_rate = scrapDoc.getElementsByClass("col desk-span-4 lap-span-12").first().select("tbody").first().child(4).getElementsByTag("td").text();
        poke_img_url = scrapDoc.getElementsByClass("col desk-span-4 lap-span-6 figure").select("img").attr("src");

        MongoCollection<org.bson.Document> collection = db.getCollection("pokemonMeta");

        org.bson.Document doc = new org.bson.Document("id", poke_id)
                .append("data", new org.bson.Document("name", poke_name)
                    .append("type",poke_type)
                    .append("species", poke_species)
                    .append("ability", poke_ability)
                    .append("growth_rate", poke_grow_rate)
                    .append("japanese_name", poke_japanese))
                .append("description", poke_desc);
        collection.insertOne(doc);
        log.info("Collection inserted for : "+ poke_name);

        try {
            URL url = new URL(poke_img_url);
            HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
            httpcon.addRequestProperty("User-Agent", "");
            BufferedImage img = ImageIO.read(httpcon.getInputStream());
            File file = new File("/home/mohitsahni/pokemonImage.jpg");
            ImageIO.write(img,"jpg", file);
            log.info("image of "+poke_name+" downloaded.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // Check if the bucket already exists.
            boolean isExist = minioClient.bucketExists("pokemonsimgs");
            if (isExist) {
                System.out.println("Bucket already exists.");
            } else {
                // Make a new bucket called asiatrip to hold a zip file of photos.
                minioClient.makeBucket("pokemonsimgs");
            }

            // Upload the zip file to the bucket with putObject
            minioClient.putObject("pokemonsimgs", poke_id+"-"+poke_name+".jpg", "/home/mohitsahni/pokemonImage.jpg");
            log.info("'/home/mohitsahni/pokemonImage.jpg' is successfully uploaded as "+poke_id+".jpg "+" to `pokemonsimgs` Bucket.");
        } catch (InvalidBucketNameException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoResponseException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (RegionConflictException e) {
            e.printStackTrace();
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }

     //   String lnk = "/pokemonsimgs/"+poke_id+"-"+poke_name+".jpg";
     //   String query = "INSERT INTO pokemondata (pokeid, pokename, pokelink) VALUES ( "+poke_id+","+poke_name+","+lnk+")";

        // Insert one record into the users table
     //   session.execute(query);
     //   log.info("Mapped into cassandra. "+poke_id+" with "+lnk);

        ArrayList<String> tp = new ArrayList<String>();
        for(int i=0; i<type_poke.length;i++){
            System.out.println(type_poke[i]);
        }

        for(int i=0; i< type_poke.length;i++){
            if(neo.checkTypeNode(type_poke[i])){
                //System.out.println("Node checked for type : "+type_poke[i]+" does it exist? :"+neo.checkTypeNode(type_poke[i]));
               // tp.add("true");
            }else{
                neo.makeTypeNode(type_poke[i]);
                //log.info("Node for Type "+type_poke[i]+" is added.");
               // tp.add("true");
            }
        }

        boolean pk = false;

        if(neo.checkPokemonNode(poke_name)){
            pk=true;
        }else{
            neo.makePokemonNode(poke_name,poke_id,poke_type);
            pk=true;
            //log.info("Node for Pokemon "+poke_name+" is added.");
        }

        boolean spec = false;

        if(neo.checkSpeciesNode(poke_species)){
            spec=true;
        }else{
            neo.makeSpeciesNode(poke_species);
            spec = true;
            //log.info("Node for Species "+poke_species+" is added.");
        }

        for(int i=0;i<type_poke.length;i++){
                neo.makeRelationPokemonAndType(poke_name,type_poke[i]);
                //log.info("Relationship for Pokemon "+poke_name+" and "+type_poke[i]+" is added.");

        }

        if( (pk == true) && (spec == true)){
            neo.makeRelationPokemonAndSpeices(poke_name,poke_species);
            //log.info("Relationship for Pokemon "+poke_name+" and "+poke_species+" is added.");
        }




    }
}
