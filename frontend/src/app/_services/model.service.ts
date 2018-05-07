import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map'

@Injectable()
export class ModelService {
    constructor(private http: HttpClient) { }

    train() {
      return this.http.get<any>('/api/model');
            
    }
    
}