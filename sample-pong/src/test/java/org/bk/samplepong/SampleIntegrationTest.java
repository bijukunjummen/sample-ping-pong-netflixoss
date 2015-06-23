package org.bk.samplepong;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpMethod;
import io.reactivex.netty.RxNetty;
import io.reactivex.netty.channel.StringTransformer;
import io.reactivex.netty.protocol.http.client.HttpClient;
import io.reactivex.netty.protocol.http.client.HttpClientRequest;
import org.bk.samplepong.domain.Message;
import org.junit.Test;

import java.nio.charset.Charset;

public class SampleIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testSampleCallToEndpoint() throws Exception {
        HttpClient<String, ByteBuf> client = RxNetty.<String, ByteBuf>newHttpClientBuilder("localhost", 8888).build();
        HttpClientRequest<String> request = HttpClientRequest.create(HttpMethod.POST, "/message");

        String message = objectMapper.writeValueAsString(new Message("1", "Ping"));

        request.withRawContent(message, StringTransformer.DEFAULT_INSTANCE);

        String responseFromServer = client.submit(request)
                .flatMap(response -> response.getContent())
                .map(data -> data.toString(Charset.defaultCharset()))
                .toBlocking().single();
        System.out.println(responseFromServer);
    }
}
