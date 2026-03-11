package com.apricart.consumer.controller;

import com.apricart.consumer.enity.FeedBack;
import com.apricart.consumer.generic.GenericResponse;
import com.apricart.consumer.generic.Response;
import com.apricart.consumer.security.dto.request.FeedBackRequestDTO;
import com.apricart.consumer.security.dto.response.FeedBackResponseDTO;
import com.apricart.consumer.security.enums.StatusType;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.FeedBackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/auth/open/feedbacks")
@Api(value = "FeedBack Controller", tags = {"FeedBack"})
public class FeedBackController {

    @Autowired
    private FeedBackService feedBackService;

    @ApiOperation(value = "Get all FeedBacks")
    @GetMapping
    public ResponseEntity<GenericResponse<List<FeedBackResponseDTO>>> getAllFeedBacks(
            @RequestHeader("Language") LanguageType lang) {
        List<FeedBackResponseDTO> feedbacks = FeedBack.toDTOList(feedBackService.findAllFeedBacks());
        return !feedbacks.isEmpty() ? Response.success(feedbacks) : Response.notFound();
    }

    @ApiOperation(value = "Add FeedBack")
    @PostMapping
    public ResponseEntity<GenericResponse<FeedBackResponseDTO>> addFeedBack(
            @Valid @RequestBody FeedBackRequestDTO feedBackRequestDTO,
            @RequestHeader("Language") LanguageType lang) {
        feedBackService.addFeedBack(feedBackRequestDTO, lang);
        return Response.created();
    }

    @ApiOperation(value = "Update FeedBack Status")
    @PutMapping("/{feedback_id}")
    public ResponseEntity<GenericResponse<FeedBackResponseDTO>> updateFeedBackStatus(
            @PathVariable("feedback_id") Long feedbackId,
            @RequestParam StatusType statusType,
            @RequestHeader("Language") LanguageType lang) {
        FeedBack feedBack = feedBackService.updateFeedBackStatus(statusType, feedbackId, lang);
        return feedBack != null ? Response.created(FeedBack.toDTO(feedBack)) : Response.notFound();
    }

}
