package com.fastcampus.boardserver.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    public enum SortStatus {
        CATEGORIES, NEWEST, OLDEST, HIGH_PRICE, LOW_PRICE, GRADE
    }

    private int id;

    private String name;

    private SortStatus sortStatus;

    private int searchCount;

    private int pagingStartOffset;
}