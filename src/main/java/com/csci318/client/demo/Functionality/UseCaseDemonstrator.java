package com.csci318.client.demo.Functionality;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import com.csci318.client.demo.Constants.CuisineType;
import com.csci318.client.demo.Constants.OrderStatus;
import com.csci318.client.demo.Constants.Service;
import com.csci318.client.demo.DTOs.AddressDTORequest;
import com.csci318.client.demo.DTOs.CartDTORequest;
import com.csci318.client.demo.DTOs.CartItemDTORequest;
import com.csci318.client.demo.DTOs.FeedbackDTORequest;
import com.csci318.client.demo.DTOs.ItemDTORequest;
import com.csci318.client.demo.DTOs.LoyaltyDTORequest;
import com.csci318.client.demo.DTOs.PaymentDTORequest;
import com.csci318.client.demo.DTOs.PromotionDTORequest;
import com.csci318.client.demo.DTOs.RestaurantDTORequest;
import com.csci318.client.demo.DTOs.UserDTORequest;
import com.csci318.client.demo.Domain.ValueObjs.PhoneNumber;
import com.github.javafaker.Faker;

public class UseCaseDemonstrator extends MujikiDemonstrator {
    private final Faker faker = new Faker(Locale.forLanguageTag("en-AU"));

    private UUID cartId;
    private UUID itemId;
    private UUID loyaltyId;
    private UUID orderId;
    private UUID paymentId;
    private UUID promotionId;
    private UUID restaurantId;
    private UUID userId;

    private String restaurantPostCode;
    private CuisineType restaurantCuisine;
    private String userName;

    public UseCaseDemonstrator() {

    }

    public void run() {
        useCase1_CreateUser();
        useCase1_1_ViewUsers();
        useCase1_2_FindUserByUsername();

        useCase2_CreateRestaurant();
        useCase2_1_ViewRestaurants();
        useCase2_2_UpdateRestaurant();

        useCase3_CreateAddressForUser();
        useCase3_1_FindAddressForUser();

        useCase4_CreateAddressForRestaurant();
        useCase4_1_FindAddressForUser();

        useCase5_SearchForRestaurants();

        useCase6_CreateItemForRestaurant();

        useCase7_CreatePromotion();
        useCase7_1_GetPromotion();
        useCase7_2_GetPromotionByRestaurant();

        useCase8_CreateFeedbackForRestaurant();

        useCase9_AddPaymentMethod();
        useCase9_1_ViewPaymentMethods();

        useCase13_0_EnrollInLoyaltyProgram();

        useCase10_0_CreateCartForUser();
        useCase10_AddItemToCart();

        useCase11_PayForCart();

        useCase12_UpdateOrderStatus();

        useCase13_GetLoyaltyStatus();

        useCase14_GetRestaurantCountsByRatings();

        useCase15_GetOrderStatusesByRestaurants();
    }

    // USE CASE 1 - Users

    private void useCase1_CreateUser() {
        printHeader("Use Case 1 - Create User");

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

        // Store for future usage.
        userName = userDTORequest.getUsername();

        userId = post(
            Service.AUTHENTICATION,
            new String[]{ "user", "signup" },
            null,
            userDTORequest
        );

        pause();

    }

    private void useCase1_1_ViewUsers() {
        printHeader("Use Case 1.1 - View Users");

        userId = get(Service.USER, null, null);

        pause();
    }

    private void useCase1_2_FindUserByUsername() {
        printHeader("Use Case 1.2 - Find User by Username");

        userId = get(Service.USER, new String[] { userName }, null);

        pause();
    }

    // USE CASE 2 - Restaurants

    private void useCase2_CreateRestaurant() {
        printHeader("Use Case 2 - Create Restaurant");

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

        restaurantCuisine = restaurantDTORequest.getCuisine();

        restaurantId = post(
            Service.AUTHENTICATION,
            new String[] { "restaurant", "signup" },
            null,
            restaurantDTORequest
        );

        pause();
    }

    private void useCase2_1_ViewRestaurants() {
        printHeader("Use Case 2.1 - View Restaurants");

        get(Service.RESTAURANT, null, null);

        pause();
    }

    private void useCase2_2_UpdateRestaurant() {
        printHeader("Use Case 2.2 - Update Restaurant");

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

        restaurantCuisine = restaurantDTORequest.getCuisine();

        restaurantId = put(
            Service.RESTAURANT,
            new String[] { restaurantId.toString() },
            null,
            restaurantDTORequest
        );

        pause();
    }

    // USE CASE 3 - User Address

