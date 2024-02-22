package com.gls.ppldv.user.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * id (ȸ�� �ĺ���) - uno <br/>
 * name (ȸ�� �̸�) - uname <br/>
 * email (ȸ�� ���̵�-�̸�������) - uid <br/>
 * password (ȸ�� ��й�ȣ) - upw <br/>
 * birth (ȸ�� �������) - birth_date <br/>
 * phoneNo (ȸ�� �н�����) - phone <br/>
 * pc5 (ȸ�� �����ȣ) - postcode <br/>
 * address (ȸ�� �ּ�) - address <br/>
 * detail (�� �ּ�) - address_detail <br/>
 * role (BUSINESS,DEVELOPER) <br/>
 * gender (MALE,FEMALE) <br/>
 * bname (ȸ�� ��) - business_name <br/>
 * bpc5 (ȸ�� �����ȣ) - business_postcode <br/>
 * baddress(ȸ�� �ּ�) - business_address <br/>
 * bdetail (ȸ�� ���ּ�) - business_address_detail <br/>
 * bphone(ȸ�� ��ȭ��ȣ) - business_phone <br/>
 * imgUrl (������ �̹���) - image_url <br/>
 * 
 * 
 * ������ �ʿ������, ���� �ʿ��ϴٸ� ȸ�� ��Ͻð�, ȸ�� �����ð�, ȸ�� �����ð� �߰�
 */
@Entity
@Table(name="member")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Member {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "uno")
	private Long id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name="uid", nullable = false)
	private String email;
	
	@Column(name="upw", nullable = false)
	private String password;
	
	@Column(name="birth_date", nullable = false)
	private Date birth;
	
	@Column(name="phone", nullable = false)
	private String phoneNo;
	
	@Column(name="postcode", nullable = false)
	private int pc5;
	
	@Column(name="address", nullable = false)
	private String address;
	
	@Column(name="address_detail", nullable = false)
	private String detail;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@Enumerated(EnumType.STRING)
	private Gender gender;
	
	@Column(name="business_name", nullable=true)
	private String bname;
	
	@Column(name="business_postcode", nullable=true)
	private int bpc5;
	
	@Column(name="business_address", nullable=true)
	private String baddress;
	
	@Column(name="business_address_detail", nullable=true)
	private String bdetail;
	
	@Column(name="business_phone", nullable=true)
	private String bphone;
	
	@Column(name="image_url", nullable=true)
	private String imgUrl;
}
