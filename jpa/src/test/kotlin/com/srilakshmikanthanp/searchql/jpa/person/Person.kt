package com.srilakshmikanthanp.searchql.jpa.person


import com.srilakshmikanthanp.searchql.jpa.restriction.SearchQLRestrictedAttribute
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant

@Entity
class Person(
  @Column(nullable = false)
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  var id: String? = null,

  @Column(nullable = false)
  var firstName: String,

  @Column(nullable = false)
  var lastName: String,

  @Column(nullable = false, unique = true)
  var userName: String,

  @Column(nullable = false)
  var email: String,

  @SearchQLRestrictedAttribute
  @Column(nullable = false)
  var password: String,

  @Column(nullable = true)
  var avatar: String? = null,

  @Column(nullable = false)
  var isActive: Boolean,

  @Column(nullable = false)
  var isVerified: Boolean = false,

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  var createdAt: Instant? = null,

  @Column(nullable = false)
  @UpdateTimestamp
  var updatedAt: Instant? = null,
)