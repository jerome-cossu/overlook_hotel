import jakarta.persistence.*;
import lombok.Data;
import scala.Int;

@Entity
@Table(name = "roles") 
@Data

public class Role{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "user")

    public Role() {}

    public Role(String name) {
        this.name = name;
    }
}