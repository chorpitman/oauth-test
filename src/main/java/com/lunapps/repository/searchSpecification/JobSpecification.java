package com.lunapps.repository.searchSpecification;

import com.lunapps.models.JobAdvert;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static com.lunapps.repository.searchSpecification.SqlOperations.*;

public class JobSpecification implements Specification<JobAdvert> {
    private SearchCriteria criteria;

    public JobSpecification(SearchCriteria searchCriteria) {
        super();
        this.criteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<JobAdvert> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if (criteria.getOperation().equalsIgnoreCase(EQUAL.getSqlOperation())) {
            return criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue());
        }

        if (criteria.getOperation().equalsIgnoreCase(GREATER_THAN.getSqlOperation())) {
            return criteriaBuilder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
        }

        if (criteria.getOperation().equalsIgnoreCase(GREATER_THAN_OR_EQUAL_TO.getSqlOperation())) {
            return criteriaBuilder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
        }

        if (criteria.getOperation().equalsIgnoreCase(LESS_THAN.getSqlOperation())) {
            return criteriaBuilder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
        }

        if (criteria.getOperation().equalsIgnoreCase(LESS_THAN_OR_EQUAL_TO.getSqlOperation())) {
            return criteriaBuilder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
        }

        if (criteria.getOperation().equalsIgnoreCase(BETWEEN.getSqlOperation())) {
            return criteriaBuilder.between(root.get(criteria.getKey()), criteria.getMin(), criteria.getMax());
        }

        if (criteria.getOperation().equalsIgnoreCase(LIKE.getSqlOperation())) {
            return criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue().toString() + "%");
        }

        return null;
    }
}
