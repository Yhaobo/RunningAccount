package erp.entity;

import lombok.Data;

@Data
public class Category extends Option {

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

}
