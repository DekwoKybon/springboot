package be.kdg.ip.web.resources;

import be.kdg.ip.domain.TourRating;
import be.kdg.ip.web.TourRatingController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 * Created by wouter on 02.02.17.
 */
@Component
public class RatingResourceAssembler extends ResourceAssemblerSupport<TourRating, RatingResource> {

    public RatingResourceAssembler() {
        super(TourRatingController.class, RatingResource.class);
    }

    /**
     * Convert the TourRating entity to a RatingResource
     *
     * @param tourRating
     * @return RatingResource
     */
    @Override
    public RatingResource toResource(TourRating tourRating) {

        return new RatingResource(tourRating.getScore(),
                tourRating.getComment(),
                tourRating.getPk().getCustomerId());
    }


}
