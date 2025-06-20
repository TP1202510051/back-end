package com.example.tesisaoskaunto.projectservice.grpc.service;

import com.example.tesisaoskaunto.projectservice.application.ProjectAssistant;
import com.example.tesisaoskaunto.projectservice.domain.models.Project;
import com.example.tesisaoskaunto.projectservice.infrastructure.repositories.ProjectRepository;
import com.example.tesisaoskaunto.projectservice.grpc.proto.*;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.Optional;

@GrpcService
public class ProjectServiceImpl extends ProjectServiceGrpc.ProjectServiceImplBase {

    private final ProjectRepository projectRepository;
    private final ProjectAssistant projectAssistant;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectAssistant projectAssistant) {
        this.projectRepository = projectRepository;
        this.projectAssistant = projectAssistant;
    }

    @Override
    public void createProject(ProjectRequest request, StreamObserver<ProjectAnswerResponse> response){
        String generatedResponse = projectAssistant.saveProject(request.getUserId(), request.getName());
        ProjectAnswerResponse reply = ProjectAnswerResponse.newBuilder().setAnswer(generatedResponse).build();
        response.onNext(reply);
        response.onCompleted();
    }

    @Override
    public void getProjectByUserId(UserIdRequest request, StreamObserver<ProjectResponse> responseObserver){
        List<Project> projects = projectRepository.findByUserId(request.getUserId());

        List<ProjectP> projectsProtos = projects.stream()
                .map(c -> ProjectP.newBuilder()
                        .setId(c.getId())
                        .setName(c.getProjectName())
                        .setCreatedAt(c.getCreatedAt())
                        .build()).toList();
        ProjectResponse response = ProjectResponse.newBuilder()
                .addAllProjects(projectsProtos)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateProject(UpdateProjectRequest request, StreamObserver<ProjectAnswerResponse> responseObserver) {
        Optional<Project> project = projectRepository.findById(request.getId());

        if (project.isEmpty()) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Project with id " + request.getId() + " not found")
                    .asRuntimeException());
            return;
        }

        Project pro = project.get();
        pro.setProjectName(request.getName());
        projectRepository.save(pro);

        ProjectAnswerResponse response = ProjectAnswerResponse.newBuilder()
                .setAnswer(pro.getProjectName())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteProject (DeleteProjectRequest request, StreamObserver<ProjectAnswerResponse> responseObserver){
        if (!projectRepository.existsById(request.getId())) {
            responseObserver.onError(Status.NOT_FOUND.withDescription("Project not found").asRuntimeException());
            return;
        }

        projectRepository.deleteById(request.getId());

        ProjectAnswerResponse response = ProjectAnswerResponse.newBuilder()
                .setAnswer("Project deleted")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
