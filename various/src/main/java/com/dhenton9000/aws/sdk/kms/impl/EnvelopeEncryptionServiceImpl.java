package com.dhenton9000.aws.sdk.kms.impl;

import com.dhenton9000.aws.sdk.kms.EnvelopeEncryptionService;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.DataKeySpec;
import software.amazon.awssdk.services.kms.model.DecryptRequest;
import software.amazon.awssdk.services.kms.model.DecryptResponse;
import software.amazon.awssdk.services.kms.model.GenerateDataKeyRequest;
import software.amazon.awssdk.services.kms.model.GenerateDataKeyResponse;

public class EnvelopeEncryptionServiceImpl implements EnvelopeEncryptionService {

    private KmsClient kmsClient;
    private final String keyArn;
    private static final Logger LOG = LoggerFactory.getLogger(EnvelopeEncryptionServiceImpl.class);

    public EnvelopeEncryptionServiceImpl(AwsCredentials awsCreds, Region region, String keyArn) {

        this.keyArn = keyArn;
        LOG.debug(this.keyArn);
        this.kmsClient = KmsClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .region(region).build();

    }

    @Override
    public SdkBytes encryptData(SdkBytes inputData, String dataKeyLocation) {
        byte[] encodedSecret = null;
        try {
            Cipher cipher = readDataKey(dataKeyLocation, Cipher.ENCRYPT_MODE);
            encodedSecret = cipher.doFinal(inputData.asByteArray());

        } catch (IOException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException
                | NoSuchAlgorithmException | NoSuchPaddingException ex) {
            LOG.error("encryptData problem " + ex.getClass().getName() + " " + ex.getMessage());
        }
        return SdkBytes.fromByteArray(encodedSecret);
    }

    @Override
    public SdkBytes decryptData(SdkBytes encryptedData, String dataKeyLocation) {
        SdkBytes decryptedData = null;
        try {
            Cipher cipher = readDataKey(dataKeyLocation, Cipher.DECRYPT_MODE);
            decryptedData
                    = SdkBytes.fromByteArray(cipher.doFinal(encryptedData.asByteArray()));
        } catch (IOException | NoSuchAlgorithmException | BadPaddingException
                | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException ex) {
            LOG.error("encryptData problem " + ex.getClass().getName() + " " + ex.getMessage());
        }
        return decryptedData;
    }

    /**
     * read in the data key and return a cipher configured for encryption or
     * decryption
     *
     * @param dataKeyLocation location of the data key file
     * @param mode Cipher.ENCRYPT_MODE | Cipher.DECRYPT_MODE
     * @return configured Cipher object
     *
     * @throws SdkClientException
     * @throws AwsServiceException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     */
    private Cipher readDataKey(String dataKeyLocation, int mode)
            throws SdkClientException, AwsServiceException, IOException,
            NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException {

        SdkBytes sdkBytes = readFromFile(dataKeyLocation);
        //decrypt the key to make it usable
        DecryptRequest decryptRequest = DecryptRequest.builder()
                .ciphertextBlob(sdkBytes).build();
        DecryptResponse decryptResponse = this.kmsClient
                .decrypt(decryptRequest);
        //configure the secret key
        SecretKeySpec secretKeySpec = new SecretKeySpec(decryptResponse
                .plaintext().asByteArray(), "AES");
        //create the cipher in the requested mode
        Cipher cipher;
        cipher = Cipher.getInstance("AES");
        cipher.init(mode, secretKeySpec);

        return cipher;
    }

    @Override
    public void createEncryptedDataKey(String path) {
        try {
            GenerateDataKeyRequest generateDataKeyRequest = GenerateDataKeyRequest
                    .builder().keyId(this.keyArn)
                    .keySpec(DataKeySpec.AES_128).build();
            GenerateDataKeyResponse generateDataKeyResponse = this.kmsClient
                    .generateDataKey(generateDataKeyRequest);

            SdkBytes encryptedDataKey = generateDataKeyResponse.ciphertextBlob();
            writeToFile(encryptedDataKey, path);

        } catch (IOException | AwsServiceException | SdkClientException ex) {
            LOG.error("createEncryptedDataKey problem " + ex.getClass().getName() + " " + ex.getMessage());
        }
    }

    private void writeToFile(SdkBytes bytesToWrite,
            String path) throws IOException {

        FileChannel fc;
        try (FileOutputStream outputStream = new FileOutputStream(path)) {
            fc = outputStream.getChannel();
            fc.write(bytesToWrite.asByteBuffer());
        }
        fc.close();
    }

    private SdkBytes readFromFile(String path) throws IOException {
        InputStream in2 = new FileInputStream(path);
        return SdkBytes.fromInputStream(in2);
    }

}
