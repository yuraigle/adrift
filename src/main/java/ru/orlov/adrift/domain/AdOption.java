package ru.orlov.adrift.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ads_options")
@IdClass(AdOption.AdOptionId.class)
public class AdOption {

    @Id
    @ManyToOne
    @JoinColumn(name = "ad_id")
    private Ad ad;

    @Id
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @Id
    @ManyToOne
    @JoinColumn(name = "option_id")
    private Option option;

    @Getter
    @Setter
    static class AdOptionId implements Serializable {
        private Ad ad;
        private Question question;
        private Option option;

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            AdOptionId that = (AdOptionId) o;
            return Objects.equals(ad.getId(), that.ad.getId())
                    && Objects.equals(question.getId(), that.question.getId())
                    && Objects.equals(option.getId(), that.option.getId());
        }

        @Override
        public int hashCode() {
            return Objects.hash(ad.getId(), question.getId(), option.getId());
        }
    }
}
