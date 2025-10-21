package com.dashboard.personal.repository;


import com.dashboard.personal.entity.Note;
import com.dashboard.personal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findAllByOrderByUpdatedAtDesc();
    List<Note> findByUserOrderByUpdatedAtDesc(User user);
}