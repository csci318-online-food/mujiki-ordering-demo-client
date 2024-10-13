package com.csci318.client.demo.Functionality;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.csci318.client.demo.Constants.CuisineType;
import com.csci318.client.demo.Constants.OrderStatus;
import com.csci318.client.demo.Constants.Service;
import com.csci318.client.demo.DTOs.CartDTORequest;
import com.csci318.client.demo.DTOs.CartItemDTORequest;
import com.csci318.client.demo.DTOs.FeedbackDTORequest;
import com.csci318.client.demo.DTOs.ItemDTORequest;
import com.csci318.client.demo.DTOs.PaymentDTORequest;
import com.csci318.client.demo.DTOs.PromotionDTORequest;
import com.csci318.client.demo.DTOs.RestaurantDTORequest;
import com.csci318.client.demo.DTOs.UserDTORequest;
import com.csci318.client.demo.Domain.ValueObjs.PhoneNumber;
import com.github.javafaker.Faker;

public class EventDemonstrator extends MujikiDemonstrator {
    private final int MAGIC_NUMBER = 16;

    private final Faker faker = new Faker(Locale.forLanguageTag("en-AU"));

    private final List<UUID> restaurants = new ArrayList<>();
    private final List<UUID> users = new ArrayList<>();
    private final List<UUID> paymentMethods = new ArrayList<>();
    private final List<UUID> carts = new ArrayList<>();
    private final List<UUID> promotions = new ArrayList<>();
    private final List<UUID> items = new ArrayList<>();

    private final Map<UUID, OrderStatus> orders = new HashMap<>();

    public void run() {
        printHeader("Generating Restaurants and Users");

        for (int i = 0; i < MAGIC_NUMBER; ++i) {
            restaurants.add(createFakeRestaurant());
            users.add(createFakeUser());
        }

        printHeader("Generating dependent objects");
        for (int i = 0; i < MAGIC_NUMBER; ++i) {
            promotions.add(createFakePromotion(restaurants.get(i)));
            for (int j = 0; j < MAGIC_NUMBER; ++j) {
                items.add(createFakeItem(restaurants.get(i)));
            }
            paymentMethods.add(createFakePaymentMethod(users.get(i)));
            carts.add(createFakeCart(users.get(i)));
        }

        while (true) {
            final int EVENT_COUNT = 6;

            switch (faker.random().nextInt(1, EVENT_COUNT)) {
                case 1:
                {
                    printHeader("EVENT 1 - Add random feedback");
                    int restaurantIndex = randomIndex();
                    int userIndex = randomIndex();
                    createFakeFeedback(
                        restaurants.get(restaurantIndex),
                        users.get(userIndex)
                    );
                }
                break;
                case 2:
                case 3:
                {
                    printHeader("EVENT 2 - Add random order");

                    int restaurantIndex = randomIndex();
                    int userIndex = randomIndex();

                    try {

                        int totalItems = randomIndex() + 1;
                        for (int i = 0; i < totalItems; ++i) {
                            int itemIndex = MAGIC_NUMBER * restaurantIndex + randomIndex();
                            int itemCount = randomIndex() + 1;

                            for (int j = 0; j < itemCount; ++j) {
                                CartItemDTORequest cartItemDTORequest = new CartItemDTORequest();
                                cartItemDTORequest.setRestaurantId(
                                    restaurants.get(restaurantIndex)
                                );
                                cartItemDTORequest.setItemId(items.get(itemIndex));

                                post(
                                    Service.CART,
                                    new String[] { carts.get(userIndex).toString(), "items" },
                                    null,
                                    cartItemDTORequest
                                );
                            }
                        }

                        HashMap<String, Object> query = new HashMap<>();
                        query.put("paymentId", paymentMethods.get(userIndex));
                        query.put("promotionId", promotions.get(restaurantIndex));

                        UUID orderId = post(
                            Service.CART,
                            new String[] { "process-order", carts.get(userIndex).toString() },
                            query,
                            null
                        );

                        if (orderId == null) {
                            throw new RuntimeException("Failed to create order.");
                        }

                        orders.put(orderId, OrderStatus.CONFIRMED);
                    } catch (Throwable t) {
                        // Silent, since we may fail because of the faker not ensuring logical
                        // constraints of the business domain.

                        // Try re-setting payment method.
                        paymentMethods.set(
                            userIndex,
                            createFakePaymentMethod(users.get(userIndex))
                        );
                    }
                }
                break;
                case 4:
                case 5:
                case 6:
                    printHeader("EVENT 3 - Change random order status");

                    if (orders.isEmpty()) {
                        continue;
                    }

                    Entry<UUID, OrderStatus> randomOrder = orders.entrySet().iterator().next();

                    // ~MAGIC_NUMBER% chance of cancellation.
                    if (faker.random().nextInt(0, 100) < MAGIC_NUMBER) {
                        // Cancel the order
                        randomOrder.setValue(OrderStatus.CANCELLED);
                    } else {
                        int maxAddition = OrderStatus.CANCELLED.ordinal() - 1 -
                            randomOrder.getValue().ordinal();
                        randomOrder.setValue(OrderStatus.values()[
                            randomOrder.getValue().ordinal() +
                            faker.random().nextInt(1, maxAddition)
                        ]);
                    }

                    HashMap<String, Object> query = new HashMap<>();
                    query.put("orderStatus", randomOrder.getValue());

                    post(
                        Service.ORDER,
                        new String[] { randomOrder.getKey().toString(), "update-order-status" },
                        query,
                        null
                    );

                    if (randomOrder.getValue() == OrderStatus.CANCELLED ||
                        randomOrder.getValue() == OrderStatus.COMPLETED
                    ) {
                        orders.remove(randomOrder.getKey());
                    }
                break;
            }
        }
    }

