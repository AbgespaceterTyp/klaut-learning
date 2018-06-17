import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http'
import { Observable } from 'rxjs/Observable';
import { Router } from '@angular/router';
import { LocalStorageService } from './localstorage.service';
import { destroy } from 'splash-screen';
import 'rxjs/add/operator/do';
import 'rxjs/add/observable/empty';
import { MessageService } from './message.service';

@Injectable()
export class ErrorHttpInterceptor implements HttpInterceptor {
    constructor(private router: Router, private localStorageService: LocalStorageService,
        private messageService: MessageService) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (!this.localStorageService.currentOrganization) {
            this.localStorageService.removeCurrentUser();
            this.navigateToLogin();
            return Observable.empty();
        }
        return next.handle(req).do(event => { }, err => {
            if (err instanceof HttpErrorResponse) {
                if (err.status == 401) {
                    this.localStorageService.removeCurrentOrganization();
                    this.localStorageService.removeCurrentUser();
                    this.navigateToLogin();
                }
                if (err.error && err.error.message) {
                    this.messageService.errorEvent.emit(err.error.message);
                } else {
                    this.messageService.errorEvent.emit("Something went wrong! " + err.statusText);
                }
            }
        });
    }

    private navigateToLogin() {
        destroy();
        this.router.navigate(['/login']);
    }
}