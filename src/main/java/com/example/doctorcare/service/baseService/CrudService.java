package com.example.doctorcare.service.baseService;

import java.util.List;

public interface CrudService<T> {

    /**
     * @param T the type of entity
     * @return void
     */
    void create(T t);

    /**
     * @param Integer the primary key of entity
     * @return T
     */
    T findById(Integer id);

    /**
     * @param T the type of entity
     * @return T
     */

    /**
     * @param T the type of entity
     * @return T
     */
    T update(T t);

    /**
     * @param T the type of entity
     * @return void
     */
    void delete(T t);

    /**
     * @param T the type of entity
     * @return List<T>
     */

    List<T> findAll();
}
