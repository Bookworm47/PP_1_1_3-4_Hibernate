package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            session.createNativeQuery("create table if not exists User" +
                            "(id bigint auto_increment primary key, " +
                            "name varchar(64) not null, " +
                            "lastname varchar(64) not null, " +
                            "age tinyint not null);")
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null &&
                    (transaction.isActive() || transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK)) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void dropUsersTable() {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            session.createSQLQuery("drop table if exists User")
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null &&
                    (transaction.isActive() || transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK)) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User(name, lastName, age);
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().getCurrentSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null &&
                    (transaction.isActive() || transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK)) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null &&
                    (transaction.isActive() || transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK)) {
                transaction.rollback();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = null;
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            userList = session.createQuery("from User").getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null &&
                    (transaction.isActive() || transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK)) {
                transaction.rollback();
            }
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.getTransaction();
            transaction.begin();
            session.createQuery("delete User").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null &&
                    (transaction.isActive() || transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK)) {
                transaction.rollback();
            }
        }
    }
}