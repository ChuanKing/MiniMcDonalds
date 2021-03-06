package com.learn.minimcdonalds.awssdk;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.ListQueuesResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.google.common.collect.ImmutableMap;

public class utils {
    
    public static AmazonSQS getSQSClient() {

        return AmazonSQSClientBuilder
                .standard()
                .withRegion(Regions.US_WEST_2)
                .build();
    }
    
    public static void createStandardQueue(final AmazonSQS sqs) {
        
        // Create standard queue        
        final CreateQueueRequest createQueueRequest = new CreateQueueRequest("MyQueue");
        final String myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
        
        System.out.println("Created a new SQS queue called MyQueue with url: " + myQueueUrl);
    }
    
    public static void createFIFOQueue(final AmazonSQS sqs) {
        
        // Create FIFO queue  
        final Map<String, String> attributes = ImmutableMap.of(
            "FifoQueue", "true",
            "ContentBasedDeduplication", "true"
        );
        
        final CreateQueueRequest createQueueRequest = new CreateQueueRequest("MyQueue.fifo").withAttributes(attributes);
        final String myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
        
        System.out.println("Created a new SQS queue called MyQueue with url: " + myQueueUrl);
    }
    
    public static void listQueue(final AmazonSQS sqs) {
        System.out.println("Listing all queues in your account.");
        
        final ListQueuesResult listQueuesResult = sqs.listQueues();
        
        for (final String queueUrl : listQueuesResult.getQueueUrls()) {
            System.out.println("  QueueUrl: " + queueUrl);
        }
    }
    
    public static void sendMessageToStandardQueue(final AmazonSQS sqs, final String queueUrl, final String message) {
        
        System.out.println("Sending a message to Standard Queue.");
        
        final SendMessageRequest request = new SendMessageRequest(queueUrl, message);
        sqs.sendMessage(request);
    }
    
    public static void sendMessageToFIFOQueue(final AmazonSQS sqs, final String queueUrl, final String message) {
        
        System.out.println("Sending a message to FIFO Queue.");
        
        final SendMessageRequest request = new SendMessageRequest(queueUrl, message)
                .withMessageGroupId("group-1")
                .withMessageDeduplicationId(UUID.randomUUID().toString());
                
        sqs.sendMessage(request);
    }

    public static void receiveMessageFromQueue(final AmazonSQS sqs, final String queueUrl) {
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

    public static void deleteMessageFromQueue(final AmazonSQS sqs, final String queueUrl, final String messageReceiptHandle) {
        System.out.println("Deleting the message.");
        
        final DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest(queueUrl, messageReceiptHandle);
        sqs.deleteMessage(deleteMessageRequest);
    }
    
    public static void deleteQueue(final AmazonSQS sqs, final String queueUrl) {
        System.out.println("Deleting the test queue.");
        
        final DeleteQueueRequest deleteQueueRequest = new DeleteQueueRequest(queueUrl);
        sqs.deleteQueue(deleteQueueRequest);
    }
}