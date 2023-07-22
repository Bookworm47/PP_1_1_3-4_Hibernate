package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    Session session = Util.getSessionFactory().getCurrentSession();
    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        String hqlNewTable = "create table if not exists nameTable " +
                "(id bigint auto_increment primary key, " +
                "name varchar(64) not null, " +
                "lastname varchar(64) not null, " +
                "age tinyint not null);";
        try {
            session.beginTransaction();
            session.createSQLQuery(hqlNewTable).addEntity(User.class);
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    @Override
    public void dropUsersTable() {
        try {
            session.beginTransaction();
            session.createSQLQuery("drop table if exists nameTable").executeUpdate();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try {
            session.beginTransaction();
            User user = new User(name, lastName, age);
            session.save(user);
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    @Override
    public void removeUserById(long id) {
        try {
            session.beginTransaction();
            User user = session.get(User.class, id);
            session.delete(user);
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    @Override
    public List<User> getAllUsers() {
//        List<User> userList = new ArrayList<>();
        List<User> userList;
        try {
            session.beginTransaction();
            userList = session.createQuery("from User")
                    .getResultList();
            session.getTransaction().commit();
        } finally {
            session.close();
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        try {
            session.beginTransaction();
            session.createSQLQuery("truncate table nameTable").executeUpdate();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }
}
