package fr.cca.messaging.controller;

import fr.cca.messaging.filepool.FileWriterPoolService;
import fr.cca.messaging.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
public class MessageReceiverController {

    @Autowired
    private FileWriterPoolService poolService;

    @RequestMapping(path = "", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void postMessage(@RequestBody Message m) throws Exception {
        poolService.treatMessage(m);
    }

}
