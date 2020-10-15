package erp.entity;

import lombok.Data;

@Data
public class Account extends Option {
    public Account() {
    }

    public Account(String name) {
        this.name = name;
    }
}
