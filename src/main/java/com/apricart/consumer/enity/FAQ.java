package com.apricart.consumer.enity;

import com.apricart.consumer.security.dto.request.FAQRequestDTO;
import com.apricart.consumer.security.dto.response.FAQResponseDTO;
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
@AllArgsConstructor
@Table(name = "FAQ")
public class FAQ extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String question;

    @Column
    private String answer;

    @Enumerated(EnumType.STRING)
    private LanguageType languageType;

    @Column
    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setting_id")
    private Setting setting;

    public static FAQResponseDTO toDTO(FAQ faq) {
        return FAQResponseDTO.builder()
                .id(faq.getId())
                .question(faq.getQuestion())
                .answer(faq.getAnswer())
                .languageType(faq.getLanguageType())
                .status(faq.getStatus())
                .build();
    }

    public static List<FAQResponseDTO> toDTOList(List<FAQ> faqs) {
        return faqs.stream()
                .sorted(Comparator.comparingLong(FAQ::getId))
                .filter(FAQ::getStatus)
                .map(FAQ::toDTO)
                .collect(Collectors.toList());
    }
    public static FAQ fromDTO(FAQRequestDTO requestDTO) {
        return FAQ.builder()
                .question(requestDTO.getQuestion()  )
                .answer(requestDTO.getAnswer())
                .status(requestDTO.getStatus())
                .build();
    }
}
