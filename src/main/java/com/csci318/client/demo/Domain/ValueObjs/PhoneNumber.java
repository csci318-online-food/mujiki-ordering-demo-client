package com.csci318.client.demo.Domain.ValueObjs;

import lombok.Value;

@Value
public class PhoneNumber {
    private final String countryCode;
    private final String number;

    public PhoneNumber(String countryCode, String number) {
        this.countryCode = countryCode;
        this.number = number;
    }

    public PhoneNumber() {
        this.countryCode = "";
        this.number = "";
    }
}
