package hr.fer.zemris.java.tecaj_13.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This class models a class which represents a blog entry.
 * 
 * @author Lovro Marković
 *
 */
@NamedQueries({
	@NamedQuery(name="BlogEntry.upit1",query="select b from BlogComment as b where b.blogEntry=:be and b.postedOn>:when")
})
@Cacheable(true)
@Entity
@Table(name = "blog_entries")
public class BlogEntry {

	/**
	 * Blog id.
	 */
	private Long id;
	
	/**
	 * List of all blog comments.
	 */
	private List<BlogComment> comments = new ArrayList<>();
	
	/**
	 * Date the blog is cretead at.
	 */
	private Date createdAt;
	
	/**
	 * Date the blog entry is last modified.
	 */
	private Date lastModifiedAt;
	
	/**
	 * Title of the blog entry.
	 */
	private String title;
	
	/**
	 * Text of the blog entry.
	 */
	private String text;

	/**
	 * Creator of the blog entry.
	 */
	private BlogUser creator;
	
	/**
	 * Default constructor.
	 */
	public BlogEntry() {
	}
	
	/**
	 * @return Returns creator of the blog entry.
	 */
	@ManyToOne
	@JoinColumn(nullable=false)
	public BlogUser getCreator() {
		return creator;
	}
	
	/**
	 * Sets new creator of the blog entry.
	 * 
	 * @param creator New entry creator.
	 */
	public void setCreator(BlogUser creator) {
		this.creator = creator;
	}
	
	/**
	 * @return Returns ID.
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	/**
	 * Setter for the user ID.
	 * 
	 * @param id New user ID.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return Returns Blog entry comments.
	 */
	@OneToMany(mappedBy = "blogEntry", 
			fetch = FetchType.LAZY, 
			cascade = CascadeType.PERSIST, 
			orphanRemoval = true)
	@OrderBy("postedOn DESC")
	public List<BlogComment> getComments() {
		return comments;
	}

	/**
	 * Sets new comments.
	 * 
	 * @param comments New comments.
	 */
	public void setComments(List<BlogComment> comments) {
		this.comments = comments;
	}

	/**
	 * @return Returns date when the blog is created.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * Sets new date the blog is created at.
	 * 
	 * @param createdAt New date the blog is created at.
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return Returns the date the blog entry is modified at.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true)
	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	/**
	 * Set new laste modified date.
	 * 
	 * @param lastModifiedAt New last modified date.
	 */
	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	/**
	 * @return Returns the blog entry title.
	 */
	@Column(length = 200, nullable = false)
	public String getTitle() {
		return title;
	}

	/**
	 * Sets a new title.
	 * 
	 * @param title New title.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return Returns blog text.
	 */
	@Column(length = 4096, nullable = false)
	public String getText() {
		return text;
	}

	/**
	 * Sets new blog text.
	 * 
	 * @param text New blog text.
	 */
	public void setText(String text) {
		this.text = text;
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
		BlogEntry other = (BlogEntry) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}