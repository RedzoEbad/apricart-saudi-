//package com.apricart.consumer.service.Impl;
//
//import com.apricart.consumer.enity.Product;
//import com.apricart.consumer.enity.SearchTag;
//import com.apricart.consumer.exceptions.ResourceNotFoundException;
//import com.apricart.consumer.repository.jpa.SearchTagRepository;
//import com.apricart.consumer.security.dto.request.SearchTagRequestDTO;
//import com.apricart.consumer.security.enums.LanguageType;
//import com.apricart.consumer.service.ProductService;
//import com.apricart.consumer.service.SearchTagService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class SearchTagServiceImpl implements SearchTagService {
//    protected static final Logger LOGGER = LoggerFactory.getLogger(SearchTagServiceImpl.class);
//
//    private static final String SEARCH_TAG_ENG = "Search Tag";
//    private static final String SEARCH_TAG_ARB = "علامة البحث";
//
//    @Autowired
//    private SearchTagRepository searchTagRepository;
//
//    @Autowired
//    private ProductService productService;
//
//    @Override
//    public SearchTag createSearchTag(SearchTagRequestDTO searchTagRequestDTO, LanguageType languageType) {
//        LOGGER.info("Adding search tag: {}", searchTagRequestDTO);
//        SearchTag searchTag = SearchTag.fromDTO(searchTagRequestDTO);
//        searchTag.setProduct(productService.findById(searchTagRequestDTO.getProductId(), languageType));
//        searchTag.setTags(searchTagRequestDTO.getTags());
//        return save(searchTag);
//    }
//
//    @Override
//    public SearchTag updateSearchTag(Long id, SearchTagRequestDTO searchTag, LanguageType languageType) {
//        LOGGER.info("Updating search tag: {}", searchTag);
//        Optional<SearchTag> existingSearchTag = searchTagRepository.findById(id);
//        if (existingSearchTag.isPresent()) {
//            SearchTag updatedSearchTag = existingSearchTag.get();
//            updatedSearchTag.setStatus(searchTag.getStatus() != null ? searchTag.getStatus() : updatedSearchTag.getStatus());
//            updatedSearchTag.setTags(searchTag.getTags() != null ? searchTag.getTags() : updatedSearchTag.getTags());
//            updatedSearchTag.setProduct(searchTag.getProductId() != null ? productService.findById(searchTag.getProductId(), languageType) : updatedSearchTag.getProduct());
//            return searchTagRepository.save(updatedSearchTag);
//        } else {
//            throw new RuntimeException("SearchTag not found with id " + id);
//        }
//    }
//
//    @Override
//    public SearchTag getSearchTagById(Long id, LanguageType languageType) {
//        LOGGER.info("Finding search tag by id: {}", id);
//        return searchTagRepository.findById(id).orElseThrow(() -> {
//            LOGGER.error("Search Tag with id {} not found", id);
//            return LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(SEARCH_TAG_ARB, id, true) : new ResourceNotFoundException(SEARCH_TAG_ENG, id, false);
//        });
//    }
//
//    @Override
//    public SearchTag getSearchTagsByProductId(Long productId, LanguageType languageType) {
//        LOGGER.info("Finding search tag by product id: {}", productId);
//        Product product = productService.findById(productId, languageType);
//        return searchTagRepository.findSearchTagByProduct(product);
//    }
//
//    @Override
//    public List<SearchTag> getActiveSearchTags(LanguageType lang) {
//        LOGGER.info("Getting active search tags");
//        return searchTagRepository.findAll()
//                .stream()
//                .filter(SearchTag::getStatus)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<SearchTag> getAllSearchTags(LanguageType languageType) {
//        LOGGER.info("Getting all search tags");
//        return searchTagRepository.findAll();
//    }
//
//    @Override
//    public void deleteSearchTag(Long id, LanguageType languageType) {
//        LOGGER.info("Removing search tags for id: {}", id);
//        if (searchTagRepository.existsById(id)) {
//            searchTagRepository.deleteById(id);
//        } else {
//            throw new RuntimeException("SearchTag not found with id " + id);
//        }
//    }
//
//    public SearchTag save(SearchTag searchTag) {
//        LOGGER.info("Saving Search Tag: {}", searchTag);
//        return searchTagRepository.save(searchTag);
//    }
//}
