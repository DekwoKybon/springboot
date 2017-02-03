package be.kdg.ip.repositories;

import be.kdg.ip.domain.Tour;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by wouter on 03.02.17.
 */
@RepositoryRestResource(exported = false)
public interface TourRepository extends CrudRepository<Tour, Integer> {


}
