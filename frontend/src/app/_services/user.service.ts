import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpRequest, HttpEvent, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import { UserDto } from '../_models';
import { LocalStorageService } from './localstorage.service';

@Injectable()
export class UserService {

    mocks = [new UserDto("Test@test", "first", "last"),
    new UserDto("Test@test", "first", "last"),
    new UserDto("Test@test", "first", "last")]

    constructor(private http: HttpClient, private localStorageService: LocalStorageService) { }

    load(): Observable<UserDto[]> {
        return this.http.get<UserDto[]>('/api/' + this.localStorageService.currentOrganization.key + '/user');
    }

    deleteUser(email: string) {
        let params = new HttpParams().set('email', email);
        return this.http.delete('/api/' + this.localStorageService.currentOrganization.key + '/user', { params });
    }

}