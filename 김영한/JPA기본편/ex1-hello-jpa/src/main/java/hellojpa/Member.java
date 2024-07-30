package hellojpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity //필수 - 그래야 jpa 로딩 시 인지함
public class Member {

    @Id
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
