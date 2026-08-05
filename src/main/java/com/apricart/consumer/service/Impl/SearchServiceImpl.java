package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.ProductWarehouse;
import com.apricart.consumer.service.ReindexingService;
import com.apricart.consumer.service.SearchService;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    private static final String[] SEARCH_FIELDS = {"product.title", "product.description", "product.sku", "product.arabicTitle", "product.arabicDescription"};
    private static final String WAREHOUSE_SEARCH_FIELD = "warehouse.id";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ReindexingService reindexingService;

    @Async
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        logger.info("Starting async reindexing after application ready...");
        reindexingService.reindex();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductWarehouse> searchProduct(String query, Long warehouseId) {
        List<ProductWarehouse> results = Collections.emptyList();
        try {
            FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

            QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                    .buildQueryBuilder()
                    .forEntity(ProductWarehouse.class)
                    .get();

            String lowerCaseQuery = query.toLowerCase();

            Query warehouseQuery = queryBuilder
                    .keyword()
                    .onField(WAREHOUSE_SEARCH_FIELD)
                    .matching(warehouseId.toString())
                    .createQuery();

            Query textQuery = queryBuilder
                    .keyword()
                    .wildcard()
                    .onFields(SEARCH_FIELDS)
                    .matching("*" + lowerCaseQuery + "*")
                    .createQuery();

            Query fuzzyQuery = queryBuilder
                    .keyword()
                    .fuzzy()
                    .withEditDistanceUpTo(2)
                    .onFields(SEARCH_FIELDS)
                    .matching(lowerCaseQuery)
                    .createQuery();

            Query combinedQuery = queryBuilder
                    .bool()
                    .must(warehouseQuery)
                    .must(queryBuilder.bool()
                            .should(textQuery)
                            .should(fuzzyQuery)
                            .createQuery())
                    .createQuery();


            org.hibernate.search.jpa.FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(combinedQuery, ProductWarehouse.class);
            logger.info("Searching products with query: '{}'", jpaQuery);
            logger.info("Searching products with keyword: '{}'", query);
            results = jpaQuery.getResultList();
        } catch (Exception e) {
            logger.error("Error occurred while searching for products with keyword: '{}'", query, e);
        }
        return results;
    }
}
