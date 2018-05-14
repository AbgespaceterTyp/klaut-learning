import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpRequest, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map'
import { ModelDto } from '../_models';
import { Word2VecParams } from '../_models/params';

@Injectable()
export class ModelService {
    constructor(private http: HttpClient) { }

    load() {
      return this.http.get<any>('/api/klaut-learning2/model', { headers: { 'Authorization': 'Basic ' + btoa("admin@klaut.de" + ":" + "password") } });
    }

    create(modelDto: ModelDto) {
      return this.http.post('/api/klaut-learning2/model', modelDto);
    }

    delete(id: Number) {
      return this.http.delete('/api/klaut-learning2/model/' + id + '/delete');
    }

    updateParams(params: Word2VecParams, id: Number) {
      return this.http.put('/api/klaut-learning2/model/' + id + '/param', params);
    }

    update(model: ModelDto, id: Number) {
      return this.http.put('/api/klaut-learning2/model/' + id + '/update', model);
    }

    uploadFile(file: any, id: Number): Observable<HttpEvent<{}>> {
      let formdata: FormData = new FormData();
 
      formdata.append('fileToUpload', file);
  
      const req = new HttpRequest('PUT', '/api/klaut-learning2/model/' + id + '/source', formdata, {
        reportProgress: true,
        responseType: 'text'
      });
  
      return this.http.request(req);
    }
    
}