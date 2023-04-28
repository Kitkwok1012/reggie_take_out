package org.learn.reggie.config;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class AwsSnsConfiguration {

    /**
     * Build the aws sns client with default configuration
     *
     * @return AmazonSNSClient
     */
    @Bean
    public AmazonSNS amazonSNS() {
        return AmazonSNSClient.builder().build();
    }
}
