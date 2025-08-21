package com.kifiya.automatic_irrigation_system.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_crop_mapping")
@Entity
public class CropMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Column(name="crop")
    private String crop;

    @Column(name="amount_of_water")
    private int amountOfWater;

    public CropMapping(int amountOfWater, String crop) {
        this.amountOfWater = amountOfWater;
        this.crop = crop;
    }
}
