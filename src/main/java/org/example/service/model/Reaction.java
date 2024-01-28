package org.example.service.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.service.model.enums.ReactType;

@Getter
@Builder
@ToString
public class Reaction {
    private String entityId;
    private String userId;
    @Setter
    private ReactType reactType;
    private Long createdAt;
}
