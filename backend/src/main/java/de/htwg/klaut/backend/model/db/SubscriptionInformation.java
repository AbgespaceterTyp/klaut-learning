package de.htwg.klaut.backend.model.db;

import lombok.Data;

@Data
public class SubscriptionInformation {
    public static final int FREE_SUBS_MAX_UPLOAD_IN_KB = 2 * 1024;
    public static final int NORMAL_SUBS_MAX_UPLOAD_IN_KB = 50 * 1024;

    private int remainingTrainings = 5; // 5 default value for free subscription
    private int maxUploadInKb = FREE_SUBS_MAX_UPLOAD_IN_KB; // 2 MB default value for free subscription
    private SubscriptionLevel subscriptionLevel = SubscriptionLevel.FREE; // default subscription level
}
