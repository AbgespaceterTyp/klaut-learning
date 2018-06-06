import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpRequest, HttpEvent, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map'
import { ModelDto, Pageable, Word2Vec } from '../_models';
import { LocalStorageService } from './localstorage.service';
import { Word2VecParams } from '../_models/params';
import { Word2VecTestingResponse } from '../_models/testing';

@Injectable()
export class ModelService {

  constructor(private http: HttpClient, private localStorageService: LocalStorageService) { }

  load() {    
    return this.http.get<ModelDto[]>('/api/' + this.localStorageService.currentOrganization.key + '/model');
  }

  loadModel(id: String) {
    return this.http.get<Word2Vec>('/api/' + this.localStorageService.currentOrganization.key + '/model/' + id);
  }

  create(modelDto: ModelDto) {
    return this.http.post('/api/' + this.localStorageService.currentOrganization.key + '/model', modelDto);
  }

  delete(id: String) {
    return this.http.delete('/api/' + this.localStorageService.currentOrganization.key + '/model/' + id + '/delete');
  }

  train(id: String) {
    return this.http.put('/api/' + this.localStorageService.currentOrganization.key + '/model/' + id + '/train', {});
  }

  updateParams(params: Word2VecParams, id: String) {
    return this.http.put('/api/' + this.localStorageService.currentOrganization.key + '/model/' + id + '/param', params);
  }

  update(model: ModelDto, id: String) {
    return this.http.put('/api/' + this.localStorageService.currentOrganization.key + '/model/' + id + '/update', model);
  }

  uploadFile(file: any, id: String): Observable<HttpEvent<{}>> {
    let formdata: FormData = new FormData();

    formdata.append('fileToUpload', file);

    const req = new HttpRequest('PUT', '/api/' + this.localStorageService.currentOrganization.key + '/model/' + id + '/source', formdata, {
      reportProgress: true,
      responseType: 'text'
    });

    return this.http.request(req);
  }

  download(id: String, sourceUrl: String) {
 
  }

  test(id: String, sourceUrl: String, testWord: String) {
    let Params = new HttpParams();

    Params = Params.append('modelSourceUrl', sourceUrl as string);
    Params = Params.append('testWord', testWord.trim() as string);
    return this.http.get<Word2VecTestingResponse>('/api/' + this.localStorageService.currentOrganization.key + '/model/' + id + '/test' , {params: Params})
  }
}