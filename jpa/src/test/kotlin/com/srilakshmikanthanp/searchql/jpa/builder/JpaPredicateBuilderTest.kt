package com.srilakshmikanthanp.searchql.jpa.builder

import com.srilakshmikanthanp.searchql.jpa.callable.MapJpaCallableManager
import com.srilakshmikanthanp.searchql.jpa.callable.builtin.LengthCallable
import com.srilakshmikanthanp.searchql.jpa.callable.builtin.ConcatCallable
import com.srilakshmikanthanp.searchql.jpa.event.Event
import com.srilakshmikanthanp.searchql.jpa.person.Person
import jakarta.persistence.EntityManager
import jakarta.persistence.Persistence
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JpaPredicateBuilderTest {
  private var entityManager = Persistence.createEntityManagerFactory("h2").createEntityManager()
  private val builder = JpaPredicateBuilder(MapJpaCallableManager.withBuiltIns())
  private val rows = 100

  @BeforeAll
  fun setup() {
    entityManager.transaction.begin()

    for (i in 1..rows) {
      val person = Person(
        firstName = "First$i",
        lastName = "Last$i",
        userName = "user$i",
        email = "user$i@example.com",
        password = "pass$i",
        isActive = true
      )

      entityManager.persist(person)

      val event = Event(
        serverIp = "192.168.0.${i % 255}",
        remoteIp = "10.0.0.${i % 255}",
        message = "Event message $i",
        person = person,
        module = "MODULE",
        operation = "OPERATION",
        objectType = "OBJECT",
        objectId = "$i"
      )

      entityManager.persist(event)
    }

    entityManager.transaction.commit()
  }

  @AfterAll
  fun tearDown() {
    if (entityManager.transaction.isActive) {
      entityManager.transaction.rollback()
    }
    entityManager.close()
  }

  private inline fun <reified T> query(expression: String): List<T> {
    val searchQlQuery = builder.parse(expression)
    val cb = entityManager.criteriaBuilder
    val query = cb.createQuery(T::class.java)
    val root = query.from(T::class.java)

    val predicate = searchQlQuery.toPredicate(
      entityManager = entityManager,
      root = root,
      criteriaBuilder = cb
    )

    query.where(predicate)

    return entityManager.createQuery(query).resultList
  }

  @Test
  fun `should handle equals operation`() {
    val results = query<Person>("firstName == 'First1'")
    assert(results.size == 1)
    val person = results.first()
    assert(person.firstName == "First1")
  }

  @Test
  fun `should handle not equals operation`() {
    val results = query<Person>("firstName != 'First1'")
    assert(results.size == rows - 1) // total - 1 excluded
    assert(results.none { it.firstName == "First1" })
  }

  @Test
  fun `should handle arithmetic operations`() {
    val results  = query<Event>("objectId == ((1 + 2) * 3) / 3 ") // "3
    assert(results.size == 1)
    val event = results.first()
    assert(event.objectId == "3")
  }

  @Test
  fun `should handle string operations`() {
    val results = query<Event>("objectId == '1' + '2'") // "123"
    assert(results.size == 1)
    val event = results.first()
    assert(event.objectId == "12")
  }

  @Test
  fun `should handle and operations`() {
    val results = query<Person>("firstName == 'First1' and lastName == 'Last1'")
    assert(results.size == 1)
    val person = results.first()
    assert(person.firstName == "First1")
    assert(person.lastName == "Last1")
  }

  @Test
  fun `should handle or operations`() {
    val results = query<Person>("firstName == 'First1' or firstName == 'First2'")
    assert(results.size == 2)
    assert(results.any { it.firstName == "First1" })
    assert(results.any { it.firstName == "First2" })
  }

  @Test
  fun `should handle not operations`() {
    val results = query<Person>("not (firstName == 'First1')")
    assert(results.size == rows - 1) // 1000 total - 1 excluded
    assert(results.none { it.firstName == "First1" })
  }

  @Test
  fun `should handle in operation`() {
    val results = query<Person>("firstName in ('First1', 'First2')")
    assert(results.size == 2)
    assert(results.any { it.firstName == "First1" })
    assert(results.any { it.firstName == "First2" })
  }

  @Test
  fun `should handle not in operation`() {
    val results = query<Person>("firstName not in ('First1', 'First2')")
    assert(results.size == rows - 2) // total - 2 excluded
    assert(results.none { it.firstName == "First1" })
    assert(results.none { it.firstName == "First2" })
  }

  @Test
  fun `should handle object access`() {
    val results = query<Event>("person.firstName == 'First1'")
    assert(results.size == 1)
    val event = results.first()
    assert(event.person.firstName == "First1")
  }

  @Test
  fun `should handle function calls`() {
    val results = query<Event>("length(objectId) == 1")
    assert(results.size == 9) // "1" to "9" have length 1
  }

  @Test
  fun `should handle variadic function`() {
    val results = query<Event>("objectId == concat('1', '2')")
    assert(results.size == 1)
    val event = results.first()
    assert(event.objectId == "12")
  }
}
