package com.apricart.consumer.service;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Service
public class ReindexingService {

    private static final Logger logger = LoggerFactory.getLogger(ReindexingService.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Scheduled(cron = "0 0 */6 * * 1-7")
    public void reindex() {
        try {
            FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
            fullTextEntityManager.createIndexer().startAndWait();
            logger.info("Reindexing completed successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during reindexing.", e);
        }
    }
}
