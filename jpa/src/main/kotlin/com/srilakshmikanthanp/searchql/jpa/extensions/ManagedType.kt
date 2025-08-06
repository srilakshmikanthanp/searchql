package com.srilakshmikanthanp.searchql.jpa.extensions

import com.srilakshmikanthanp.searchql.jpa.restriction.SearchQLRestrictedEntity
import com.srilakshmikanthanp.searchql.jpa.restriction.SearchQLRestrictedAttribute
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
  return this.javaType.isAnnotationPresent(SearchQLRestrictedEntity::class.java)
}

fun <T> ManagedType<T>.isRestrictedAttribute(name: String): Boolean {
  val attribute = this.getAttribute(name)
  val member = attribute.javaMember
  return when (member) {
    is Method -> member.isAnnotationPresent(SearchQLRestrictedAttribute::class.java)
    is Field -> member.isAnnotationPresent(SearchQLRestrictedAttribute::class.java)
    else -> false
  }
}
