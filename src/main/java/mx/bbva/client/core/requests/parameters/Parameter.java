package mx.bbva.client.core.requests.parameters;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Parameter {
    String parameterName;

    public Parameter parameterName(final String parameterName) {
        this.parameterName = parameterName;
        return this;
    }
}
