import { Word2VecParams } from "./params";
import { TrainingData } from "./training";
import { Word2VecTestingResponse } from "./testing";

export class Word2Vec {
  id?: String;
  name: String;
  algorithm: String;
  description: String;
  params: Word2VecParams;
  sourceUrl: String;
  training: Boolean;
  trainingData: [TrainingData];
  trainingDuration: Number;
}

export class ModelDto {
  id?: String;
  name: String;
  description: String;
  algorithm: String;
}

export class OrganizationDto {
  constructor(public name: String,
    public key: String,
    public iconUrl: String) { }
}

export class UserDto {
  constructor(private email: String,
    private firstName: String,
    private lastName: String) { }
}

// TODO: add other necessary properties to Pageable
export class Pageable<T> {
  constructor(public content: T[]) { }
}
