package jks.lototronback.persistence.userimage;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jks.lototronback.persistence.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_image", schema = "lototron")
public class UserImage {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_image_id_gen")
    @SequenceGenerator(name = "user_image_id_gen", sequenceName = "user_image_id_seq", schema = "lototron", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(name = "data", nullable = false)
    private byte[] data;

}