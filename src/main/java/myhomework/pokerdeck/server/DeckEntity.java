package myhomework.pokerdeck.server;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;

import myhomework.pokerdeck.model.DeckSnapshot;


/**
 * Basic entity used to represent the Deck resource. It is used for both storage (JPA) and JSON output (Jackson library).
 * Instead of storing the full content of the PokerDeck, this class just keeps a {@link DeckSnapshot snapshot} of the deck.
 * This will allows restoring the PokerDeck in the same state on the next call.
 * 
 * @author Bruno Martinez
 */
@Entity
@Table(name = "Deck")
@XmlRootElement
public class DeckEntity {
	
	/**
	 * Builds a default entity with a default {@link DeckSnapshot deck snapshot} (corresponding to a new created deck, not shuffled). 
	 */
	public DeckEntity() {
		snaphot = new DeckSnapshot();
	}
	
	
	/**
	 * For storage and output. Unique ID of the reource.
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Column(name = "id")
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * For storage only, the last snapshot (state) of the deck.
	 */
	@Embedded
	private DeckSnapshot snaphot;
	@JsonIgnore
	public DeckSnapshot getSnapshot() {
		return snaphot;
	}
	public void setSnapshot(DeckSnapshot snaphot) {
		if(snaphot == null) {
			snaphot = new DeckSnapshot();
		}
		this.snaphot = snaphot;
	}
	
	
	/**
	 * For JSON output only, the link to this Deck resources
	 */
	@Transient
	private String link;
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
	/**
	 * For JSON output only, the link to this deckdeal resource (to deal card)
	 */
	@Transient
	private String linkDeal;
	public String getLinkDeal() {
		return linkDeal;
	}
	public void setLinkDeal(String linkDeal) {
		this.linkDeal = linkDeal;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((snaphot == null) ? 0 : snaphot.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeckEntity other = (DeckEntity) obj;
		if (id != other.id)
			return false;
		if (snaphot == null) {
			if (other.snaphot != null)
				return false;
		} else if (!snaphot.equals(other.snaphot))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DeckEntity [id=" + id + ", snaphot=" + snaphot + ", link=" + link + "]";
	}
	

}
