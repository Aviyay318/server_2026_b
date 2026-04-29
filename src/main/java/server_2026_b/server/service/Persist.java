package server_2026_b.server.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import server_2026_b.server.entities.Employee;
import server_2026_b.server.entities.Employer;

import java.util.List;

@Transactional
@Component
@SuppressWarnings("unchecked")
public class Persist {

    private final SessionFactory sessionFactory;

    public Persist(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public <T> void saveAll(List<T> objects) {
        for (T object : objects) {
            sessionFactory.getCurrentSession().saveOrUpdate(object);
        }
    }

    public void remove(Object o) {
        sessionFactory.getCurrentSession().remove(o);
    }

    public Session getQuerySession() {
        return sessionFactory.getCurrentSession();
    }

    public void save(Object object) {
        sessionFactory.getCurrentSession().saveOrUpdate(object);
    }

    public <T> T loadObject(Class<T> clazz, long oid) {
        return getQuerySession().get(clazz, oid);
    }

    public <T> List<T> loadList(Class<T> clazz) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM " + clazz.getSimpleName()).list();
    }

    public Employee getEmployeeByUsernameAndPassword(String username, String password) {
        return getQuerySession()
                .createQuery(
                        "FROM Employee WHERE username = :username AND password = :password",
                        Employee.class
                )
                .setParameter("username", username)
                .setParameter("password", password)
                .uniqueResult();
    }

    public Employer getEmployerByUsernameAndPassword(String username, String password) {
        return getQuerySession()
                .createQuery(
                        "FROM Employer WHERE username = :username AND password = :password",
                        Employer.class
                )
                .setParameter("username", username)
                .setParameter("password", password)
                .uniqueResult();
    }

}
