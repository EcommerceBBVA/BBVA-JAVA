/**
 *
 */
package mx.bbva.client;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Luis Delucio
 */
@Getter
@ToString
public class ExchangeRate {

    @SerializedName("from")
    private String fromCurrency;

    @SerializedName("to")
    private String toCurrency;

    private Date date;

    private BigDecimal value;

    private BigDecimal rate;

}
