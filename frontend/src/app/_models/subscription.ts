export class Subscription {
  maxTrainings: Number;
  maxUploadInKb: Number;
  remainingTrainings: Number;
  subscriptionLevel: String;
}

export class RenewSubscriptionDto {
  subscriptionLevel: String
}
