package org.bk.samplepong.config;

import io.netty.buffer.ByteBuf;
import netflix.karyon.transport.http.KaryonHttpModule;
import org.bk.samplepong.app.RxNettyHandler;

public class AppModule extends KaryonHttpModule<ByteBuf, ByteBuf> {

    public AppModule() {
        super("appModule", ByteBuf.class, ByteBuf.class);
    }

    @Override
    protected void configureServer() {
        bindRouter().to(RxNettyHandler.class);
        server().port(8888);
    }
}
