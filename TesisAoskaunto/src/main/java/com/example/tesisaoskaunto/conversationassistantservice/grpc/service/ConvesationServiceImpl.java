package com.example.tesisaoskaunto.conversationassistantservice.grpc.service;

import com.example.tesisaoskaunto.conversationassistantservice.application.ConversationAssistant;
import com.example.tesisaoskaunto.conversationservice.grpc.proto.ConversationServiceGrpc;
import com.example.tesisaoskaunto.conversationservice.grpc.proto.ConversationAnswerResponse;
import com.example.tesisaoskaunto.conversationservice.grpc.proto.ConversationRequest;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class ConvesationServiceImpl extends ConversationServiceGrpc.ConversationServiceImplBase {

    private final ConversationAssistant conversationAssistant;

    public ConvesationServiceImpl(ConversationAssistant conversationAssistant) {
        this.conversationAssistant = conversationAssistant;
    }

    @Override
    public void createConversation(ConversationRequest request, StreamObserver<ConversationAnswerResponse> responseObserver) {
        String generatedResponse = conversationAssistant.saveConversationInformation(request.getUserId(), request.getName());
        ConversationAnswerResponse reply = ConversationAnswerResponse.newBuilder().setAnswer(generatedResponse).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
