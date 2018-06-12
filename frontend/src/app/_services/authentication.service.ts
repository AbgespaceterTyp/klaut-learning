import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LocalStorageService } from './localstorage.service';
import { OrganizationService } from './organization.service';
import 'rxjs/add/operator/map'
import 'rxjs/add/operator/mergeMap'
import { OrganizationDto, SearchOrganizationRequestDto, SearchOrganizationResponsetDto } from '../_models';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class AuthenticationService {
    constructor(private http: HttpClient, private localStorageService: LocalStorageService, private organizationService: OrganizationService) { }

    login(username: string, organizationName: string, password: string) {
        return this.fetchOrganizationKey(organizationName)
            .flatMap(organizationKey => {
                this.localStorageService.currentOrganization = new OrganizationDto(organizationName, organizationKey);
                return this.http.get<any>('/api/' + organizationKey + '/user/me',
                    { headers: { 'Authorization': 'Basic ' + btoa(username + ":" + password) } })
                    .map(user => {
                        if (user) {
                            this.localStorageService.currentUser = user.email;
                        }
                        return user;
                    });
            })
    }
    private fetchOrganizationKey(organizationName: string): Observable<String> {
        let searchOrganizationRequestDto: SearchOrganizationRequestDto = new SearchOrganizationRequestDto(organizationName);
        return this.http.post<SearchOrganizationResponsetDto>('/api/organization/key', searchOrganizationRequestDto)
            .map(searchOrganizationResponsetDto => {
                if (searchOrganizationResponsetDto) {
                    return searchOrganizationResponsetDto.organizationKey
                }
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