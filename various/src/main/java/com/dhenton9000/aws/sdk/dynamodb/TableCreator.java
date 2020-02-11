package com.dhenton9000.aws.sdk.dynamodb;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeAction;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

public class TableCreator {

    private static final Logger LOG = LoggerFactory.getLogger(TableCreator.class);
    private DynamoDbClient ddb = null;
    protected static final String PRODUCT_CATALOG = "ProductCatalog";
    private static final String USER_POSTS = "UserPosts";

    public DynamoDbClient getClient() {
        if (ddb == null) {
            ddb = DynamoDbClient.builder().endpointOverride(URI.create("http://localhost:8000")).build();
        }
        return ddb;
    }

    public void createTable(
            String tableName, long readCapacityUnits, long writeCapacityUnits,
            String hashKeyName) {

        createTable(tableName, readCapacityUnits, writeCapacityUnits,
                hashKeyName, null, null);
    }
    
    
    
    public void createUserPostsTable() {
        
        String keyName = "userName";
        
        //add the key
        AttributeDefinition.Builder attrsBuilder = AttributeDefinition.builder()
                .attributeName(keyName)
                .attributeType(ScalarAttributeType.S);
        KeySchemaElement.Builder schemaBuilder = KeySchemaElement.builder()
                .attributeName(keyName)
                .keyType(KeyType.HASH);
        
        //add the posts column
        
       
        
         CreateTableRequest tableRequest = CreateTableRequest.builder()
                .attributeDefinitions(attrsBuilder.build())
                .keySchema(schemaBuilder.build())
                .provisionedThroughput(ProvisionedThroughput.builder()
                        .readCapacityUnits(10L)
                        .writeCapacityUnits(10L)
                        .build())
                .tableName(USER_POSTS).build();
         
         try {
            CreateTableResponse response = getClient().createTable(tableRequest);
            LOG.info(response.tableDescription().tableName());
        } catch (DynamoDbException e) {
            LOG.error(e.getMessage());
            throw e;
        }
         
         
    }
    

    public void createTable(String tableName, long readCapacityUnits, long writeCapacityUnits,
            String hashKeyName,
            String rangeKeyName, String rangeKeyType) {

        AttributeDefinition.Builder attrsBuilder = AttributeDefinition.builder()
                .attributeName(hashKeyName)
                .attributeType(ScalarAttributeType.S);

        KeySchemaElement.Builder schemaBuilder = KeySchemaElement.builder()
                .attributeName(hashKeyName)
                .keyType(KeyType.HASH);

        if (rangeKeyName != null) {

            schemaBuilder.attributeName(rangeKeyName)
                    .keyType(KeyType.RANGE);
            attrsBuilder.attributeName(rangeKeyName)
                    .attributeType(rangeKeyType);
        }

        CreateTableRequest tableRequest = CreateTableRequest.builder()
                .attributeDefinitions(attrsBuilder.build())
                .keySchema(schemaBuilder.build())
                .provisionedThroughput(ProvisionedThroughput.builder()
                        .readCapacityUnits(readCapacityUnits)
                        .writeCapacityUnits(writeCapacityUnits)
                        .build())
                .tableName(tableName).build();

        try {
            CreateTableResponse response = getClient().createTable(tableRequest);
            LOG.info(response.tableDescription().tableName());
        } catch (DynamoDbException e) {
            LOG.error(e.getMessage());
            throw e;
        }

    }

    public void updateAndAddColumn() {
        LOG.info("doing update with add column");
        Map<String, AttributeValue> keyMap = new HashMap<>();
        keyMap.put("Id", AttributeValue.builder().s("100").build());
        Map<String, AttributeValueUpdate> itemValues = new HashMap<>();
        itemValues.put("Owner", AttributeValueUpdate.builder()
                .action(AttributeAction.PUT)
                .value(AttributeValue.builder().s("Horton Who").build())
                .build());
        UpdateItemRequest updateRequest = UpdateItemRequest.builder()
                .tableName(PRODUCT_CATALOG)
                .key(keyMap)
                .attributeUpdates(itemValues).build();
        performUpdate(updateRequest);

    }

