package com.myproject.book.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    @Query("""
    SELECT b 
    FROM Book b
    WHERE b.archived = false
    AND b.shareable = true
    AND b.owner.id != :id
    """)
    Page<Book> findAllDisplayableBooks(Pageable pageable,@Param("id") Long id);

    @Query("SELECT b FROM Book b WHERE b.archived = false AND b.shareable = true")

    Page<Book> findAllBooks(Pageable pageable);
}
