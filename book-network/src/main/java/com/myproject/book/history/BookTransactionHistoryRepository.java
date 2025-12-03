package com.myproject.book.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, Long> {
    @Query("""
            SELECT history FROM BookTransactionHistory history
            WHERE history.user.id = :id
            """)
    Page<BookTransactionHistory> findAllBorrowedBooks(Long id, Pageable pageable);

    @Query("""
    SELECT history 
    FROM BookTransactionHistory history
    WHERE history.returned = true
    """)
    Page<BookTransactionHistory> findAllReturnedBooks(Long id, Pageable pageable);

    @Query("""
        SELECT (COUNT(*) > 0) AS isBorrowed
        FROM BookTransactionHistory history
        WHERE history.book.id = :bookId 
        AND history.user.id = :id
        AND history.returnApproved = false
        """)
    boolean isAlreadyBorrowedByUser(Long bookId, Long id);

    @Query("""
        SELECT history FROM BookTransactionHistory history
        WHERE history.book.id = :bookId 
        AND history.user.id = :id
        AND history.returned = false
        AND history.returnApproved = false
        """)
    Optional<BookTransactionHistory> findByBookIdAndUserId(Long bookId, Long id);

    @Query("""
        SELECT history FROM BookTransactionHistory history
        WHERE history.book.id = :bookId 
        AND history.book.owner.id = :id
        AND history.returned = true
        AND history.returnApproved = false
        """)
    Optional<BookTransactionHistory> findByBookIdAndOwnerId(Long bookId, Long id);
}
