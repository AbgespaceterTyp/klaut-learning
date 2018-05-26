import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpRequest, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map'
import { ModelDto , Pageable} from '../_models';
import { LocalStorageService } from './localstorage.service';
import { Word2VecParams } from '../_models/params';

@Injectable()
export class ModelService {

  constructor(private http: HttpClient, private localStorageService: LocalStorageService) { }

  load() {
    return this.http.get<Pageable<ModelDto>>('/api/' + this.localStorageService.currentOrganization + '/model');
  }

  create(modelDto: ModelDto) {
    return this.http.post('/api/' + this.localStorageService.currentOrganization + '/model', modelDto);
  }

  delete(id: Number) {
    return this.http.delete('/api/' + this.localStorageService.currentOrganization + '/model/' + id + '/delete');
  }

  train(id: Number) {
    return this.http.put('/api/' + this.localStorageService.currentOrganization + '/model/' + id + '/train', {});
  }

  updateParams(params: Word2VecParams, id: Number) {
    return this.http.put('/api/' + this.localStorageService.currentOrganization + '/model/' + id + '/param', params);
  }

  update(model: ModelDto, id: Number) {
    return this.http.put('/api/' + this.localStorageService.currentOrganization + '/model/' + id + '/update', model);
  }

  uploadFile(file: any, id: Number): Observable<HttpEvent<{}>> {
    let formdata: FormData = new FormData();

    formdata.append('fileToUpload', file);

    const req = new HttpRequest('PUT', '/api/' + this.localStorageService.currentOrganization + '/model/' + id + '/source', formdata, {
      reportProgress: true,
      responseType: 'text'
    });

    return this.http.request(req);
  }

}