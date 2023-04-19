package de.gothaer.webapp.repositories.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.Hibernate;


import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name ="tbl_personen")
public class PersonEntity {

    @Id
    @Column(nullable = false, length = 36)
    private String id;
    @Column(nullable = false, length = 30)
    private String vorname;
    @Column(nullable = false, length = 30)
    private String nachname;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PersonEntity person = (PersonEntity) o;
        return id != null && Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
