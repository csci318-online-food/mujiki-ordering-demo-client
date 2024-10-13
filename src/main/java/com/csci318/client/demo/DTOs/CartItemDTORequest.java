package com.csci318.client.demo.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartItemDTORequest {
    private UUID restaurantId;  // Restaurant ID associated with the item
    private UUID itemId;        // The ID of the item being added to the cart
}
