package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

public interface ItemRequestRepository extends JpaRepository<Item, Long> {
}
