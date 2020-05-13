# MiniMcDonalds
In this tutorial, you will learn how to use AWS SDK for SQS

## Step 1:
In this step, you need to add AWS SQS dependency, and setup Amazon SQS client

dependency
1. go to build.gradle
2. copy following code in dependencies section

`compile group: 'com.amazonaws', name: 'aws-java-sdk-sqs', version: '1.11.774'`


Amazon SQS client:

Create client with AWS credential in your machine
```
    private static AmazonSQS getSQSClient() {

        return AmazonSQSClientBuilder
                .standard()
                .withRegion(Regions.US_WEST_2)
                .build();
    }
```

Create client with AWS credential in code 
```
    private static AmazonSQS getSQSClient(String accessKey, String secretKey) {

        final BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        final AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);

        return AmazonSQSClientBuilder
                .standard()
                .withCredentials(credentialsProvider)
                .withRegion(Regions.US_WEST_2)
                .build();
    }
```