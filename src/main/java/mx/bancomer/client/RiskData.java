package mx.bancomer.client;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RiskData {

    private String score;

    private List<String> rules;

}
