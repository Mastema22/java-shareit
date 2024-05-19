package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i WHERE i.ownerId = :id")
    List<Item> getItemsByOwner(@Param("id") Long ownerId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Item i WHERE i.id = :itemId AND i.ownerId = :id")
    void deleteItem(@Param("itemId") Long itemId, @Param("id") Long ownerId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Item i WHERE i.ownerId = :id")
    void deleteItemsByOwner(@Param("id") Long ownerId);

    List<Item> findAllByOwnerIdOrderById(Long userId);

    default List<Item> getItemsBySearchQuery(String text) {
        if (text.isBlank()) {
            throw new ValidationException("Передан пустой аргумент!");
        }
        return findAll().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }
}