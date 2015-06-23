package org.bk.samplepong.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import io.reactivex.netty.protocol.http.server.RequestHandler;
import netflix.karyon.transport.http.health.HealthCheckEndpoint;
import org.bk.samplepong.domain.Message;
import org.bk.samplepong.domain.MessageAcknowledgement;
import rx.Observable;

import java.io.IOException;
import java.nio.charset.Charset;


public class RxNettyHandler implements RequestHandler<ByteBuf, ByteBuf> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Observable<Void> handle(HttpServerRequest<ByteBuf> request, HttpServerResponse<ByteBuf> response) {
        return request.getContent().map(byteBuf -> byteBuf.toString(Charset.forName("UTF-8")))
                .map(s -> {
                    try {
                        Message m = objectMapper.readValue(s, Message.class);
                        return m;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(m -> new MessageAcknowledgement(m.getId(), m.getPayload(), "Pong"))
                .flatMap(ack -> {
                            try {
                                return response.writeStringAndFlush(objectMapper.writeValueAsString(ack));
                            } catch (Exception e) {
                                response.setStatus(HttpResponseStatus.BAD_REQUEST);
                                return response.close();
                            }
                        }
                );
    }
}
