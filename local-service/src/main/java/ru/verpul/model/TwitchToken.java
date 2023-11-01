package ru.verpul.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class TwitchToken extends BaseEntity {

    @Column(name = "access_token", nullable = false)
    @JsonProperty(value = "access_token")
    private String accessToken;

    @Column(name = "refresh_token", nullable = false)
    @JsonProperty(value = "refresh_token")
    private String refreshToken;

}