    private void useCase3_CreateAddressForUser()
    {
        printHeader("Use Case 3 - Create Address for User");

        AddressDTORequest addressDTORequest = new AddressDTORequest();
        addressDTORequest.setStreet(faker.address().streetAddress());
        addressDTORequest.setSuburb(faker.address().city());
        addressDTORequest.setState(faker.address().state());
        addressDTORequest.setPostcode(faker.address().zipCode());
        addressDTORequest.setCountry(faker.address().country());

        post(
            Service.ADDRESS,
            new String[] { "forUser", userId.toString() },
            null,
            addressDTORequest
        );

        pause();
    }

    private void useCase3_1_FindAddressForUser()
    {
        printHeader("Use Case 3.1 - Find Address for User");

        get(Service.ADDRESS, new String[] { "forUser", userId.toString() }, null);

        pause();
    }

    // USE CASE 4 - Restaurant Address

    private void useCase4_CreateAddressForRestaurant()
    {
        printHeader("Use Case 4 - Create Address for Restaurant");

        AddressDTORequest addressDTORequest = new AddressDTORequest();
        addressDTORequest.setStreet(faker.address().streetAddress());
        addressDTORequest.setSuburb(faker.address().city());
        addressDTORequest.setState(faker.address().state());
        addressDTORequest.setPostcode(faker.address().zipCode());
        addressDTORequest.setCountry(faker.address().country());

        restaurantPostCode = addressDTORequest.getPostcode();

        post(
            Service.ADDRESS,
            new String[] { "forRestaurant", restaurantId.toString() },
            null,
            addressDTORequest
        );

        pause();
    }

    private void useCase4_1_FindAddressForUser()
    {
        printHeader("Use Case 4.1 - Find Address for Restaurant");

        get(Service.ADDRESS, new String[] { "forRestaurant", restaurantId.toString() }, null);

        pause();
    }

    // USE CASE 5 - Restaurant Complex Query

    private void useCase5_SearchForRestaurants()
    {
        printHeader("Use Case 5 - Search for Restaurants");

        HashMap<String, Object> query = new HashMap<>();
        // query.put("name", "Nguyen's Bun Cha");
        query.put("cuisine", restaurantCuisine);
        // query.put("minRating", 0);
        // query.put("maxRating", 5);
        // query.put("opened", true);
        query.put("postcode", restaurantPostCode);

        get(
            Service.RESTAURANT,
            new String[] { "search" },
            query
        );

        pause();
    }

    // USE CASE 6 - Restaurant Items

    private void useCase6_CreateItemForRestaurant()
    {
        printHeader("Use Case 6 - Create Item for Restaurant");

        HashMap<String, Object> query = new HashMap<>();
        query.put("restaurantId", restaurantId);

        ItemDTORequest itemDTORequest = new ItemDTORequest();
        itemDTORequest.setName(faker.food().dish());
        itemDTORequest.setDescription(faker.lorem().sentence());
        itemDTORequest.setPrice(faker.random().nextDouble() * 100.0);
        itemDTORequest.setAvailability(true);

        itemId = post(
            Service.ITEM,
            new String[] { "create" },
            query,
            itemDTORequest
        );

        pause();
    }

    // USE CASE 7 - Promotion

    private void useCase7_CreatePromotion()
    {
        printHeader("Use Case 7 - Create Promotion");

        PromotionDTORequest promotionDTORequest = new PromotionDTORequest();
        promotionDTORequest.setRestaurantId(restaurantId);
        promotionDTORequest.setCode(faker.random().hex(8));
        promotionDTORequest.setDescription(faker.lorem().sentence());
        promotionDTORequest.setPercentage(faker.random().nextInt(10, 90));
        promotionDTORequest.setExpiryDate(
            LocalDateTime.now().plusDays(faker.random().nextInt(10, 365))
        );
        promotionDTORequest.setActive(true);
        promotionDTORequest.setStock(faker.random().nextInt(10, 1000));

        promotionId = post(
            Service.PROMOTION,
            new String[] { "create" },
            null,
            promotionDTORequest
        );

        pause();
    }

    private void useCase7_1_GetPromotion()
    {
        printHeader("Use Case 7.1 - Get Promotion");

        promotionId = get(Service.PROMOTION, new String[] { promotionId.toString() }, null);

        pause();
    }

    private void useCase7_2_GetPromotionByRestaurant()
    {
        printHeader("Use Case 7.2 - Get Promotions by Restaurant");

        promotionId = get(
            Service.PROMOTION,
            new String[] { "restaurant", restaurantId.toString() },
            null
        );

        pause();
    }

    // USE CASE 8 - Feedback

