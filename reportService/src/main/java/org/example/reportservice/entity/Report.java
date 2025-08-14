package org.example.reportservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity
@Table(name = "reports", schema = "public")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "data")
    LocalDate dateOfCreation;
    @Column(name = "description")
    String description;
    @Column(name = "url")
    String url;
    @Column(name = "user_id")
    Long userId;
    @Enumerated(EnumType.STRING)
    Status status;
    @Column(name = "cost")
    Integer cost;
    @ManyToOne
    @JoinColumn(name = "report_activity_id")
    ReportActivity reportActivity;
}
