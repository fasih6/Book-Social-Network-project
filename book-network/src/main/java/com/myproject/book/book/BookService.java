package com.myproject.book.book;

import com.myproject.book.common.PageResponse;
import com.myproject.book.exception.OperationNotPermittedException;
import com.myproject.book.file.FileStorageService;
import com.myproject.book.history.BookTransactionHistory;
import com.myproject.book.history.BookTransactionHistoryRepository;
import com.myproject.book.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.OperationNotSupportedException;
import java.util.List;

import static com.myproject.book.book.BookSpecification.*;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
    private final FileStorageService fileStorageService;
    private final BookMapper bookMapper;
    public Long saveBook(BookRequest bookRequest, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookMapper.toBook(bookRequest);
        book.setOwner(user);
        return bookRepository.save(book).getId();
    }

    public BookResponse findById(Long bookId) {
        return bookRepository.findById(bookId)
                .map(bookMapper::toBookResponse)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id" + bookId));
    }

    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
        System.out.println("=== findAllBooks called ===");
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending()); // Pageable from springframework.data.domain
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, user.getId()); // get all books except the user's own books
//        Page<Book> books = bookRepository.findAllBooks(pageable);
        List<BookResponse> bookResponse = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );

    }

    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending()); // Pageable from springframework.data.domain
        Page<Book> books = bookRepository.findAll(withOwnerId(user.getId()), pageable);

        List<BookResponse> bookResponse = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending()); // Pageable from springframework.data.domain
        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepository
                .findAllBorrowedBooks(user.getId(), pageable);
        List<BorrowedBookResponse> bookResponse = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponse,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        System.out.println("findAllReturnedBooks called");
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending()); // Pageable from springframework.data.domain
        Page<BookTransactionHistory> allReturnedBooks = bookTransactionHistoryRepository
                .findAllReturnedBooks(user.getId(), pageable);
        List<BorrowedBookResponse> bookResponse = allReturnedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponse,
                allReturnedBooks.getNumber(),
                allReturnedBooks.getSize(),
                allReturnedBooks.getTotalElements(),
                allReturnedBooks.getTotalPages(),
                allReturnedBooks.isFirst(),
                allReturnedBooks.isLast()
        );
    }

    public Long updateBookShareableStatus(Long bookId, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id" + bookId));
        // only the owner of the book can update the status of his books
        if (!book.getOwner().getId().equals(user.getId())) {
            throw new OperationNotPermittedException("You are not allowed to update(shareable status) this book");
        }
        book.setShareable(!book.isShareable());
        return bookRepository.save(book).getId();
    }

    public Long updateBookArchivedStatus(Long bookId, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id" + bookId));
        // only the owner of the book can update the status of his books
        if (!book.getOwner().getId().equals(user.getId())) {
            throw new OperationNotPermittedException("You are not allowed to update(archived status) this book");
        }
        book.setArchived(!book.isArchived());
        return bookRepository.save(book).getId();
    }

    public Long borrowBook(Long bookId, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id" + bookId));

        if(book.isArchived() || !book.isShareable()){ // if book is archived or not shareable
            throw new OperationNotPermittedException("You are not allowed to borrow this book");
        }
        if (book.getOwner().getId().equals(user.getId())) { // you cannot borrow your own book
            throw new OperationNotPermittedException("You cannot borrow your own book");
        }
        // check if book is already borrowed or not
        final boolean isAlreadyBorrowed = bookTransactionHistoryRepository.isAlreadyBorrowedByUser(bookId, user.getId());
        if(isAlreadyBorrowed){
            throw new OperationNotPermittedException("This book is already borrowed");
        }
        BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
                .user(user)
                .book(book)
                .returned(false)
                .returnApproved(false)
                .build();
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();

    }

    public Long returnBorrowedBook(Long bookId, Authentication connectedUser) {
    //    System.out.println("=== returnBorrowedBook called ===");
        User user = (User) connectedUser.getPrincipal();

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId));

        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("The requested book is archived or not shareable");
        }

        if (book.getOwner().getId().equals(user.getId())) {
            throw new OperationNotPermittedException("You cannot borrow or return your own book");
        }

        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository
                .findByBookIdAndUserId(bookId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("You have not borrowed this book"));

        if (bookTransactionHistory.isReturned()) {
            throw new OperationNotPermittedException("You have already returned this book");
        }

        bookTransactionHistory.setReturned(true);

        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public Long approveReturnBorrowedBook(Long bookId, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id" + bookId));

        System.out.println("=== APPROVE RETURN DEBUG ===");
        System.out.println("Logged in user ID: " + user.getId());
        System.out.println("Book ID: " + bookId);
        System.out.println("Book owner ID: " + book.getOwner().getId());
        System.out.println("Is user the owner? " + book.getOwner().getId().equals(user.getId()));


        if(book.isArchived() || !book.isShareable()){ // if book is archived or not shareable
            throw new OperationNotPermittedException("You are not allowed to borrow this book");
        }
        if (!book.getOwner().getId().equals(user.getId())) { // User MUST be the owner to approve returns
            throw new OperationNotPermittedException("You can only approve returns for books you own");
        }
        System.out.println("Looking for transaction with bookId: " + bookId + " and ownerId: " + user.getId());
        // we need to make sure the user has already borrowed this book
        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository.findByBookIdAndOwnerId(bookId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("The book has not been returned or already approved yet so you cannot approve its return"));
        bookTransactionHistory.setReturnApproved(true);
        System.out.println("Found transaction ID: " + bookTransactionHistory.getId());
        System.out.println("Transaction returned: " + bookTransactionHistory.isReturned());
        System.out.println("Transaction returnApproved: " + bookTransactionHistory.isReturnApproved());
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();

    }

    public void uploadBookCoverPicture(Long bookId, MultipartFile file, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id" + bookId));
        var coverPicture = fileStorageService.saveFile(file, user.getId());
        book.setCoverImage(coverPicture);
        bookRepository.save(book);
    }
}
