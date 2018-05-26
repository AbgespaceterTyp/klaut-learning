import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpRequest, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import { ModelDto } from '../_models';
import { LocalStorageService } from './localstorage.service';
import { OrganizationDto, Pageable } from '../_models/model';

@Injectable()
export class OrganizationService {
    private mocks = [new OrganizationDto("Klaut Learnig 1", "key1", "https://picsum.photos/300/300/?random"),
    new OrganizationDto("Klaut Learnig 2", "key2", "https://picsum.photos/300/300/?random")]

    constructor(private http: HttpClient, private localStorageService: LocalStorageService) { }

    load(): Observable<Pageable<OrganizationDto>> {    
        return Observable.of(new Pageable(this.mocks));
    }
}
