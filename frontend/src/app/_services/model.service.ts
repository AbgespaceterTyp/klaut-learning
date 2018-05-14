import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpRequest, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map'
import { ModelDto } from '../_models';
import { Word2VecParams } from '../_models/params';

@Injectable()
export class ModelService {
    private organization;
  
    constructor(private http: HttpClient) { 
      this.organization = localStorage.getItem('currentOrganization');
    }

    load() {
      return this.http.get<any>('/api/'+ this.organization +'/model');
    }

    create(modelDto: ModelDto) {
      return this.http.post('/api/' + this.organization + '/model', modelDto);
    }

    delete(id: Number) {
      return this.http.delete('/api/' + this.organization + '/model/' + id + '/delete');
    }

    train(id: Number) {
      return this.http.put('/api/' + this.organization + '/model/' + id + '/train', {});
    }

    updateParams(params: Word2VecParams, id: Number) {
      return this.http.put('/api/' + this.organization + '/model/' + id + '/param', params);
    }

    update(model: ModelDto, id: Number) {
      return this.http.put('/api/' + this.organization + '/model/' + id + '/update', model);
    }

    uploadFile(file: any, id: Number): Observable<HttpEvent<{}>> {
      let formdata: FormData = new FormData();
 
      formdata.append('fileToUpload', file);
  
      const req = new HttpRequest('PUT', '/api/' + this.organization + '/model/' + id + '/source', formdata, {
        reportProgress: true,
        responseType: 'text'
      });
  
      return this.http.request(req);
    }
    
}