package com.myproject.book.feedback;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponse {

    private Double rating;
    private String comment;
    private boolean ownFeedback;
}

