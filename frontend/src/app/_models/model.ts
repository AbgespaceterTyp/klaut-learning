import { Word2VecParams } from "./params";
import { TrainingData } from "./training";

export {
  Word2Vec,
  ModelDto,
  OrganizationDto,
  UserDto,
  Pageable,
}

class Word2Vec {
  id?: Number;
  name: String;
  algorithm: String;
  description: String;
  params: Word2VecParams;
  sourceUrl: String;
  training: Boolean;
  trainingData: [TrainingData];
  trainingDuration: Number;
}

class ModelDto {
  id?: Number;
  name: String;
  description: String;
  algorithm: String;
}

class OrganizationDto {
  constructor(public name: String,
    public key: String,
    public iconUrl: String) { }
}

class UserDto {
  constructor(private email: String,
    private firstName: String,
    private lastName: String) { }
}

// TODO: add other necessary properties to Pageable
class Pageable<T> {
  constructor(public content: T[]) { }
}
