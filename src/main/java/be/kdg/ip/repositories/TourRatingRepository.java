package be.kdg.ip.repositories;

import be.kdg.ip.domain.TourRating;
import be.kdg.ip.domain.TourRatingPk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Tour Rating Repository Interface
 * <p>
 * Created by Mary Ellen Bowman
 */
@RepositoryRestResource(path = "tours", collectionResourceRel = "tours", exported = true) // bug? exported = true
public interface TourRatingRepository extends CrudRepository<TourRating, TourRatingPk> {


    /**
     * Lookup all the TourRatings for a tour.
     *
     * @param tourId is the tour Identifier
     * @return a List of any found TourRatings
     */
    @RestResource(exported = false)
    List<TourRating> findByPkTourId(Integer tourId);

    /**
     * Lookup a page of TourRatings for a tour.
     *
     * @param tourId   tourId is the tour Identifier
     * @param pageable details for the desired page
     * @return a Page of any found TourRatings
     */
    @RestResource(exported = false)
    Page<TourRating> findByPkTourId(Integer tourId, Pageable pageable);

    /**
     * Lookup a TourRating by the TourId and Customer Id
     *
     * @param tourId
     * @param customerId
     * @return TourRating if found, null otherwise.
     */
    @RestResource(exported = false)
    TourRating findByPkTourIdAndPkCustomerId(Integer tourId, Integer customerId);
}