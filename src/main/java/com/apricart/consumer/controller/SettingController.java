package com.apricart.consumer.controller;

import com.apricart.consumer.enity.Setting;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.dto.request.SettingRequestDTO;
import com.apricart.consumer.security.dto.response.FAQResponseDTO;
import com.apricart.consumer.security.dto.response.SettingResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.FAQService;
import com.apricart.consumer.service.SettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.SETTINGS_REMOVED_SUCCESSFULLY_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.SETTINGS_REMOVED_SUCCESSFULLY;

@RestController
@RequestMapping("/v1/auth/open/settings")
@Api(value = "Setting Controller", tags = {"Setting"})
public class SettingController {

    @Autowired
    private SettingService settingService;

    @Autowired
    private FAQService faqService;

    @ApiOperation(value = "Get all settings")
    @GetMapping
    public ResponseEntity<GenericResponse<List<SettingResponseDTO>>> getAllSettings(@RequestHeader("Language") LanguageType lang) {
        List<SettingResponseDTO> settings = Setting.toDTOList(settingService.getAllSettings(lang));
        return !settings.isEmpty() ? Response.success(settings) : Response.notFound();
    }
    @ApiOperation(value = "Get setting by Id")
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<SettingResponseDTO>> findSettingById(@PathVariable Long id, @RequestHeader("Language") LanguageType lang) {

        SettingResponseDTO setting = Setting.toDTO(settingService.findById(id, lang));
        List<FAQResponseDTO> faqsEnglish  = faqService.findBySettingIdAndLanguage(id, LanguageType.ENG);
        List<FAQResponseDTO> faqsArabic  = faqService.findBySettingIdAndLanguage(id, LanguageType.ARB);

        if(setting != null){
            setting.setFaqs(faqsEnglish != null && !faqsEnglish.isEmpty() ? faqsEnglish : Collections.emptyList());
            setting.setArabicFaqs(faqsArabic != null && !faqsArabic.isEmpty() ? faqsArabic : Collections.emptyList());
        }

        return setting != null ? Response.success(setting) : Response.notFound();
    }

    @ApiOperation(value = "Get setting by warehouseId")
    @GetMapping("/warehouse/{id}")
    public ResponseEntity<GenericResponse<List<SettingResponseDTO>>> findSettingByWarehouseId(@PathVariable Long id, @RequestHeader("Language") LanguageType lang) {

        List<SettingResponseDTO> settings = Setting.toDTOList(settingService.findByWarehouseId(id));

        if (settings == null || settings.isEmpty()) { return Response.notFound(); }

        for (SettingResponseDTO setting : settings) {
            List<FAQResponseDTO> faqsEnglish = faqService.findBySettingIdAndLanguage(setting.getId(), LanguageType.ENG);
            List<FAQResponseDTO> faqsArabic = faqService.findBySettingIdAndLanguage(setting.getId(), LanguageType.ARB);

            setting.setFaqs(faqsEnglish != null && !faqsEnglish.isEmpty() ? faqsEnglish : Collections.emptyList());
            setting.setArabicFaqs(faqsArabic != null && !faqsArabic.isEmpty() ? faqsArabic : Collections.emptyList());
        }

        return Response.success(settings);
    }

    @ApiOperation(value = "Add Setting")
    @PostMapping
    public ResponseEntity<GenericResponse<SettingResponseDTO>> addSetting(@Valid @RequestBody SettingRequestDTO settingRequestDTO, @RequestHeader("Language") LanguageType lang) {
        settingService.addSetting(settingRequestDTO);
        return Response.created();
    }

    @ApiOperation(value = "Update Setting")
    @PutMapping
    public ResponseEntity<GenericResponse<SettingResponseDTO>> updateSetting(@Valid @RequestBody SettingRequestDTO settingRequestDTO, @RequestHeader("Language") LanguageType lang) {
        SettingResponseDTO updatedSetting = Setting.toDTO(settingService.updateSetting(settingRequestDTO, lang));
        return updatedSetting != null ? Response.success(updatedSetting) : Response.notFound();
    }
    @ApiOperation(value = "Delete Setting By Id")
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<String>> deleteSetting(@PathVariable Long id, @RequestHeader("Language") LanguageType lang) {
        settingService.deleteSetting(id, lang);
        return lang.equals(LanguageType.ARB) ? Response.success(SETTINGS_REMOVED_SUCCESSFULLY_ARABIC) : Response.success(SETTINGS_REMOVED_SUCCESSFULLY);
    }
}