package com.readify.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "customers")
public class Customer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "name", nullable = false)
  @NotBlank(message = "{billing.name.notBlank}")
  @Size(max = 50, message = "{billing.name.maxSize}")
  private @NotBlank(message = "{billing.name.notBlank}") @Size(
      max = 50,
      message = "{billing.name.maxSize}") String name;

  @Column(name = "surname", nullable = false)
  @NotBlank(message = "{billing.surname.notBlank}")
  @Size(max = 50, message = "{billing.surname.maxSize}")
  private @NotBlank(message = "{billing.surname.notBlank}") @Size(
      max = 50,
      message = "{billing.surname.maxSize}") String surname;

  @Column(name = "country_region", nullable = false)
  @NotBlank(message = "{billing.countryRegion.notBlank}")
  @Size(max = 55, message = "{billing.countryRegion.maxSize}")
  private @NotBlank(message = "{billing.countryRegion.notBlank}") @Size(
      max = 55,
      message = "{billing.countryRegion.maxSize}") String countryRegion;

  @Column(name = "street_and_house_number", nullable = false)
  @NotBlank(message = "{billing.streetAndHouseNumber.notBlank}")
  @Size(max = 100, message = "{billing.streetAndHouseNumber.maxSize}")
  private @NotBlank(message = "{billing.streetAndHouseNumber.notBlank}") @Size(
      max = 100,
      message = "{billing.streetAndHouseNumber.maxSize}") String streetAndHouseNumber;

  @Column(name = "city", nullable = false)
  @NotBlank(message = "{billing.city.notBlank}")
  @Size(max = 60, message = "{billing.city.maxSize}")
  private @NotBlank(message = "{billing.city.notBlank}") @Size(
      max = 60,
      message = "{billing.city.maxSize}") String city;

  @Column(name = "postal_code", nullable = false)
  @NotBlank(message = "{billing.postalCode.notBlank}")
  @Size(max = 18, message = "{billing.postalCode.maxSize}")
  private @NotBlank(message = "{billing.postalCode.notBlank}") @Size(
      max = 18,
      message = "{billing.postalCode.maxSize}") String postalCode;

  @Column(name = "phone_number", nullable = false)
  @NotBlank(message = "{billing.phoneNumber.notBlank}")
  @Size(max = 15, message = "{billing.phoneNumber.maxSize}")
  private @NotBlank(message = "{billing.phoneNumber.notBlank}") @Size(
      max = 15,
      message = "{billing.phoneNumber.maxSize}") String phoneNumber;

  @Column(name = "email", nullable = false)
  @NotBlank(message = "{billing.email.notBlank}")
  @Size(max = 254, message = "{billing.email.maxSize}")
  @Email
  private @NotBlank(message = "{billing.email.notBlank}") @Size(
      max = 254,
      message = "{billing.email.maxSize}") @Email String email;

  public Customer() {}

  public Customer(
      Long id,
      String name,
      String surname,
      String countryRegion,
      String streetAndHouseNumber,
      String city,
      String postalCode,
      String phoneNumber,
      String email) {
    this.id = id;
    this.name = name;
    this.surname = surname;
    this.countryRegion = countryRegion;
    this.streetAndHouseNumber = streetAndHouseNumber;
    this.city = city;
    this.postalCode = postalCode;
    this.phoneNumber = phoneNumber;
    this.email = email;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSurname() {
    return this.surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public String getCountryRegion() {
    return this.countryRegion;
  }

  public void setCountryRegion(String countryRegion) {
    this.countryRegion = countryRegion;
  }

  public String getStreetAndHouseNumber() {
    return this.streetAndHouseNumber;
  }

  public void setStreetAndHouseNumber(String streetAndHouseNumber) {
    this.streetAndHouseNumber = streetAndHouseNumber;
  }

  public String getCity() {
    return this.city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getPostalCode() {
    return this.postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getPhoneNumber() {
    return this.phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String toString() {
    return "Customer [id="
        + this.id
        + ", name="
        + this.name
        + ", surname="
        + this.surname
        + ", countryRegion="
        + this.countryRegion
        + ", streetAndHouseNumber="
        + this.streetAndHouseNumber
        + ", city="
        + this.city
        + ", postalCode="
        + this.postalCode
        + ", phoneNumber="
        + this.phoneNumber
        + ", email="
        + this.email
        + "]";
  }
}
