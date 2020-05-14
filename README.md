# MiniMcDonalds
In this tutorial, you will learn how to use AWS SDK for SQS

## Step 5:
In this step, we will going to do the following tasks:

1. create a queue
2. send message synchronously
3. Receiving message synchronously

### create a queue
standard queue
```
    private static void createStandardQueue(SQSConnection connection) throws JMSException {
        
        // Get the wrapped client
        AmazonSQSMessagingClientWrapper client = connection.getWrappedAmazonSQSClient();
        String queueName = "MyQueue";

        // Create an SQS queue named MyQueue, if it doesn't already exist
        if (!client.queueExists(queueName)) {
            CreateQueueResult createQueueResult = client.createQueue(queueName);
            System.out.println("Create a queue with URL: " + createQueueResult.getQueueUrl());
        }
    }
```
FIFO queue
```
    private static void createFIFOQueue(SQSConnection connection) throws JMSException {
        
        // Get the wrapped client
        AmazonSQSMessagingClientWrapper client = connection.getWrappedAmazonSQSClient();
        String queueName = "MyQueue.fifo";

        // Create an SQS queue named MyQueue, if it doesn't already exist
        if (!client.queueExists(queueName)) {
            
            CreateQueueRequest createQueueRequest = new CreateQueueRequest()
                    .withQueueName(queueName)
                    .withAttributes(ImmutableMap.of(
                            "FifoQueue", "true",
                            "ContentBasedDeduplication", "true"
                    ));
            CreateQueueResult createQueueResult = client.createQueue(createQueueRequest);
            
            System.out.println("Create a queue with URL: " + createQueueResult.getQueueUrl());
        }
    }
```

### send message synchronously
standard queue
```
    private static void sendMessageStandardQueue(SQSConnection connection, String msg) throws JMSException {
        
        // Create the nontransacted session with AUTO_ACKNOWLEDGE mode
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create a queue identity and specify the queue name to the session
        Queue queue = session.createQueue("MyQueue");

        // Create a producer for the 'MyQueue'
        MessageProducer producer = session.createProducer(queue);

        // Create the text message
        TextMessage message = session.createTextMessage(msg);

        // Send the message
        producer.send(message);
    }
```
FIFO queue
```
    private static void sendMessageFIFOQueue(SQSConnection connection, String msg) throws JMSException {
        
        // Create the nontransacted session with AUTO_ACKNOWLEDGE mode
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create a queue identity and specify the queue name to the session
        Queue queue = session.createQueue("MyQueue.fifo");

        // Create a producer for the 'MyQueue'
        MessageProducer producer = session.createProducer(queue);

        // Create the text message
        TextMessage message = session.createTextMessage(msg);
        message.setStringProperty("JMSXGroupID", "group-1");
        message.setStringProperty("JMS_SQS_DeduplicationId", UUID.randomUUID().toString());

        // Send the message
        producer.send(message);
    }
```

### receive message synchronously
```
    private static void receiveMessage(SQSConnection connection) throws Exception {

        // Create the nontransacted session with AUTO_ACKNOWLEDGE mode
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create a queue identity and specify the queue name to the session
        Queue queue = session.createQueue("MyQueue.fifo");

        // Create a consumer for the 'MyQueue'
        MessageConsumer consumer = session.createConsumer(queue);
        connection.start();

        // Receive a message from 'MyQueue' and wait up to 1 second
        Message receivedMessage = consumer.receive(1000);

        // Cast the received message as TextMessage and display the text
        if (receivedMessage != null) {
            System.out.println("Received: " + ((TextMessage) receivedMessage).getText());
        }

    }
```

### receive message asynchronously
```
    private static void receiveMessageAsync(SQSConnection connection) throws JMSException {
        // Create the nontransacted session with AUTO_ACKNOWLEDGE mode
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create a queue identity and specify the queue name to the session
        Queue queue = session.createQueue("MyQueue");

        // Create a consumer for the 'MyQueue'.
        MessageConsumer consumer = session.createConsumer(queue);

        // Instantiate and set the message listener for the consumer.
        consumer.setMessageListener(new MessageListener() {
            
            @Override
            public void onMessage(Message message) {
                try {
                    // Cast the received message as TextMessage and print the text to screen.
                    System.out.println("Received: " + ((TextMessage) message).getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        // Start receiving incoming messages.
        connection.start();
    }
```