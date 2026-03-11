package com.apricart.consumer.enity;

import com.apricart.consumer.security.dto.dto.UserDto;
import com.apricart.consumer.security.enums.UserRole;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created on Feb, 2024
 *
 * @author Kashaf Arshad
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"password", "accessToken", "salePerson", "city"})
@Table(name = "CUSTOMER")
public class Customer  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String arabicName;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    @NonNull
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Nullable
    private String accessToken = null;

    @Nullable
    private Boolean isActive ;

    @Nullable
    @Column(unique = true)
    private String phoneNumber;

    @Column
    private String ipAddress;

    @Column
    private String tradelicense;

    @Column
    private String typeOfBusiness;

    @Column(name = "create_date_time")
    @CreationTimestamp
    private LocalDateTime createDateTime;

    @Column(name = "update_date_time")
    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_person_id")
    private SalePerson salePerson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    @Column
    private String customerId; //ZOHO

    public static UserDto toDTO(Customer customer) {
        return UserDto.builder()
                .name(customer.getName())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .isActive(customer.getIsActive())
                .build();
    }
}
