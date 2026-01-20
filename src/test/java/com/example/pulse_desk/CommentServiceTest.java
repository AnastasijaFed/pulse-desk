package com.example.pulse_desk;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.pulse_desk.dto.CommentResponse;
import com.example.pulse_desk.model.Comment;
import com.example.pulse_desk.model.CommentStatus;
import com.example.pulse_desk.repository.CommentRepository;
import com.example.pulse_desk.service.CommentService;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository repository;

    @InjectMocks
    private CommentService service;

    @Test
    void getAllComments_returnsEmptyList_whenRepoEmpty() {
        when(repository.findAll()).thenReturn(List.of());

        List<CommentResponse> result = service.getAllComments();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository).findAll();
    }

    @Test
    void getAllComments_mapsEntitiesToDtos() {
        Instant createdAt = Instant.parse("2026-01-18T10:15:30Z");

        Comment c1 = mock(Comment.class);
        when(c1.getId()).thenReturn(1L);
        when(c1.getContent()).thenReturn("Nice app!");
        when(c1.getStatus()).thenReturn(CommentStatus.ANALYZED);
        when(c1.getCreatedAt()).thenReturn(createdAt);

        when(repository.findAll()).thenReturn(List.of(c1));

        List<CommentResponse> result = service.getAllComments();

        assertEquals(1, result.size());
        CommentResponse dto = result.get(0);
        assertEquals(1L, dto.id());
        assertEquals("Nice app!", dto.content());
        assertEquals("ANALYZED", dto.status());
        assertEquals(createdAt, dto.createdAt());

        verify(repository).findAll();
    }

    @Test
    void submitComment_savesComment_andReturnsDto() {
        String content = "I can't log in";
        Long userId = 42L;

        Instant createdAt = Instant.parse("2026-01-18T10:15:30Z");


        when(repository.save(any(Comment.class))).thenAnswer(invocation -> {
            Comment saved = invocation.getArgument(0);

            if (saved.getCreatedAt() == null) {
            }

            return saved;
        });


        ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);

        CommentResponse dto = service.submitComment(content);

        verify(repository).save(captor.capture());

        Comment saved = captor.getValue();
        assertEquals(content, saved.getContent());
        assertEquals(CommentStatus.RECEIVED, saved.getStatus());


        assertEquals(content, dto.content());
        assertEquals("RECEIVED", dto.status());


        assertNotNull(dto.createdAt());
    }
}

