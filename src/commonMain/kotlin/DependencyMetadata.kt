data class DependencyMetadataRequest(
    val group: String,
    val artifact: String,
    val version: String
)

data class DependencyMetadataResponse(
    val name: String,
    val url: String,
    val license: String
)