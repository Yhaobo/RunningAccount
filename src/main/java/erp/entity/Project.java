package erp.entity;

import lombok.Data;

@Data
public class Project extends Option {

    public Project() {
    }

    public Project(String name) {
        this.name = name;
    }

}
