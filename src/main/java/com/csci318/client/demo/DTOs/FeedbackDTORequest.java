package com.csci318.client.demo.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FeedbackDTORequest {
    private UUID restaurantId;
    private UUID userId;
    private Integer rating;
    private String comments;
}
