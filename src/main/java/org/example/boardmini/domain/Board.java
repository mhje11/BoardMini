package org.example.boardmini.domain;


import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    @Id
    private Long id;
    private String name;
    private String title;
    private String password;
    private String content;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private Long likes = 0L; //예약어여서 like 안됨

}
