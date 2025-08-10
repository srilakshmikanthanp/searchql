package com.srilakshmikanthanp.searchql.jpa.extensions

import com.srilakshmikanthanp.searchql.jpa.restriction.SearchQlRestrictedEntity
import com.srilakshmikanthanp.searchql.jpa.restriction.SearchQlRestrictedAttribute
import jakarta.persistence.metamodel.*
import java.lang.reflect.Field
import java.lang.reflect.Method

fun <T> ManagedType<T>.hasProperty(name: String): Boolean {
  return this.attributes.any { it.name == name }
}

fun <T> ManagedType<T>.isAssociation(name: String): Boolean {
  return this.getAttribute(name).isAssociation
}

fun <T> ManagedType<T>.isRestricted(): Boolean {
  return this.javaType.isAnnotationPresent(SearchQlRestrictedEntity::class.java)
}

fun <T> ManagedType<T>.isRestrictedAttribute(name: String): Boolean {
  val attribute = this.getAttribute(name)
  val member = attribute.javaMember
  return when (member) {
    is Method -> member.isAnnotationPresent(SearchQlRestrictedAttribute::class.java)
    is Field -> member.isAnnotationPresent(SearchQlRestrictedAttribute::class.java)
    else -> false
  }
}
