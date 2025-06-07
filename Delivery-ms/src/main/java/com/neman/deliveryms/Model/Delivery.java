package com.neman.deliveryms.Model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long orderId;
    String trackingNumber;
    String deliveryAddress;
    String deliveryStatus;
    String carrier;
    LocalDate expectedDate;
    LocalDate shippedDate;
    LocalDate deliveredDate;
    String recipientName;
    String recipientPhone;

}
