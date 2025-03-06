package jks.lototronback.persistence.icebreakerquestion;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "icebreaker_question", schema = "lototron")
public class IcebreakerQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "icebreaker_question_id_gen")
    @SequenceGenerator(name = "icebreaker_question_id_gen", sequenceName = "icebreaker_question_id_seq", schema = "lototron", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "question", nullable = false)
    private String question;

}