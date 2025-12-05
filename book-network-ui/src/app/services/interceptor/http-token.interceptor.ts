import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { TokenService } from '../token/token.service';

@Injectable()
export class HttpTokenInterceptor implements HttpInterceptor {
  constructor(private tokenService: TokenService) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const token = this.tokenService.token;
    // const token = localStorage.getItem('access_token'); // <- get fresh token

    console.log('Token from service:', token); // DEBUG

    if (token) {
      const clonedReq = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        },
      });
      // console.log(
      //   'Authorization header:',
      //   clonedReq.headers.get('Authorization')
      // );
      // console.log('All headers:', clonedReq.headers.keys());
      return next.handle(clonedReq); // Return the CLONED request
    }

    return next.handle(req); // Return original if no token
  }
}
