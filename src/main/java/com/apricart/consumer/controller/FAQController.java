package com.apricart.consumer.controller;

import com.apricart.consumer.enity.FAQ;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.dto.request.FAQRequestDTO;
import com.apricart.consumer.security.dto.response.FAQResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.FAQService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.FAQ_REMOVED_SUCCESSFULLY_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.FAQ_REMOVED_SUCCESSFULLY;

@RestController
@RequestMapping("/v1/auth/open/faqs")
@Api(value = "FAQ Controller", tags = {"FAQ"})
public class FAQController {

    @Autowired
    private FAQService faqService;

    @ApiOperation(value = "Get all FAQs")
    @GetMapping
    public ResponseEntity<GenericResponse<List<FAQResponseDTO>>> getAllFAQs(@RequestHeader("Language") LanguageType lang) {
        List<FAQResponseDTO> faqs = FAQ.toDTOList(faqService.getAllFAQs(lang));
        return !faqs.isEmpty() ? Response.success(faqs) : Response.notFound();
    }
    @ApiOperation(value = "Get FAQ by Id")
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<FAQResponseDTO>> findFAQById(@PathVariable Long id,
                                                                       @RequestHeader("Language") LanguageType lang) {
        FAQResponseDTO faq = FAQ.toDTO(faqService.findById(id, lang));
        return faq != null ? Response.success(faq) : Response.notFound();
    }

    @ApiOperation(value = "Get FAQ by SettingId")
    @GetMapping("/setting/{id}")
    public ResponseEntity<GenericResponse<List<FAQResponseDTO>>> findFAQBySettingId(@PathVariable Long id, @RequestHeader("Language") LanguageType lang) {
        List<FAQResponseDTO> faqs = faqService.findBySettingIdAndLanguage(id, lang);
        return !faqs.isEmpty() ? Response.success(faqs) : Response.notFound();
    }

    @ApiOperation(value = "Add FAQ")
    @PostMapping
    public ResponseEntity<GenericResponse<FAQResponseDTO>> addFAQ(@Valid @RequestBody FAQRequestDTO faqRequestDTO,
                                                                  @RequestHeader("Language") LanguageType lang, @RequestParam("SettingId") Long settingId) {
        faqService.addFaq(faqRequestDTO,settingId, lang);
        return Response.created();
    }

    @ApiOperation(value = "Update FAQ")
    @PutMapping
    public ResponseEntity<GenericResponse<FAQResponseDTO>> updateFAQ(@Valid @RequestBody FAQRequestDTO faqRequestDTO, @RequestHeader("Language") LanguageType lang) {
        FAQResponseDTO updatedFAQ = FAQ.toDTO(faqService.updateFaq(faqRequestDTO, lang));
        return updatedFAQ != null ? Response.success(updatedFAQ) : Response.notFound();
    }
    @ApiOperation(value = "Delete FAQ By Id")
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<String>> deleteSetting(@PathVariable Long id, @RequestHeader("Language") LanguageType lang) {
        faqService.deleteFaq(id, lang);
        return lang.equals(LanguageType.ARB) ? Response.success(FAQ_REMOVED_SUCCESSFULLY_ARABIC) : Response.success(FAQ_REMOVED_SUCCESSFULLY);
    }
}
