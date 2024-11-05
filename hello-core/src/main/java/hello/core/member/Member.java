package hello.core.member;

public class Member {
    private Long id;
    private String name;
    private Grade garde;

    public Member(Long id, String name, Grade garde) {
        this.id = id;
        this.name = name;
        this.garde = garde;
    }

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

    public Grade getGarde() {
        return garde;
    }

    public void setGarde(Grade garde) {
        this.garde = garde;
    }
}
