package org.example.dto;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class EntityReactRequestDTO {
    private String userId;
    private String reactType;
}

