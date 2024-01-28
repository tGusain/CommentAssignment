package org.example.accessor.user;

import org.example.accessor.user.model.User;
import org.example.exceptions.ValidationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserAccessorImpl {

    private final Map<String, User> userMap = new HashMap<>();

    public void validateUser(String userId)  {
        if (!userMap.containsKey(userId)) {
            throw new ValidationException(String.format("User with userId %s is not present.", userId));
        }
    }

    public List<User> bulkCreateUser() {
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            users.add(createUser(User.builder()
                    .emailId("god" + i + "@gmail.com")
                    .userId("god" + i)
                    .userName("god" + i)
                    .build()));
        }
        return users;
    }

    public User createUser(final User user) {
        userMap.put(user.getUserId(), user);
        return user;
    }
}
