package com.bletzzi.ranks.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor @Getter
public class User {
    private final UUID uuid;
    private int rank;

    public void setRank(final int order) {
        this.rank = order;
        // TODO Add lógica de executar comando quando upar de rank e etc
    }
}