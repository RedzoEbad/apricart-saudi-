package com.apricart.consumer.enity;

import com.apricart.consumer.security.dto.request.FeedBackRequestDTO;
import com.apricart.consumer.security.dto.response.FeedBackResponseDTO;
import com.apricart.consumer.security.enums.StatusType;
import com.apricart.consumer.security.enums.LanguageType;
import lombok.*;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Table(name = "FEEDBACK")
public class FeedBack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String phoneNumber;

    @Column
    private String email;

    @Column
    private String message;

    @Column
    private String lang;

    @Enumerated(EnumType.STRING)
    private StatusType feedbackStatus;

    public static FeedBackResponseDTO toDTO(FeedBack feedBack) {
        return FeedBackResponseDTO.builder()
                .id(feedBack.getId())
                .name(feedBack.getName())
                .phoneNumber(feedBack.getPhoneNumber())
                .email(feedBack.getEmail())
                .message(feedBack.getMessage())
                .feedbackStatus(feedBack.getFeedbackStatus())
                .build();
    }

    public static List<FeedBackResponseDTO> toDTOList(List<FeedBack> feedBacks) {
        return feedBacks.stream()
                .map(FeedBack::toDTO)
                .filter(dto -> dto.getPhoneNumber() != null)
                .sorted(Comparator.comparing(FeedBackResponseDTO::getPhoneNumber))
                .collect(Collectors.toList());
    }

    public static FeedBack fromDTO(FeedBackRequestDTO feedBack, LanguageType lang) {
        return FeedBack.builder()
                .name(feedBack.getName())
                .phoneNumber(feedBack.getPhoneNumber())
                .email(feedBack.getEmail())
                .message(feedBack.getMessage())
                .lang(lang.toString())
                .feedbackStatus(feedBack.getFeedbackStatus())
                .build();
    }
}
