package com.example.pulse_desk;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.pulse_desk.controller.CommentController;
import com.example.pulse_desk.dto.CommentResponse;
import com.example.pulse_desk.service.CommentService;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService service;

    @Test
    void getComments_returnsList() throws Exception {
        CommentResponse dto =
                new CommentResponse(1L, "Hello", 5L, "RECEIVED", Instant.now());

        when(service.getAllComments()).thenReturn(List.of(dto));

        mockMvc.perform(get("/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(service).getAllComments();
    }

    @Test
    void postComments_createsComment() throws Exception {
        CommentResponse created =
                new CommentResponse(99L, "Test", 7L, "RECEIVED", Instant.now());

        when(service.submitComment("Test", 7L)).thenReturn(created);

        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"Test\",\"userId\":7}"))
                .andExpect(status().isCreated());

        verify(service).submitComment("Test", 7L);
    }
}
