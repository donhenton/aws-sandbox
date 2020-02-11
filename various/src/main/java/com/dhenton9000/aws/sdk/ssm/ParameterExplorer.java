package com.dhenton9000.aws.sdk.ssm;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmAsyncClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;

//https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/creating-clients.html
public class ParameterExplorer {

    public static final Logger LOG = LoggerFactory.getLogger(ParameterExplorer.class);
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        LOG.debug("beginning program");

        SsmAsyncClient client = SsmAsyncClient.builder()
                .region(Region.US_EAST_2)
                .credentialsProvider(ProfileCredentialsProvider.builder()
                        .profileName("default").build()).build();
        GetParameterRequest getParameterRequest = GetParameterRequest.builder()
                .name("/dev/db/password").build();

        CompletableFuture<GetParameterResponse> mainResponse = client.getParameter(getParameterRequest);

        LOG.debug("computed response");

        // System.out.println(ff.toString());
        CompletableFuture<Map<String, String>> cleanedMap
                = mainResponse.thenApply((GetParameterResponse parms) -> {
                   LOG.debug("in then apply");
                    Map<String, String> parmMap = new HashMap<>();
                    try {
                        if (parms != null) {
                            parmMap.put(parms.parameter().name(), parms.parameter().value());
                        }
                      
                    } catch (Exception ee) {
                        LOG.error("error " + ee.getMessage());
                    } finally {
                       LOG.debug("in finally closing client");
                        client.close();
                    }
                    return parmMap;
                });

        cleanedMap.whenComplete((parms, err) -> {
            parms.keySet().forEach(k -> {
                LOG.debug("key " + k + " --> " + parms.get(k));
            });

        });
        
        mainResponse.get();

    }
}
