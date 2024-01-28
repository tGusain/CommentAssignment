package org.example.service.model.enums;

import lombok.Getter;
import org.example.exceptions.ValidationException;

@Getter
public enum ReactType {
    UPVOTE("upVote"), DOWNVOTE("downVote");

    final String val;

    ReactType(String val) {
        this.val = val;
    }

    public static void validateReactType(String reactType) {
        boolean isValid = false;
        for (ReactType value : ReactType.values()) {
            if (value.name().equals(reactType.toUpperCase())) {
                isValid = true;
                break;
            }
        }
        if (!isValid) {
            throw new ValidationException(String.format("ReactType %s passed is invalid.", reactType));
        }
    }
}
