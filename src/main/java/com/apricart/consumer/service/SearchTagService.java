//package com.apricart.consumer.service;
//
//import com.apricart.consumer.enity.SearchTag;
//import com.apricart.consumer.security.dto.request.SearchTagRequestDTO;
//import com.apricart.consumer.security.enums.LanguageType;
//
//import java.util.List;
//
//public interface SearchTagService {
//
//    SearchTag createSearchTag(SearchTagRequestDTO searchTagRequestDTO, LanguageType languageType);
//    SearchTag updateSearchTag(Long id, SearchTagRequestDTO searchTag, LanguageType languageType);
//    SearchTag getSearchTagById(Long id, LanguageType languageType);
//    SearchTag getSearchTagsByProductId(Long productId, LanguageType languageType);
//    List<SearchTag> getActiveSearchTags(LanguageType lang);
//    List<SearchTag> getAllSearchTags(LanguageType languageType);
//    void deleteSearchTag(Long id, LanguageType languageType);
//}
