package de.htwg.klaut.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.PAYMENT_REQUIRED, reason = "Payment is required for this action")
public class PaymentRequiredException extends RuntimeException {
}
