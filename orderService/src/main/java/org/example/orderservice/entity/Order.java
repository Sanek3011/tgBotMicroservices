package org.example.orderservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder

@Entity
@Table(name = "orders", schema = "public")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "item_id")
    Item item;
    @Column(name = "user_id")
    Long userId;
    @Column(name = "data")
    @CreationTimestamp
    LocalDateTime date;
    @Enumerated(EnumType.STRING)
    Status status;

    @Override
    public String toString() {
        return String.format("id: %d Заказчик: ID: %s, заказ: %s, дата заказа: %s", id, userId, item.getDesc(), date);
    }
}
