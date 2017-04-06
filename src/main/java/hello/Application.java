package hello;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.util.StringUtils;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Value("${AWS_REGION}")
    private String awsRegion;
    
    @Value("${AWS_ACCESS_KEY_ID}")
    private String awsAccessKeyId;
    
    @Value("${AWS_SECRET_ACCESS_KEY}")
    private String awsSecretAccessKey;

    @Value("${AWS_ENDPOINT:#{null}}")
    private String awsEndpoint;

    /**
     * AWS S3 Client configuration
     * @param endpointConfiguration
     * @return
     */
	@Bean
    public AmazonS3 amazonS3(AwsClientBuilder.EndpointConfiguration endpointConfiguration) {
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey);
        return AmazonS3ClientBuilder.standard()
        		.withCredentials(new AWSStaticCredentialsProvider(awsCreds))
        		.withRegion(awsRegion)
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(endpointConfiguration)
                .build();
    }

    /**
     * S3 Endpoint configuration
     * When AWS_ENDPOINT variable not provided - threat as AWS
     * When AWS_ENDPOINT variable provided - threat as Minio
     * @return
     */
	@Bean
    public AwsClientBuilder.EndpointConfiguration endpointConfiguration() {
        if (StringUtils.isNullOrEmpty(awsEndpoint)) {
            return null;
        } else {
            return new AwsClientBuilder.EndpointConfiguration(awsEndpoint, awsRegion);
        }
    }
}
