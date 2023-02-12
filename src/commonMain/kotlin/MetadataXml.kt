data class MetadataXml(
    val groupId: String,
    val artifactId: String,
    val versioning: Versioning
)

data class Versioning(
    val latest: String,
    val release: String,
    val versions: Versions,
    val lastUpdated: String
)

data class Versions(
    val version: List<String>
)
