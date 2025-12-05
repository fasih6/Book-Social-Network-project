import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RegisterationRequest } from 'src/app/services/models/registeration-request';
import { register } from 'src/app/services/fn/auth-controller/register';
import { activateAccount } from 'src/app/services/functions';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent {
  registerRequest: RegisterationRequest = {
    email: '',
    firstName: '',
    lastName: '',
    password: '',
  };

  errorMsg: Array<string> = [];
  successMsg: string = '';

  constructor(private router: Router, private http: HttpClient) {}

  login() {
    this.router.navigate(['login']);
  }

  register() {
    this.errorMsg = [];
    this.successMsg = '';

    const rootUrl = 'http://localhost:8088/api/v1';

    register(this.http, rootUrl, { body: this.registerRequest }).subscribe({
      next: (): void => {
        this.router.navigate(['activate-account']);
      },
      error: (err): void => {
        this.errorMsg = err.error.validationErrors;
      },
    });
  }
}
