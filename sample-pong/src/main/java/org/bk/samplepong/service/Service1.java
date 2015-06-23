package org.bk.samplepong.service;

import com.netflix.governator.annotations.AutoBindSingleton;
import org.bk.samplepong.domain.Message;
import org.bk.samplepong.domain.MessageAcknowledgement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@AutoBindSingleton
public class Service1 {
    private static final Logger logger = LoggerFactory.getLogger(Service1.class);

    private final ExecutorService customObservableExecutor = Executors.newFixedThreadPool(250);

    public Observable<MessageAcknowledgement> handleMessage(Message message) {
        return Observable.<MessageAcknowledgement>create(s -> {
            s.onNext(new MessageAcknowledgement(message.getId(), message.getPayload(), "Pong"));
            s.onCompleted();
        });
    }


}
