package fr.cca.messaging.filepool;

import fr.cca.messaging.filepool.burk.FileWriterPool;
import fr.cca.messaging.filepool.ok.DataPooler;
import fr.cca.messaging.model.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FileWriterPoolService {

    @Value("${filepool.rootdir}")
    private String rootDir;

    @Value("${filepool.filename}")
    private String fileName;


    private Map<String, UUID> uuidMap = new ConcurrentHashMap<String, UUID>();
    private Map<UUID, IFileWriterPool> fileWriterPoolMap = new ConcurrentHashMap<>();

    public void treatMessage(Message m) throws Exception {
        IFileWriterPool fwp;
        switch (m.getType()){
            case START:
                checkUUIDisNew(m.getUuid());
                UUID uuid = UUID.randomUUID();
                uuidMap.put(m.getUuid(),uuid);
                fwp = new DataPooler();
                fileWriterPoolMap.put(uuid,fwp);
                Path baseDirectory = Files.createDirectories(Paths.get(rootDir, uuid.toString()));
                fwp.start(baseDirectory.toString() ,fileName);
                break;
            case DATA:
                checkUUIDExists(m.getUuid());
                fwp = fileWriterPoolMap.get(uuidMap.get(m.getUuid()));
                fwp.writeRow(m.getData().getMessageStr());
                break;
            case END:
                checkUUIDExists(m.getUuid());
                fwp = fileWriterPoolMap.get(uuidMap.get(m.getUuid()));
                fwp.close();
                fileWriterPoolMap.remove(uuidMap.get(m.getUuid()));
                uuidMap.remove(m.getUuid());
                break;
        }


    }

    private void checkUUIDisNew(String uuid) {
        if (uuidMap.containsKey(uuid)){
            throw new RuntimeException(String.format("This uuid was already used. You cannot start a new process with uuid : %s", uuid));
        }
    }

    private void checkUUIDExists(String uuid) {
        if (!uuidMap.containsKey(uuid)){
            throw new RuntimeException(String.format("Process with UUID %s does not exist. Start the process first", uuid));
        }
    }
}
