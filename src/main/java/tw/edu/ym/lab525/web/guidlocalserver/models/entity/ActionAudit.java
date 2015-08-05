package tw.edu.ym.lab525.web.guidlocalserver.models.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class ActionAudit extends AbstractPersistable<Long> {

  private static final long serialVersionUID = 1L;

  @Column(nullable = false)
  private Date actionTimeStamp;

  @Column(nullable = false)
  private String action;

  @Column(nullable = false)
  // @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  // @JoinColumn(name = "USER_ID")
  private AccountUsers user;

  /**
   * @return the actionTimeStamp
   */
  public Date getActionTimeStamp() {
    return actionTimeStamp;
  }

  /**
   * @param actionTimeStamp
   *          the actionTimeStamp to set
   */
  public void setActionTimeStamp(Date actionTimeStamp) {
    this.actionTimeStamp = actionTimeStamp;
  }

  /**
   * @return the action
   */
  public String getAction() {
    return action;
  }

  /**
   * @param action
   *          the action to set
   */
  public void setAction(String action) {
    this.action = action;
  }

  /**
   * @return the user
   */
  public AccountUsers getUser() {
    return user;
  }

  /**
   * @param user
   *          the user to set
   */
  public void setUser(AccountUsers user) {
    this.user = user;
  }

}
