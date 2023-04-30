package org.learn.reggie.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class AwsSnsConfiguration {

    @Value("${amazonProperties.accessKey}")
    private String accessKey;

    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    /**
     * Build the aws sns client with default configuration
     *
     * @return AmazonSNSClient
     */
    public AmazonSNS amazonSNS() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        return AmazonSNSClient.builder().withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).withRegion("us-west-1").build();
    }
}
