import { Word2VecParams } from "./params";
import { TrainingData } from "./training";

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
    public key: String) { }
}

export class UserDto {
  constructor(private email?: String,
    private firstName?: String,
    private lastName?: String,
    private password?: String) { }
}

export class SearchOrganizationRequestDto {
  constructor(private organizationName: String) { }
}

export class SearchOrganizationResponsetDto {
  constructor(public organizationKey: String) { }
}

export class CreateOrganizationDto {
  constructor(public name?: string, public adminEmail?: string,
    public adminPassword?: string) { }
}
