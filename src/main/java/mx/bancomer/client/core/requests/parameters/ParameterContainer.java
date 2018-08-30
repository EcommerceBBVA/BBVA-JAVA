package mx.bancomer.client.core.requests.parameters;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Data
public class ParameterContainer extends Parameter {
    List<Parameter> parameterValues;

    public ParameterContainer(String parameterName, List<Parameter> parameterValues) {
        this.parameterName = parameterName;
        this.parameterValues = parameterValues;
    }

    public ParameterContainer(String container) {
        this.parameterName = container;
        this.parameterValues = new ArrayList<Parameter>();
    }

    public void addValue(String name, String value) {
        this.parameterValues.add(new SingleParameter(name, value));
    }

    public void addMultiValue(ParameterContainer multiValue) {
        this.parameterValues.add(multiValue);
    }
}
