//package com.example.tesisaoskaunto.windowservice.grpc.service;
//
//import com.example.tesisaoskaunto.projectservice.grpc.proto.*;
//import com.example.tesisaoskaunto.windowservice.application.WindowAssistant;
//import com.example.tesisaoskaunto.windowservice.domain.models.Project;
//import com.example.tesisaoskaunto.windowservice.infrastructure.repositories.WindowRepository;
//import io.grpc.Status;
//import io.grpc.stub.StreamObserver;
//import net.devh.boot.grpc.server.service.GrpcService;
//
//import java.util.List;
//import java.util.Optional;
//
//@GrpcService
//public class WindowServiceImpl extends ProjectServiceGrpc.ProjectServiceImplBase {
//
//    private final WindowRepository windowRepository;
//    private final WindowAssistant windowAssistant;
//
//    public WindowServiceImpl(WindowRepository windowRepository, WindowAssistant windowAssistant) {
//        this.windowRepository = windowRepository;
//        this.windowAssistant = windowAssistant;
//    }
//
//    @Override
//    public void createProject(ProjectRequest request, StreamObserver<ProjectAnswerResponse> response){
//        String generatedResponse = windowAssistant.saveProject(request.getUserId(), request.getName());
//        ProjectAnswerResponse reply = ProjectAnswerResponse.newBuilder().setAnswer(generatedResponse).build();
//        response.onNext(reply);
//        response.onCompleted();
//    }
//
//    @Override
//    public void getProjectByUserId(UserIdRequest request, StreamObserver<ProjectResponse> responseObserver){
//        List<Project> projects = windowRepository.findByUserId(request.getUserId());
//
//        List<ProjectP> projectsProtos = projects.stream()
//                .map(c -> ProjectP.newBuilder()
//                        .setId(c.getId())
//                        .setName(c.getProjectName())
//                        .setCreatedAt(c.getCreatedAt())
//                        .build()).toList();
//        ProjectResponse response = ProjectResponse.newBuilder()
//                .addAllProjects(projectsProtos)
//                .build();
//        responseObserver.onNext(response);
//        responseObserver.onCompleted();
//    }
//
//    @Override
//    public void updateProject(UpdateProjectRequest request, StreamObserver<ProjectAnswerResponse> responseObserver) {
//        Optional<Project> project = windowRepository.findById(request.getId());
//
//        if (project.isEmpty()) {
//            responseObserver.onError(Status.NOT_FOUND
//                    .withDescription("Project with id " + request.getId() + " not found")
//                    .asRuntimeException());
//            return;
//        }
//
//        Project pro = project.get();
//        pro.setProjectName(request.getName());
//        windowRepository.save(pro);
//
//        ProjectAnswerResponse response = ProjectAnswerResponse.newBuilder()
//                .setAnswer(pro.getProjectName())
//                .build();
//
//        responseObserver.onNext(response);
//        responseObserver.onCompleted();
//    }
//
//    @Override
//    public void deleteProject (DeleteProjectRequest request, StreamObserver<ProjectAnswerResponse> responseObserver){
//        if (!windowRepository.existsById(request.getId())) {
//            responseObserver.onError(Status.NOT_FOUND.withDescription("Project not found").asRuntimeException());
//            return;
//        }
//
//        windowRepository.deleteById(request.getId());
//
//        ProjectAnswerResponse response = ProjectAnswerResponse.newBuilder()
//                .setAnswer("Project deleted")
//                .build();
//
//        responseObserver.onNext(response);
//        responseObserver.onCompleted();
//    }
//}
