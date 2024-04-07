package com.spring.iot.repositories;

import com.spring.iot.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryCustom {

    @Autowired
    EntityManager em;
    public List<User> getListUser(Map<String, String> params){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> q = cb.createQuery(User.class);
        Root<User> userRoot = q.from(User.class);
        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();
            String id = params.get("id");
            if (id != null && !id.isEmpty()) {
                predicates.add(cb.equal(userRoot.get("id"), id));
            }
            String name = params.get("name");
            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(userRoot.get("name"), String.format("%%%s%%", name)));
            }
            String email = params.get("email");
            if (email != null && !email.isEmpty()) {
                predicates.add(cb.like(userRoot.get("email"), String.format("%%%s%%", email)));
            }
            String role = params.get("role");
            if (role != null && !role.isEmpty()) {
                predicates.add(cb.equal(userRoot.get("role"), role));
            }
            q.where(predicates.toArray(Predicate[]::new));
        }
        Query query = em.createQuery(q);
        return query.getResultList();
    }
}
