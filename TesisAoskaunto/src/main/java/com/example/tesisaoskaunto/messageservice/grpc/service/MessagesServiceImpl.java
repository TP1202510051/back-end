package com.example.tesisaoskaunto.messageservice.grpc.service;

import com.example.tesisaoskaunto.conversationservice.application.ConversationAssistant;
import com.example.tesisaoskaunto.messageservice.grpc.proto.MessageRequest;
import com.example.tesisaoskaunto.messageservice.grpc.proto.MessageServiceGrpc;
import com.example.tesisaoskaunto.messageservice.grpc.proto.ConversationIdRequest;
import com.example.tesisaoskaunto.messageservice.grpc.proto.MessageProto;
import com.example.tesisaoskaunto.messageservice.grpc.proto.MessageResponse;
import com.example.tesisaoskaunto.messageservice.grpc.proto.AnswerResponse;
import com.example.tesisaoskaunto.messageservice.infrastructure.repositories.MessagesRepository;
import com.example.tesisaoskaunto.messageservice.domain.models.Message;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

@GrpcService
public class MessagesServiceImpl extends MessageServiceGrpc.MessageServiceImplBase {

    private final MessagesRepository messagesRepository;
    private final ConversationAssistant conversationAssistant;

    public MessagesServiceImpl(MessagesRepository messagesRepository, ConversationAssistant conversationAssistant) {
        this.conversationAssistant = conversationAssistant;
        this.messagesRepository = messagesRepository;
    }

    @Override
    public void getMessagesByConversationId(ConversationIdRequest request, StreamObserver<MessageResponse> responseObserver){
        List<Message> messages = messagesRepository.findByConversationId(request.getConversationId());

        List<MessageProto> messageProtos = messages.stream()
                .map(m -> MessageProto.newBuilder()
                        .setId(m.getId())
                        .setContent(m.getContent())
                        .setConversationId(m.getConversationId())
                        .setType(m.getType())
                        .setCreatedAt(m.getCreatedAt())
                        .build()).toList();
        MessageResponse response = MessageResponse.newBuilder()
                .addAllMessages(messageProtos)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void createMessage(MessageRequest request, StreamObserver<AnswerResponse> responseObserver) {
        String generatedResponse = conversationAssistant.saveMessageAndType(request.getMessage(), request.getType(), request.getConversationId());
        AnswerResponse reply = AnswerResponse.newBuilder().setAnswer(generatedResponse).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
