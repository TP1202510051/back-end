package com.example.tesisaoskaunto.conversationservice.grpc.service;

import com.example.tesisaoskaunto.conversationservice.application.ConversationAssistant;
import com.example.tesisaoskaunto.conversationservice.domain.models.Conversation;
import com.example.tesisaoskaunto.conversationservice.grpc.proto.*;
import com.example.tesisaoskaunto.conversationservice.infrastructure.repositories.ConversationRepository;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.Optional;

@GrpcService
public class ConvesationServiceImpl extends ConversationServiceGrpc.ConversationServiceImplBase {

    private final ConversationAssistant conversationAssistant;
    private final ConversationRepository conversationRepository;

    public ConvesationServiceImpl(ConversationAssistant conversationAssistant, ConversationRepository conversationRepository) {
        this.conversationAssistant = conversationAssistant;
        this.conversationRepository = conversationRepository;
    }

    @Override
    public void createConversation(ConversationRequest request, StreamObserver<ConversationAnswerResponse> responseObserver) {
        String generatedResponse = conversationAssistant.saveConversationInformation(request.getUserId(), request.getName());
        ConversationAnswerResponse reply = ConversationAnswerResponse.newBuilder().setAnswer(generatedResponse).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void getConversationByUserId(UserIdRequest request, StreamObserver<ConversationResponse> responseObserver){
        List<Conversation> conversations = conversationRepository.findByUserId(request.getUserId());

        List<ConversationP> conversationsProtos = conversations.stream()
                .map(c -> ConversationP.newBuilder()
                        .setId(c.getId())
                        .setName(c.getConversationName())
                        .setCreatedAt(c.getCreatedAt())
                        .build()).toList();
        ConversationResponse response = ConversationResponse.newBuilder()
                .addAllConversations(conversationsProtos)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateConversation(UpdateConversationRequest request, StreamObserver<ConversationAnswerResponse> responseObserver) {
        Optional<Conversation> conversation = conversationRepository.findById(request.getId());

        if (conversation.isEmpty()) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Conversation not found")
                    .asRuntimeException());
            return;
        }

        Conversation conv = conversation.get();
        conv.setConversationName(request.getName());
        conversationRepository.save(conv);

        ConversationAnswerResponse response = ConversationAnswerResponse.newBuilder()
                .setAnswer(conv.getConversationName())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteConversation (DeleteConversationRequest request, StreamObserver<ConversationAnswerResponse> responseObserver){
        if (!conversationRepository.existsById(request.getId())) {
            responseObserver.onError(Status.NOT_FOUND.withDescription("Conversation not found").asRuntimeException());
            return;
        }

        conversationRepository.deleteById(request.getId());

        ConversationAnswerResponse response = ConversationAnswerResponse.newBuilder()
                .setAnswer("Conversation deleted")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
