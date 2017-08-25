package sahnimohit;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

/**
 * Created by mohitsahni on 17/7/17.
 */
public class MinioConnection extends CassandraConnection{
    private String name = "MinioConnection";
    private static final Logger log = Logger.getLogger(MinioConnection.class.getName());
    private String uri="http://192.168.100.215:9000";
    private String secretKey="NQROL6QT8V2OIT583I4Y";
    private String accessKey="DPhyIPhe5/LgG+b3j0tm0OEUG5c21gBW2h9wOTz4";

    protected MinioClient minioClient;

    public void minioconnect() throws NoSuchAlgorithmException,IOException,InvalidKeyException,XmlPullParserException {
        // Create a minioClient with the Minio Server name, Port, Access key and Secret key.
        try {
            minioClient = new MinioClient(uri,secretKey,accessKey);
            log.info("Connection Established! "+minioClient);
        } catch (InvalidEndpointException e) {
            e.printStackTrace();
        } catch (InvalidPortException e) {
            e.printStackTrace();
        }
    }
}
