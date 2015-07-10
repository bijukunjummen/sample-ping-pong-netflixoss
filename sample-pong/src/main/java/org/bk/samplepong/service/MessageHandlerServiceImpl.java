package org.bk.samplepong.service;

import org.bk.samplepong.domain.Message;
import org.bk.samplepong.domain.MessageAcknowledgement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

public class MessageHandlerServiceImpl implements MessageHandlerService {
    private static final Logger logger = LoggerFactory.getLogger(MessageHandlerServiceImpl.class);

    public Observable<MessageAcknowledgement> handleMessage(Message message) {
        return Observable.<MessageAcknowledgement>create(s -> {
            s.onNext(new MessageAcknowledgement(message.getId(), message.getPayload(), "Pong"));
            s.onCompleted();
        });
    }


}
