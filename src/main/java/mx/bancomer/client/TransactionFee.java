package mx.bancomer.client;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TransactionFee {
	
	private BigDecimal amount;
	
	private BigDecimal tax;
}
