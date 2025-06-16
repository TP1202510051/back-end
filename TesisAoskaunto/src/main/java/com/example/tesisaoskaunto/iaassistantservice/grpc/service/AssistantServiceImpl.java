package com.example.tesisaoskaunto.iaassistantservice.grpc.service;

import com.example.tesisaoskaunto.iaassistantservice.application.AssistantService;
import com.example.tesisaoskaunto.iaassistantservice.grpc.proto.AssistantServiceGrpc;
import com.example.tesisaoskaunto.iaassistantservice.grpc.proto.MessageRequest;
import com.example.tesisaoskaunto.iaassistantservice.grpc.proto.AnswerResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class AssistantServiceImpl extends AssistantServiceGrpc.AssistantServiceImplBase {

    private final AssistantService assistantService;

    public AssistantServiceImpl(AssistantService assistantService) {
        this.assistantService = assistantService;
    }

    @Override
    public void getAnswer(MessageRequest request, StreamObserver<AnswerResponse> responseObserver) {
        String generatedResponse = assistantService.saveMessageAndType(request.getMessage(), request.getType(), request.getConversationId());
        AnswerResponse reply = AnswerResponse.newBuilder().setAnswer(generatedResponse).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}