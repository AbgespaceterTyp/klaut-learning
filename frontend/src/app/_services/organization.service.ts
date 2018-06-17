import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpRequest, HttpEvent, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import { ModelDto, UserDto, CreateOrganizationDto } from '../_models';
import { LocalStorageService } from './localstorage.service';
import { Subscription, RenewSubscriptionDto } from '../_models/subscription';

@Injectable()
export class OrganizationService {

    constructor(private http: HttpClient, private localStorageService: LocalStorageService) { }

    getSubscription() {
        return this.http.get<Subscription>('/api/' + this.localStorageService.currentOrganization.key + '/subscription');
    }

    renewSubscription(level: String) {
      let dto: RenewSubscriptionDto = {
        subscriptionLevel: level
      }
      return this.http.post<Subscription>('/api/' + this.localStorageService.currentOrganization.key + '/subscription', dto);
  }

    create(createOrganizationDto: CreateOrganizationDto) {
        return this.http.post('/api/organization', createOrganizationDto);
    }

    getOrganizationImageUrl() {
        return `api/${this.localStorageService.currentOrganization.key}/image`;
    }

    uploadFile(file: any): Observable<HttpEvent<{}>> {
        let formdata: FormData = new FormData();

        formdata.append('image', file);

        const req = new HttpRequest('PUT', '/api/' + this.localStorageService.currentOrganization.key + '/image', formdata, {
            reportProgress: true,
            responseType: 'text'
        });

        return this.http.request(req);
    }
}
