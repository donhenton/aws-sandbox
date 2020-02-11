package com.dhenton9000.aws.sdk.kms;

//http://jamesabrannan.com/2019/06/14/aws-key-management-system-kms-to-encrypt-and-decrypt-using-the-asw-java-2-sdk/
import com.dhenton9000.aws.sdk.kms.impl.EnvelopeEncryptionServiceImpl;
import com.dhenton9000.aws.sdk.ssm.ParameterExplorer;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;

public class KmsExplorer {

    private  String masterKeyId = "master key is in mainId.properrties file see below";
    static final Logger LOG = LoggerFactory.getLogger(KmsExplorer.class);
    private static final String ENCRYPTED_DATA_KEY
            = Paths.get(".").toAbsolutePath().normalize().toString() + "/encryptedDataKey.cpt";
    
    /**
     * the masterid is in a file called mainId.properties which is not checked
     * into source. the property is mainId=arn.........
     */
    private void getMasterId() {
        String propPath = 
        Paths.get(".").toAbsolutePath().normalize().toString() + "/mainId.properties";
        
        
        try (InputStream input = new FileInputStream(propPath)) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);
            this.masterKeyId = prop.getProperty("mainId");
             LOG.debug("key "+this.masterKeyId);

        } catch (IOException ex) {
            LOG.error("properties problem "+ex.getMessage());
        }
    }

    public void doWork() {
        getMasterId();
        LOG.debug("key "+this.masterKeyId);
         AwsCredentials awsCredentials = ProfileCredentialsProvider.create().resolveCredentials();
        EnvelopeEncryptionServiceImpl encryptionService = new EnvelopeEncryptionServiceImpl(awsCredentials,
                Region.US_EAST_2, masterKeyId);

        //create and store the encrypted key to disk
        encryptionService.createEncryptedDataKey(ENCRYPTED_DATA_KEY);
        //use that key and to encrypt the inputData and write to file
        SdkBytes inputData = SdkBytes.fromByteArray("fred says 'get a job', bozo!!!!!!".getBytes());
        SdkBytes encData = encryptionService.encryptData(inputData, ENCRYPTED_DATA_KEY);
        
        String actual = encryptionService.decryptData(encData, ENCRYPTED_DATA_KEY).asUtf8String();
        
        // 

          LOG.debug("did the thing and got "+actual);
    }

    public static void main(String[] args) {

        (new KmsExplorer()).doWork();

    }

}
