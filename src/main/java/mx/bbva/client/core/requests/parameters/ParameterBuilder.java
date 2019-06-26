package mx.bbva.client.core.requests.parameters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParameterBuilder {

    /**
     * Method to pass the parameters to a map
     */
    public Map<String, Object> AsMap(List<Parameter> params) {
        Map<String, Object> paramsAsMap = new HashMap<String, Object>();
        for (Parameter param : params) {
            paramsAsMap.putAll(instanceSingleParameter(param));
            paramsAsMap.putAll(instanceContainer(param));
        }
        return paramsAsMap;
    }

    private Map<String, Object> instanceSingleParameter(Parameter parameter) {
        Map<String, Object> values = new HashMap<String, Object>();
        if (parameter instanceof SingleParameter) {
            SingleParameter finalParam = (SingleParameter) parameter;
            values.put(finalParam.getParameterName(), finalParam.getParameterValue());
        }
        return values;
    }

    private Map<String, Object> instanceContainer(Parameter parameter) {
        Map<String, Object> finalValues = new HashMap<String, Object>();
        if (parameter instanceof ParameterContainer) {
            Map<String, Object> values = new HashMap<String, Object>();
            ParameterContainer container = (ParameterContainer) parameter;
            for (Parameter param : container.getParameterValues()) {
                values.putAll(instanceSingleParameter(param));
                values.putAll(instanceContainer(param));
            }
            finalValues.put(container.getParameterName(), values);
        }
        return finalValues;
    }
    /** end */
}
