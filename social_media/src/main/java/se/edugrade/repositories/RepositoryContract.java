package se.edugrade.repositories;
import java.util.List;
import java.util.Optional;

//Kontrakt för samtliga repositories som ska använda sig av queries
public interface RepositoryContract<T> {
    T create(T entity);
    Optional findById(Long id);
    List<T> findAll();
    T update(T entity);
    void delete(Long id);
}
