package be.kdg.ip.repositories;

import be.kdg.ip.domain.Tour;
import be.kdg.ip.domain.TourPackage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by wouter on 03.02.17.
 */
@RepositoryRestResource(path = "packages", collectionResourceRel = "packages")
public interface TourPackageRepository extends CrudRepository<TourPackage,String>{

    TourPackage findByName(String name);
}
