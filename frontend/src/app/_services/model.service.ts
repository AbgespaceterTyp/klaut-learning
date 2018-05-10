import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map'
import { ModelDto } from '../_models';
import { Word2VecParams } from '../_models/params';

@Injectable()
export class ModelService {
    constructor(private http: HttpClient) { }

    load() {
      return this.http.get<any>('/api/klaut-learning2/model');
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
    
}