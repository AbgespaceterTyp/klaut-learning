import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LocalStorageService } from './localstorage.service';
import 'rxjs/add/operator/map'

@Injectable()
export class AuthenticationService {
    constructor(private http: HttpClient, private localStorageService: LocalStorageService) { }

    login(username: string, organization: string, password: string) {
        this.localStorageService.currentOrganization = organization;
        return this.http.get<any>('/api/' + organization + '/user/me', { headers: { 'Authorization': 'Basic ' + btoa(username + ":" + password) } })
            .map(user => {
                if (user) {
                    this.localStorageService.currentUser = user.email;
                }
                return user;
            });
    }

    callUserMe() {
        let organization = this.localStorageService.currentOrganization
        return this.http.get('/api/' + organization + '/user/me');
    }

    logout() {
        // remove user from local storage to log user out
        this.localStorageService.removeCurrentOrganization();
        this.localStorageService.removeCurrentUser();
    }
}