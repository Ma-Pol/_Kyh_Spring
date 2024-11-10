package hello.core;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HelloLombok {
    private String name;
    private int age;

    public static void main(String[] args) {
        HelloLombok helloLombok = new HelloLombok();
        helloLombok.setName("Lombok");
        helloLombok.setAge(30);

        String name = helloLombok.getName();
        System.out.println("name = " + name);

        int age = helloLombok.getAge();
        System.out.println("age = " + age);

        System.out.println("helloLombok = " + helloLombok);
    }
}
