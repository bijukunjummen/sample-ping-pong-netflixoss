package org.bk.samplepong.app;

import io.netty.buffer.ByteBuf;
import netflix.adminresources.resources.KaryonWebAdminModule;
import netflix.karyon.Karyon;
import netflix.karyon.KaryonBootstrapModule;
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

    public static void main(String[] args) {
        HealthCheck healthCheckHandler = new HealthCheck();

        SimpleUriRouter<ByteBuf, ByteBuf> router = new SimpleUriRouter<>();

        router.addUri("/healthcheck", new HealthCheckEndpoint(healthCheckHandler));
        router.addUri("/message", new ApplicationMessageHandler(new MessageHandlerServiceImpl()));

        HttpInterceptorSupport<ByteBuf, ByteBuf> interceptorSupport = new HttpInterceptorSupport<>();
        interceptorSupport.forUri("/*").intercept(new LoggingInterceptor());

        Karyon.forRequestHandler(8888,
                new HttpRequestHandler<>(router, interceptorSupport),
                new KaryonBootstrapModule(healthCheckHandler),
                new ArchaiusBootstrapModule("sample-pong"),
//                KaryonEurekaModule.asBootstrapModule(),
                Karyon.toBootstrapModule(KaryonWebAdminModule.class),
                ShutdownModule.asBootstrapModule(),
                KaryonServoModule.asBootstrapModule()
        ).startAndWaitTillShutdown();
    }
}
