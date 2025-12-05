import { Component, inject, OnInit } from '@angular/core';
import { BookRequest } from 'src/app/services/models';
import { saveBook } from 'src/app/services/fn/book/save-book';
import { HttpClient } from '@angular/common/http';
import { uploadBookCoverPicture } from 'src/app/services/functions';
import { ActivatedRoute, Router } from '@angular/router';
@Component({
  selector: 'app-manage-book',
  templateUrl: './manage-book.component.html',
  styleUrls: ['./manage-book.component.scss'],
})
export class ManageBookComponent implements OnInit {
  bookRequest: BookRequest = {
    authorName: '',
    isbn: '',
    synopsis: '',
    title: '',
    shareable: false, // ADD THIS - it's used in the template but missing here
  };
  errorMsg: Array<string> = [];
  selectedPicture: string | undefined;
  selectedBookCover: any;
  private activatedRoute = inject(ActivatedRoute);

  constructor(private http: HttpClient, private router: Router) {}

  rootUrl = 'http://localhost:8088/api/v1';

  ngOnInit(): void {
    const bookId = this.activatedRoute.snapshot.paramMap.get('bookId');
    if (bookId) {
      this.http
        .get<BookRequest>(`${this.rootUrl}/books/${bookId}`)
        .subscribe((response) => {
          this.bookRequest = response;
        });
    }
  }
  onFileSelected(event: any) {
    this.selectedBookCover = event.target.files[0];
    console.log(this.selectedBookCover);

    if (this.selectedBookCover) {
      const reader = new FileReader();
      reader.onload = () => {
        this.selectedPicture = reader.result as string;
      };
      reader.readAsDataURL(this.selectedBookCover);
    }
  }

  saveBook() {
    saveBook(this.http, this.rootUrl, { body: this.bookRequest }).subscribe({
      next: (response) => {
        const bookId = response.body;

        if (this.selectedBookCover) {
          uploadBookCoverPicture(this.http, this.rootUrl, {
            'book-id': bookId as number,
            body: {
              file: this.selectedBookCover,
            },
          }).subscribe({
            next: () => {
              this.router.navigate(['books/my-books']);
            },
            error: (error) => {
              console.error('Error uploading cover:', error);
              // Navigate anyway since book was saved
              this.router.navigate(['books/my-books']);
            },
          });
        } else {
          this.router.navigate(['books/my-books']);
        }
      },
      error: (error) => {
        console.error('Error saving book:', error);
        this.errorMsg = error.error?.validationErrors || [
          'Failed to save book',
        ];
      },
    });
  }
}