    private void useCase8_CreateFeedbackForRestaurant()
    {
        printHeader("Use Case 8 - Create Feedback for Restaurant");

        FeedbackDTORequest feedbackDTORequest = new FeedbackDTORequest();
        feedbackDTORequest.setRestaurantId(restaurantId);
        feedbackDTORequest.setUserId(userId);
        feedbackDTORequest.setRating(faker.random().nextInt(1, 5));
        feedbackDTORequest.setComments(faker.lorem().paragraph());

        post(
            Service.FEEDBACK,
            new String[] { "create" },
            null,
            feedbackDTORequest
        );

        pause();
    }

    // USE CASE 9 - Payment

    private void useCase9_AddPaymentMethod()
    {
        printHeader("Use Case 9 - Add Payment Method");

        PaymentDTORequest paymentDTORequest = new PaymentDTORequest();
        paymentDTORequest.setUserId(userId);
        paymentDTORequest.setHolderName(faker.name().fullName());
        paymentDTORequest.setCardNumber(faker.business().creditCardNumber());

        paymentId = post(Service.PAYMENT, null, null, paymentDTORequest);

        pause();
    }

    private void useCase9_1_ViewPaymentMethods()
    {
        printHeader("Use Case 9.1 - View Payment Methods");

        paymentId = get(Service.PAYMENT, new String[] { "user", userId.toString() }, null);

        pause();
    }

    // Preparation for Use Case 13 below.
    // Must be enrolled in loyalty program then make an order for a meaningful status to display.

    private void useCase13_0_EnrollInLoyaltyProgram()
    {
        printHeader("Use Case 13.0 - Enroll in Loyalty Program");

        LoyaltyDTORequest loyaltyDTORequest = new LoyaltyDTORequest();
        loyaltyDTORequest.setUserId(userId);

        loyaltyId = post(Service.LOYALTY, new String[] { "enroll" }, null, loyaltyDTORequest);

        pause();
    }

    // USE CASE 10 - Cart

    private void useCase10_0_CreateCartForUser()
    {
        printHeader("Use Case 10.0 - Create Cart for User");

        CartDTORequest cartDTORequest = new CartDTORequest();
        cartDTORequest.setUserId(userId);

        cartId = post(Service.CART, null, null, cartDTORequest);

        pause();
    }

    private void useCase10_AddItemToCart()
    {
        printHeader("Use Case 10 - Add Item to Cart");

        CartItemDTORequest cartItemDTORequest = new CartItemDTORequest();
        cartItemDTORequest.setRestaurantId(restaurantId);
        cartItemDTORequest.setItemId(itemId);

        cartId = post(
            Service.CART,
            new String[] { cartId.toString(), "items" },
            null,
            cartItemDTORequest
        );

        pause();
    }

    // USE CASE 11 - Pay

    private void useCase11_PayForCart()
    {
        printHeader("Use Case 11 - Pay for Cart");

        HashMap<String, Object> query = new HashMap<>();
        query.put("paymentId", paymentId);
        query.put("promotionId", promotionId);

        orderId = post(
            Service.CART,
            new String[] { "process-order", cartId.toString() },
            query,
            null
        );

        pause();
    }

    // USE CASE 12 - Order

    private void useCase12_UpdateOrderStatus()
    {
        printHeader("Use Case 12 - Update Order Status");

        HashMap<String, Object> query = new HashMap<>();
        query.put("orderStatus", OrderStatus.COMPLETED);

        orderId = post(
            Service.ORDER,
            new String[] { orderId.toString(), "status" },
            query,
            null
        );

        pause();
    }

    // USE CASE 13 - Get Loyalty Status

    private void useCase13_GetLoyaltyStatus()
    {
        printHeader("Use Case 13 - Get Loyalty Status");

        loyaltyId = get(Service.LOYALTY, new String[] { loyaltyId.toString() }, null);

        pause();
    }

    // USE CASE 14 - Real-time Ratings Query

    private void useCase14_GetRestaurantCountsByRatings()
    {
        printHeader("Use Case 14 - Get Restaurant Counts by Ratings");

        get(Service.ANALYTICS, new String[] { "ratings" }, null);

        pause();
    }

    // USE CASE 15 - Real-time Order Status Query

    private void useCase15_GetOrderStatusesByRestaurants()
    {
        printHeader("Use Case 15 - Get Order Statuses by Restaurants");

        get(Service.ANALYTICS, new String[] { "orders" }, null);

        pause();
    }

    private void pause() {
        System.console().printf("Press Enter to continue...");
        System.console().readLine();
    }
}
