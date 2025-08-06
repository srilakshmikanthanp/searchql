package com.srilakshmikanthanp.searchql.jpa.event

import com.srilakshmikanthanp.searchql.jpa.person.Person
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant

@Entity
class Event (
  @Column(nullable = false)
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  var id: String? = null,

  @Column(nullable = false)
  var serverIp: String,

  @Column(nullable = false)
  var remoteIp: String,

  @Column(nullable = false)
  var message: String,

  @ManyToOne
  @JoinColumn(name = "person_id", nullable = false)
  var person: Person,

  @Column(nullable = false)
  var module: String,

  @Column(nullable = false)
  var operation: String,

  @Column(nullable = false)
  var objectType: String,

  @Column(nullable = false)
  var objectId: String,

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  var createdAt: Instant? = null,

  @Column(nullable = false)
  @UpdateTimestamp
  var updatedAt: Instant? = null,
)