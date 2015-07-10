package org.bk.samplepong.governator;

import com.google.inject.Singleton;
import com.netflix.governator.annotations.Modules;
import netflix.adminresources.resources.KaryonWebAdminModule;
import netflix.karyon.KaryonBootstrap;
import netflix.karyon.ShutdownModule;
import netflix.karyon.archaius.ArchaiusBootstrap;
import netflix.karyon.servo.KaryonServoModule;
import org.bk.samplepong.common.health.HealthCheck;
import org.bk.samplepong.config.AppModule;
import org.bk.samplepong.config.KaryonAppModule;

@ArchaiusBootstrap
@KaryonBootstrap(name = "sample-pong", healthcheck = HealthCheck.class)
@Singleton
@Modules(include = {
        ShutdownModule.class,
        KaryonServoModule.class,
        KaryonWebAdminModule.class,
        // KaryonEurekaModule.class, // Uncomment this to enable Eureka client.
        AppModule.class,
        KaryonAppModule.class
})
public interface SamplePongAppGovernator {}
