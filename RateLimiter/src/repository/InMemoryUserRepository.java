package repository;

import model.User;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserRepository {
    private final Map<Long, User> users = new HashMap<>();
    public User getUseraById(long id) {
        return users.get(id);
    }
    public User addUser(User user) {
        users.put(user.getId(), user);
        return user;
    }
}
