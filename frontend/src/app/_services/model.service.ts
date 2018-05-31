import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpRequest, HttpEvent, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map'
import { ModelDto, Pageable } from '../_models';
import { LocalStorageService } from './localstorage.service';
import { Word2VecParams } from '../_models/params';

@Injectable()
export class ModelService {

  constructor(private http: HttpClient, private localStorageService: LocalStorageService) { }

  load() {    
    return this.http.get<ModelDto[]>('/api/' + this.localStorageService.currentOrganization.key + '/model');
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

    // Begin assigning parameters
    Params = Params.append('modelSourceUrl', sourceUrl as string);
    Params = Params.append('testWord', testWord as string);
    return this.http.get('/api/' + this.localStorageService.currentOrganization.key + '/model/' + id + '/test' , {params: Params})
  }


}