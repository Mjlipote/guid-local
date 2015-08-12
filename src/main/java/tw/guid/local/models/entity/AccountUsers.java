package tw.guid.local.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.data.jpa.domain.AbstractPersistable;

import tw.guid.local.models.Role;

@Entity
public class AccountUsers extends AbstractPersistable<Long> {

  private static final long serialVersionUID = 1L;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String institute;

  @Column(nullable = false)
  private String prefix;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Role role;

  @Column(nullable = true)
  private String jobTitle;

  @Column(nullable = true)
  private String telephone;

  @Column(nullable = true)
  private String address;

  public AccountUsers() {

  }

  /**
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * @param username
   *          the username to set
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * @param password
   *          the password to set
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * @param email
   *          the email to set
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * @return the institute
   */
  public String getInstitute() {
    return institute;
  }

  /**
   * @param institute
   *          the institute to set
   */
  public void setInstitute(String institute) {
    this.institute = institute;
  }

  /**
   * @return the prefix
   */
  public String getPrefix() {
    return prefix;
  }

  /**
   * @param prefix
   *          the prefix to set
   */
  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  /**
   * @return the role
   */
  public Role getRole() {
    return role;
  }

  /**
   * @param role
   *          the role to set
   */
  public void setRole(Role role) {
    this.role = role;
  }

  /**
   * @return the jobTitle
   */
  public String getJobTitle() {
    return jobTitle;
  }

  /**
   * @param jobTitle
   *          the jobTitle to set
   */
  public void setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle;
  }

  /**
   * @return the telephone
   */
  public String getTelephone() {
    return telephone;
  }

  /**
   * @param telephone
   *          the telephone to set
   */
  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  /**
   * @return the address
   */
  public String getAddress() {
    return address;
  }

  /**
   * @param address
   *          the address to set
   */
  public void setAddress(String address) {
    this.address = address;
  }

  // /**
  // *
  // * @author apple
  // *
  // */
  // public static class Builder {
  //
  // private final String username;
  //
  // private final String password;
  //
  // private final String email;
  //
  // private final String institute;
  //
  // private final String prefix;
  //
  // private String jobTitle;
  //
  // private String telephone;
  //
  // private String address;
  //
  // /**
  // * Required parameters
  // *
  // * @param username
  // * @param password
  // * @param email
  // * @param institute
  // * @param prefix
  // */
  //
  // public Builder(String username, String password, String email, String
  // institute, String prefix) {
  // this.username = NullValidator.checkNotNull(username, "Username can't be
  // null.");
  //
  // this.password = NullValidator.checkNotNull(password, "Password can't be
  // null.");
  //
  // this.email = NullValidator.checkNotNull(email, "Email can't be null.");
  //
  // this.institute = NullValidator.checkNotNull(institute, "Institute can't be
  // null.");
  //
  // this.prefix = NullValidator.checkNotNull(prefix, "Prefix can't be null.");
  // }
  //
  // /**
  // * Optional parameter
  // *
  // * @param str
  // * @return
  // */
  //
  // public Builder jobTitle(String str) {
  // jobTitle = NullValidator.checkNotNull(str, "Job Title can't be null.");
  // return this;
  // }
  //
  // public Builder telephone(String str) {
  // telephone = NullValidator.checkNotNull(str, "Job Title can't be null.");
  // return this;
  // }
  //
  // public Builder address(String str) {
  // address = NullValidator.checkNotNull(str, "Job Title can't be null.");
  // return this;
  // }
  //
  // public AccountUsers build() {
  // return new AccountUsers(this);
  // }
  //
  // }
  //
  // private AccountUsers(Builder builder) {
  // this.username = builder.username;
  // this.password = builder.password;
  // this.email = builder.email;
  // this.institute = builder.institute;
  // this.prefix = builder.prefix;
  // this.jobTitle = builder.jobTitle;
  // this.telephone = builder.telephone;
  // this.address = builder.address;
  // }

}
