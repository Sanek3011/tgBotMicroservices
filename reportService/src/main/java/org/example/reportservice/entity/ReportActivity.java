package org.example.reportservice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity
@Table(name = "report_activity", schema = "public")
public class ReportActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "type")
    String type;
    @Column(name = "description")
    String description;
    @Column(name = "base_price")
    Integer basePrice;
    @Column(name = "coefficient")
    Boolean coefficient;
}
