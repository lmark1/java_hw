package hr.fer.zemris.java.tecaj_13.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * This class represents a blog user.
 * 
 * @author Lovro Marković
 *
 */
@Entity
@Table(name="blog_users")
public class BlogUser {
	
	/**
	 * Id of the blog user.
	 */
	private Long id;
	
	/**
	 * User first name.
	 */
	private String firstName;
	
	/**
	 * User last name.
	 */
	private String lastName;
	
	/**
	 * User nickname.
	 */
	private String nick;
	
	/**
	 * User email.
	 */
	private String email;
	
	/**
	 * User password hash.
	 */
	private String passwordHash;
	
	/**
	 * List of blog entries of the user.
	 */
	private List<BlogEntry> blogEntries = new ArrayList<BlogEntry>();
	
	/**
	 * Default constructor.
	 */
	public BlogUser() {
	}
	
	/**
	 * @return Returns Blog entry comments.
	 */
	@OneToMany(mappedBy = "creator", 
			fetch = FetchType.LAZY, 
			cascade = CascadeType.PERSIST, 
			orphanRemoval = true)
	@OrderBy("lastModifiedAt DESC")
	public List<BlogEntry> getBlogEntries() {
		return blogEntries;
	}
	
	/**
	 * Set new blog entries.
	 * 
	 * @param blogEntries New blog entries.
	 */
	public void setBlogEntries(List<BlogEntry> blogEntries) {
		this.blogEntries = blogEntries;
	}
	
	/**
	 * @return Return user email.
	 */
	@Column(length=100,nullable=false)
	public String getEmail() {
		return email;
	}
	
	/**
	 * Sets new user email.
	 * 
	 * @param email New user email.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * @return Returns first name.
	 */
	@Column(length=100,nullable=false)
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Sets new first name.
	 * 
	 * @param firstName New first name.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * @return Returns user ID.
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	
	/**
	 * Sets new user ID.
	 * 
	 * @param id New user ID.
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @return Returns user last name.
	 */
	@Column(length=100,nullable=false)
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Sets new last name.
	 * 
	 * @param lastName New last name.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * @return Returns user nick.
	 */
	@JoinColumn(unique=true)
	@Column(length=100,nullable=false)
	public String getNick() {
		return nick;
	}
	
	/**
	 * Sets new user nick.
	 * 
	 * @param nick New user nick.
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}
	
	/**
	 * @return Returns password hash.
	 */
	@Column(length=100,nullable=false)
	public String getPasswordHash() {
		return passwordHash;
	}
	
	/**
	 * Set new password hash.
	 * 
	 * @param passwordHash New password hash.
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
}
