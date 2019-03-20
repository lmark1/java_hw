package hr.fer.zemris.java.tecaj_13.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This class models a blog comment.
 * 
 * @author Lovro Marković
 *
 */
@Entity
@Table(name="blog_comments")
public class BlogComment {

	/**
	 * Comment ID.
	 */
	private Long id;
	
	/**
	 * Blog entry the comment is posted on.
	 */
	private BlogEntry blogEntry;
	
	/**
	 * User e-mail. 
	 */
	private String usersEMail;
	
	/**
	 * Comment message.
	 */
	private String message;
	
	/**
	 * Date the comment is posted on.
	 */
	private Date postedOn;
	
	/**
	 * Default constructor.
	 */
	public BlogComment() {
	}
	
	/**
	 * @return Returns comment ID.
	 */
	@Id @GeneratedValue
	public Long getId() {
		return id;
	}
	
	/**
	 * Setter for the comment ID.
	 * 
	 * @param id New comment ID.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return Returns the blog entry the comment is posted on.
	 */
	@ManyToOne
	@JoinColumn(nullable=false)
	public BlogEntry getBlogEntry() {
		return blogEntry;
	}
	
	/**
	 * Setter for the coment blog entry.
	 * 
	 * @param blogEntry New blog entry.
	 */
	public void setBlogEntry(BlogEntry blogEntry) {
		this.blogEntry = blogEntry;
	}

	/**
	 * @return Returns user email.
	 */
	@Column(length=100,nullable=false)
	public String getUsersEMail() {
		return usersEMail;
	}

	/**
	 * Setter user email.
	 * 
	 * @param usersEMail New user email.
	 */
	public void setUsersEMail(String usersEMail) {
		this.usersEMail = usersEMail;
	}

	/**
	 * @return Returns comment message.
	 */
	@Column(length=4096,nullable=false)
	public String getMessage() {
		return message;
	}

	/**
	 * Sets a new comment message.
	 * 
	 * @param message New comment message.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return Returns the date the comment is posted on.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	public Date getPostedOn() {
		return postedOn;
	}

	/**
	 * Sets a new date.
	 * 
	 * @param postedOn New date the comment is posted on.
	 */
	public void setPostedOn(Date postedOn) {
		this.postedOn = postedOn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		BlogComment other = (BlogComment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}