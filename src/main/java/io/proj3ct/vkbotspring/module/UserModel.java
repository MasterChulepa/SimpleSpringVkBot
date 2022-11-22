package io.proj3ct.vkbotspring.module;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import java.sql.Timestamp;

@Entity(name = "usersData")
@Data
public class UserModel {
    @Id
    private Long id;

    private String firstName;

    private String lastName;
    @NotNull
    private String username;

    private Timestamp registeredAt;

}
