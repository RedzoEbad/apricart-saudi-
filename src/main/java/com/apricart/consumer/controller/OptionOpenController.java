package com.apricart.consumer.controller;

import com.apricart.consumer.enity.Option;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.dto.request.OptionRequestDTO;
import com.apricart.consumer.security.dto.response.OptionResponseDTO;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.OptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.OPTION_DISABLED_SUCCESSFULLY_ARABIC;
import static com.apricart.consumer.security.constants.ResponseMessage.OPTION_DISABLED_SUCCESSFULLY;

@RestController
@RequestMapping("/v1/auth/open/options")
@Api(value = "Option Controller", tags = {"Option"})
public class OptionOpenController {

    @Autowired
    private OptionService optionService;

    @ApiOperation(value = "Get all options")
    @GetMapping
    public ResponseEntity<GenericResponse<List<OptionResponseDTO>>> getAllOptions(@RequestHeader("Language") LanguageType lang) {
        List<OptionResponseDTO> options = Option.toDTOList(optionService.getAllOptions());
        return !options.isEmpty() ? Response.success(options) : Response.notFound();
    }

    @ApiOperation(value = "Get Option by key")
    @GetMapping("/key/{key}")
    public ResponseEntity<GenericResponse<OptionResponseDTO>> findOptionByKey(@PathVariable String key,
                                                                              @RequestHeader("Language") LanguageType lang) {
        Option option = optionService.findByKey(key);
        return option != null ? Response.success(Option.toDTO(option)) : Response.notFound();
    }

    @ApiOperation(value = "Get Option value by key")
    @GetMapping("/value/{key}")
    public ResponseEntity<GenericResponse<String>> findOptionValueByKey(@PathVariable String key,
                                                                        @RequestHeader("Language") LanguageType lang) {
        String optionValue = optionService.findValueByKey(key, lang);
        return optionValue != null ? Response.success(optionValue) : Response.notFound();
    }

    @ApiOperation(value = "Get Option by Id")
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<OptionResponseDTO>> findOptionById(@PathVariable Long id,
                                                                             @RequestHeader("Language") LanguageType lang) {
        Option option = optionService.findById(id, lang);
        return option != null ? Response.success(Option.toDTO(option)) : Response.notFound();
    }

    @ApiOperation(value = "Get all active options")
    @GetMapping("/active")
    public ResponseEntity<GenericResponse<List<OptionResponseDTO>>> getActiveOptions(@RequestHeader("Language") LanguageType lang) {
        List<OptionResponseDTO> optionList = Option.toDTOList(optionService.getActiveOptions(lang));
        return !optionList.isEmpty() ? Response.success(optionList) : Response.notFound();
    }

    @ApiOperation(value = "Add Option")
    @PostMapping
    public ResponseEntity<GenericResponse<OptionResponseDTO>> addOption(@Valid @RequestBody OptionRequestDTO optionRequestDTO,
                                                                        @RequestHeader("Language") LanguageType lang) {
        optionService.addOption(optionRequestDTO);
        return Response.created();
    }

    @ApiOperation(value = "Update Option")
    @PutMapping
    public ResponseEntity<GenericResponse<OptionResponseDTO>> updateOption(@Valid @RequestBody OptionRequestDTO optionRequestDTO,
                                                                           @RequestHeader("Language") LanguageType lang) {
        Option updateOption = optionService.updateOption(optionRequestDTO, lang);
        return updateOption != null ? Response.success(Option.toDTO(updateOption)) : Response.notFound();
    }

    @ApiOperation(value = "Delete option By Id")
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<String>> disableOption(@PathVariable Long id,
                                                                 @RequestHeader("Language") LanguageType lang) {
        optionService.disableOption(id, lang);
        return lang.equals(LanguageType.ARB) ? Response.success(OPTION_DISABLED_SUCCESSFULLY_ARABIC) : Response.success(OPTION_DISABLED_SUCCESSFULLY);
    }

    @ApiOperation(value = "Update Option Status By Id")
    @PutMapping("/{id}/{status}")
    public ResponseEntity<GenericResponse<OptionResponseDTO>> updateOptionStatusById(@PathVariable Long id,
                                                                                     @PathVariable Boolean status,
                                                                                     @RequestHeader("Language") LanguageType lang) {
        Option updatedOption = optionService.updateOptionStatusById(id, status, lang);
        return updatedOption != null ? Response.success(Option.toDTO(updatedOption)) : Response.notFound();
    }

    @ApiOperation(value = "Get Multiple Option values by keys")
    @GetMapping("/key/multiple")
    public ResponseEntity<GenericResponse<List<OptionResponseDTO>>> getMultipleOptionsByKeys(@RequestParam Map<String, String> keyMap,
                                                                                             @RequestHeader("Language") LanguageType lang) {
        List<String> keys = new ArrayList<>(keyMap.values());
        List<Option> options = optionService.findByKeys(keys);
        return !options.isEmpty() ? Response.success(Option.toDTOList(options)) : Response.notFound();
    }
}
