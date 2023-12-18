package ru.headsandhands.manga.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.headsandhands.manga.Model.User;

@Repository
public interface UserRepositories extends JpaRepository<User, Long> {
}
