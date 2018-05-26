import { Word2VecParams } from "./params";

export class Word2Vec {
  id?: Number;
  name: String;
  algorithm: String;
  description: String;
  params: Word2VecParams
}

export class ModelDto {
  id?: Number;
  name: String;
  description: String;
  algorithm: String;
}

export class OrganizationDto {
  constructor(private name: String,
    private key: String,
    private iconUrl: String) { }
}

// TODO: add other necessary properties to Pageable
export class Pageable<T> {
  constructor(public content: T[]) { }
}
