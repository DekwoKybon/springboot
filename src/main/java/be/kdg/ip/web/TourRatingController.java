package be.kdg.ip.web;

import be.kdg.ip.domain.Tour;
import be.kdg.ip.domain.TourRating;
import be.kdg.ip.domain.TourRatingPk;
import be.kdg.ip.repositories.TourRatingRepository;
import be.kdg.ip.repositories.TourRepository;
import be.kdg.ip.web.resources.RatingResource;
import be.kdg.ip.web.resources.RatingResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by wouter on 02.02.17.
 */
@RepositoryRestController
public class TourRatingController {

    private TourRatingRepository tourRatingRepository;
    private TourRepository tourRepository;
    private RatingResourceAssembler ratingResourceAssembler;


    @Autowired
    public TourRatingController(TourRatingRepository tourRatingRepository,
                                TourRepository tourRepository,
                                RatingResourceAssembler ratingResourceAssembler) {

        this.tourRatingRepository = tourRatingRepository;
        this.tourRepository = tourRepository;
        this.ratingResourceAssembler = ratingResourceAssembler;
    }

    protected TourRatingController() {

    }

    /**
     * Create a Tour Rating.
     *
     * @param tourId
     * @param RatingResource
     */
    @RequestMapping(path = "/tours/{tourId}/ratings", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createTourRating(@PathVariable(value = "tourId") int tourId,
                                 @RequestBody @Validated RatingResource RatingResource) {

        Tour tour = verifyTour(tourId);
        tourRatingRepository.save(new TourRating(new TourRatingPk(tour, RatingResource.getCustomerId()),
                RatingResource.getScore(), RatingResource.getComment()));
    }

    /**
     * Lookup a the Ratings for a tour.
     *
     * @param tourId
     * @param pageable
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, path="/tours/{tourId}/ratings")
    @ResponseBody
    public Page<RatingResource> getAllRatingsForTour(@PathVariable(value = "tourId") int tourId, Pageable pageable) {

        Tour tour = verifyTour(tourId);
        Page<TourRating> tourRatingPage = tourRatingRepository.findByPkTourId(tour.getId(), pageable);
        List<RatingResource> ratingDtoList = tourRatingPage.getContent()
                .stream()
                .map(tourRating -> ratingResourceAssembler.toResource(tourRating)).collect(Collectors.toList());

        return new PageImpl<RatingResource>(ratingDtoList, pageable, tourRatingPage.getTotalPages());
    }

    /**
     * Calculate the average Score of a Tour.
     *
     * @param tourId
     * @return Tuple of "average" and the average value.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/tours/{tourId}/ratings/average")
    public Map.Entry<String, Double> getAverage(@PathVariable(value = "tourId") int tourId) {

        List<TourRating> ratings = tourRatingRepository.findByPkTourId(tourId);
        OptionalDouble average = ratings.stream().mapToInt(TourRating::getScore).average();
        double result = average.isPresent() ? average.getAsDouble() : null;
        return new AbstractMap.SimpleEntry<>("average", result);
    }

    /**
     * Update score and comment of a Tour Rating
     *
     * @param tourId
     * @param RatingResource
     * @return The modified Rating DTO.
     */
    @RequestMapping(path = "/tours/{tourId}/ratings", method = RequestMethod.PUT)
    public RatingResource updateWithPut(@PathVariable(value = "tourId") int tourId,
                                        @RequestBody @Validated RatingResource RatingResource) {
        TourRating rating = verifyTourRating(tourId, RatingResource.getCustomerId());
        rating.setScore(RatingResource.getScore());
        rating.setComment(RatingResource.getComment());
        return ratingResourceAssembler.toResource(tourRatingRepository.save(rating));
    }

    /**
     * Update score or comment of a Tour Rating
     *
     * @param tourId
     * @param RatingResource
     * @return The modified Rating DTO.
     */
    @RequestMapping(path = "/tours/{tourId}/ratings", method = RequestMethod.PATCH)
    public RatingResource updateWithPatch(@PathVariable(value = "tourId") int tourId, @RequestBody @Validated RatingResource RatingResource) {
        TourRating rating = verifyTourRating(tourId, RatingResource.getCustomerId());
        if (RatingResource.getScore() != null) {
            rating.setScore(RatingResource.getScore());
        }
        if (RatingResource.getComment() != null) {
            rating.setComment(RatingResource.getComment());
        }
        return ratingResourceAssembler.toResource(tourRatingRepository.save(rating));
    }

    /**
     * Delete a Rating of a tour made by a customer
     *
     * @param tourId
     * @param customerId
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/tours/{tourId}/ratings/{customerId}")
    public void delete(@PathVariable(value = "tourId") int tourId, @PathVariable(value = "customerId") int customerId) {
        TourRating rating = verifyTourRating(tourId, customerId);
        tourRatingRepository.delete(rating);
    }

    /**
     * Verify and return the TourRating for a particular tourId and Customer
     *
     * @param tourId
     * @param customerId
     * @return the found TourRating
     * @throws NoSuchElementException if no TourRating found
     */
    private TourRating verifyTourRating(int tourId, int customerId) throws NoSuchElementException {
        TourRating rating = tourRatingRepository.findByPkTourIdAndPkCustomerId(tourId, customerId);
        if (rating == null) {
            throw new NoSuchElementException("Tour-Rating pair for request("
                    + tourId + " for customer" + customerId);
        }
        return rating;
    }

    /**
     * Verify and return the Tour given a tourId.
     *
     * @param tourId
     * @return the found Tour
     * @throws NoSuchElementException if no Tour found.
     */
    private Tour verifyTour(int tourId) throws NoSuchElementException {
        Tour tour = tourRepository.findOne(tourId);
        if (tour == null) {
            throw new NoSuchElementException("Tour does not exist " + tourId);
        }
        return tour;
    }
}
