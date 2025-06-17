package com.example.tesisaoskaunto.conversationassistantservice.grpc.service;

import com.example.tesisaoskaunto.conversationassistantservice.application.ConversationAssistant;
import com.example.tesisaoskaunto.conversationassistantservice.grpc.proto.ConversationAssistantServiceGrpc;
import com.example.tesisaoskaunto.conversationassistantservice.grpc.proto.MessageRequest;
import com.example.tesisaoskaunto.conversationassistantservice.grpc.proto.AnswerResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class ConversationAssistantServiceImpl extends ConversationAssistantServiceGrpc.ConversationAssistantServiceImplBase {

    private final ConversationAssistant conversationAssistant;

    public ConversationAssistantServiceImpl(ConversationAssistant conversationAssistant) {
        this.conversationAssistant = conversationAssistant;
    }

    @Override
    public void getAnswer(MessageRequest request, StreamObserver<AnswerResponse> responseObserver) {
        String generatedResponse = conversationAssistant.saveMessageAndType(request.getMessage(), request.getType(), request.getConversationId());
        AnswerResponse reply = AnswerResponse.newBuilder().setAnswer(generatedResponse).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}