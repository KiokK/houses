package ru.clevertec.houses.dao.impl;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.houses.dao.PersonDao;
import ru.clevertec.houses.dao.model.PaginationInfo;
import ru.clevertec.houses.model.House;
import ru.clevertec.houses.model.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PersonDaoImpl implements PersonDao {

    private final SessionFactory sessionFactory;

    @Override
    @Transactional
    public Person create(Person person) {
        Session session = sessionFactory.getCurrentSession();
        House house = session.createQuery("SELECT h FROM House h WHERE h.uuid= ?1", House.class)
                .setParameter(1, person.getResidentOf().getUuid())
                .getSingleResultOrNull();
        if (house == null) {
            return null;
        }
        person.setResidentOf(house);
        session.persist(person);

        return person;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Person> findPersonByUuid(UUID uuid) {
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery("SELECT p FROM Person p WHERE p.uuid= ?1", Person.class)
                .setParameter(1, uuid)
                .getResultStream().findAny();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> findAll(PaginationInfo paginationInfo) {
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery("SELECT p FROM Person p", Person.class)
                .setFirstResult(paginationInfo.getOffset())
                .setMaxResults(paginationInfo.getPageSize()).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Person> findWithHousesByPersonUuid(UUID uuid) {
        Session session = sessionFactory.getCurrentSession();
        Query<Person> query = session.createQuery("SELECT p FROM Person p WHERE p.uuid= ?1", Person.class);
        query.setParameter(1, uuid);
        Optional<Person> person = query.getResultStream().findAny();
        person.ifPresent(p -> Hibernate.initialize(p.getHouses()));

        return person;
    }

    @Override
    @Transactional
    public boolean update(Person person) {
        Session session = sessionFactory.getCurrentSession();

        Query<Person> query = session.createQuery("SELECT p FROM Person p WHERE p.uuid= ?1", Person.class);
        query.setParameter(1, person.getUuid());
        Person currentPerson = query.getResultStream().findAny().orElse(null);

        if (currentPerson == null) {
            return false;
        }

        UUID curHouseUuid = currentPerson.getResidentOf().getUuid();
        UUID newHouseUuid = person.getResidentOf().getUuid();
        if (!curHouseUuid.equals(newHouseUuid)) {
            House newHouse = session.createQuery("SELECT h FROM House h WHERE h.uuid = ?1", House.class)
                    .setParameter(1, newHouseUuid).getSingleResultOrNull();

            if (newHouse != null) {
                person.setResidentOf(newHouse);
            } else {
                person.setResidentOf(currentPerson.getResidentOf());
            }
        } else {
            person.setResidentOf(currentPerson.getResidentOf());
        }

        if (!person.equals(currentPerson)) {
            person.setId(currentPerson.getId());
            person.setCreateDate(currentPerson.getCreateDate());
            person.setHouses(currentPerson.getHouses());
            session.merge(person);

            return true;
        }

        return false;
    }

    @Override
    @Transactional
    public boolean updateWithOwnHouses(Person person) {
        Session session = sessionFactory.getCurrentSession();

        Person currentPerson = session.createQuery("SELECT p FROM Person p WHERE p.uuid= ?1", Person.class)
                .setParameter(1, person.getUuid())
                .getResultStream()
                .findAny().orElse(null);

        if (currentPerson == null) {
            return false;
        }

        List<House> newPersonHouses = new ArrayList<>();
        if (!person.getHouses().isEmpty()) {
            String uuids = person.getHouses().stream()
                    .map(house -> "'" + house.getUuid() + "'")
                    .toList().toString();
            newPersonHouses = session.createNativeQuery(
                            String.format("SELECT * FROM House h WHERE h.uuid IN (%s)", uuids.substring(1, uuids.length() - 1)),
                            House.class)
                    .getResultList();
        }

        boolean isElementsEquals = currentPerson.getHouses().containsAll(newPersonHouses);
        if (!isElementsEquals || currentPerson.getHouses().size() != newPersonHouses.size()) {
            currentPerson.setUpdateDate(person.getUpdateDate());
            currentPerson.setHouses(newPersonHouses);
            session.merge(currentPerson);

            return true;
        }

        return false;
    }


    @Override
    @Transactional
    public boolean deleteByUuid(UUID uuid) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("DELETE FROM Person p WHERE p.uuid= ?1")
                .setParameter(1, uuid)
                .executeUpdate() == 1;
    }

}
