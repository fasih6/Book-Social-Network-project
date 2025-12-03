package com.myproject.book.book;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BorrowedBookResponse {

    private Long id;
    private String title;
    private String authorName;
    private String isbn;
    private double rate;  // this is the average rating of the book we have to calculate it (we are doing this in book entity)
    private boolean returned;
    private boolean returnApproved;

}
