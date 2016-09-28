package org.bk.samplepong.app;

import io.netty.buffer.ByteBuf;
import netflix.adminresources.resources.KaryonWebAdminModule;
import netflix.karyon.Karyon;
import netflix.karyon.KaryonBootstrapModule;
import netflix.karyon.KaryonServer;
import netflix.karyon.ShutdownModule;
import netflix.karyon.archaius.ArchaiusBootstrapModule;
import netflix.karyon.servo.KaryonServoModule;
import netflix.karyon.transport.http.HttpInterceptorSupport;
import netflix.karyon.transport.http.HttpRequestHandler;
import netflix.karyon.transport.http.SimpleUriRouter;
import netflix.karyon.transport.http.health.HealthCheckEndpoint;
import org.bk.samplepong.common.LoggingInterceptor;
import org.bk.samplepong.common.health.HealthCheck;
import org.bk.samplepong.service.MessageHandlerServiceImpl;

public class SamplePongApp {
    private final KaryonServer server;

    public SamplePongApp(int port) {
        server = create(port);
    }

    private KaryonServer create(int port) {
        final HealthCheck healthCheckHandler = new HealthCheck();

        final SimpleUriRouter<ByteBuf, ByteBuf> router = new SimpleUriRouter<>();
        router.addUri("/healthcheck", new HealthCheckEndpoint(healthCheckHandler));
        router.addUri("/message", new ApplicationMessageHandler(new MessageHandlerServiceImpl()));

        final HttpInterceptorSupport<ByteBuf, ByteBuf> interceptorSupport = new HttpInterceptorSupport<>();
        interceptorSupport.forUri("/*").intercept(new LoggingInterceptor());
        return Karyon.forRequestHandler(port,
                new HttpRequestHandler<>(router, interceptorSupport),
                new KaryonBootstrapModule(healthCheckHandler),
                new ArchaiusBootstrapModule("sample-pong"),
//                KaryonEurekaModule.asBootstrapModule(),
                Karyon.toBootstrapModule(KaryonWebAdminModule.class),
                ShutdownModule.asBootstrapModule(),
                KaryonServoModule.asBootstrapModule()
        );
    }

    public KaryonServer server() {
        return server;
    }

    public static void main(String[] args) {
        new SamplePongApp(8888).server().startAndWaitTillShutdown();
    }
}
