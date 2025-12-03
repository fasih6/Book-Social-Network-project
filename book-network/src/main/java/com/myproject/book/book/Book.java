package com.myproject.book.book;

import com.myproject.book.common.BaseEntity;
import com.myproject.book.feedback.Feedback;
import com.myproject.book.history.BookTransactionHistory;
import com.myproject.book.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book extends BaseEntity {

    private String title;
    private String authorName;
    private String isbn;
    private String synopsis;
    private String coverImage;  // file path of our uploaded image
    private boolean archived;
    private boolean shareable;

    @ManyToOne
    @JoinColumn(name = "owner_id") // foreign key column = owner_id, that stores the id of the User who owns the book.
    private User owner;  // one book has one owner and user has many books

    @OneToMany(mappedBy = "book")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> transactionHistories;

    @Transient
    public double getRate() {
        if(feedbacks == null || feedbacks.isEmpty()) {
            return 0.0;
        }
        var rate = this.feedbacks.stream().mapToDouble(Feedback::getRating).average().orElse(0.0);
        // rounding the rate to 2 decimal places
        return Math.round(rate * 10.0) / 10.0;
    }


}
