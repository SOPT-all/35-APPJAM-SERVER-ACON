package com.acon.server.spot.infra.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "menu",
        indexes = @Index(
                name = "idx_menu_spot_id",
                columnList = "spot_id"
        )
)
public class MenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "spot_id", nullable = false)
    private Long spotId;

    @Column(name = "image")
    private String image;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "main_menu", nullable = false)
    private boolean mainMenu;

    @Builder
    public MenuEntity(
            Long id,
            Long spotId,
            String image,
            String name,
            int price,
            boolean mainMenu
    ) {
        this.id = id;
        this.spotId = spotId;
        this.image = image;
        this.name = name;
        this.price = price;
        this.mainMenu = mainMenu;
    }
}
