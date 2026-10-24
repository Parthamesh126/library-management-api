package com.symphonyai.library.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(
    name = "members",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_member_email",
        columnNames = "email"
    )
)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(length = 20)
    private String phone;

    @OneToMany(mappedBy = "member")
    private List<BorrowRecord> borrowRecords = new ArrayList<>();

}