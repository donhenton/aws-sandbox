/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.aws.sdk.kms;

import software.amazon.awssdk.core.SdkBytes;


public interface EnvelopeEncryptionService {

    /**
     * create an encrypted data key suitable for encryption/decryption
     * @param path the string representation of the path location to save
     * the encrypted data key
     * 
     */
    void createEncryptedDataKey(String path);
    /**
     * decrypt data
     * @param encryptedData the data to decrypt as a byte array
     * @param dataKeyLocation the path to the data key
     * @return the decrypted result as a byte array
     */
    SdkBytes decryptData(SdkBytes encryptedData, String dataKeyLocation);

    /**
     * encrypt data
     * @param inputData the data to encrypt as a byte array
     * @param dataKeyLocation the path to the data key
     * @return the encrypted result as a byte array
     */
    SdkBytes encryptData(SdkBytes inputData, String dataKeyLocation);
    
}
