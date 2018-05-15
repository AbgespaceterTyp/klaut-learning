import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import 'rxjs/add/operator/map'

@Injectable()
export class AuthenticationService {
    constructor(private http: HttpClient) { }

    login(username: string, organization: string, password: string) {
        return this.http.get<any>('/api/' + organization +'/user/me', { headers: { 'Authorization': 'Basic ' + btoa(username + ":" + password) } })
            .map(user => {
                if (user ) {
                    localStorage.setItem('currentUser', user.email);
                    localStorage.setItem('currentOrganization', organization);
                }
                return user;
            });
    }

    callUserMe() {
        let organization = localStorage.getItem('currentOrganization')
        return this.http.get('/api/' + organization + '/user/me');
    }

    logout() {
        // remove user from local storage to log user out
        localStorage.removeItem('currentUser');
        localStorage.removeItem('currentOrganization');
        
    }
}