import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { findAllBooks } from 'src/app/services/fn/book/find-all-books';
import { BookResponse } from 'src/app/services/models/book-response';
import { PageResponseBookResponse } from 'src/app/services/models/page-response-book-response';
import { Router } from '@angular/router';
import { borrowBook } from 'src/app/services/fn/book/borrow-book';

@Component({
  selector: 'app-book-list',
  templateUrl: './book-list.component.html',
  styleUrls: ['./book-list.component.scss'],
})
export class BookListComponent implements OnInit {
  bookResponse: PageResponseBookResponse = {};
  books: BookResponse[] = [];
  page: number = 0;
  size: number = 2;
  message: string = '';
  level: string = 'success';

  private readonly rootUrl = 'http://localhost:8088/api/v1';

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.findAllBooks();
  }
  findAllBooks(): void {
    findAllBooks(this.http, this.rootUrl, {
      page: this.page,
      size: this.size,
    }).subscribe({
      next: ({ body }) => {
        this.bookResponse = body || {};
        this.books = body?.content || [];
        console.log(this.books); // <-- check this in browser console
      },
      error: (error) => {
        console.log(error);
      },
    });
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllBooks();
  }
  goToPreviousPage() {
    this.page--;
    this.findAllBooks();
  }
  goToPage(page: number) {
    this.page = page;
    this.findAllBooks();
  }
  goToNextPage() {
    this.page++;
    this.findAllBooks();
  }
  goToLastPage() {
    this.page = (this.bookResponse.totalPages as number) - 1;
    this.findAllBooks();
  }

  get isLastPage(): boolean {
    return this.page == (this.bookResponse.totalPages as number) - 1;
  }

  borrowBook(book: BookResponse): void {
    this.message = '';
    // Call the borrow book API endpoint
    borrowBook(this.http, this.rootUrl, {
      'book-id': book.id as number,
    }).subscribe({
      next: () => {
        this.level = 'success';
        this.message = 'Book successfully added to your list';
        // Optionally refresh the book list to update availability
        // this.findAllBooks();
      },
      error: (error): void => {
        console.log(error);

        this.level = 'danger';
        this.message = error?.error?.error || 'An unexpected error occurred';

        // this.message =
        //   error?.error?.error || // <- your backend sends this
        //   error?.error?.message ||
        //   error?.message ||
        //   'An unexpected error occurred';
      },
    });
  }
}
