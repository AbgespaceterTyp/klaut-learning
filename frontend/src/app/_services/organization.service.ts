import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpRequest, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import { ModelDto, Pageable, UserDto, OrganizationDto } from '../_models';
import { LocalStorageService } from './localstorage.service';
import { Word2VecParams } from '../_models/params';

@Injectable()
export class OrganizationService {

    constructor(private http: HttpClient, private localStorageService: LocalStorageService) { }
}