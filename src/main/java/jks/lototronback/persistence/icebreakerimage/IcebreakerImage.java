package jks.lototronback.persistence.icebreakerimage;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jks.lototronback.persistence.icebreakerquestion.IcebreakerQuestion;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "icebreaker_image", schema = "lototron")
public class IcebreakerImage {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "icebreaker_image_id_gen")
    @SequenceGenerator(name = "icebreaker_image_id_gen", sequenceName = "icebreaker_image_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "data", nullable = false)
    private byte[] data;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "icebreaker_question_id", nullable = false)
    private IcebreakerQuestion icebreakerQuestion;

}