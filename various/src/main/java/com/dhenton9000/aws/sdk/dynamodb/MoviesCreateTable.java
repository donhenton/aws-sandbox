package com.dhenton9000.aws.sdk.dynamodb;

 
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;

public class MoviesCreateTable {

    private static final Logger LOG = LoggerFactory.getLogger(MoviesCreateTable.class);
    public static void main(String[] args) {

        String table_name = "moviesTable";

        LOG.info(String.format(
                "Creating table \"%s\" with a simple primary key: \"Name\".\n",
                table_name));

        CreateTableRequest request = CreateTableRequest.builder()
                .attributeDefinitions(AttributeDefinition.builder()
                        .attributeName("Name")
                        .attributeType(ScalarAttributeType.S)
                        .build())
                .keySchema(KeySchemaElement.builder()
                        .attributeName("Name")
                        .keyType(KeyType.HASH)
                        .build())
                .provisionedThroughput(ProvisionedThroughput.builder()
                        .readCapacityUnits(new Long(10))
                        .writeCapacityUnits(new Long(10))
                        .build())
                .tableName(table_name)
                .build();

        
        DynamoDbClient ddb = DynamoDbClient.builder().endpointOverride(URI.create("http://localhost:8000")).build();
 

        try {
            CreateTableResponse response = ddb.createTable(request);
           LOG.info(response.tableDescription().tableName());
        } catch (DynamoDbException e) {
            LOG.error(e.getMessage());
            System.exit(1);
        }

        System.out.println("Done!");
    }
}
