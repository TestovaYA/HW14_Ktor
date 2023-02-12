data class Project(
    val modelVersion: String,
    val groupId: String,
    val artifactId: String,
    val version: String,
    val name: String,
    val description: String,
    val url: String,
    val licenses: Licenses,
    val developers: Developers,
    val scm: Scm,
    val dependencies: Dependencies
)

data class License(
    val name: String,
    val url: String
)

data class Licenses(
    val license: List<License>
)

data class Developer(
    val name: String,
    val organization: String,
    val organizationUrl: String
)

data class Developers(
    val developer: List<Developer>
)

data class Scm(
    val connection: String,
    val developerConnection: String,
    val url: String
)

data class Dependency(
    val groupId: String,
    val artifactId: String,
    val version: String,
    val scope: String
)

data class Dependencies(
    val dependency: List<Dependency>
)