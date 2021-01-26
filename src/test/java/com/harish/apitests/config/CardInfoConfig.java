package com.harish.apitests.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.harish.apitests.models.CardInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardInfoConfig {
    List<CardInfo> cards;

    public CardInfo getCardByCardNumber(final String cardNo){
        return cards.stream()
                .filter( card -> card.getCardNo().equals(cardNo))
                .findFirst()
                .orElse(null);
    }
}