    public void updateItem() {
        LOG.info("doing update");
        Map<String, AttributeValueUpdate> itemValues = new HashMap<>();
        Map<String, AttributeValue> keyMap = new HashMap<>();
        itemValues.put("Price", AttributeValueUpdate.builder()
                .action(AttributeAction.PUT)
                .value(AttributeValue.builder().n((new Float(1000.0f)).toString()).build())
                .build());
        keyMap.put("Id", AttributeValue.builder().s("100").build());
        UpdateItemRequest updateRequest = UpdateItemRequest.builder()
                .tableName(PRODUCT_CATALOG)
                .key(keyMap)
                .attributeUpdates(itemValues).build();

        performUpdate(updateRequest);

    }

    private void performUpdate(UpdateItemRequest updateRequest) throws SdkClientException, AwsServiceException {
        try {
            getClient().updateItem(updateRequest);
        } catch (ResourceNotFoundException e) {
            LOG.error(String.format("Error: The table \"%s\" can't be found.\n", PRODUCT_CATALOG));
            LOG.error("Be sure that it exists and that you've typed its name correctly!");
            throw e;

        } catch (DynamoDbException e) {
            LOG.error(e.getMessage());
            throw e;
        }
    }
    
     public void loadUsers() {
        Map<String, AttributeValue> itemValues = new HashMap<>();

        itemValues.put("userName", AttributeValue.builder().s("dmj130").build());
        
        Map<String,AttributeValue> emails = new HashMap<>();
        
        emails.put("home",  AttributeValue.builder().s("smurf@fred.com").build());
        emails.put("work",  AttributeValue.builder().s("carlos.danger@hotmail.com").build());
        emails.put("secret",  AttributeValue.builder().s("roy.cohn@betterdeadthanred.com").build());
        
        itemValues.put("emails", AttributeValue.builder().m(emails).build());
        itemValues.put("userType", AttributeValue.builder().s("admin").build());
         

        PutItemRequest request = PutItemRequest.builder()
                .tableName(USER_POSTS)
                .item(itemValues)
                .build();
        try {
            getClient().putItem(request);
        } catch (ResourceNotFoundException e) {
            LOG.error(String.format("Error: The table \"%s\" can't be found.\n", PRODUCT_CATALOG));
            LOG.error("Be sure that it exists and that you've typed its name correctly!");
            throw e;

        } catch (DynamoDbException e) {
            LOG.error(e.getMessage());
            throw e;
        }
    }
    

    public void loadItems() {
        Map<String, AttributeValue> itemValues = new HashMap<>();

        itemValues.put("Id", AttributeValue.builder().s("100").build());
        itemValues.put("Name", AttributeValue.builder().s("Loucious Lugnuts").build());
        itemValues.put("Price", AttributeValue.builder().n((new Float(100.0f)).toString()).build());
         

        PutItemRequest request = PutItemRequest.builder()
                .tableName(PRODUCT_CATALOG)
                .item(itemValues)
                .build();
        try {
            getClient().putItem(request);
        } catch (ResourceNotFoundException e) {
            LOG.error(String.format("Error: The table \"%s\" can't be found.\n", PRODUCT_CATALOG));
            LOG.error("Be sure that it exists and that you've typed its name correctly!");
            throw e;

        } catch (DynamoDbException e) {
            LOG.error(e.getMessage());
            throw e;
        }
    }

    public static void main(String[] args) throws Exception {

        TableCreator creator = new TableCreator();
        
        
        // creator.createTable(PRODUCT_CATALOG, 10L, 5L, "Id");
        //creator.loadItems();
        //creator.updateItem();
        //creator.updateAndAddColumn();
        
        //user Posts
        //creator.createUserPostsTable();
        creator.loadUsers();
        
        
    }

}
