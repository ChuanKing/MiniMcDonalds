# MiniMcDonalds
In this tutorial, you will learn how to use AWS SDK for SQS

## Step 2:
In this step, you will learn how to:
1. create queue
2. list queue
3. send message to queue
4. receive message from queue
5. delete message from queue
6. delete queue

### create queue
create standard queue
```
    private static void createStandardQueue(AmazonSQS sqs) {
        
        // Create standard queue        
        final CreateQueueRequest createQueueRequest = new CreateQueueRequest("MyQueue");
        final String myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
        
        System.out.println("Created a new SQS queue called MyQueue with url: " + myQueueUrl);
    }
```
create FIFO queue   
```    
    private static void createFIFOQueue(AmazonSQS sqs) {
        
        // Create FIFO queue  
        final Map<String, String> attributes = ImmutableMap.of(
            "FifoQueue", "true",
            "ContentBasedDeduplication", "true"
        );
        
        final CreateQueueRequest createQueueRequest = new CreateQueueRequest("MyQueue.fifo").withAttributes(attributes);
        final String myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
        
        System.out.println("Created a new SQS queue called MyQueue with url: " + myQueueUrl);
    }
```

### list queue
```  
    private static void listQueue(final AmazonSQS sqs) {
        System.out.println("Listing all queues in your account.");
        
        final ListQueuesResult listQueuesResult = sqs.listQueues();
        
        for (final String queueUrl : listQueuesResult.getQueueUrls()) {
            System.out.println("  QueueUrl: " + queueUrl);
        }
    }
```

### send message to queue
Standard Queue
```      
    private static void sendMessageToStandardQueue(final AmazonSQS sqs, final String queueUrl, final String message) {
        
        System.out.println("Sending a message to Standard Queue.");
        
        final SendMessageRequest request = new SendMessageRequest(queueUrl, message);
        sqs.sendMessage(request);
    }
```

FIFO Queue
```      
    private static void sendMessageToFIFOQueue(final AmazonSQS sqs, final String queueUrl, final String message) {
        
        System.out.println("Sending a message to FIFO Queue.");
        
        final SendMessageRequest request = new SendMessageRequest(queueUrl, message)
                .withMessageGroupId("group-1")
                .withMessageDeduplicationId(UUID.randomUUID().toString());
                
        sqs.sendMessage(request);
    }
```

### receive message from queue
```  
    private static void receiveMessageFromQueue(final AmazonSQS sqs, final String queueUrl) {
        System.out.println("Receiving messages from MyQueue.");
        
        final ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
        final List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
        
        for (final Message message : messages) {
            System.out.println("Message");
            System.out.println("  MessageId:     " + message.getMessageId());
            System.out.println("  ReceiptHandle: " + message.getReceiptHandle());
            System.out.println("  MD5OfBody:     " + message.getMD5OfBody());
            System.out.println("  Body:          " + message.getBody());
            
            for (final Entry<String, String> entry : message.getAttributes().entrySet()) {
                System.out.println("Attribute");
                System.out.println("  Name:  " + entry.getKey());
                System.out.println("  Value: " + entry.getValue());
            }
        }
    }
```

### delete message from queue
```  
    private static void deleteMessageFromQueue(final AmazonSQS sqs, final String queueUrl, final String messageReceiptHandle) {
        System.out.println("Deleting the message.");
        
        final DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest(queueUrl, messageReceiptHandle);
        sqs.deleteMessage(deleteMessageRequest);
    }
```

### delete queue
```      
    private static void deleteQueue(final AmazonSQS sqs, final String queueUrl) {
        System.out.println("Deleting the test queue.");
        
        final DeleteQueueRequest deleteQueueRequest = new DeleteQueueRequest(queueUrl);
        sqs.deleteQueue(deleteQueueRequest);
    }
```