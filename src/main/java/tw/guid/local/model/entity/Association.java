/**
 *
 * @author Ming-Jheng Li
 *
 *
 * Copyright 2015 Ming-Jheng Li
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package tw.guid.local.model.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.data.jpa.domain.AbstractPersistable;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ComparisonChain;

import tw.guid.local.model.Gender;

@Entity
public class Association extends AbstractPersistable<Long>
    implements Comparable<Association> {

  private static final long serialVersionUID = 1L;

  @Column(unique = true, nullable = false)
  private String spguid;

  @Column(unique = true, nullable = false)
  private String subjectId;

  @Column(nullable = false)
  private String mrn;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Integer birthOfYear;

  @Column(nullable = false)
  private Integer birthOfMonth;

  @Column(nullable = false)
  private Integer birthOfDay;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Gender gender;

  @Column(nullable = false)
  private String sid;

  @Column(nullable = true)
  private String hospital;

  @Column(nullable = true)
  private String doctor;

  @Column(nullable = true)
  private String telephone;

  @Column(nullable = true)
  private String address;

  public Association() {}

  /**
   * @return the spguid
   */
  public String getSpguid() {
    return spguid;
  }

  /**
   * @param spguid
   *          the spguid to set
   */
  public void setSpguid(String spguid) {
    this.spguid = spguid;
  }

  /**
   * @return the subjectId
   */
  public String getSubjectId() {
    return subjectId;
  }

  /**
   * @param subjectId
   *          the subjectId to set
   */
  public void setSubjectId(String subjectId) {
    this.subjectId = subjectId;
  }

  /**
   * @return the mrn
   */
  public String getMrn() {
    return mrn;
  }

  /**
   * @param mrn
   *          the mrn to set
   */
  public void setMrn(String mrn) {
    this.mrn = mrn;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name
   *          the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the birthOfYear
   */
  public Integer getBirthOfYear() {
    return birthOfYear;
  }

  /**
   * @param birthOfYear
   *          the birthOfYear to set
   */
  public void setBirthOfYear(Integer birthOfYear) {
    this.birthOfYear = birthOfYear;
  }

  /**
   * @return the birthOfMonth
   */
  public Integer getBirthOfMonth() {
    return birthOfMonth;
  }

  /**
   * @param birthOfMonth
   *          the birthOfMonth to set
   */
  public void setBirthOfMonth(Integer birthOfMonth) {
    this.birthOfMonth = birthOfMonth;
  }

  /**
   * @return the birthOfDay
   */
  public Integer getBirthOfDay() {
    return birthOfDay;
  }

  /**
   * @param birthOfDay
   *          the birthOfDay to set
   */
  public void setBirthOfDay(Integer birthOfDay) {
    this.birthOfDay = birthOfDay;
  }

  /**
   * @return the gender
   */
  public Gender getGender() {
    return gender;
  }

  /**
   * @param gender
   *          the gender to set
   */
  public void setGender(Gender gender) {
    this.gender = gender;
  }

  /**
   * @return the sid
   */
  public String getSid() {
    return sid;
  }

  /**
   * @param sid
   *          the sid to set
   */
  public void setSid(String sid) {
    this.sid = sid;
  }

  /**
   * @return the hospital
   */
  public String getHospital() {
    return hospital;
  }

  /**
   * @param hospital
   *          the hospital to set
   */
  public void setHospital(String hospital) {
    this.hospital = hospital;
  }

  /**
   * @return the doctor
   */
  public String getDoctor() {
    return doctor;
  }

  /**
   * @param doctor
   *          the doctor to set
   */
  public void setDoctor(String doctor) {
    this.doctor = doctor;
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

  @Override
  public int compareTo(final Association other) {
    return ComparisonChain.start().compare(spguid, other.spguid)
        .compare(subjectId, other.subjectId).compare(mrn, other.mrn)
        .compare(name, other.name).compare(birthOfYear, other.birthOfYear)
        .compare(birthOfMonth, other.birthOfMonth)
        .compare(birthOfDay, other.birthOfDay).compare(gender, other.gender)
        .compare(sid, other.sid).compare(hospital, other.hospital)
        .compare(doctor, other.doctor).compare(telephone, other.telephone)
        .compare(address, other.address).result();
  }

  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof Association)) {
      return false;
    }
    Association castOther = (Association) other;
    return Objects.equals(spguid, castOther.spguid)
        && Objects.equals(subjectId, castOther.subjectId)
        && Objects.equals(mrn, castOther.mrn)
        && Objects.equals(name, castOther.name)
        && Objects.equals(birthOfYear, castOther.birthOfYear)
        && Objects.equals(birthOfMonth, castOther.birthOfMonth)
        && Objects.equals(birthOfDay, castOther.birthOfDay)
        && Objects.equals(gender, castOther.gender)
        && Objects.equals(sid, castOther.sid)
        && Objects.equals(hospital, castOther.hospital)
        && Objects.equals(doctor, castOther.doctor)
        && Objects.equals(telephone, castOther.telephone)
        && Objects.equals(address, castOther.address);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(spguid);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("spguid", spguid)
        .add("subjectId", subjectId).add("mrn", mrn).add("name", name)
        .add("birthOfYear", birthOfYear).add("birthOfMonth", birthOfMonth)
        .add("birthOfDay", birthOfDay).add("gender", gender).add("sid", sid)
        .add("hospital", hospital).add("doctor", doctor)
        .add("telephone", telephone).add("address", address).toString();
  }

}
