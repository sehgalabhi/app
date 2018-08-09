package com.abhi.springdata;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Objects;

@SpringBootApplication
public class SpringDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDataApplication.class, args);

    }

    @Bean
    CommandLineRunner initData(EmployeeRepository employeeRepository, ManagerRepository managerRepository) {
        return args -> {

            Manager abhi = managerRepository.save(new Manager("Abhi"));
            Manager m2 = managerRepository.save(new Manager("Sauron"));

            employeeRepository.save(new Employee("Frodo", "Baggins", "ring bearer", abhi));
            employeeRepository.save(new Employee("Frodo", "Baggins", "burglar", abhi));
            employeeRepository.save(new Employee("Samwise", "gambee", "gardener", abhi));
        };
    }

}

@Entity
class Employee {
    @GeneratedValue
    @Id
    Long id;
    String firstName;
    String lastName;
    String role;

    @ManyToOne
    Manager manager;


    public Employee(String firstName, String lastName, String role, Manager manager) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.manager = manager;
    }

    public Employee() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) &&
                Objects.equals(firstName, employee.firstName) &&
                Objects.equals(lastName, employee.lastName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, firstName, lastName);
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

@RepositoryRestResource
interface EmployeeRepository extends CrudRepository<Employee, Long> {

    List<Employee> findByLastName(@Param("q") String lastName);
}

@Entity
class Manager{
    @Id @GeneratedValue Long id;
    String name;

    @OneToMany(mappedBy = "manager")
    List<Employee> employees;

    public Manager() {
    }

    public Manager(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

@RepositoryRestResource
interface ManagerRepository extends CrudRepository<Manager, Long>{

    List<Manager> findByEmployeesRoleContains(@Param("q") String role);
    List<Manager> findDistinctByEmployeesLastName(@Param("q") String role);

}