import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { borrowBook } from 'src/app/services/fn/book/borrow-book';
import { findAllBooks } from 'src/app/services/fn/book/find-all-books';
import {
  findAllBooksByOwner,
  updateBookArchivedStatus,
  updateBookShareableStatus,
} from 'src/app/services/functions';
import { BookResponse } from 'src/app/services/models/book-response';
import { PageResponseBookResponse } from 'src/app/services/models/page-response-book-response';

@Component({
  selector: 'app-my-books',
  templateUrl: './my-books.component.html',
  styleUrls: ['./my-books.component.scss'],
})
export class MyBooksComponent implements OnInit {
  bookResponse: PageResponseBookResponse = {};
  books: BookResponse[] = [];
  page: number = 0;
  size: number = 2;

  private readonly rootUrl = 'http://localhost:8088/api/v1';

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.findAllBooks();
  }
  findAllBooks(): void {
    findAllBooksByOwner(this.http, this.rootUrl, {
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

  shareBook(book: BookResponse) {
    updateBookShareableStatus(this.http, this.rootUrl, {
      'book-id': book.id as number,
    }).subscribe({
      next: () => {
        // Update book state
        book.shareable = !book.shareable;
      },
      error: (error) => {
        console.log(error);
      },
    });
  }
  editBook(book: BookResponse) {
    this.router.navigate(['books', 'manage', book.id]);
  }
  archiveBook(book: BookResponse) {
    updateBookArchivedStatus(this.http, this.rootUrl, {
      'book-id': book.id as number,
    }).subscribe({
      next: () => {
        // Update book state
        book.archived = !book.archived;
      },
      error: (error) => {
        console.log(error);
      },
    });
  }
}
