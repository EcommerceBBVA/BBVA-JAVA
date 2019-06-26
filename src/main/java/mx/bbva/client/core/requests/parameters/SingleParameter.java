package mx.bbva.client.core.requests.parameters;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleParameter extends Parameter {
    private String parameterValue;

    public SingleParameter(String name, String value) {
        this.parameterName = name;
        this.parameterValue = value;
    }
}
