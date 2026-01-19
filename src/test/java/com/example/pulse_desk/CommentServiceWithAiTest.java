package com.example.pulse_desk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.pulse_desk.ai.AiTicketDecision;
import com.example.pulse_desk.ai.AiTriageService;
import com.example.pulse_desk.dto.CommentResponse;
import com.example.pulse_desk.dto.TicketResponse;
import com.example.pulse_desk.model.Comment;
import com.example.pulse_desk.model.CommentStatus;
import com.example.pulse_desk.model.TicketCategory;
import com.example.pulse_desk.model.TicketPriority;
import com.example.pulse_desk.repository.CommentRepository;
import com.example.pulse_desk.service.CommentService;
import com.example.pulse_desk.service.TicketService;

@ExtendWith(MockitoExtension.class)
class CommentServiceWithAiTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private AiTriageService aiTriageService;

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private CommentService service;

    @BeforeEach
    void setup() throws Exception {
        // Simulate JPA assigning an ID to the Comment on first save
        when(commentRepository.save(any(Comment.class)))
                .thenAnswer(invocation -> {
                    Comment c = invocation.getArgument(0);
                    if (c.getId() == null) {
                        var idField = Comment.class.getDeclaredField("id");
                        idField.setAccessible(true);
                        idField.set(c, 1L);
                    }
                    return c;
                });
    }

    @Test
    void submitComment_whenAiSaysNoTicket_setsAnalyzed() {
        when(aiTriageService.analyzeComment(any()))
                .thenReturn(new AiTicketDecision(
                        false,
                        null,
                        null,
                        null,
                        null
                ));

        CommentResponse response = service.submitComment("Nice app!", 10L);

        assertEquals(CommentStatus.ANALYZED.name(), response.status());
        verify(ticketService, never()).createNewTicket(any(), any(), any(), any(), anyLong());
        verify(commentRepository, times(2)).save(any(Comment.class));
    }

    @Test
    void submitComment_whenAiSaysCreateTicket_createsTicket() {
        when(aiTriageService.analyzeComment(any()))
                .thenReturn(new AiTicketDecision(
                        true,
                        "Cannot log in",
                        "account",
                        "high",
                        "User cannot log in"
                ));

        // Only stub TicketService in the test that actually uses it (avoids UnnecessaryStubbingException)
        when(ticketService.createNewTicket(any(), any(), any(TicketCategory.class), any(TicketPriority.class), anyLong()))
                .thenReturn(new TicketResponse(
                        100L,
                        1L,
                        "Cannot log in",
                        TicketCategory.ACCOUNT.name(),
                        TicketPriority.HIGH.name(),
                        "User cannot log in",
                        java.time.Instant.now()
                ));

        CommentResponse response = service.submitComment("I cannot log in", 5L);

        assertEquals(CommentStatus.TICKET_CREATED.name(), response.status());

        verify(ticketService).createNewTicket(
                eq("Cannot log in"),
                eq("User cannot log in"),
                eq(TicketCategory.ACCOUNT),
                eq(TicketPriority.HIGH),
                eq(1L) // assigned in setup() on first comment save
        );

        verify(commentRepository, times(2)).save(any(Comment.class));
    }

    @Test
    void submitComment_whenAiThrowsException_setsAnalysisFailed() {
        when(aiTriageService.analyzeComment(any()))
                .thenThrow(new RuntimeException("HF down"));

        CommentResponse response = service.submitComment("Something broke", 3L);

        assertEquals(CommentStatus.ANALYSIS_FAILED.name(), response.status());
        verify(ticketService, never()).createNewTicket(any(), any(), any(), any(), anyLong());
        verify(commentRepository, times(2)).save(any(Comment.class));
    }

    @Test
    void submitComment_alwaysSavesCommentFirst() {
        when(aiTriageService.analyzeComment(any()))
                .thenThrow(new RuntimeException("HF timeout"));

        InOrder inOrder = inOrder(commentRepository, aiTriageService);

        service.submitComment("Test comment", 1L);

        inOrder.verify(commentRepository).save(any(Comment.class));
        inOrder.verify(aiTriageService).analyzeComment(any());

        verify(commentRepository, times(2)).save(any(Comment.class));
    }
}
