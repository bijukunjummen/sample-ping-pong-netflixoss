package org.bk.samplepong;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpMethod;
import io.reactivex.netty.RxNetty;
import io.reactivex.netty.channel.StringTransformer;
import io.reactivex.netty.protocol.http.client.HttpClient;
import io.reactivex.netty.protocol.http.client.HttpClientRequest;
import org.bk.samplepong.app.SamplePongApp;
import org.bk.samplepong.domain.Message;
import org.bk.samplepong.domain.MessageAcknowledgement;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.assertj.core.api.StrictAssertions.assertThat;

public class SampleIntegrationTest {

    private static final int PORT = 8080;
    private static final SamplePongApp app = new SamplePongApp(PORT);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeClass
    public static void beforTests() {
        app.server().start();
    }

    @AfterClass
    public static void afterTests() {
        app.server().shutdown();
    }

    @Test
    public void testSampleCallToEndpoint() throws Exception {
        HttpClient<String, ByteBuf> client = RxNetty.<String, ByteBuf>newHttpClientBuilder("localhost", PORT).build();
        HttpClientRequest<String> request = HttpClientRequest.create(HttpMethod.POST, "/message");
        String message = objectMapper.writeValueAsString(new Message("1", "Ping"));
        request.withRawContent(message, StringTransformer.DEFAULT_INSTANCE);
        final MessageAcknowledgement reply = client.submit(request)
                .flatMap(response -> response.getContent())
                .map(byteBuf -> {
                    try {
                        return objectMapper.readValue(byteBuf.toString(Charset.forName("UTF-8")), MessageAcknowledgement.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).toBlocking().single();
        assertThat(reply.getPayload()).isEqualTo("Pong");
    }
}
