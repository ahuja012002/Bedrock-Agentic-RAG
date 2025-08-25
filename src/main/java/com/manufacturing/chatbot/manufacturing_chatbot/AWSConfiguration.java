package com.manufacturing.chatbot.manufacturing_chatbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrock.BedrockClient;
import software.amazon.awssdk.services.bedrockagent.BedrockAgentClient;

@Configuration
public class AWSConfiguration {

    @Value("${aws.region:us-east-1}")
    private String region;

    @Bean
    public BedrockClient bedrockClient() {
        return BedrockClient.builder()
            .region(Region.of(region))
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build();
    }

    @Bean
    public BedrockAgentClient bedrockAgentClient() {
        return BedrockAgentClient.builder()
            .region(Region.of(region))
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build();
    }
}
