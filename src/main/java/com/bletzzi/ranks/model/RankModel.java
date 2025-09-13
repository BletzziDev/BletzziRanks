package com.bletzzi.ranks.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;

@AllArgsConstructor @Getter
public class RankModel {
    private final int order;
    private final String name;
    private final HashMap<String, Double> prices;
    private final List<String> commands;
}