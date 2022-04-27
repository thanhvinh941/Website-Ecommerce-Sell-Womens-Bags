package com.nhom2.common.entity;

import java.beans.Transient;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "customers")
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 128, nullable = false, unique = true)
	private String email;

	@Column(length = 64, nullable = false)
	private String password;

	@Column(name = "fisrt_name", length = 45, nullable = false)
	private String firstname;

	@Column(name = "last_name", length = 45, nullable = false)
	private String lastname;

	@Column(name = "phone_number", length = 15, nullable = false)
	private String phoneNumber;

	@Column(length = 64)
	private String photos;

	private boolean enabled;
	
	@Column(name = "gioi_tinh")
	private String gioiTinh;
	
	@Column(name = "ngay_sinh")
	private Date ngaySinh;
	
	@Column(name = "verification_code", length = 64)
	private String verificationCode;

	@Column(name = "created_time")
	private Date createdTime;

	@OneToMany(mappedBy = "customer")
	private Set<Address> address;
	
	@Column(name="reset_password")
	private String resetPasswordToken;
	
	public Customer() {

	}

	public Customer(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public Customer(String email, String password, String firstname, String lastname, String phoneNumber) {
		this.email = email;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.phoneNumber = phoneNumber;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getPhotos() {
		return photos;
	}

	public void setPhotos(String photos) {
		this.photos = photos;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getFullName() {
		return firstname + " " + lastname;
	}

	public String getGioiTinh() {
		return gioiTinh;
	}

	public void setGioiTinh(String gioiTinh) {
		this.gioiTinh = gioiTinh;
	}

	public Date getNgaySinh() {
		return ngaySinh;
	}

	public void setNgaySinh(Date ngaySinh) {
		this.ngaySinh = ngaySinh;
	}

	public Set<Address> getAddress() {
		return address;
	}

	public void setAddress(Set<Address> address) {
		this.address = address;
	}

	public String getResetPasswordToken() {
		return resetPasswordToken;
	}

	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", email=" + email + ", password=" + password + ", firstname=" + firstname
				+ ", lastname=" + lastname + ", phoneNumber=" + phoneNumber + ", photos=" + photos + ", enabled="
				+ enabled + ", verificationCode=" + verificationCode + ", createdTime=" + createdTime + "]";
	}
	
	@Transient
	public String getPhotosImagesPath() {
		if (id == null || photos == null)
			return "/images/default-user.png";
		return "/customer-photos/" + this.id + "/" + this.photos;
	}
}
