import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { AuthenticationRequest } from 'src/app/services/models/authentication-request';
import { login } from 'src/app/services/fn/auth-controller/login';
import { TokenService } from 'src/app/services/token/token.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  authRequest: AuthenticationRequest = {
    email: '',
    password: '',
  };

  errorMsg: Array<string> = [];

  constructor(
    private router: Router,
    private http: HttpClient,
    private tokenService: TokenService
  ) {}

  register() {
    this.router.navigate(['register']);
  }

  login() {
    this.errorMsg = [];

    const rootUrl = 'http://localhost:8088/api/v1';

    login(this.http, rootUrl, { body: this.authRequest }).subscribe({
      next: (response) => {
        // Extract the body from StrictHttpResponse
        const authResponse = response.body;

        // Save the JWT token from response body
        this.tokenService.token = authResponse?.token as string;

        // Navigate to books page
        this.router.navigate(['books']);
      },
      error: (error) => {
        console.error('Login error:', error);

        // Handle different error formats from backend
        if (error.error?.validationErrors) {
          this.errorMsg = error.error.validationErrors;
        } else if (error.error?.error) {
          this.errorMsg.push(error.error.error);
        } else if (error.error?.message) {
          this.errorMsg.push(error.error.message);
        } else if (error.message) {
          this.errorMsg.push(error.message);
        } else {
          this.errorMsg.push(
            'Login failed. Please check your credentials and try again.'
          );
        }
      },
    });
  }
}
