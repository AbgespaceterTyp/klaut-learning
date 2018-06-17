package de.htwg.klaut.backend.model.dto;

import de.htwg.klaut.backend.model.db.SubscriptionLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SubscriptionRenewDto {
    private SubscriptionLevel subscriptionLevel;
}
