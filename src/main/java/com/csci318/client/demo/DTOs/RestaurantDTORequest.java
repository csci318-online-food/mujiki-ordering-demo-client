package com.csci318.client.demo.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

import com.csci318.client.demo.Constants.CuisineType;
import com.csci318.client.demo.Domain.ValueObjs.PhoneNumber;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RestaurantDTORequest {
    private String name;
    private PhoneNumber phone;
    private String email;
    private String password;
    private CuisineType cuisine;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Double rating;
    private String description;
}
