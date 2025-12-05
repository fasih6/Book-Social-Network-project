import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { activateAccount } from 'src/app/services/fn/auth-controller/activate-account';

@Component({
  selector: 'app-activate-account',
  templateUrl: './activate-account.component.html',
  styleUrls: ['./activate-account.component.scss'],
})
export class ActivateAccountComponent {
  message = '';
  isOkay = true;
  submitted = false;

  constructor(private router: Router, private http: HttpClient) {}

  redirectToLogin() {
    this.router.navigate(['login']);
  }

  onCodeCompleted(token: string) {
    this.confirmAccount(token);
  }

  confirmAccount(token: string) {
    activateAccount(this.http, 'http://localhost:8088/api/v1', {
      token,
    }).subscribe({
      next: () => {
        this.message = 'Your account has been activated. Proceed to login.';
        this.submitted = true;
        this.isOkay = true;
      },
      error: () => {
        this.message = 'Token has expired or is invalid. Please try again.';
        this.submitted = true;
        this.isOkay = false;
      },
    });
  }
}
