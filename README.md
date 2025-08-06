<!-- PROJECT LOGO -->
<br />
<div align="center">
    <h3 align="center">searchql</h3>

  <p align="center">
    Let users search their need, let SearchQL search the database.
    <br />
    <a href="https://github.com/srilakshmikanthanp/searchql"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/srilakshmikanthanp/searchql">View Demo</a>
    &middot;
    <a href="https://github.com/srilakshmikanthanp/searchql/issues/new?labels=bug">Report Bug</a>
    &middot;
    <a href="https://github.com/srilakshmikanthanp/searchql/issues/new?labels=enhancement">Request Feature</a>
  </p>
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#Grammar">Grammar</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->
## About The Project

SearchQL lets users search/filter data from the UI using simple, flexible queries, which it automatically converts into
SQL. It supports the JPA Criteria API and works with any JPA provider like Hibernate, making it easy to add advanced
search features.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Built With

* Kotlin
* Antlr

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- GETTING STARTED -->
## Getting Started

These instructions will help you get started with the project.

### Prerequisites

* Java 11+

### Installation

This project is not yet available in any package manager. You can clone the repository and build it using Gradle.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- USAGE EXAMPLES -->
## Usage

Let's say you have the following JPA entities:

<details>
  <summary>Click to view Person Entity</summary>

```kotlin
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

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  var createdAt: Instant? = null,

  @Column(nullable = false)
  @UpdateTimestamp
  var updatedAt: Instant? = null,
)
```

</details>

<details>
  <summary>Click to view Event Entity</summary>

```kotlin
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
```

</details>

You can use the `SearchQL` class to create a search query for these entities. Here's an example of how to use it:

```kotlin
// Fields start from root entity
val builder = SearchQLJpaPredicateBuilder.parse("operation =='CLICK' and person.firstName == 'John' and person.lastName == 'Doe'")
val jpaCallableManager = MapJpaCallableManager() // You can register custom functions that extend JpaCallable with it
val entityManager = Persistence.createEntityManagerFactory("h2").createEntityManager()
val cb = entityManager.criteriaBuilder
val query = cb.createQuery(Event::class.java)
val root = query.from(Event::class.java)

val predicate = builder.toPredicate(
  jpaCallableProvider = jpaCallableManager,   
  entityManager = entityManager,   
  root = root,
  criteriaBuilder = cb
)

query.where(predicate)

val results = entityManager.createQuery(query).resultList
```

you can restrict the search query by annotating the attributes in your JPA entities with `@SearchQLRestrictedAttribute` 
or entire Entity with `@SearchQLRestrictedEntity`. This will prevent the user from searching on those entries.

## Grammar

* **Arithmetic Operators**:

  `+`, `-`, `*`, `/`, `%`

  *(Note: `+` also supports string concatenation)*

* **Comparison Operators**:

  `==`, `!=`, `>`, `<`, `>=`, `<=`

* **Logical Operators**:

  `and`, `or`, `not`

* **Membership Operators**:

  `in`, `not in`

  *Example: `status in ('ACTIVE', 'PENDING')`, `role not in ('ADMIN', 'GUEST')`*

* **Field Access**:

  Dot notation for nested fields, e.g., `person.firstName`

* **Function Calls**:.

  *Example:`length('John')`*

  *(Note: Supports custom functions registered with `JpaCallableManager`)*

* **Grouping and Precedence**:

  *Example: `operation == 'CLICK' and (person.firstName == 'John' or person.lastName == 'Doe')`*

<!-- ROADMAP -->
## Roadmap

See the [open issues](https://github.com/srilakshmikanthanp/searchql/issues) for a full list of proposed features (and known issues).

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any
contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can 
also simply open an issue with the tag "enhancement". Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<!-- LICENSE -->
## License

Distributed under the MIT License. See [LICENSE](LICENSE) for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTACT -->
## Contact

Sri Lakshmi Kanthan P - [@itsmekanth](https://twitter.com/itsmekanth) - srilakshmikanthanp@gmail.com

Project Link: [https://github.com/srilakshmikanthanp/searchql](https://github.com/srilakshmikanthanp/searchql)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- ACKNOWLEDGMENTS -->
## Acknowledgments

* [Antlr](https://www.antlr.org/)

<p align="right">(<a href="#readme-top">back to top</a>)</p>
