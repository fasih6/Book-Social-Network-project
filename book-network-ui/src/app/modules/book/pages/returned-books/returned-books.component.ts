import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import {
  approveReturnBorrowedBook,
  findAllReturnedBooks,
} from 'src/app/services/functions';
import {
  BorrowedBookResponse,
  PageResponseBorrowedBookResponse,
} from 'src/app/services/models';

@Component({
  selector: 'app-returned-books',
  templateUrl: './returned-books.component.html',
  styleUrls: ['./returned-books.component.scss'],
})
export class ReturnedBooksComponent implements OnInit {
  private readonly rootUrl = 'http://localhost:8088/api/v1';
  returnedBooks: PageResponseBorrowedBookResponse = {};
  page: number = 0;
  size: number = 10;
  message: string = '';
  level: string = 'success';
  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.findAllReturnedBooks();
  }

  findAllReturnedBooks(): void {
    findAllReturnedBooks(this.http, this.rootUrl, {
      page: this.page,
      size: this.size,
    }).subscribe({
      next: ({ body }) => {
        this.returnedBooks = body || {};
      },
      error: (error) => {
        console.error('Error loading borrowed books:', error.error?.error);
      },
    });
  }

  approveBookReturn(book: BorrowedBookResponse) {
    console.log('Approving return for book:', book);
    console.log('Book ID:', book.id);

    if (!book.returned) {
      this.message = 'Book is not marked as returned yet';
      this.level = 'error';
      return;
    }
    approveReturnBorrowedBook(this.http, this.rootUrl, {
      'book-id': book.id as number,
    }).subscribe({
      next: ({ body }) => {
        this.message = 'Book returned successfully';
        this.level = 'success';
        this.findAllReturnedBooks();
      },
      error: (error) => {
        console.error('Error loading borrowed books:', error.error?.error);
      },
    });
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllReturnedBooks();
  }
  goToPreviousPage() {
    this.page--;
    this.findAllReturnedBooks();
  }
  goToPage(page: number) {
    this.page = page;
    this.findAllReturnedBooks();
  }
  goToNextPage() {
    this.page++;
    this.findAllReturnedBooks();
  }
  goToLastPage() {
    this.page = (this.returnedBooks.totalPages as number) - 1;
    this.findAllReturnedBooks();
  }

  get isLastPage(): boolean {
    return this.page == (this.returnedBooks.totalPages as number) - 1;
  }
}
