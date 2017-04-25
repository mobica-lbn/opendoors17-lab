package com.mobica.cloud.aws.db;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DbService {

    private final DbMessageRepository dbMessageRepository;

    public DbService(DbMessageRepository dbMessageRepository) {
        this.dbMessageRepository = dbMessageRepository;
    }

    public void saveMessage(DbMessage dbMessage) {
        dbMessageRepository.saveMessage(dbMessage);
    }

    public Optional<DbMessage> getMessageBy(Long id) {
        return dbMessageRepository.getMessage(id);
    }

    public void removeMessageBy(Long id) {
        dbMessageRepository.removeMessage(id);
    }
}