    private UUID createFakeRestaurant() {
        RestaurantDTORequest restaurantDTORequest = new RestaurantDTORequest();
        restaurantDTORequest.setName(faker.name().lastName() + "'s " + faker.food().dish());
        restaurantDTORequest.setPhone(new PhoneNumber(
            "+" + faker.random().nextInt(10, 99).toString(),
            faker.phoneNumber().cellPhone()
        ));
        restaurantDTORequest.setEmail(faker.internet().emailAddress());
        restaurantDTORequest.setPassword(
            faker.internet()
                .password(8, 16, true)
        );
        restaurantDTORequest.setCuisine(CuisineType.values()[
            faker.random().nextInt(0, CuisineType.values().length - 1)
        ]);
        restaurantDTORequest.setOpenTime(LocalTime.of(
            faker.random().nextInt(0, 23),
            0)
        );
        restaurantDTORequest.setCloseTime(LocalTime.of(
            faker.random().nextInt(0, 23),
            0)
        );
        restaurantDTORequest.setDescription(faker.lorem().sentence());

        return post(
            Service.AUTHENTICATION,
            new String[] { "restaurant", "signup" },
            null,
            restaurantDTORequest
        );
    }

    private UUID createFakeItem(UUID restaurantId) {
        HashMap<String, Object> query = new HashMap<>();
        query.put("restaurantId", restaurantId);

        ItemDTORequest itemDTORequest = new ItemDTORequest();
        itemDTORequest.setName(faker.food().dish());
        itemDTORequest.setDescription(faker.lorem().sentence());
        itemDTORequest.setPrice(faker.random().nextDouble() * 100.0);
        itemDTORequest.setAvailability(true);

        return post(
            Service.ITEM,
            new String[] { "create" },
            query,
            itemDTORequest
        );
    }

    private UUID createFakePromotion(UUID restaurantId) {
        PromotionDTORequest promotionDTORequest = new PromotionDTORequest();
        promotionDTORequest.setRestaurantId(restaurantId);
        promotionDTORequest.setCode(faker.random().hex(8));
        promotionDTORequest.setDescription(faker.lorem().sentence());
        promotionDTORequest.setPercentage(faker.random().nextInt(10, 90));
        promotionDTORequest.setExpiryDate(
            LocalDateTime.now().plusDays(faker.random().nextInt(10, 365))
        );
        promotionDTORequest.setActive(true);
        promotionDTORequest.setStock(faker.random().nextInt(10000000, Integer.MAX_VALUE));

        return post(
            Service.PROMOTION,
            new String[] { "create" },
            null,
            promotionDTORequest
        );
    }

    private UUID createFakeUser() {
        UserDTORequest userDTORequest = new UserDTORequest();

        userDTORequest.setUsername(faker.name().username());
        userDTORequest.setPassword(
            faker.internet()
                .password(8, 16, true)
        );
        userDTORequest.setFirstName(faker.name().firstName());
        userDTORequest.setLastName(faker.name().lastName());
        userDTORequest.setEmail(faker.internet().emailAddress());
        userDTORequest.setPhone(faker.phoneNumber().cellPhone());

        return post(
            Service.AUTHENTICATION,
            new String[]{ "user", "signup" },
            null,
            userDTORequest
        );
    }

    private UUID createFakePaymentMethod(UUID userId) {
        PaymentDTORequest paymentDTORequest = new PaymentDTORequest();
        paymentDTORequest.setUserId(userId);
        paymentDTORequest.setHolderName(faker.name().fullName());
        paymentDTORequest.setCardNumber(faker.business().creditCardNumber());

        return post(Service.PAYMENT, null, null, paymentDTORequest);
    }

    private UUID createFakeCart(UUID userId) {
        CartDTORequest cartDTORequest = new CartDTORequest();
        cartDTORequest.setUserId(userId);

        return post(Service.CART, null, null, cartDTORequest);
    }

    private UUID createFakeFeedback(UUID restaurantId, UUID userId) {
        FeedbackDTORequest feedbackDTORequest = new FeedbackDTORequest();
        feedbackDTORequest.setRestaurantId(restaurantId);
        feedbackDTORequest.setUserId(userId);
        feedbackDTORequest.setRating(faker.random().nextInt(1, 5));
        feedbackDTORequest.setComments(faker.lorem().paragraph());

        return post(
            Service.FEEDBACK,
            new String[] { "create" },
            null,
            feedbackDTORequest
        );
    }

    private int randomIndex() {
        return faker.random().nextInt(0, MAGIC_NUMBER - 1);
    }
}
