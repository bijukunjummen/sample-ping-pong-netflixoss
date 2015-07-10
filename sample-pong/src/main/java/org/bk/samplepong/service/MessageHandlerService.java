package org.bk.samplepong.service;

import org.bk.samplepong.domain.Message;
import org.bk.samplepong.domain.MessageAcknowledgement;
import rx.Observable;

public interface MessageHandlerService {
    Observable<MessageAcknowledgement> handleMessage(Message message);
}
