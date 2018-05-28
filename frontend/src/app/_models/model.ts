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
