package de.htwg.klaut.backend.model.db;

import lombok.Data;

@Data
public class SubscriptionInformation {
    private int remainingTrainings = 5; // 5 default value for free subscription
    private int maxUploadInKb = 2 * 1024; // 2 MB default value for free subscription
    private SubscriptionLevel subscriptionLevel = SubscriptionLevel.FREE; // default subscription level
}
