package com.apricart.consumer.service.Impl;

import com.apricart.consumer.enity.FeedBack;
import com.apricart.consumer.exceptions.ResourceNotFoundException;
import com.apricart.consumer.repository.jpa.FeedBackRepository;
import com.apricart.consumer.security.dto.request.FeedBackRequestDTO;
import com.apricart.consumer.security.enums.StatusType;
import com.apricart.consumer.security.enums.LanguageType;
import com.apricart.consumer.service.FeedBackService;
import com.apricart.consumer.utils.EmailUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.apricart.consumer.security.constants.ArabicResponseMessages.FEEDBACK_NOT_SENT_ARABIC;
import static com.apricart.consumer.security.constants.Constants.EMAIL_SUBJECT_FEEDBACK_SUBMISSION;
import static com.apricart.consumer.security.constants.ResponseMessage.FEEDBACK_NOT_SENT;

@Service
public class FeedBackServiceImpl implements FeedBackService {
    protected static final Logger LOGGER = LoggerFactory.getLogger(FeedBackServiceImpl.class);
    private static final String FEEDBACK_ENG = "Feedback";
    private static final String FEEDBACK_ARB = "ملاحظات";

    @Autowired
    EmailUtils emailUtils;

    @Autowired
    private FeedBackRepository feedBackRepository;

    @Override
    public void addFeedBack(FeedBackRequestDTO dto, LanguageType lang) {
        FeedBack feedBack = FeedBack.fromDTO(dto, lang);

        if (dto.getFeedbackStatus() == null) {
            feedBack.setFeedbackStatus(StatusType.OPEN);
        }

        save(feedBack);

        EmailUtils.SUBJECT = EMAIL_SUBJECT_FEEDBACK_SUBMISSION;

        sendFeedbackSubmissionEmail(dto);
    }

    private void sendFeedbackSubmissionEmail(FeedBackRequestDTO dto) {
        try {
            emailUtils.sendFeedBackSubmitEmail(dto.getEmail(), dto.getName());
        } catch (Exception e) {
            String errorMessage = String.format("%s / %s",
                    String.format(FEEDBACK_NOT_SENT, dto.getEmail()),
                    String.format(FEEDBACK_NOT_SENT_ARABIC, dto.getEmail()));
            throw new RuntimeException(errorMessage, e);
        }
    }

    @Override
    public FeedBack updateFeedBackStatus(StatusType statusType, Long feedBackId, LanguageType lang) {
        FeedBack existingFeedBack = findById(feedBackId, lang);
        existingFeedBack.setFeedbackStatus(statusType != null ? statusType : existingFeedBack.getFeedbackStatus());
        return save(existingFeedBack);
    }

    public FeedBack save(FeedBack feedBack) {
        LOGGER.info("Saving feedback: {}", feedBack);
        return feedBackRepository.save(feedBack);
    }

    public List<FeedBack> findAllFeedBacks() {
        LOGGER.info("Getting all feedbacks");
        return feedBackRepository.findAll();
    }

    @Override
    public FeedBack findById(Long id, LanguageType languageType) {
        LOGGER.info("Getting all feedbacks");
        return feedBackRepository.findById(id).orElseThrow(() -> {
            LOGGER.error("Feedback with id {} not found", id);
            return LanguageType.ARB.equals(languageType) ? new ResourceNotFoundException(FEEDBACK_ARB, id, true) : new ResourceNotFoundException(FEEDBACK_ENG, id, false);
        });
    }
}
