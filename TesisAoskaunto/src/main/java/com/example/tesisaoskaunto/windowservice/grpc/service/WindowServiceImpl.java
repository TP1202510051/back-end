package com.example.tesisaoskaunto.windowservice.grpc.service;

import com.example.tesisaoskaunto.windowservice.grpc.proto.*;
import com.example.tesisaoskaunto.windowservice.application.WindowAssistant;
import com.example.tesisaoskaunto.windowservice.domain.models.Window;
import com.example.tesisaoskaunto.windowservice.grpc.proto.WindowAnswerResponse;
import com.example.tesisaoskaunto.windowservice.infrastructure.repositories.WindowRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.Optional;

@GrpcService
public class WindowServiceImpl extends WindowServiceGrpc.WindowServiceImplBase {

    private final WindowRepository windowRepository;
    private final WindowAssistant windowAssistant;

    public WindowServiceImpl(WindowRepository windowRepository, WindowAssistant windowAssistant) {
        this.windowRepository = windowRepository;
        this.windowAssistant = windowAssistant;
    }

    @Override
    public void createWindow(WindowRequest request, StreamObserver<WindowAnswerResponse> response){
        String generatedResponse = windowAssistant.saveWindow(request.getProjectId(), request.getName());
        WindowAnswerResponse reply = WindowAnswerResponse.newBuilder().setAnswer(generatedResponse).build();
        response.onNext(reply);
        response.onCompleted();
    }

    @Override
    public void getWindowByProjectId(WProjectIdRequest request, StreamObserver<WindowResponse> responseObserver){
        List<Window> window = windowRepository.findByProjectId(request.getProjectId());

        List<WindowP> windowsProtos = window.stream()
                .map(c -> WindowP.newBuilder()
                        .setId(c.getId())
                        .setName(c.getWindowName())
                        .setCreatedAt(c.getCreatedAt())
                        .build()).toList();
        WindowResponse response = WindowResponse.newBuilder()
                .addAllProjects(windowsProtos)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateWindow(UpdateWindowRequest request, StreamObserver<WindowAnswerResponse> responseObserver) {
        Optional<Window> window = windowRepository.findById(request.getId());

        if (window.isEmpty()) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Window with id " + request.getId() + " not found")
                    .asRuntimeException());
            return;
        }

        Window win = window.get();
        win.setWindowName(request.getName());
        windowRepository.save(win);

        WindowAnswerResponse response = WindowAnswerResponse.newBuilder()
                .setAnswer(win.getWindowName())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteWindow (DeleteWindowRequest request, StreamObserver<WindowAnswerResponse> responseObserver){
        if (!windowRepository.existsById(request.getId())) {
            responseObserver.onError(Status.NOT_FOUND.withDescription("Window not found").asRuntimeException());
            return;
        }

        windowRepository.deleteById(request.getId());

        WindowAnswerResponse response = WindowAnswerResponse.newBuilder()
                .setAnswer("Window deleted")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
