import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LocalStorageService } from './localstorage.service';
import { OrganizationService } from './organization.service';
import 'rxjs/add/operator/map'
import { OrganizationDto } from '../_models';

@Injectable()
export class AuthenticationService {
    constructor(private http: HttpClient, private localStorageService: LocalStorageService, private organizationService: OrganizationService) { }

    login(username: string, organization: string, password: string) {
        this.localStorageService.currentOrganization = new OrganizationDto("", organization, "");

        return this.http.get<any>('/api/' + organization + '/user/me', { headers: { 'Authorization': 'Basic ' + btoa(username + ":" + password) } })
            .map(user => {
                if (user) {
                    this.localStorageService.currentUser = user.email;
                    this.organizationService.loadCurrent();
                }
                return user;
            });
    }

    loggedIn() {
        return this.localStorageService.currentUser &&
            this.localStorageService.currentOrganization ? true : false
    }

    logout() {
        // remove user from local storage to log user out
        this.localStorageService.removeCurrentOrganization();
        this.localStorageService.removeCurrentUser();
    }
}