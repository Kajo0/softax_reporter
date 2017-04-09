package softax.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class FileToProcess implements Serializable {

    public enum Status {NEW, IN_PROGRESS, DONE, ERROR}

    private String path;
    private Status status;
    private Boolean pdf;

}
