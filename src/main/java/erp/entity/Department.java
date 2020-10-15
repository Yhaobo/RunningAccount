package erp.entity;

import lombok.Data;

@Data
public class Department extends Option {

    public Department() {
    }

    public Department(String name) {
        this.name = name;
    }
}
