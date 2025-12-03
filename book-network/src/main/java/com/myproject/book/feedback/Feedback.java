package com.myproject.book.feedback;

import com.myproject.book.book.Book;
import com.myproject.book.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Feedback extends BaseEntity {

    private Double rating;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "book_id") //foreign key column book_id in the feedback table. to stores the ID of the book that each feedback is linked to.
    private Book book;


}
