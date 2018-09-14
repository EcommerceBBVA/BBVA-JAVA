package mx.bancomer.client.core.requests.parameters;

import com.google.gson.internal.LinkedTreeMap;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Slf4j
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

    @SuppressWarnings("unchecked")
    public ParameterContainer(String name, Map<String, ?> map) {
        this.parameterName = name;
        this.parameterValues = new ArrayList<Parameter>();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            if (entry.getValue() instanceof LinkedTreeMap) {
                LinkedTreeMap treeMap = (LinkedTreeMap) entry.getValue();
                addMultiValue(new ParameterContainer(entry.getKey(), treeMap));
            } else {
                addValue(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
    }

    public void addValue(String name, String value) {
        this.parameterValues.add(new SingleParameter(name, value));
    }

    public void addMultiValue(ParameterContainer multiValue) {
        this.parameterValues.add(multiValue);
    }

    public SingleParameter getSingleValue(String value) {
        for (Parameter parameter : this.parameterValues) {
            if (parameter.getParameterName().equals(value) && parameter instanceof SingleParameter) {
                return (SingleParameter) parameter;
            }
        }
        return null;
    }

    public ParameterContainer getContainerValue(String value) {
        for (Parameter parameter : this.parameterValues) {
            if (parameter.getParameterName().equals(value) && parameter instanceof ParameterContainer) {
                return (ParameterContainer) parameter;
            }
        }
        return null;
    }
}
