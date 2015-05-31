package org.bk.samplepong.app;

import netflix.adminresources.resources.KaryonWebAdminModule;
import netflix.karyon.Karyon;
import netflix.karyon.KaryonBootstrapModule;
import netflix.karyon.ShutdownModule;
import netflix.karyon.archaius.ArchaiusBootstrapModule;
import netflix.karyon.eureka.KaryonEurekaModule;
import netflix.karyon.servo.KaryonServoModule;
import netflix.karyon.transport.http.health.HealthCheckEndpoint;
import org.bk.samplepong.resource.HealthCheck;

public class SamplePongApp {

    public static void main(String[] args) {
        HealthCheck healthCheckHandler = new HealthCheck();
        Karyon.forRequestHandler(8888,
                new RxNettyHandler("/healthcheck",
                        new HealthCheckEndpoint(healthCheckHandler)),
                new KaryonBootstrapModule(healthCheckHandler),
                new ArchaiusBootstrapModule("sample-pong"),
                KaryonEurekaModule.asBootstrapModule(),
                Karyon.toBootstrapModule(KaryonWebAdminModule.class),
                ShutdownModule.asBootstrapModule(),
                KaryonServoModule.asBootstrapModule()
        ).startAndWaitTillShutdown();
    }
}
