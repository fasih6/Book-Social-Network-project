package com.myproject.book.feedback;

import com.myproject.book.book.Book;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class FeedbackMapper {
    public Feedback toFeedback(@Valid FeedbackRequest feedbackRequest) {
        return Feedback.builder()
                .rating(feedbackRequest.rating())
                .comment(feedbackRequest.comment())
                .book(Book.builder()
                        .id(feedbackRequest.bookId())
                        .archived(false) // Not required and has no impact : just to satisfy lombok
                        .shareable(false) // Not required and has no impact : just to satisfy lombok
                        .build()
                )
                .build();
    }


    public FeedbackResponse toFeedbackResponse(Feedback feedback, Long id) {
        return FeedbackResponse.builder()
                .rating(feedback.getRating())
                .comment(feedback.getComment())
                .ownFeedback(feedback.getCreatedBy().equals(id))
                .build();
    }
}
