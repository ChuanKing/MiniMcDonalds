# MiniMcDonalds
In this tutorial, you will learn how to use AWS SDK for SQS

## Step 4:
In this step, will going to setup SQS Java Message Library (JMS)

1. Add JMS dependency:
	compile group: 'com.amazonaws', name: 'amazon-sqs-java-messaging-lib', version: '1.0.8'

2. create SQS connection
```
    private static SQSConnection createSQSConnection() throws JMSException {
        // Create a new connection factory with all defaults (credentials and region) set automatically
        SQSConnectionFactory connectionFactory = new SQSConnectionFactory(
                new ProviderConfiguration(),
                AmazonSQSClientBuilder.defaultClient()
        );

        // Create the connection.
        return connectionFactory.createConnection();
    }
```