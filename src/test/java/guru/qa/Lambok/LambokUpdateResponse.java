package guru.qa.Lambok;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LambokUpdateResponse {
    private String id;
    private String name;
    private String job;
    private String createdAt;
    private String updateAt;
    private String token;
}
