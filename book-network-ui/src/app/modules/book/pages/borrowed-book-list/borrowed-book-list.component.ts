import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import {
  findAllBorrowedBooks,
  returnBorrowedBook,
  saveFeedback,
} from 'src/app/services/functions';
import {
  BorrowedBookResponse,
  FeedbackRequest,
  PageResponseBorrowedBookResponse,
} from 'src/app/services/models';

@Component({
  selector: 'app-borrowed-book-list',
  templateUrl: './borrowed-book-list.component.html',
  styleUrls: ['./borrowed-book-list.component.scss'],
})
export class BorrowedBookListComponent implements OnInit {
  borrowedBooks: PageResponseBorrowedBookResponse = {};
  feedbackRequest: FeedbackRequest = {
    bookId: 0,
    comment: '',
    rating: 0,
  };
  selectedBook: BorrowedBookResponse | undefined = undefined; // undefined also means no book is selected
  private readonly rootUrl = 'http://localhost:8088/api/v1';
  page: number = 0;
  size: number = 5;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.findAllBorrowedBooks();
  }
  findAllBorrowedBooks(): void {
    findAllBorrowedBooks(this.http, this.rootUrl, {
      page: this.page,
      size: this.size,
    }).subscribe({
      next: ({ body }) => {
        this.borrowedBooks = body || {};
        // console.log('Borrowed books loaded:', this.borrowedBooks);
      },
      error: (error) => {
        console.error('Error loading borrowed books:', error.error?.error);
      },
    });
  }

  returnBorrowedBook(book: BorrowedBookResponse) {
    this.selectedBook = book;
    this.feedbackRequest.bookId = book.id as number;
  }

  returnBook(withFeedback: boolean) {
    returnBorrowedBook(this.http, this.rootUrl, {
      'book-id': this.selectedBook?.id as number,
    }).subscribe({
      next: () => {
        if (withFeedback) {
          this.giveFeedback();
        }
        this.selectedBook = undefined;
        this.findAllBorrowedBooks();
      },
      error: (error) => {
        console.error('Error returning book:', error.error?.error);
      },
    });
  }

  giveFeedback() {
    saveFeedback(this.http, this.rootUrl, {
      body: this.feedbackRequest,
    }).subscribe({
      next: () => {},
      error: (error) => {
        console.error('Error giving feedback:', error.error?.error);
      },
    });
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllBorrowedBooks();
  }
  goToPreviousPage() {
    this.page--;
    this.findAllBorrowedBooks();
  }
  goToPage(page: number) {
    this.page = page;
    this.findAllBorrowedBooks();
  }
  goToNextPage() {
    this.page++;
    this.findAllBorrowedBooks();
  }
  goToLastPage() {
    this.page = (this.borrowedBooks.totalPages as number) - 1;
    this.findAllBorrowedBooks();
  }

  get isLastPage(): boolean {
    return this.page == (this.borrowedBooks.totalPages as number) - 1;
  }
}
