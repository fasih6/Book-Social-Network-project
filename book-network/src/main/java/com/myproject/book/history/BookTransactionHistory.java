package com.myproject.book.history;

import com.myproject.book.book.Book;
import com.myproject.book.common.BaseEntity;
import com.myproject.book.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class BookTransactionHistory extends BaseEntity {
    //user relationship
    @ManyToOne
    @JoinColumn(name = "user_id") //foreign key column user_id in the book_transaction_history table. to stores the ID of the user who made the transaction.
    private User user;
    //book relationship
    @ManyToOne
    @JoinColumn(name = "book_id") //foreign key column book_id in the book_transaction_history table. to stores the ID of the book that each transaction is linked to.
    private Book book;

    private boolean returned;
    private boolean returnApproved;

}
