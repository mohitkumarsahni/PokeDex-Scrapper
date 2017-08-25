package sahnimohit;

import io.minio.errors.MinioException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

/**
 * Created by mohitsahni on 17/7/17.
 */
public class ImageToMinio extends MinioConnection {
    private String name = "ImageToMinio";
    private static final Logger log = Logger.getLogger(ImageToMinio.class.getName());
    public void fileUpload() throws NoSuchAlgorithmException,IOException,InvalidKeyException,XmlPullParserException {
        try {
            // Check if the bucket already exists.
            boolean isExist = minioClient.bucketExists("pokemonsimgs");
            if (isExist) {
                log.info("Bucket already exists.");
            } else {
                // Make a new bucket called asiatrip to hold a zip file of photos.
                minioClient.makeBucket("pokemonsimgs");
            }

            // Upload the zip file to the bucket with putObject
            minioClient.putObject("pokemonsimgs", "test2.jpg", "/home/mohitsahni/jsoup.jpg");

        } catch (MinioException e) {
            log.info("Error occurred: " + e);
        }

    }
}
