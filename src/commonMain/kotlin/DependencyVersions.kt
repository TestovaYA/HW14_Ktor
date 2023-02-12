data class DependencyVersionsRequest(
    val group: String,
    val artifact: String
)

data class DependencyVersionsResponse(
    val name: String,
    val versions: List<String>
)