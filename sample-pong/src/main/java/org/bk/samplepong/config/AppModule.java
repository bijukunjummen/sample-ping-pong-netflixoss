package org.bk.samplepong.config;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import org.bk.samplepong.service.MessageHandlerService;
import org.bk.samplepong.service.MessageHandlerServiceImpl;

public class AppModule extends AbstractModule {


    @Override
    protected void configure() {
        bind(MessageHandlerService.class).to(MessageHandlerServiceImpl.class).in(Scopes.SINGLETON);
    }
}
