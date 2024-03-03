package be.brahms.repositories.impl;

import be.brahms.entities.Author;
import be.brahms.repositories.AuthorRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class AuthorRepositoryImpl implements AuthorRepository {

    //Connect to DataBase
    private static final SessionFactory sf = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    @Override
    public void create(Author author) {
        Session s = sf.openSession();
        Transaction tx = null;

        try {
            tx = s.beginTransaction();
            s.persist(author);
            tx.commit();
        } finally {
            s.close();
        }
    }

    @Override
    public Author update(long id, Author author) {
        Session s = sf.openSession();
        Transaction tx = null;

        try{
            Author updateAuthor = s.get(Author.class, id);

            if( author.getName() != null ) {
                updateAuthor.setName( author.getName() );
            } if ( author.getFirstname() != null ) {
                updateAuthor.setFirstname( author.getFirstname() );
            }

            tx = s.beginTransaction();
            s.merge(updateAuthor);
            tx.commit();
        } finally {
            s.close();
        }
        return null;
    }

    @Override
    public List<Author> getAll() {
        Session s = sf.openSession();

        try {
            String hql = "FROM Author";
            Query<Author> query = s.createQuery(hql, Author.class);
            return query.getResultList();
        } finally {
            s.close();
        }

    }

    @Override
    public List<Author> getByName() {
        return null;
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public boolean isAuthorExists(String name, String firstname) {
        Session s = sf.openSession();

        try {
            String hql = "SELECT COUNT(*)FROM Author a WHERE a.name =:name AND a.firstname =:firstname";
            Query<Long> query = s.createQuery(hql, Long.class);
            query.setParameter("name", name);
            query.setParameter("firstname", firstname);

            Long count = query.uniqueResult();
            return count !=null && count > 0;
        } finally {
            s.close();
        }

    }
}
