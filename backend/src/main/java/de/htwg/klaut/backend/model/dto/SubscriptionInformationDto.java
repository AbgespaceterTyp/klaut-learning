package de.htwg.klaut.backend.model.dto;

import de.htwg.klaut.backend.model.db.SubscriptionInformation;
import de.htwg.klaut.backend.model.db.SubscriptionLevel;
import lombok.Data;

@Data
public class SubscriptionInformationDto {
    private int maxTrainings = 1; // 1 default value for free subscription
    private int remainingTrainings = 5; // 5 default value for free subscription
    private int maxUploadInKb = 2 * 1024; // 2 MB default value for free subscription
    private SubscriptionLevel subscriptionLevel = SubscriptionLevel.FREE; // default subscription level

    public SubscriptionInformationDto(SubscriptionInformation subscriptionInformation){
        setMaxTrainings(subscriptionInformation.getMaxTrainings());
        setRemainingTrainings(subscriptionInformation.getRemainingTrainings());
        setMaxUploadInKb(subscriptionInformation.getMaxUploadInKb());
        setSubscriptionLevel(subscriptionInformation.getSubscriptionLevel());
    }
}
