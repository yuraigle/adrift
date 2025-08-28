package ru.orlov.adrift.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ads_images")
public class AdImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ad_id")
    private Ad ad;

    private String filename;

    private Integer ord;

    @Column(name = "orig_filename")
    private String origFilename;

    private String alt;
}
