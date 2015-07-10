package org.bk.samplepong.app;

import com.netflix.governator.guice.BootstrapModule;
import io.netty.buffer.ByteBuf;
import netflix.adminresources.resources.KaryonWebAdminModule;
import netflix.karyon.Karyon;
import netflix.karyon.KaryonBootstrapModule;
import netflix.karyon.KaryonRunner;
import netflix.karyon.ShutdownModule;
import netflix.karyon.archaius.ArchaiusBootstrapModule;
import netflix.karyon.servo.KaryonServoModule;
import netflix.karyon.transport.http.HttpInterceptorSupport;
import netflix.karyon.transport.http.HttpRequestHandler;
import netflix.karyon.transport.http.SimpleUriRouter;
import netflix.karyon.transport.http.health.HealthCheckEndpoint;
import org.bk.samplepong.common.LoggingInterceptor;
import org.bk.samplepong.common.health.HealthCheck;
import org.bk.samplepong.governator.SamplePongAppGovernator;
import org.bk.samplepong.service.MessageHandlerServiceImpl;

public class SamplePongGovernatorAppRunner {

    public static void main(String[] args) {
        Karyon.forApplication(SamplePongAppGovernator.class, (BootstrapModule[]) null)
                .startAndWaitTillShutdown();
    }
}
