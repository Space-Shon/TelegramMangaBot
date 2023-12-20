package ru.headsandhands.manga.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.headsandhands.manga.Model.News;

@Repository
public interface NewsRepositories extends JpaRepository<News, Long> {
}
