package com.example.tesisaoskaunto.messagesservice.grpc.service;


import com.example.tesisaoskaunto.messagesservice.grpc.proto.MessageServiceGrpc;
import com.example.tesisaoskaunto.messagesservice.grpc.proto.ConversationIdRequest;
import com.example.tesisaoskaunto.messagesservice.grpc.proto.MessageProto;
import com.example.tesisaoskaunto.messagesservice.grpc.proto.MessageResponse;
import com.example.tesisaoskaunto.messagesservice.infrastructure.repositories.MessagesRepository;
import com.example.tesisaoskaunto.iaassistantservice.domain.models.Message;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

@GrpcService
public class MessagesServiceImpl extends MessageServiceGrpc.MessageServiceImplBase {

    private final MessagesRepository messagesRepository;

    public MessagesServiceImpl(MessagesRepository messagesRepository) {
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
                .addAllEntities(messageProtos)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
