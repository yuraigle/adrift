package ru.orlov.adrift.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ads_fields")
@IdClass(AdField.AdFieldId.class)
public class AdField {

    @Id
    @ManyToOne
    @JoinColumn(name = "ad_id")
    private Ad ad;

    @Id
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @Column(name = "val_number")
    private Integer valNumber;

    @Column(name = "val_decimal")
    private BigDecimal valDecimal;

    @Column(name = "val_text")
    private String valText;

    @Getter
    @Setter
    static class AdFieldId implements Serializable {
        private Ad ad;
        private Question question;

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            AdFieldId that = (AdFieldId) o;
            return Objects.equals(ad.getId(), that.ad.getId())
                    && Objects.equals(question.getId(), that.question.getId());
        }

        @Override
        public int hashCode() {
            return Objects.hash(ad.getId(), question.getId());
        }
    }

    public String toString() {
        return switch (question.getType()) {
            case NUMBER -> valNumber.toString();
            case DECIMAL -> valDecimal.toString();
            default -> valText;
        };
    }
}
