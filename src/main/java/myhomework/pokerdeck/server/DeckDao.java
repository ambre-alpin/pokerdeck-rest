package myhomework.pokerdeck.server;


import org.hibernate.Session;

/**
 * Interface for Persistence storage of Deck. Singleton class 
 * @author Bruno Martinez
 */
public class DeckDao {

	private static volatile DeckDao instance = null;

	private DeckDao() {
	}

	public static DeckDao getInstance() {
		if (instance == null) {
			instance = new DeckDao();
		}
		return instance;
	}

	public DeckEntity getDeck(int id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		DeckEntity deck = (DeckEntity) session.get(DeckEntity.class, id);
		session.close();
		return deck;
	}

	public void saveDeck(DeckEntity deck) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.saveOrUpdate(deck);
		session.flush();
		session.close();
	}

	public void deleteDeck(int id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		DeckEntity deck = getDeck(id);
		if (deck != null) {
			session.delete(deck);
			session.flush();
		}
		session.close();
	}
	

}
