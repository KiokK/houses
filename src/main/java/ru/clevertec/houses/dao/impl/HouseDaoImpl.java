package ru.clevertec.houses.dao.impl;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.houses.dao.HouseDao;
import ru.clevertec.houses.dao.model.PaginationInfo;
import ru.clevertec.houses.model.House;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HouseDaoImpl implements HouseDao {

    private final SessionFactory sessionFactory;

    @Override
    @Transactional
    public House create(House house) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(house);

        return house;
    }

    @Override
    public Optional<House> findHouseByUuid(UUID uuid) {
        Session session = sessionFactory.getCurrentSession();
        House house = session.createQuery("SELECT h FROM House h WHERE h.uuid = ?1", House.class)
                .setParameter(1, uuid).getSingleResultOrNull();
        return Optional.ofNullable(house);
    }

    @Override
    public List<House> findAll(PaginationInfo paginationInfo) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT h FROM House h", House.class)
                .setFirstResult(paginationInfo.getOffset())
                .setMaxResults(paginationInfo.getPageSize()).getResultList();
    }

    @Override
    public Optional<House> findWithResidentsByUuid(UUID uuid) {
        Session session = sessionFactory.getCurrentSession();
        House house = session.createQuery("SELECT h FROM House h WHERE h.uuid = ?1", House.class)
                .setParameter(1, uuid).getSingleResultOrNull();

        if (house == null) {
            return Optional.empty();
        }

        Hibernate.initialize(house.getResidents());
        return Optional.of(house);
    }

    @Override
    @Transactional
    public Optional<House> update(House house) {
        Session session = sessionFactory.getCurrentSession();

        House currentHouse = session.createQuery("SELECT h FROM House h WHERE h.uuid = ?1", House.class)
                .setParameter(1, house.getUuid())
                .getSingleResultOrNull();
        if (currentHouse == null) {
            return Optional.empty();
        }

        if (!house.equals(currentHouse)) {
            house.setCreateDate(currentHouse.getCreateDate());
            house.setId(currentHouse.getId());
            return Optional.ofNullable(session.merge(house));
        }

        return Optional.of(currentHouse);
    }

    /**
     * Не удаляет house при наличии residents
     * @param uuid house
     * @return true, если объект был удален - иначе false
     */
    @Override
    @Transactional
    public boolean deleteByUuid(UUID uuid) {
        Session session = sessionFactory.getCurrentSession();
        House house = session.createQuery("SELECT h FROM House h WHERE h.uuid = ?1", House.class)
                .setParameter(1, uuid).getSingleResultOrNull();
        if (house == null || !house.getResidents().isEmpty()) {
            return false;
        }
        session.remove(house);

        return true;
    }

}
