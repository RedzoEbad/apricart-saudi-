package com.apricart.consumer.enity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;

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
@Table(name = "USER_PORTAL")
public class UserPortal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String username;

    private String password;
    @Nullable
    private String phoneNumber;

    @Column(unique = true)
    private String email;

    @Nullable
    private String accessToken;

    @Nullable
    private Boolean isActive;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Roles roles;

}
