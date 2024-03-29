package org.example.accessor.user.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class User {
    private String userId;
    private String userName;
    private String emailId;
}