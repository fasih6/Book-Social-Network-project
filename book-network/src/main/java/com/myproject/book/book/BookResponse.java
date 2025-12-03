package com.myproject.book.book;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {
    private Long id;
    private String title;
    private String authorName;
    private String isbn;
    private String synopsis;
    private String owner;
    private byte[] coverImage;
    private double rate;  // this is the average rating of the book we have to calculate it (we are doing this in book entity)
    private boolean archived;
    private boolean shareable;
}
