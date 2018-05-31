import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http'
import { Observable } from 'rxjs/Observable';
import { Router } from '@angular/router';
import { LocalStorageService } from './localstorage.service';
import { destroy } from 'splash-screen';
import 'rxjs/add/operator/do';
import 'rxjs/add/observable/empty';

@Injectable()
export class ErrorHttpInterceptor implements HttpInterceptor {
    constructor(private router: Router, private localStorageService: LocalStorageService) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (!this.localStorageService.currentOrganization) {
            this.localStorageService.removeCurrentUser();
            this.navigateToLogin();
            return Observable.empty();
        }
        return next.handle(req).do(event => { }, err => {
            if (err instanceof HttpErrorResponse && err.status == 401) {
                this.localStorageService.removeCurrentOrganization();
                this.localStorageService.removeCurrentUser();
                this.navigateToLogin();
            }
        });
    }

    private navigateToLogin() {
        destroy();
        this.router.navigate(['/login']);
    }
}