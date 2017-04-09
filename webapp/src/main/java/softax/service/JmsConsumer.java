package softax.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class JmsConsumer {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @JmsListener(destination = "jmsQueue", containerFactory = "jmsFactory")
    public void receiveMessage(String msg) {
        log.debug("Received path to process: {}.", msg);
    }

}
