//package com.apricart.consumer.controller;
//
//import com.apricart.consumer.enity.SearchTag;
//import com.apricart.consumer.generic.GenericResponse;
//import com.apricart.consumer.generic.Response;
//import com.apricart.consumer.security.dto.request.SearchTagRequestDTO;
//import com.apricart.consumer.security.dto.response.SearchTagResponseDTO;
//import com.apricart.consumer.security.enums.LanguageType;
//import com.apricart.consumer.service.SearchTagService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//import java.util.List;
//
//import static com.apricart.consumer.security.constants.ArabicResponseMessages.BANNER_REMOVED_SUCCESSFULLY_ARABIC;
//import static com.apricart.consumer.security.constants.ResponseMessage.BANNER_REMOVED_SUCCESSFULLY;
//
//@RestController
//@RequestMapping("/v1/auth/open/search-tags")
//@Api(value = "SearchTag Controller", tags = {"SearchTag"})
//public class SearchTagController {
//
//    @Autowired
//    private SearchTagService searchTagService;
//
//    @ApiOperation(value = "Get all Search Tags")
//    @GetMapping
//    public ResponseEntity<GenericResponse<List<SearchTagResponseDTO>>> getAllSearchTags(@RequestHeader("Language") LanguageType lang) {
//        List<SearchTagResponseDTO> searchTagResponseDTOS = SearchTag.toDTOList(searchTagService.getAllSearchTags(lang));
//        return !searchTagResponseDTOS.isEmpty() ? Response.success(searchTagResponseDTOS) : Response.notFound();
//    }
//
//    @ApiOperation(value = "Get Search Tag by Id")
//    @GetMapping("/{id}")
//    public ResponseEntity<GenericResponse<SearchTagResponseDTO>> findSearchTagById(@PathVariable Long id, @RequestHeader("Language") LanguageType lang) {
//        SearchTag searchTag = searchTagService.getSearchTagById(id, lang);
//        return searchTag != null ? Response.success(SearchTag.toDTO(searchTag)) : Response.notFound();
//    }
//
//    @ApiOperation(value = "Get Search Tag by Product Id")
//    @GetMapping("/product/{productId}")
//    public ResponseEntity<GenericResponse<SearchTagResponseDTO>> findSearchTagByProductId(@PathVariable Long productId, @RequestHeader("Language") LanguageType lang) {
//        SearchTag searchTag = searchTagService.getSearchTagsByProductId(productId, lang);
//        return searchTag != null ? Response.success(SearchTag.toDTO(searchTag)) : Response.notFound();
//    }
//
//    @ApiOperation(value = "Get all Active Search Tags")
//    @GetMapping("/active")
//    public ResponseEntity<GenericResponse<List<SearchTagResponseDTO>>> getActiveSearchTags(@RequestHeader("Language") LanguageType lang) {
//        List<SearchTagResponseDTO> searchTags = SearchTag.toDTOList(searchTagService.getActiveSearchTags(lang));
//        return !searchTags.isEmpty() ? Response.success(searchTags) : Response.notFound();
//    }
//
//    @ApiOperation(value = "Add Search Tag")
//    @PostMapping
//    public ResponseEntity<GenericResponse<SearchTagResponseDTO>> addSearchTag(@Valid @RequestBody SearchTagRequestDTO searchTagRequestDTO, @RequestHeader("Language") LanguageType lang) {
//        return Response.created(SearchTag.toDTO(searchTagService.createSearchTag(searchTagRequestDTO, lang)));
//    }
//
//    @ApiOperation(value = "Update Search Tag")
//    @PutMapping
//    public ResponseEntity<GenericResponse<SearchTagResponseDTO>> updateSearchTag(@RequestBody SearchTagRequestDTO searchTagRequestDTO, @RequestHeader("Language") LanguageType lang, @RequestParam Long id) {
//        SearchTag updateSearchTag = searchTagService.updateSearchTag(id, searchTagRequestDTO, lang);
//        return updateSearchTag != null ? Response.success(SearchTag.toDTO(updateSearchTag)) : Response.notFound();
//    }
//
//    @ApiOperation(value = "Delete Search Tag By Id")
//    @DeleteMapping("/{id}")
//    public ResponseEntity<GenericResponse<String>> deleteSearchTag(@PathVariable Long id, @RequestHeader("Language") LanguageType lang) {
//        searchTagService.deleteSearchTag(id, lang);
//        return lang.equals(LanguageType.ARB) ? Response.success(BANNER_REMOVED_SUCCESSFULLY_ARABIC) : Response.success(BANNER_REMOVED_SUCCESSFULLY);
//    }
//}
