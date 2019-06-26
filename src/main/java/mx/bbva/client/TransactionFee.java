package mx.bbva.client;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class TransactionFee {

    private BigDecimal amount;

    private BigDecimal tax;
}
