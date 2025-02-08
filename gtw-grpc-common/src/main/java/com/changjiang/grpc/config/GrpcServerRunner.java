package com.changjiang.grpc.config;

import com.changjiang.grpc.annotation.GrpcService;
import com.changjiang.grpc.lib.GrpcServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.SmartLifecycle;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class GrpcServerRunner implements SmartLifecycle {
    private static final Logger logger = LoggerFactory.getLogger(GrpcServerRunner.class);

    private final GrpcServerConfig config;
    private final ApplicationContext applicationContext;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private Server server;

    public GrpcServerRunner(GrpcServerConfig config, ApplicationContext applicationContext) {
        this.config = config;
        this.applicationContext = applicationContext;
    }

    @Override
    public void start() {
        if (running.get()) {
            return;
        }

        try {
            ServerBuilder<?> serverBuilder = ServerBuilder.forPort(config.getPort());

            // 注册所有的gRPC服务实现
            Map<String, Object> grpcServices = applicationContext.getBeansWithAnnotation(GrpcService.class);
            for (Object grpcService : grpcServices.values()) {
                if (grpcService instanceof GrpcServiceGrpc.GrpcServiceImplBase) {
                    serverBuilder.addService((GrpcServiceGrpc.GrpcServiceImplBase) grpcService);
                    logger.info("Registered gRPC service: {}", grpcService.getClass().getName());
                }
            }

            server = serverBuilder.build().start();
            running.set(true);

            logger.info("gRPC Server started, listening on port {}", config.getPort());

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("Shutting down gRPC server...");
                GrpcServerRunner.this.stop();
                logger.info("gRPC server shut down completed.");
            }));
        } catch (IOException e) {
            throw new RuntimeException("Failed to start gRPC server", e);
        }
    }

    @Override
    public void stop() {
        if (server != null) {
            server.shutdown();
            try {
                server.awaitTermination();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("gRPC server interrupted during shutdown", e);
            }
            running.set(false);
        }
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        stop();
        callback.run();
    }
} 