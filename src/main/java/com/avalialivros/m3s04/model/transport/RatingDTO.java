package com.avalialivros.m3s04.model.transport;

import com.avalialivros.m3s04.model.Rating;

public record RatingDTO(String guid, Integer grade, PersonDTO ratedBy) {
    public RatingDTO(Rating rating){
        this(rating.getGuid(),
                rating.getGrade(),
                new PersonDTO(rating.getRatedBy())
        );
    }
}
