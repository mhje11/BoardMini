package org.example.boardmini.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {
    @Id
    private Long id;
    @Column(value = "userId")
    //왜 userId는 명시적으로 안적어주면 인식못하는지 잘 모르겠음
    private String userId;
    private String username;
    private String password;
}
